package software.amazon.identitystore.common;

import software.amazon.awssdk.services.identitystore.IdentitystoreClient;
import software.amazon.cloudformation.LambdaWrapper;

import java.net.URI;

public class ClientBuilder {

  /**
   * Create an IdentityStore Client
   * @return IdentityStoreClient
   */
  public static IdentitystoreClient getClient() {
    return IdentitystoreClient.builder()
            .httpClient(LambdaWrapper.HTTP_CLIENT)
            .build();
  }
}
