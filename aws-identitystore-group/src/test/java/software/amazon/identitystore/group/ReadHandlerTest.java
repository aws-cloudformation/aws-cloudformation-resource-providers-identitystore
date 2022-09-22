package software.amazon.identitystore.group;

import software.amazon.awssdk.services.identitystore.IdentitystoreClient;
import software.amazon.awssdk.services.identitystore.model.DescribeGroupRequest;
import software.amazon.awssdk.services.identitystore.model.DescribeGroupResponse;
import software.amazon.awssdk.services.identitystore.model.ExternalId;
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
import static software.amazon.identitystore.group.TestConstants.TEST_EXTERNAL_ID_ISSUER_VALUE;
import static software.amazon.identitystore.group.TestConstants.TEST_EXTERNAL_ID_VALUE;
import static software.amazon.identitystore.group.TestConstants.TEST_GROUP_DESCRIPTION;
import static software.amazon.identitystore.group.TestConstants.TEST_GROUP_DISPLAY_NAME;
import static software.amazon.identitystore.group.TestConstants.TEST_GROUP_ID;
import static software.amazon.identitystore.group.TestConstants.TEST_IDENTITY_STORE_ID;

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
        final Credentials MOCK_CREDENTIALS = new Credentials("accessKey", "secretKey", "token");
        logger = new LoggerProxy();
        handler = new ReadHandler();
        proxy = new AmazonWebServicesClientProxy(logger, MOCK_CREDENTIALS, () -> Duration.ofSeconds(600).toMillis());
        proxyClient = proxy.newProxy(() -> identitystoreClient);
    }

    @Test
    public void testHandleRequest_readGroup_success() {
        final DescribeGroupResponse describeGroupResponse = DescribeGroupResponse.builder()
                .groupId(TEST_GROUP_ID)
                .identityStoreId(TEST_IDENTITY_STORE_ID)
                .displayName(TEST_GROUP_DISPLAY_NAME)
                .description(TEST_GROUP_DESCRIPTION)
                .externalIds(
                        ExternalId.builder()
                                .issuer(TEST_EXTERNAL_ID_ISSUER_VALUE)
                                .id(TEST_EXTERNAL_ID_VALUE)
                                .build())
                .build();

        final ResourceModel model = ResourceModel.builder()
                .identityStoreId(TEST_IDENTITY_STORE_ID)
                .groupId(TEST_GROUP_ID)
                .build();

        final ResourceHandlerRequest<ResourceModel> request = ResourceHandlerRequest.<ResourceModel>builder()
            .desiredResourceState(model)
            .build();

        when(proxyClient.client().describeGroup(any(DescribeGroupRequest.class))).thenReturn(describeGroupResponse);

        final ProgressEvent<ResourceModel, CallbackContext> response
                = handler.handleRequest(proxy, request, new CallbackContext(), proxyClient, logger);

        assertThat(response).isNotNull();
        assertThat(response.getStatus()).isEqualTo(OperationStatus.SUCCESS);
        assertThat(response.getCallbackContext()).isNull();
        assertThat(response.getCallbackDelaySeconds()).isEqualTo(0);
        assertThat(response.getResourceModel()).isEqualTo(request.getDesiredResourceState());
        assertThat(response.getResourceModels()).isNull();
        assertThat(response.getResourceModel().getGroupId()).isEqualTo(TEST_GROUP_ID);
        assertThat(response.getResourceModel().getIdentityStoreId()).isEqualTo(TEST_IDENTITY_STORE_ID);
        assertThat(response.getResourceModel().getDisplayName()).isEqualTo(TEST_GROUP_DISPLAY_NAME);
        assertThat(response.getResourceModel().getDescription()).isEqualTo(TEST_GROUP_DESCRIPTION);
        assertThat(response.getMessage()).isNull();
        assertThat(response.getErrorCode()).isNull();
    }

    @Test
    public void testHandleRequest_whenNullRequest_throwsNullPointerException() {
        final ResourceModel model = ResourceModel.builder()
                .build();

        final ResourceHandlerRequest<ResourceModel> request = ResourceHandlerRequest.<ResourceModel>builder()
                .desiredResourceState(model)
                .build();
        assertThrows(
                NullPointerException.class,
                () -> handler.handleRequest(proxy, request, new CallbackContext(), proxyClient, logger)
        );
    }

    @Test
    public void testHandlerRequest_whenResourceNotFound_throwsCfnNotFoundException() {
        final ResourceModel model = ResourceModel.builder()
                .identityStoreId(TEST_IDENTITY_STORE_ID)
                .groupId(TEST_GROUP_ID)
                .build();

        final ResourceHandlerRequest<ResourceModel> request = ResourceHandlerRequest.<ResourceModel>builder()
                .desiredResourceState(model)
                .build();

        when(proxyClient.client().describeGroup(any(DescribeGroupRequest.class))).thenThrow(ResourceNotFoundException.class);
        assertThrows(CfnNotFoundException.class, () ->
                handler.handleRequest(proxy, request, new CallbackContext(), proxyClient, logger));
    }
}
