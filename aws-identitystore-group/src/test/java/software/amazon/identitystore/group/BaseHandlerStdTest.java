package software.amazon.identitystore.group;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import software.amazon.awssdk.awscore.AwsRequest;
import software.amazon.awssdk.awscore.AwsResponse;
import software.amazon.awssdk.awscore.exception.AwsErrorDetails;
import software.amazon.awssdk.services.identitystore.IdentitystoreClient;
import software.amazon.awssdk.services.identitystore.model.AccessDeniedException;
import software.amazon.awssdk.services.identitystore.model.ConflictException;
import software.amazon.awssdk.services.identitystore.model.IdentitystoreException;
import software.amazon.awssdk.services.identitystore.model.InternalServerException;
import software.amazon.awssdk.services.identitystore.model.ResourceNotFoundException;
import software.amazon.awssdk.services.identitystore.model.ServiceQuotaExceededException;
import software.amazon.awssdk.services.identitystore.model.ThrottlingException;
import software.amazon.awssdk.services.identitystore.model.ValidationException;
import software.amazon.cloudformation.exceptions.CfnAccessDeniedException;
import software.amazon.cloudformation.exceptions.CfnGeneralServiceException;
import software.amazon.cloudformation.exceptions.CfnInvalidRequestException;
import software.amazon.cloudformation.exceptions.CfnNotFoundException;
import software.amazon.cloudformation.exceptions.CfnResourceConflictException;
import software.amazon.cloudformation.exceptions.CfnServiceInternalErrorException;
import software.amazon.cloudformation.exceptions.CfnServiceLimitExceededException;
import software.amazon.cloudformation.exceptions.CfnThrottlingException;
import software.amazon.cloudformation.proxy.AmazonWebServicesClientProxy;
import software.amazon.cloudformation.proxy.Credentials;
import software.amazon.cloudformation.proxy.LoggerProxy;
import software.amazon.cloudformation.proxy.ProxyClient;

import java.time.Duration;
import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BaseHandlerStdTest {
    private AmazonWebServicesClientProxy proxy;
    private LoggerProxy logger;

    @Mock
    private ProxyClient<IdentitystoreClient> proxyClient;
    @Mock
    private Function<AwsRequest, AwsResponse> function;

    @BeforeEach
    public void setup() {
        logger = new LoggerProxy();
        final Credentials MOCK_CREDENTIALS = new Credentials("accessKey", "secretKey", "token");
        proxy = new AmazonWebServicesClientProxy(logger, MOCK_CREDENTIALS, () -> Duration.ofSeconds(600).toMillis());
    }

    @Test
    public void testInvoke_success() {
        final AwsRequest request = mock(AwsRequest.class);
        final AwsResponse expectedResponse = mock(AwsResponse.class);
        when(proxyClient.injectCredentialsAndInvokeV2(request,function)).thenReturn(expectedResponse);
        AwsResponse awsResponse = BaseHandlerStd.invoke(request, proxyClient, function, logger);
        assertThat(awsResponse).isEqualTo(expectedResponse);
    }

    @Test
    public void testInvoke_throwsException() {
        final AwsRequest request = mock(AwsRequest.class);
        when(proxyClient.injectCredentialsAndInvokeV2(request,function)).thenThrow(RuntimeException.class);
        assertThrows(CfnGeneralServiceException.class, () ->
                BaseHandlerStd.invoke(request, proxyClient, function, logger));
    }
}
