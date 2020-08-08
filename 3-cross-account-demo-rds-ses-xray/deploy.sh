#!/usr/bin/env bash

set -e

if [ -z $PROJECT_NAME ]; then
    echo "PROJECT_NAME environment variable is not set."
    exit 1
fi

if [ -z $AWS_PRIMARY_ACCOUNT_ID ]; then
    echo "AWS_PRIMARY_ACCOUNT_ID environment variable is not set."
    exit 1
fi

if [ -z $AWS_SECONDARY_ACCOUNT_ID ]; then
    echo "AWS_SECONDARY_ACCOUNT_ID environment variable is not set."
    exit 1
fi

if [ -z $AWS_PRIMARY_PROFILE ]; then
    echo "AWS_PRIMARY_PROFILE environment variable is not set."
    exit 1
fi

if [ -z $AWS_SECONDARY_PROFILE ]; then
    echo "AWS_SECONDARY_PROFILE environment variable is not set."
    exit 1
fi

if [ -z $AWS_DEFAULT_REGION ]; then
    echo "AWS_DEFAULT_REGION environment variable is not set."
    exit 1
fi

if [ -z $ENVOY_IMAGE ]; then
    echo "ENVOY_IMAGE environment variable is not set to App Mesh Envoy, see https://docs.aws.amazon.com/app-mesh/latest/userguide/envoy.html"
    exit 1
fi

if [ -z $DEALER_IMAGE_VER1 ]; then
    echo "DEALER_IMAGE_VER1 environment variable is not set."
    exit 1
fi

if [ -z $DEALER_IMAGE_VER2 ]; then
    echo "DEALER_IMAGE_VER2 environment variable is not set."
    exit 1
fi

if [ -z $KEY_PAIR ]; then
    echo "KEY_PAIR environment variable is not set. This must be the name of an SSH key pair, see https://docs.aws.amazon.com/AWSEC2/latest/UserGuide/ec2-key-pairs.html"
    exit 1
fi

DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" >/dev/null && pwd)"
PRIMARY_ACCOUNT_DIR="${DIR}/primary-account"
SECONDARY_ACCOUNT_DIR="${DIR}/secondary-account"

update_route() {
    echo "Updating route........."
    aws --profile ${AWS_SECONDARY_PROFILE} appmesh update-route \
    --mesh-owner ${AWS_PRIMARY_ACCOUNT_ID} \
    --mesh-name ${PROJECT_NAME}-mesh \
    --virtual-router-name dealer-vr \
    --route-name weighted-route \
    --cli-input-json file://`pwd`/updated-route.json
    echo "Route updated...."
}
print_endpoint() {
    echo "Setting AWS_PROFILE=${AWS_PRIMARY_PROFILE} to get public endpoint"
    export AWS_PROFILE=${AWS_PRIMARY_PROFILE}
    echo "Public endpoint:"
    endpoint=$(aws cloudformation describe-stacks \
        --stack-name="${PROJECT_NAME}-app" \
        --query="Stacks[0].Outputs[?OutputKey=='PublicEndpoint'].OutputValue" \
        --output=text)
        
    echo "Application is available at ${endpoint}"

    bastionip=$(aws cloudformation describe-stacks \
        --stack-name="${PROJECT_NAME}-infra" \
        --query="Stacks[0].Outputs[?OutputKey=='BastionIp'].OutputValue" \
        --output=text)
    echo "Bastion ip is ${bastionip}"

    rdsdnsendpoint=$(aws cloudformation describe-stacks \
        --stack-name="${PROJECT_NAME}-rds" \
        --query="Stacks[0].Outputs[?OutputKey=='RDSEndPoint'].OutputValue" \
        --output=text)
    echo "RDS DNS Endpoint is ${rdsdnsendpoint}"

}

action=${1:-"deploy"}
if [ "$action" == "delete" ]; then
    ${SECONDARY_ACCOUNT_DIR}/deploy.sh delete
    ${PRIMARY_ACCOUNT_DIR}/deploy.sh delete
    exit 0
fi

if [ "$action" == "deploy" ]; then
    ${PRIMARY_ACCOUNT_DIR}/deploy.sh deploy_1
    ${SECONDARY_ACCOUNT_DIR}/deploy.sh deploy
    ${PRIMARY_ACCOUNT_DIR}/deploy.sh deploy_2
    print_endpoint
fi

if [ "$action" == "update-route" ]; then
    update_route
fi