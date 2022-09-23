package software.amazon.identitystore.groupmembership;

import software.amazon.awssdk.awscore.AwsRequest;
import software.amazon.awssdk.awscore.AwsResponse;
import software.amazon.awssdk.services.identitystore.IdentitystoreClient;
import software.amazon.cloudformation.proxy.AmazonWebServicesClientProxy;
import software.amazon.cloudformation.proxy.Logger;
import software.amazon.cloudformation.proxy.ProgressEvent;
import software.amazon.cloudformation.proxy.ProxyClient;
import software.amazon.cloudformation.proxy.ResourceHandlerRequest;
import software.amazon.identitystore.common.ClientBuilder;
import software.amazon.identitystore.common.ExceptionTranslator;

import java.util.function.Function;

/**
 * This is a base class for used for handlers of IdentityStore GroupMembership Resource
 */
public abstract class BaseHandlerStd extends BaseHandler<CallbackContext> {
    @Override
    public ProgressEvent<ResourceModel, CallbackContext> handleRequest(
            final AmazonWebServicesClientProxy proxy,
            final ResourceHandlerRequest<ResourceModel> request,
            final CallbackContext callbackContext,
            final Logger logger) {

        return handleRequest(
                proxy,
                request,
                callbackContext != null ? callbackContext : new CallbackContext(),
                proxy.newProxy(() -> ClientBuilder.getClient()),
                logger);
    }

    protected abstract ProgressEvent<ResourceModel, CallbackContext> handleRequest(
            final AmazonWebServicesClientProxy proxy,
            final ResourceHandlerRequest<ResourceModel> request,
            final CallbackContext callbackContext,
            final ProxyClient<IdentitystoreClient> proxyClient,
            final Logger logger);

    /**
     * Method used to invoke the service API call
     * @param request RequestObject to make Service API call
     * @param proxyClient ServiceClient
     * @param requestFunction Service API to be invoked
     * @param logger Logger object
     * @param <RequestT> AwsRequest
     * @param <ResponseT>  AwsResponse
     * @return Service API response
     */
    protected static <RequestT extends AwsRequest, ResponseT extends AwsResponse> ResponseT invoke(
            final RequestT request,
            final ProxyClient<IdentitystoreClient> proxyClient,
            final Function<RequestT, ResponseT> requestFunction,
            final Logger logger) {
        ResponseT response = null;
        try {
            response = proxyClient.injectCredentialsAndInvokeV2(request, requestFunction);
        } catch (Exception e) {
            ExceptionTranslator.translateToCfnException(e, ResourceModel.TYPE_NAME);
        }
        return response;
    }

}
