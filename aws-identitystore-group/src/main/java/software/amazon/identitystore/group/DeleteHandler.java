package software.amazon.identitystore.group;

import software.amazon.awssdk.services.identitystore.IdentitystoreClient;
import software.amazon.awssdk.services.identitystore.model.DescribeGroupRequest;
import software.amazon.cloudformation.proxy.AmazonWebServicesClientProxy;
import software.amazon.cloudformation.proxy.Logger;
import software.amazon.cloudformation.proxy.ProgressEvent;
import software.amazon.cloudformation.proxy.ProxyClient;
import software.amazon.cloudformation.proxy.ResourceHandlerRequest;
import software.amazon.identitystore.common.ExceptionTranslator;

public class DeleteHandler extends BaseHandlerStd {
    private Logger logger;

    public ProgressEvent<ResourceModel, CallbackContext> handleRequest(
        final AmazonWebServicesClientProxy proxy,
        final ResourceHandlerRequest<ResourceModel> request,
        final CallbackContext callbackContext,
        final ProxyClient<IdentitystoreClient> proxyClient,
        final Logger logger) {

        this.logger = logger;
        final ResourceModel model = request.getDesiredResourceState();

        // Throw Exception if the group doesn't exist. DeleteGroup returns 200 on deleting non-existent group.
        try {
            final DescribeGroupRequest describeGroupRequest = Translator.translateToDescribeGroupRequest(model);

            logger.log(String.format("Describing Group before deleting the Group [%s]", model.getGroupId()));
            proxyClient.injectCredentialsAndInvokeV2(describeGroupRequest, proxyClient.client()::describeGroup);
        } catch (Exception e) {
            logger.log(String.format("Exception caught when describing Resource [%s] with ID [%s]", ResourceModel.TYPE_NAME, model.getGroupId()));
            ExceptionTranslator.translateToCfnException(e, ResourceModel.TYPE_NAME);
        }

        logger.log(String.format("Invoked DeleteGroup Handler for Group [%s]", model.getGroupId()));

        return proxy.initiate("AWS-IdentityStore-Group::Delete", proxyClient, model, callbackContext)
                .translateToServiceRequest(Translator::translateToDeleteGroupRequest)
                .makeServiceCall((deleteGroupRequest, proxyClient1)
                        -> invoke(deleteGroupRequest, proxyClient1, proxyClient1.client()::deleteGroup, logger))
                .done(response -> ProgressEvent.defaultSuccessHandler(null));
    }
}
