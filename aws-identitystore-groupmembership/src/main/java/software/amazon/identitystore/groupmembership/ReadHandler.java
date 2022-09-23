package software.amazon.identitystore.groupmembership;

import software.amazon.awssdk.services.identitystore.IdentitystoreClient;
import software.amazon.cloudformation.proxy.AmazonWebServicesClientProxy;
import software.amazon.cloudformation.proxy.Logger;
import software.amazon.cloudformation.proxy.ProgressEvent;
import software.amazon.cloudformation.proxy.ProxyClient;
import software.amazon.cloudformation.proxy.ResourceHandlerRequest;

public class ReadHandler extends BaseHandlerStd {

    public ProgressEvent<ResourceModel, CallbackContext> handleRequest(
            final AmazonWebServicesClientProxy proxy,
            final ResourceHandlerRequest<ResourceModel> request,
            final CallbackContext callbackContext,
            final ProxyClient<IdentitystoreClient> proxyClient,
            final Logger logger) {

        final ResourceModel model = request.getDesiredResourceState();
        logger.log(String.format("Invoking Read GroupMembership Handler with MembershipId [%s]", model.getPrimaryIdentifier()));

        return proxy.initiate("AWS-IdentityStore-GroupMembership::READ", proxyClient, model, callbackContext)
                .translateToServiceRequest(Translator::translateToDescribeGroupMembershipRequest)
                .makeServiceCall((readRequest, clientProxy)
                        -> invoke(readRequest, clientProxy, clientProxy.client()::describeGroupMembership, logger))
                .done(response -> ProgressEvent.defaultSuccessHandler(Translator.setGroupMembershipProperties(model, response)));
    }
}
