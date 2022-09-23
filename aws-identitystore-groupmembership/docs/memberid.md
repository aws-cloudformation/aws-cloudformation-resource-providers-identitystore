# AWS::IdentityStore::GroupMembership MemberId

An object containing the identifier of a group member.

## Syntax

To declare this entity in your AWS CloudFormation template, use the following syntax:

### JSON

<pre>
{
    "<a href="#userid" title="UserId">UserId</a>" : <i>String</i>
}
</pre>

### YAML

<pre>
<a href="#userid" title="UserId">UserId</a>: <i>String</i>
</pre>

## Properties

#### UserId

The identifier for a user in the identity store.

_Required_: Yes

_Type_: String

_Minimum_: <code>1</code>

_Maximum_: <code>47</code>

_Pattern_: <code>^([0-9a-f]{10}-|)[A-Fa-f0-9]{8}-[A-Fa-f0-9]{4}-[A-Fa-f0-9]{4}-[A-Fa-f0-9]{4}-[A-Fa-f0-9]{12}$</code>

_Update requires_: [No interruption](https://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/using-cfn-updating-stacks-update-behaviors.html#update-no-interrupt)
