package software.amazon.identitystore.groupmembership;

import software.amazon.awssdk.services.identitystore.IdentitystoreClient;
import software.amazon.awssdk.services.identitystore.model.DescribeGroupMembershipRequest;
import software.amazon.cloudformation.proxy.AmazonWebServicesClientProxy;
import software.amazon.cloudformation.proxy.Logger;
import software.amazon.cloudformation.proxy.ProgressEvent;
import software.amazon.cloudformation.proxy.ProxyClient;
import software.amazon.cloudformation.proxy.ResourceHandlerRequest;
import software.amazon.identitystore.common.ExceptionTranslator;

public class DeleteHandler extends BaseHandlerStd {

    public ProgressEvent<ResourceModel, CallbackContext> handleRequest(
            final AmazonWebServicesClientProxy proxy,
            final ResourceHandlerRequest<ResourceModel> request,
            final CallbackContext callbackContext,
            final ProxyClient<IdentitystoreClient> proxyClient,
            final Logger logger) {

        final ResourceModel model = request.getDesiredResourceState();

        // Throw Exception if the groupMembership doesn't exist. DeleteGroupMembership returns 200 on deleting non-existent
        // groupMembership.
        try {
            final DescribeGroupMembershipRequest describeRequest = Translator.translateToDescribeGroupMembershipRequest(model);

            logger.log(String.format("Describing GroupMembership before deleting Member with Id [%s]", model.getPrimaryIdentifier()));
            proxyClient.injectCredentialsAndInvokeV2(describeRequest, proxyClient.client()::describeGroupMembership);
        } catch (Exception e) {
            logger.log(String.format("Exception caught when describing Resource [%s] with ID [%s]", ResourceModel.TYPE_NAME, model.getMembershipId()));
            ExceptionTranslator.translateToCfnException(e, ResourceModel.TYPE_NAME);
        }

        logger.log(String.format("Invoked DeleteGroupMembership Handler with MembershipId [%s]", model.getPrimaryIdentifier()));

        return  proxy.initiate("AWS-IdentityStore-GroupMembership::Delete", proxyClient, model, callbackContext)
                .translateToServiceRequest(Translator::translateToDeleteGroupMembershipRequest)
                .makeServiceCall((deleteRequest, clientProxy)
                        -> invoke(deleteRequest, clientProxy, clientProxy.client()::deleteGroupMembership, logger))
                .done(response ->  ProgressEvent.defaultSuccessHandler(null));
    }
}
