package software.amazon.identitystore.group;

import software.amazon.awssdk.services.identitystore.IdentitystoreClient;
import software.amazon.awssdk.services.identitystore.model.CreateGroupRequest;
import software.amazon.awssdk.services.identitystore.model.CreateGroupResponse;
import software.amazon.cloudformation.proxy.AmazonWebServicesClientProxy;
import software.amazon.cloudformation.proxy.Logger;
import software.amazon.cloudformation.proxy.ProgressEvent;
import software.amazon.cloudformation.proxy.ProxyClient;
import software.amazon.cloudformation.proxy.ResourceHandlerRequest;

public class CreateHandler extends BaseHandlerStd {
    private Logger logger;

    /**
     * * The resource creation process involves following steps:
     *  1) Initiate the call context for the API.
     *  2) Transform the incoming resource model properties to the underlying service API request
     *  3) Make the service call
     *  4) Handle errors (Optional)
     *  5) Handle stabilization (Optional, if you need resource to be in a specific state before you apply the next state.)
     *  6) Finalize progress to the next part of the call chain, or indicate successful completion.
     */
    public ProgressEvent<ResourceModel, CallbackContext> handleRequest(
            final AmazonWebServicesClientProxy proxy,
            final ResourceHandlerRequest<ResourceModel> request,
            final CallbackContext callbackContext,
            final ProxyClient<IdentitystoreClient> proxyClient,
            final Logger logger) {

        this.logger = logger;
        final ResourceModel resourceModel = request.getDesiredResourceState();

        logger.log(String.format("Invoking CreateGroup Handler with IdentityStore [%s]", resourceModel.getIdentityStoreId()));

        return ProgressEvent.progress(resourceModel, callbackContext)
                .then(progress ->
                    proxy.initiate("AWS-IdentityStore-Group::Create", proxyClient, progress.getResourceModel(), callbackContext)
                        .translateToServiceRequest(Translator::translateToCreateGroupRequest)
                        .makeServiceCall((createRequest, clientProxy)
                                -> invoke(createRequest, clientProxy, clientProxy.client()::createGroup, logger))
                        // Using stabilize to pass the groupID of the newly created group from the Create API to the Read API.
                        .stabilize((createGroupRequest, createGroupResponse, clientProxy, model, context) -> {
                            model.setIdentityStoreId(createGroupResponse.identityStoreId());
                            model.setGroupId(createGroupResponse.groupId());
                            return true;
                        })
                        .progress()
                )
                // describe call to return the resource model
                .then(response -> new ReadHandler().handleRequest(proxy, request, callbackContext, proxyClient, logger));
    }
}
