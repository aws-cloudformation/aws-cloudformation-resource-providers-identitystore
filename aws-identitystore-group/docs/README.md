# AWS::IdentityStore::Group

Resource Type definition for AWS::IdentityStore::Group

## Syntax

To declare this entity in your AWS CloudFormation template, use the following syntax:

### JSON

<pre>
{
    "Type" : "AWS::IdentityStore::Group",
    "Properties" : {
        "<a href="#description" title="Description">Description</a>" : <i>String</i>,
        "<a href="#displayname" title="DisplayName">DisplayName</a>" : <i>String</i>,
        "<a href="#identitystoreid" title="IdentityStoreId">IdentityStoreId</a>" : <i>String</i>
    }
}
</pre>

### YAML

<pre>
Type: AWS::IdentityStore::Group
Properties:
    <a href="#description" title="Description">Description</a>: <i>String</i>
    <a href="#displayname" title="DisplayName">DisplayName</a>: <i>String</i>
    <a href="#identitystoreid" title="IdentityStoreId">IdentityStoreId</a>: <i>String</i>
</pre>

## Properties

#### Description

A string containing the description of the group.

_Required_: No

_Type_: String

_Minimum_: <code>1</code>

_Maximum_: <code>1024</code>

_Pattern_: <code>^[\p{L}\p{M}\p{S}\p{N}\p{P}\t\n\r  　]+$</code>

_Update requires_: [No interruption](https://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/using-cfn-updating-stacks-update-behaviors.html#update-no-interrupt)

#### DisplayName

A string containing the name of the group. This value is commonly displayed when the group is referenced.

_Required_: No

_Type_: String

_Minimum_: <code>1</code>

_Maximum_: <code>1024</code>

_Pattern_: <code>^[\p{L}\p{M}\p{S}\p{N}\p{P}\t\n\r  ]+$</code>

_Update requires_: [No interruption](https://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/using-cfn-updating-stacks-update-behaviors.html#update-no-interrupt)

#### IdentityStoreId

The globally unique identifier for the identity store.

_Required_: No

_Type_: String

_Minimum_: <code>1</code>

_Maximum_: <code>36</code>

_Pattern_: <code>^d-[0-9a-f]{10}$|^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$</code>

_Update requires_: [Replacement](https://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/using-cfn-updating-stacks-update-behaviors.html#update-replacement)

## Return Values

### Fn::GetAtt

The `Fn::GetAtt` intrinsic function returns a value for a specified attribute of this type. The following are the available attributes and sample return values.

For more information about using the `Fn::GetAtt` intrinsic function, see [Fn::GetAtt](https://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/intrinsic-function-reference-getatt.html).

#### GroupId

The unique identifier for a group in the identity store.
