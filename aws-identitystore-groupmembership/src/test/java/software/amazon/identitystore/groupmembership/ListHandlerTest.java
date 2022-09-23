package software.amazon.identitystore.groupmembership;

import software.amazon.awssdk.services.identitystore.IdentitystoreClient;
import software.amazon.awssdk.services.identitystore.model.ListGroupMembershipsRequest;
import software.amazon.awssdk.services.identitystore.model.ListGroupMembershipsResponse;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static software.amazon.identitystore.groupmembership.TestConstants.TEST_GROUP_ID;
import static software.amazon.identitystore.groupmembership.TestConstants.TEST_GROUP_MEMBERSHIP;
import static software.amazon.identitystore.groupmembership.TestConstants.TEST_IDENTITY_STORE_ID;
import static software.amazon.identitystore.groupmembership.TestConstants.TEST_MEMBERSHIP_ID;
import static software.amazon.identitystore.groupmembership.TestConstants.TEST_USER_ID;

@ExtendWith(MockitoExtension.class)
public class ListHandlerTest {

    private ListHandler handler;
    private AmazonWebServicesClientProxy proxy;
    private ProxyClient<IdentitystoreClient> proxyClient;
    private LoggerProxy logger;

    @Mock
    private IdentitystoreClient identitystoreClient;

    @BeforeEach
    public void setup() {
        final Credentials mockCredentials = new Credentials("accessKey", "secretKey", "token");
        logger = new LoggerProxy();
        handler = new ListHandler();
        proxy = new AmazonWebServicesClientProxy(logger, mockCredentials, () -> Duration.ofSeconds(600).toMillis());
        proxyClient = proxy.newProxy(() -> identitystoreClient);
    }

    @Test
    public void handleRequest_success() {
        final ListGroupMembershipsResponse listGroupMembershipsResponse = ListGroupMembershipsResponse.builder()
                .groupMemberships(TEST_GROUP_MEMBERSHIP)
                .build();

        when(proxyClient.client().listGroupMemberships(any(ListGroupMembershipsRequest.class)))
                .thenReturn(listGroupMembershipsResponse);

        final ResourceModel model = ResourceModel.builder()
                .identityStoreId(TEST_IDENTITY_STORE_ID)
                .build();

        final ResourceHandlerRequest<ResourceModel> request = ResourceHandlerRequest.<ResourceModel>builder()
                .desiredResourceState(model)
                .build();

        final ProgressEvent<ResourceModel, CallbackContext> response =
                handler.handleRequest(proxy, request, new CallbackContext(), proxyClient, logger);

        assertThat(response).isNotNull();
        assertThat(response.getStatus()).isEqualTo(OperationStatus.SUCCESS);
        assertThat(response.getCallbackContext()).isNull();
        assertThat(response.getCallbackDelaySeconds()).isEqualTo(0);
        assertThat(response.getResourceModel()).isNull();
        assertThat(response.getResourceModels()).isNotNull();
        assertThat(response.getMessage()).isNull();
        assertThat(response.getErrorCode()).isNull();

        assertThat(response.getResourceModels().get(0).getMembershipId()).isEqualTo(TEST_MEMBERSHIP_ID);
        assertThat(response.getResourceModels().get(0).getMemberId().getUserId()).isEqualTo(TEST_USER_ID);
        assertThat(response.getResourceModels().get(0).getGroupId()).isEqualTo(TEST_GROUP_ID);
        assertThat(response.getResourceModels().get(0).getIdentityStoreId()).isEqualTo(TEST_IDENTITY_STORE_ID);

    }
}
