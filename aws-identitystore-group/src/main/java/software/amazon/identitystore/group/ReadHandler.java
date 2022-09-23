package software.amazon.identitystore.group;

import software.amazon.awssdk.services.identitystore.IdentitystoreClient;
import software.amazon.cloudformation.proxy.AmazonWebServicesClientProxy;
import software.amazon.cloudformation.proxy.Logger;
import software.amazon.cloudformation.proxy.ProgressEvent;
import software.amazon.cloudformation.proxy.ProxyClient;
import software.amazon.cloudformation.proxy.ResourceHandlerRequest;

public class ReadHandler extends BaseHandlerStd {
    private Logger logger;

    public ProgressEvent<ResourceModel, CallbackContext> handleRequest(
        final AmazonWebServicesClientProxy proxy,
        final ResourceHandlerRequest<ResourceModel> request,
        final CallbackContext callbackContext,
        final ProxyClient<IdentitystoreClient> proxyClient,
        final Logger logger) {

        this.logger = logger;
        final ResourceModel model = request.getDesiredResourceState();

        logger.log(String.format("Invoking ReadGroup Handler with GroupId [%s]", model.getGroupId()));

        return proxy.initiate("AWS-IdentityStore-Group::READ", proxyClient, model, callbackContext)
                .translateToServiceRequest(Translator::translateToDescribeGroupRequest)
                .makeServiceCall((describeGroupRequest, clientProxy)
                        -> invoke(describeGroupRequest, clientProxy, clientProxy.client()::describeGroup, logger))
                .done(response -> ProgressEvent.defaultSuccessHandler(Translator.setGroupProperties(model, response)));
    }
}
