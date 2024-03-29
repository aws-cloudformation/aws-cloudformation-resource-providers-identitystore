## AWS CloudFormation Resource Provider Package for `AWS::IdentityStore::*` namespace

Usage
-----
This package contains resource handlers for following resources
- AWS::IdentityStore::Group
- AWS::IdentityStore::GroupMembership

Development
-----------

First, you will need to install the [CloudFormation CLI](https://github.com/aws-cloudformation/aws-cloudformation-rpdk), as it is a required dependency:

```shell
pip3 install cloudformation-cli
pip3 install cloudformation-cli-java-plugin
```

Linting and running unit tests is done via [pre-commit](https://pre-commit.com/), and so is performed automatically on commit. The continuous integration also runs these checks.

```shell
pre-commit install
```

Manual options are available so you don't have to commit:

```shell
# run all hooks on all files, mirrors what the CI runs
pre-commit run --all-files
# run unit tests and coverage checks
mvn verify
```

## Security

See [CONTRIBUTING](CONTRIBUTING.md#security-issue-notifications) for more information.

## License

This project is licensed under the Apache-2.0 License.
