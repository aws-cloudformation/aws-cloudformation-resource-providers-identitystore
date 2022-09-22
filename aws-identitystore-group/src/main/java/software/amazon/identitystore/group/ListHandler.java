package software.amazon.identitystore.group;

import software.amazon.awssdk.services.identitystore.IdentitystoreClient;
import software.amazon.cloudformation.proxy.AmazonWebServicesClientProxy;
import software.amazon.cloudformation.proxy.Logger;
import software.amazon.cloudformation.proxy.ProgressEvent;
import software.amazon.cloudformation.proxy.OperationStatus;
import software.amazon.cloudformation.proxy.ProxyClient;
import software.amazon.cloudformation.proxy.ResourceHandlerRequest;


public class ListHandler extends BaseHandlerStd {
    private Logger logger;

    public ProgressEvent<ResourceModel, CallbackContext> handleRequest(
        final AmazonWebServicesClientProxy proxy,
        final ResourceHandlerRequest<ResourceModel> request,
        final CallbackContext callbackContext,
        final ProxyClient<IdentitystoreClient> proxyClient,
        final Logger logger) {

        this.logger = logger;

        final ResourceModel resourceModel = request.getDesiredResourceState();

        logger.log(String.format("Invoked ListGroup Handler with IdentityStore [%s]", resourceModel.getIdentityStoreId()));

        return proxy.initiate("AWS-IdentityStore-Group::List", proxyClient, resourceModel, callbackContext)
                .translateToServiceRequest((model) -> Translator.translateToListGroupRequest(model, request.getNextToken()))
                .makeServiceCall((listRequest, proxyClient1)
                        -> invoke(listRequest, proxyClient1, proxyClient1.client()::listGroups, logger))
                .done(((listGroupsRequest, response, proxyClient1, resourceModel1, callbackContext1) ->
                        ProgressEvent.<ResourceModel, CallbackContext>builder()
                                .status(OperationStatus.SUCCESS)
                                .resourceModels(Translator.translateFromListResponse(response))
                                .nextToken(response.nextToken())
                                .build()));
    }
}
