package software.amazon.identitystore.groupmembership;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import software.amazon.awssdk.awscore.AwsRequest;
import software.amazon.awssdk.awscore.AwsResponse;
import software.amazon.awssdk.services.identitystore.IdentitystoreClient;
import software.amazon.cloudformation.proxy.AmazonWebServicesClientProxy;
import software.amazon.cloudformation.proxy.Credentials;
import software.amazon.cloudformation.proxy.LoggerProxy;
import software.amazon.cloudformation.proxy.ProxyClient;
import software.amazon.identitystore.groupmembership.BaseHandlerStd;

import java.time.Duration;
import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;
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
        final Credentials mockCredentials = new Credentials("accessKey", "secretKey", "token");
        proxy = new AmazonWebServicesClientProxy(logger, mockCredentials, () -> Duration.ofSeconds(600).toMillis());
    }

    @Test
    public void testInvoke_success() {
        final AwsRequest request = mock(AwsRequest.class);
        final AwsResponse expectedResponse = mock(AwsResponse.class);
        when(proxyClient.injectCredentialsAndInvokeV2(request,function)).thenReturn(expectedResponse);
        AwsResponse awsResponse = BaseHandlerStd.invoke(request, proxyClient, function, logger);
        assertThat(awsResponse).isEqualTo(expectedResponse);
    }
}
