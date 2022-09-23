package software.amazon.identitystore.groupmembership;

import org.junit.jupiter.api.AfterEach;
import software.amazon.awssdk.services.identitystore.IdentitystoreClient;
import software.amazon.awssdk.services.identitystore.model.ConflictException;
import software.amazon.awssdk.services.identitystore.model.CreateGroupMembershipRequest;
import software.amazon.awssdk.services.identitystore.model.CreateGroupMembershipResponse;
import software.amazon.awssdk.services.identitystore.model.DescribeGroupMembershipRequest;
import software.amazon.awssdk.services.identitystore.model.DescribeGroupMembershipResponse;
import software.amazon.cloudformation.exceptions.CfnGeneralServiceException;
import software.amazon.cloudformation.proxy.AmazonWebServicesClientProxy;
import software.amazon.cloudformation.proxy.Credentials;
import software.amazon.cloudformation.proxy.LoggerProxy;
import software.amazon.cloudformation.proxy.OperationStatus;
import software.amazon.cloudformation.proxy.ProgressEvent;
import software.amazon.cloudformation.proxy.ProxyClient;
import software.amazon.cloudformation.proxy.ResourceHandlerRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static software.amazon.identitystore.groupmembership.TestConstants.TEST_GROUP_ID;
import static software.amazon.identitystore.groupmembership.TestConstants.TEST_IDENTITY_STORE_ID;
import static software.amazon.identitystore.groupmembership.TestConstants.TEST_MEMBERSHIP_ID;
import static software.amazon.identitystore.groupmembership.TestConstants.TEST_MEMBER_ID_WITH_USER;
import static software.amazon.identitystore.groupmembership.TestConstants.TEST_MODEL_MEMBER_ID_WITH_USER;

@ExtendWith(MockitoExtension.class)
public class CreateHandlerTest {

    private CreateHandler handler;
    private AmazonWebServicesClientProxy proxy;
    private ProxyClient<IdentitystoreClient> proxyClient;
    private LoggerProxy logger;

    @Mock
    private IdentitystoreClient identitystoreClient;

    @BeforeEach
    public void setup() {
        final Credentials mockCredentials = new Credentials("accessKey", "secretKey", "token");
        logger = new LoggerProxy();
        handler = new CreateHandler();
        proxy = new AmazonWebServicesClientProxy(logger, mockCredentials, () -> Duration.ofSeconds(600).toMillis());
        proxyClient = proxy.newProxy(() -> identitystoreClient);
    }

    @AfterEach
    public void post_execute() {
        verify(identitystoreClient, times(1)).createGroupMembership(any(CreateGroupMembershipRequest.class));
    }

    @Test
    public void handleRequest_success() {
        final CreateGroupMembershipResponse createGroupMembershipResponse = CreateGroupMembershipResponse.builder()
                .membershipId(TEST_MEMBERSHIP_ID)
                .identityStoreId(TEST_IDENTITY_STORE_ID)
                .build();

        final DescribeGroupMembershipResponse describeGroupMembershipResponse = DescribeGroupMembershipResponse.builder()
                .membershipId(TEST_MEMBERSHIP_ID)
                .identityStoreId(TEST_IDENTITY_STORE_ID)
                .groupId(TEST_GROUP_ID)
                .memberId(TEST_MODEL_MEMBER_ID_WITH_USER)
                .build();

        when(proxyClient.client().createGroupMembership(any(CreateGroupMembershipRequest.class)))
                .thenReturn(createGroupMembershipResponse);
        when(proxyClient.client().describeGroupMembership(any(DescribeGroupMembershipRequest.class)))
                .thenReturn(describeGroupMembershipResponse);

        final ResourceModel model = ResourceModel.builder()
                .identityStoreId(TEST_IDENTITY_STORE_ID)
                .groupId(TEST_GROUP_ID)
                .memberId(TEST_MEMBER_ID_WITH_USER)
                .build();

        final ResourceHandlerRequest<ResourceModel> request = ResourceHandlerRequest.<ResourceModel>builder()
                .desiredResourceState(model)
                .build();

        final ProgressEvent<ResourceModel, CallbackContext> response
                = handler.handleRequest(proxy, request, new CallbackContext(), proxyClient, logger);

        assertThat(response).isNotNull();
        assertThat(response.getStatus()).isEqualTo(OperationStatus.SUCCESS);
        assertThat(response.getCallbackContext()).isNull();
        assertThat(response.getCallbackDelaySeconds()).isEqualTo(0);
        assertThat(response.getResourceModel()).isEqualTo(request.getDesiredResourceState());
        assertThat(response.getResourceModels()).isNull();
        assertThat(response.getMessage()).isNull();
        assertThat(response.getErrorCode()).isNull();
        assertThat(response.getResourceModel().getMembershipId()).isEqualTo(TEST_MEMBERSHIP_ID);
        assertThat(response.getResourceModel().getIdentityStoreId()).isEqualTo(TEST_IDENTITY_STORE_ID);
    }

    @Test
    public void handleRequest_throwsException_exceptionHandled() {
        when(proxyClient.client().createGroupMembership(any(CreateGroupMembershipRequest.class)))
                .thenThrow(new RuntimeException());

        final ResourceModel model = ResourceModel.builder()
                .identityStoreId(TEST_IDENTITY_STORE_ID)
                .groupId(TEST_GROUP_ID)
                .memberId(TEST_MEMBER_ID_WITH_USER)
                .build();

        final ResourceHandlerRequest<ResourceModel> request = ResourceHandlerRequest.<ResourceModel>builder()
                .desiredResourceState(model)
                .build();

        assertThrows(CfnGeneralServiceException.class, () ->
                handler.handleRequest(proxy, request, new CallbackContext(), proxyClient, logger));
    }

    @Test
    public void handleRequest_membershipAlreadyExists_throwsConflictException() {
        when(proxyClient.client().createGroupMembership(any(CreateGroupMembershipRequest.class)))
                .thenThrow(ConflictException.class);

        final ResourceModel model = ResourceModel.builder()
                .identityStoreId(TEST_IDENTITY_STORE_ID)
                .groupId(TEST_GROUP_ID)
                .memberId(TEST_MEMBER_ID_WITH_USER)
                .build();

        final ResourceHandlerRequest<ResourceModel> request = ResourceHandlerRequest.<ResourceModel>builder()
                .desiredResourceState(model)
                .build();

        assertThrows(software.amazon.cloudformation.exceptions.CfnResourceConflictException.class, () ->
                handler.handleRequest(proxy, request, new CallbackContext(), proxyClient, logger));
    }
}
