package software.amazon.identitystore.groupmembership;

import software.amazon.awssdk.services.identitystore.model.GroupMembership;
import software.amazon.awssdk.services.identitystore.model.MemberId;

public class TestConstants {

    public final static String TEST_IDENTITY_STORE_ID = "d-123456789";
    public final static String TEST_USER_ID = "test-user-id";
    public final static String TEST_GROUP_ID = "test-group-id";
    public static final String TEST_MEMBERSHIP_ID = "test-membership-id";

    public static final MemberId TEST_MODEL_MEMBER_ID_WITH_USER = MemberId.builder()
            .userId(TEST_USER_ID)
            .build();

    public static final software.amazon.identitystore.groupmembership.MemberId TEST_MEMBER_ID_WITH_USER =
            software.amazon.identitystore.groupmembership.MemberId.builder()
                    .userId(TEST_USER_ID)
                    .build();

    public static final GroupMembership TEST_GROUP_MEMBERSHIP = GroupMembership.builder()
            .groupId(TEST_GROUP_ID)
            .memberId(TEST_MODEL_MEMBER_ID_WITH_USER)
            .membershipId(TEST_MEMBERSHIP_ID)
            .identityStoreId(TEST_IDENTITY_STORE_ID)
            .build();
}
