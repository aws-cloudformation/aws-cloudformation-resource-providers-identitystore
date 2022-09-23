package software.amazon.identitystore.groupmembership;

import software.amazon.awssdk.services.identitystore.IdentitystoreClient;
import software.amazon.cloudformation.proxy.AmazonWebServicesClientProxy;
import software.amazon.cloudformation.proxy.Logger;
import software.amazon.cloudformation.proxy.ProgressEvent;
import software.amazon.cloudformation.proxy.ProxyClient;
import software.amazon.cloudformation.proxy.ResourceHandlerRequest;

public class CreateHandler extends BaseHandlerStd {
    private Logger logger;

    public ProgressEvent<ResourceModel, CallbackContext> handleRequest(
        final AmazonWebServicesClientProxy proxy,
        final ResourceHandlerRequest<ResourceModel> request,
        final CallbackContext callbackContext,
        final ProxyClient<IdentitystoreClient> proxyClient,
        final Logger logger) {

        this.logger = logger;
        final ResourceModel resourceModel = request.getDesiredResourceState();

        logger.log(String.format("Invoking CreateGroupMembership Handler with IdentityStore [%s]", resourceModel.getIdentityStoreId()));

        return ProgressEvent.progress(resourceModel, callbackContext)
            .then(progress ->
                proxy.initiate("AWS-IdentityStore-GroupMembership::Create", proxyClient, progress.getResourceModel(), callbackContext)
                    .translateToServiceRequest(Translator::translateToCreateRequest)
                    .makeServiceCall((createRequest, clientProxy)
                        -> invoke(createRequest, clientProxy, clientProxy.client()::createGroupMembership, logger))
                        // Using stabilize to pass the groupID of the newly created group from the Create API to the Read API.
                    .stabilize((createRequest, createResponse, clientProxy, model, context) -> {
                        model.setMembershipId(createResponse.membershipId());
                        model.setIdentityStoreId(createResponse.identityStoreId());
                        return true;
                    })
                    .progress()
            )
            // describe call to return the resource model
            .then(response -> new ReadHandler().handleRequest(proxy, request, callbackContext, proxyClient, logger));
    }
}
