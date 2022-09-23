# AWS::IdentityStore::GroupMembership

Resource Type Definition for AWS:IdentityStore::GroupMembership

## Syntax

To declare this entity in your AWS CloudFormation template, use the following syntax:

### JSON

<pre>
{
    "Type" : "AWS::IdentityStore::GroupMembership",
    "Properties" : {
        "<a href="#groupid" title="GroupId">GroupId</a>" : <i>String</i>,
        "<a href="#identitystoreid" title="IdentityStoreId">IdentityStoreId</a>" : <i>String</i>,
        "<a href="#memberid" title="MemberId">MemberId</a>" : <i><a href="memberid.md">MemberId</a></i>,
    }
}
</pre>

### YAML

<pre>
Type: AWS::IdentityStore::GroupMembership
Properties:
    <a href="#groupid" title="GroupId">GroupId</a>: <i>String</i>
    <a href="#identitystoreid" title="IdentityStoreId">IdentityStoreId</a>: <i>String</i>
    <a href="#memberid" title="MemberId">MemberId</a>: <i><a href="memberid.md">MemberId</a></i>
</pre>

## Properties

#### GroupId

The unique identifier for a group in the identity store.

_Required_: No

_Type_: String

_Minimum_: <code>1</code>

_Maximum_: <code>47</code>

_Pattern_: <code>^([0-9a-f]{10}-|)[A-Fa-f0-9]{8}-[A-Fa-f0-9]{4}-[A-Fa-f0-9]{4}-[A-Fa-f0-9]{4}-[A-Fa-f0-9]{12}$</code>

_Update requires_: [No interruption](https://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/using-cfn-updating-stacks-update-behaviors.html#update-no-interrupt)

#### IdentityStoreId

The globally unique identifier for the identity store.

_Required_: No

_Type_: String

_Minimum_: <code>1</code>

_Maximum_: <code>36</code>

_Pattern_: <code>^d-[0-9a-f]{10}$|^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$</code>

_Update requires_: [Replacement](https://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/using-cfn-updating-stacks-update-behaviors.html#update-replacement)

#### MemberId

An object containing the identifier of a group member.

_Required_: No

_Type_: <a href="memberid.md">MemberId</a>

_Update requires_: [No interruption](https://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/using-cfn-updating-stacks-update-behaviors.html#update-no-interrupt)

## Return Values

### Fn::GetAtt

The `Fn::GetAtt` intrinsic function returns a value for a specified attribute of this type. The following are the available attributes and sample return values.

For more information about using the `Fn::GetAtt` intrinsic function, see [Fn::GetAtt](https://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/intrinsic-function-reference-getatt.html).

#### MembershipId

The identifier for a GroupMembership in the identity store.
