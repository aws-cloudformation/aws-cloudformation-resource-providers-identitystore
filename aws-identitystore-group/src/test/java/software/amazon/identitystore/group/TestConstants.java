package software.amazon.identitystore.group;

import software.amazon.awssdk.services.identitystore.model.Group;

public class TestConstants {

    public final static String DESCRIPTION = "Description";
    public final static String DISPLAY_NAME = "DisplayName";

    public final static String TEST_IDENTITY_STORE_ID = "d-123456789";
    public final static String TEST_GROUP_DISPLAY_NAME = "testDisplayName";
    public final static String TEST_GROUP_DESCRIPTION = "This is a test group description";
    public final static String UPDATED_GROUP_DESCRIPTION = "Updated group description";
    public final static String TEST_GROUP_ID = "9067059d54-4ec2679b-5de9-49c7-b94e-8f6a84e08200";

    public final static String TEST_NEXT_TOKEN = "testNextToken";
    public final static String TEST_EXTERNAL_ID_VALUE = "testExternalId";
    public final static String TEST_EXTERNAL_ID_ISSUER_VALUE = "testExternalIdIssuer";


    public final static Group TESTGROUP = Group.builder()
            .groupId(TEST_GROUP_ID)
            .description(TEST_GROUP_DESCRIPTION)
            .displayName(TEST_GROUP_DISPLAY_NAME)
            .identityStoreId(TEST_IDENTITY_STORE_ID)
            .build();
}
