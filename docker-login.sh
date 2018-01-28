#!/bin/bash
## REQUIREMENTS
# profiles configured in your ~/.aws/config and aws-cli installed
## ENVIRONMENT VARIABLES REQUIRED
# example: export AWS_ACCOUNT="1234343"
# example: export AWS_PROFILE="myprofile"
# example: export AWS_DEFAULT_REGION="us-east-1"
## USAGE
# ./docker-login.sh repo

echo "$(aws ecr get-authorization-token | \
  jq -r '.authorizationData[].authorizationToken' | \
  base64 -D | cut -d: -f2)" | \
  docker login -u AWS \
    "https://${AWS_ACCOUNT}.dkr.ecr.${AWS_DEFAULT_REGION}.amazonaws.com/$1" \
    --password-stdin
