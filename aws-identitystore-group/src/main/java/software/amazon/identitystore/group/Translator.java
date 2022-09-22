package software.amazon.identitystore.group;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import software.amazon.awssdk.core.document.Document;
import software.amazon.awssdk.services.identitystore.model.CreateGroupRequest;
import software.amazon.awssdk.services.identitystore.model.DeleteGroupRequest;
import software.amazon.awssdk.services.identitystore.model.DescribeGroupRequest;
import software.amazon.awssdk.services.identitystore.model.DescribeGroupResponse;
import software.amazon.awssdk.services.identitystore.model.Group;
import software.amazon.awssdk.services.identitystore.model.ListGroupsRequest;
import software.amazon.awssdk.services.identitystore.model.ListGroupsResponse;
import software.amazon.awssdk.services.identitystore.model.UpdateGroupRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * This class is a centralized placeholder for
 *  - api request construction
 *  - object translation to/from aws sdk
 *  - resource model construction for read/list handlers
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Translator {

    public final static String DESCRIPTION = "Description";
    public final static String DISPLAY_NAME = "DisplayName";

    public static CreateGroupRequest translateToCreateGroupRequest(ResourceModel model) {
        return CreateGroupRequest.builder()
            .identityStoreId(model.getIdentityStoreId())
            .displayName(model.getDisplayName())
            .description(model.getDescription())
            .build();
    }

    public static DeleteGroupRequest translateToDeleteGroupRequest(ResourceModel model) {
        return DeleteGroupRequest.builder()
                .identityStoreId(model.getIdentityStoreId())
                .groupId(model.getGroupId())
                .build();
    }

    public static DescribeGroupRequest translateToDescribeGroupRequest(ResourceModel model) {
        return DescribeGroupRequest.builder()
                .identityStoreId(model.getIdentityStoreId())
                .groupId(model.getGroupId())
                .build();
    }

    public static ResourceModel setGroupProperties(final ResourceModel model, final DescribeGroupResponse response) {
        model.setGroupId(response.groupId());
        model.setDisplayName(response.displayName());
        model.setDescription(response.description());
        model.setIdentityStoreId(response.identityStoreId());
        return model;
    }

    public static ListGroupsRequest translateToListGroupRequest(ResourceModel model, final String nextToken) {
        return ListGroupsRequest.builder()
                .identityStoreId(model.getIdentityStoreId())
                .nextToken(nextToken)
                .build();
    }

    static List<ResourceModel> translateFromListResponse(final ListGroupsResponse response) {
        return  response.groups()
                .stream()
                .map(group -> translateFromListGroupResponse(group))
                .collect(Collectors.toList());
    }

    static ResourceModel translateFromListGroupResponse(final Group group) {
        return  ResourceModel.builder()
                .groupId(group.groupId())
                .identityStoreId(group.identityStoreId())
                .description(group.description())
                .displayName(group.displayName())
                .build();
    }

    public static UpdateGroupRequest translateToUpdateGroupRequest(ResourceModel model) {
        return UpdateGroupRequest.builder()
                .identityStoreId(model.getIdentityStoreId())
                .groupId(model.getGroupId())
                .operations(convertAttributesToOperation(model))
                .build();
    }

    private static List<software.amazon.awssdk.services.identitystore.model.AttributeOperation> convertAttributesToOperation(
            ResourceModel model) {
        List<software.amazon.awssdk.services.identitystore.model.AttributeOperation> operationList = new ArrayList<>();

        operationList.add(convertToOperation(DESCRIPTION, model.getDescription()));
        operationList.add(convertToOperation(DISPLAY_NAME, model.getDisplayName()));
        return operationList;
    }

    private static software.amazon.awssdk.services.identitystore.model.AttributeOperation convertToOperation(
            String attributePath,  String attributeValue) {
        if (Objects.isNull(attributeValue)) {
            return software.amazon.awssdk.services.identitystore.model.AttributeOperation.builder()
                .attributePath(attributePath)
                .build();
        }
        return software.amazon.awssdk.services.identitystore.model.AttributeOperation.builder()
                .attributePath(attributePath)
                .attributeValue(Document.fromString(attributeValue))
                .build();
    }
}
