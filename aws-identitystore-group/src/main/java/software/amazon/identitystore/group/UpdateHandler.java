package software.amazon.identitystore.group;

import software.amazon.awssdk.services.identitystore.IdentitystoreClient;
import software.amazon.awssdk.services.identitystore.model.DescribeGroupRequest;
import software.amazon.cloudformation.proxy.AmazonWebServicesClientProxy;
import software.amazon.cloudformation.proxy.Logger;
import software.amazon.cloudformation.proxy.ProgressEvent;
import software.amazon.cloudformation.proxy.ProxyClient;
import software.amazon.cloudformation.proxy.ResourceHandlerRequest;
import software.amazon.identitystore.common.ExceptionTranslator;

public class UpdateHandler extends BaseHandlerStd {

    public ProgressEvent<ResourceModel, CallbackContext> handleRequest(
        final AmazonWebServicesClientProxy proxy,
        final ResourceHandlerRequest<ResourceModel> request,
        final CallbackContext callbackContext,
        final ProxyClient<IdentitystoreClient> proxyClient,
        final Logger logger) {

        final ResourceModel model = request.getDesiredResourceState();

        try {
            final DescribeGroupRequest describeGroupRequest = Translator.translateToDescribeGroupRequest(model);

            logger.log(String.format("Describing Group before Updating the Group [%s]", model.getGroupId()));
            proxyClient.injectCredentialsAndInvokeV2(describeGroupRequest, proxyClient.client()::describeGroup);
        } catch (Exception e) {
            logger.log(String.format("Exception caught when describing Resource [%s] with ID [%s]", ResourceModel.TYPE_NAME, model.getGroupId()));
            ExceptionTranslator.translateToCfnException(e, ResourceModel.TYPE_NAME);
        }

        logger.log(String.format("Invoked Group UpdateHandler with GroupId [%s]", model.getGroupId()));

        return ProgressEvent.progress(request.getDesiredResourceState(), callbackContext)
                .then(progress ->
                    proxy.initiate("AWS-IdentityStore-Group::Update", proxyClient, model, callbackContext)
                        .translateToServiceRequest(Translator::translateToUpdateGroupRequest)
                        .makeServiceCall((updateGroupRequest, proxyClient1)
                            -> invoke(updateGroupRequest, proxyClient1, proxyClient1.client()::updateGroup, logger))
                        .progress())
                // describe call to return the resource model
                .then(response -> new ReadHandler().handleRequest(proxy, request, callbackContext, proxyClient, logger));
    }
}
