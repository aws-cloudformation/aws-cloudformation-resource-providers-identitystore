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
        final ResourceModel model = request.getDesiredResourceState();

        logger.log(String.format("Invoking Create GroupMembership Handler with IdentityStore [%s]", model.getIdentityStoreId()));

        return proxy.initiate("AWS-IdentityStore-GroupMembership::Create", proxyClient, model, callbackContext)
                .translateToServiceRequest(Translator::translateToCreateRequest)
                .makeServiceCall((createRequest, clientProxy)
                        -> invoke(createRequest, clientProxy, clientProxy.client()::createGroupMembership, logger))
                .done(response -> ProgressEvent.defaultSuccessHandler(Translator.translateFromCreateResponse(model, response)));
    }
}
