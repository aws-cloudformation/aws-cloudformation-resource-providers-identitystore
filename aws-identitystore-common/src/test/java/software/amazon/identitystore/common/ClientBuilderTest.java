package software.amazon.identitystore.common;

import com.amazonaws.regions.Regions;
import org.junit.jupiter.api.Test;
import software.amazon.awssdk.services.identitystore.IdentitystoreClient;

import static org.assertj.core.api.Assertions.assertThat;

public class ClientBuilderTest {

    @Test
    public void testCreateCloudFrontClient() {
        IdentitystoreClient client = ClientBuilder.getClient();
        assertThat(client).isNotNull();
    }
}
