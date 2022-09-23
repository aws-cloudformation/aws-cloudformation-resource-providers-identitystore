package software.amazon.identitystore.groupmembership;

import software.amazon.awssdk.services.identitystore.IdentitystoreClient;
import software.amazon.awssdk.services.identitystore.model.DescribeGroupMembershipRequest;
import software.amazon.awssdk.services.identitystore.model.DescribeGroupMembershipResponse;
import software.amazon.awssdk.services.identitystore.model.ResourceNotFoundException;
import software.amazon.cloudformation.exceptions.CfnNotFoundException;
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
import static org.mockito.Mockito.when;
import static software.amazon.identitystore.groupmembership.TestConstants.TEST_GROUP_ID;
import static software.amazon.identitystore.groupmembership.TestConstants.TEST_IDENTITY_STORE_ID;
import static software.amazon.identitystore.groupmembership.TestConstants.TEST_MEMBERSHIP_ID;
import static software.amazon.identitystore.groupmembership.TestConstants.TEST_MODEL_MEMBER_ID_WITH_USER;
import static software.amazon.identitystore.groupmembership.TestConstants.TEST_USER_ID;

@ExtendWith(MockitoExtension.class)
public class ReadHandlerTest {

    private ReadHandler handler;
    private AmazonWebServicesClientProxy proxy;
    private ProxyClient<IdentitystoreClient> proxyClient;
    private LoggerProxy logger;

    @Mock
    private IdentitystoreClient identitystoreClient;

    @BeforeEach
    public void setup() {
        final Credentials mockCredentials = new Credentials("accessKey", "secretKey", "token");
        logger = new LoggerProxy();
        handler = new ReadHandler();
        proxy = new AmazonWebServicesClientProxy(logger, mockCredentials, () -> Duration.ofSeconds(600).toMillis());
        proxyClient = proxy.newProxy(() -> identitystoreClient);
    }

    @Test
    public void handleRequest_success() {
        final DescribeGroupMembershipResponse describeGroupMembershipResponse = DescribeGroupMembershipResponse.builder()
                .membershipId(TEST_MEMBERSHIP_ID)
                .identityStoreId(TEST_IDENTITY_STORE_ID)
                .groupId(TEST_GROUP_ID)
                .memberId(TEST_MODEL_MEMBER_ID_WITH_USER)
                .build();

        final ResourceModel model = ResourceModel.builder()
                .identityStoreId(TEST_IDENTITY_STORE_ID)
                .membershipId(TEST_MEMBERSHIP_ID)
                .build();

        final ResourceHandlerRequest<ResourceModel> request = ResourceHandlerRequest.<ResourceModel>builder()
                .desiredResourceState(model)
                .build();

        when(proxyClient.client().describeGroupMembership(any(DescribeGroupMembershipRequest.class)))
                .thenReturn(describeGroupMembershipResponse);

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
        assertThat(response.getResourceModel().getGroupId()).isEqualTo(TEST_GROUP_ID);
        assertThat(response.getResourceModel().getMemberId().getUserId()).isEqualTo(TEST_USER_ID);
        assertThat(response.getResourceModel().getIdentityStoreId()).isEqualTo(TEST_IDENTITY_STORE_ID);
    }

    @Test
    public void handleRequest_membershipIdDoesNotExist_throwsResourceNotFound() {
        final ResourceModel model = ResourceModel.builder()
                .identityStoreId(TEST_IDENTITY_STORE_ID)
                .membershipId(TEST_MEMBERSHIP_ID)
                .build();

        final ResourceHandlerRequest<ResourceModel> request = ResourceHandlerRequest.<ResourceModel>builder()
                .desiredResourceState(model)
                .build();

        when(proxyClient.client().describeGroupMembership(any(DescribeGroupMembershipRequest.class)))
                .thenThrow(ResourceNotFoundException.class);

        assertThrows(CfnNotFoundException.class, () ->
                handler.handleRequest(proxy, request, new CallbackContext(), proxyClient, logger));
    }
}
