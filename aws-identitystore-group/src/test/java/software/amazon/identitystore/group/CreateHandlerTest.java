package software.amazon.identitystore.group;

import org.junit.jupiter.api.AfterEach;
import software.amazon.awssdk.services.identitystore.IdentitystoreClient;
import software.amazon.awssdk.services.identitystore.model.ConflictException;
import software.amazon.awssdk.services.identitystore.model.CreateGroupRequest;
import software.amazon.awssdk.services.identitystore.model.CreateGroupResponse;
import software.amazon.awssdk.services.identitystore.model.DescribeGroupRequest;
import software.amazon.awssdk.services.identitystore.model.DescribeGroupResponse;
import software.amazon.cloudformation.exceptions.CfnGeneralServiceException;
import software.amazon.cloudformation.exceptions.CfnResourceConflictException;
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
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static software.amazon.identitystore.group.TestConstants.TEST_EXTERNAL_ID_ISSUER_VALUE;
import static software.amazon.identitystore.group.TestConstants.TEST_EXTERNAL_ID_VALUE;
import static software.amazon.identitystore.group.TestConstants.TEST_GROUP_DESCRIPTION;
import static software.amazon.identitystore.group.TestConstants.TEST_GROUP_DISPLAY_NAME;
import static software.amazon.identitystore.group.TestConstants.TEST_GROUP_ID;
import static software.amazon.identitystore.group.TestConstants.TEST_IDENTITY_STORE_ID;


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
        final Credentials MOCK_CREDENTIALS = new Credentials("accessKey", "secretKey", "token");
        logger = new LoggerProxy();
        handler = new CreateHandler();
        proxy = new AmazonWebServicesClientProxy(logger, MOCK_CREDENTIALS, () -> Duration.ofSeconds(600).toMillis());
        proxyClient = proxy.newProxy(() -> identitystoreClient);
    }

    @AfterEach
    public void post_execute() {
        verify(identitystoreClient, times(1)).createGroup(any(CreateGroupRequest.class));
    }

    @Test
    public void testCreateHandleRequest_success() {
        final CreateGroupResponse createGroupResponse = CreateGroupResponse.builder()
                .groupId(TEST_GROUP_ID)
                .identityStoreId(TEST_IDENTITY_STORE_ID)
                .build();

        final DescribeGroupResponse describeGroupResponse = DescribeGroupResponse.builder()
                .groupId(TEST_GROUP_ID)
                .identityStoreId(TEST_IDENTITY_STORE_ID)
                .displayName(TEST_GROUP_DISPLAY_NAME)
                .build();

        when(proxyClient.client().createGroup(any(CreateGroupRequest.class))).thenReturn(createGroupResponse);
        when(proxyClient.client().describeGroup(any(DescribeGroupRequest.class))).thenReturn(describeGroupResponse);

        final ResourceModel model = ResourceModel.builder()
                .identityStoreId(TEST_IDENTITY_STORE_ID)
                .displayName(TEST_GROUP_DISPLAY_NAME)
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
        assertThat(response.getResourceModel().getGroupId()).isEqualTo(TEST_GROUP_ID);
        assertThat(response.getResourceModel().getIdentityStoreId()).isEqualTo(TEST_IDENTITY_STORE_ID);
        assertThat(response.getResourceModel().getDisplayName()).isEqualTo(TEST_GROUP_DISPLAY_NAME);
        assertNull(response.getResourceModel().getDescription());
        verify(identitystoreClient, times(1)).describeGroup(any(DescribeGroupRequest.class));
    }

    @Test
    public void testCreateHandleRequest_withGroupDescription_success() {
        final CreateGroupResponse createGroupResponse = CreateGroupResponse.builder()
                .groupId(TEST_GROUP_ID)
                .identityStoreId(TEST_IDENTITY_STORE_ID)
                .build();

        final DescribeGroupResponse describeGroupResponse = DescribeGroupResponse.builder()
                .groupId(TEST_GROUP_ID)
                .identityStoreId(TEST_IDENTITY_STORE_ID)
                .displayName(TEST_GROUP_DISPLAY_NAME)
                .description(TEST_GROUP_DESCRIPTION)
                .build();

        when(proxyClient.client().createGroup(any(CreateGroupRequest.class))).thenReturn(createGroupResponse);
        when(proxyClient.client().describeGroup(any(DescribeGroupRequest.class))).thenReturn(describeGroupResponse);

        final ResourceModel model = ResourceModel.builder()
                .identityStoreId(TEST_IDENTITY_STORE_ID)
                .displayName(TEST_GROUP_DISPLAY_NAME)
                .description(TEST_GROUP_DESCRIPTION)
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
        assertThat(response.getResourceModel().getGroupId()).isEqualTo(TEST_GROUP_ID);
        assertThat(response.getResourceModel().getIdentityStoreId()).isEqualTo(TEST_IDENTITY_STORE_ID);
        assertThat(response.getResourceModel().getDisplayName()).isEqualTo(TEST_GROUP_DISPLAY_NAME);
        assertThat(response.getResourceModel().getDescription()).isEqualTo(TEST_GROUP_DESCRIPTION);
        verify(identitystoreClient, times(1)).describeGroup(any(DescribeGroupRequest.class));
    }

    @Test
    public void testHandleRequest_throwsRuntimeException_exceptionHandled() {
        when(proxyClient.client().createGroup(any(CreateGroupRequest.class))).thenThrow(new RuntimeException());

        final ResourceModel model = ResourceModel.builder()
                .identityStoreId(TEST_IDENTITY_STORE_ID)
                .displayName(TEST_GROUP_DISPLAY_NAME)
                .description(TEST_GROUP_DESCRIPTION)
                .build();

        final ResourceHandlerRequest<ResourceModel> request = ResourceHandlerRequest.<ResourceModel>builder()
                .desiredResourceState(model)
                .build();

        assertThrows(CfnGeneralServiceException.class, () ->
                handler.handleRequest(proxy, request, new CallbackContext(), proxyClient, logger));
    }

    @Test
    public void testHandleRequest_groupAlreadyExists_throwsConflictException() {
        when(proxyClient.client().createGroup(any(CreateGroupRequest.class))).thenThrow(ConflictException.class);

        final ResourceModel model = ResourceModel.builder()
                .identityStoreId(TEST_IDENTITY_STORE_ID)
                .displayName(TEST_GROUP_DISPLAY_NAME)
                .description(TEST_GROUP_DESCRIPTION)
                .build();

        final ResourceHandlerRequest<ResourceModel> request = ResourceHandlerRequest.<ResourceModel>builder()
                .desiredResourceState(model)
                .build();

        assertThrows(CfnResourceConflictException.class, () ->
                handler.handleRequest(proxy, request, new CallbackContext(), proxyClient, logger));
    }
}
