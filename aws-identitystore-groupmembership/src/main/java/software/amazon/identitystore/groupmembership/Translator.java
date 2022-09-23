package software.amazon.identitystore.groupmembership;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import software.amazon.awssdk.services.identitystore.model.CreateGroupMembershipRequest;
import software.amazon.awssdk.services.identitystore.model.CreateGroupMembershipResponse;
import software.amazon.awssdk.services.identitystore.model.DeleteGroupMembershipRequest;
import software.amazon.awssdk.services.identitystore.model.DescribeGroupMembershipRequest;
import software.amazon.awssdk.services.identitystore.model.DescribeGroupMembershipResponse;
import software.amazon.awssdk.services.identitystore.model.GroupMembership;
import software.amazon.awssdk.services.identitystore.model.ListGroupMembershipsRequest;
import software.amazon.awssdk.services.identitystore.model.ListGroupMembershipsResponse;
import software.amazon.awssdk.services.identitystore.model.MemberId;

import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Translator {

    public static CreateGroupMembershipRequest translateToCreateRequest(ResourceModel model) {
        return CreateGroupMembershipRequest.builder()
                .identityStoreId(model.getIdentityStoreId())
                .groupId(model.getGroupId())
                .memberId(translateFromModelMemberId(model.getMemberId()))
                .build();
    }

    /**
     * Translates MemberId from ResourceModel type to Request/Response type
     * @param memberId - ResourceModel type
     * @return memberId - Request/Response type
     */
    public static MemberId translateFromModelMemberId(software.amazon.identitystore.groupmembership.MemberId memberId) {
        return MemberId.builder()
                .userId(memberId.getUserId())
                .build();
    }

    /**
     * Translate MemberId from Request/Response type to ResourceModel type
     * @param memberId - Request/Response type
     * @return memberId - ResourceModel type
     */
    public static software.amazon.identitystore.groupmembership.MemberId translateToModelMemberId(MemberId memberId) {
        return software.amazon.identitystore.groupmembership.MemberId.builder()
                .userId(memberId.userId())
                .build();
    }

    public static ResourceModel translateFromCreateResponse(ResourceModel model,
                                                            final CreateGroupMembershipResponse response) {
        model.setMembershipId(response.membershipId());
        model.setIdentityStoreId(response.identityStoreId());
        return model;
    }

    public static DeleteGroupMembershipRequest translateToDeleteGroupMembershipRequest(ResourceModel model) {
        return DeleteGroupMembershipRequest.builder()
                .identityStoreId(model.getIdentityStoreId())
                .membershipId(model.getMembershipId())
                .build();
    }

    public static DescribeGroupMembershipRequest translateToDescribeGroupMembershipRequest(ResourceModel model) {
        return DescribeGroupMembershipRequest.builder()
                .identityStoreId(model.getIdentityStoreId())
                .membershipId(model.getMembershipId())
                .build();
    }

    public static ResourceModel setGroupMembershipProperties(ResourceModel model,
                                                       final DescribeGroupMembershipResponse response) {
        model.setGroupId(response.groupId());
        model.setMemberId(Translator.translateToModelMemberId(response.memberId()));
        model.setIdentityStoreId(response.identityStoreId());
        return model;
    }

    public static ListGroupMembershipsRequest translateToListGroupMembershipsRequest(final ResourceModel model,
                                                                                     final String nextToken) {
        return ListGroupMembershipsRequest.builder()
                .identityStoreId(model.getIdentityStoreId())
                .groupId(model.getGroupId())
                .nextToken(nextToken)
                .build();
    }

    public static List<ResourceModel> translateFromListGroupMembershipsResponse(ListGroupMembershipsResponse response) {
        return response.groupMemberships()
                .stream()
                .map(groupMembership -> translateFromListResponse(groupMembership))
                .collect(Collectors.toList());
    }

    private static ResourceModel translateFromListResponse(final GroupMembership groupMembership) {
        return  ResourceModel.builder()
                .membershipId(groupMembership.membershipId())
                .identityStoreId(groupMembership.identityStoreId())
                .groupId(groupMembership.groupId())
                .memberId(translateToModelMemberId(groupMembership.memberId()))
                .build();
    }
}
