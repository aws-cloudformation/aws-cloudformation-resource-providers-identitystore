package software.amazon.identitystore.groupmembership;

import software.amazon.awssdk.services.identitystore.IdentitystoreClient;
import software.amazon.cloudformation.proxy.AmazonWebServicesClientProxy;
import software.amazon.cloudformation.proxy.Logger;
import software.amazon.cloudformation.proxy.ProgressEvent;
import software.amazon.cloudformation.proxy.OperationStatus;
import software.amazon.cloudformation.proxy.ProxyClient;
import software.amazon.cloudformation.proxy.ResourceHandlerRequest;


public class ListHandler extends BaseHandlerStd {

    public ProgressEvent<ResourceModel, CallbackContext> handleRequest(
            final AmazonWebServicesClientProxy proxy,
            final ResourceHandlerRequest<ResourceModel> request,
            final CallbackContext callbackContext,
            final ProxyClient<IdentitystoreClient> proxyClient,
            final Logger logger) {

        final ResourceModel resourceModel = request.getDesiredResourceState();

        logger.log(String.format("Invoked ListGroupMembership Handler with IdentityStoreId [%s]", resourceModel.getIdentityStoreId()));

        return proxy.initiate("AWS-IdentityStore-GroupMembership::List", proxyClient, resourceModel, callbackContext)
                .translateToServiceRequest((model)
                        -> Translator.translateToListGroupMembershipsRequest(resourceModel, request.getNextToken()))
                .makeServiceCall((listRequest, clientProxy)
                        -> invoke(listRequest, clientProxy, clientProxy.client()::listGroupMemberships, logger))
                .done(((listGroupsRequest, response, proxyClient1, model, callbackContext1) ->
                        ProgressEvent.<ResourceModel, CallbackContext>builder()
                                .status(OperationStatus.SUCCESS)
                                .resourceModels(Translator.translateFromListGroupMembershipsResponse(response))
                                .nextToken(response.nextToken())
                                .build()));
    }
}
