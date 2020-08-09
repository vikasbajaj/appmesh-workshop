#!/usr/bin/env bash

set -e

if [ -z $BOOK_CATALOGUE_IMAGE ]; then
    echo "Book Catalogue container image is not set"
    exit 1
fi

if [ -z $BOOK_ORDER_IMAGE ]; then
    echo "Book Order container image is not set"
    exit 1
fi

if [ -z $EMAIL_IMAGE ]; then
    echo "Email Notification container image is not set"
    exit 1
fi

if [ -z $AWS_PRIMARY_ACCOUNT_ID ]; then
    echo "AWS Account ID is not set"
    exit 1
fi

if [ -z $PROJECT_NAME ]; then
    echo "Project Name is not set"
    exit 1
fi

if [ -z $AWS_DEFAULT_REGION ]; then
    echo "AWS Default Region is not set"
    exit 1
fi

if [ -z $AWS_DEFAULT_REGION ]; then
    echo "AWS Default Region is not set"
    exit 1
fi

if [ -z $KEY_PAIR ]; then
    echo "AWS Key Pair is not set"
    exit 1
fi

if [ -z $ENVOY_IMAGE ]; then
    echo "Envoy Image is not set"
    exit 1
fi

if [ -z $DB_USER ]; then
    echo "DB User is not set"
    exit 1
fi

if [ -z $DB_PWD ]; then
    echo "DB Password is not set"
    exit 1
fi

if [ -z $DB_NAME ]; then
    echo "DB Name is not set"
    exit 1
fi

if [ -z $SES_USER ]; then
    echo "SES User is not set"
    exit 1
fi

if [ -z $SES_PWD ]; then
    echo "SES Password is not set"
    exit 1
fi

if [ -z $EMAIL_FROM ]; then
    echo "Email From is not set"
    exit 1
fi

if [ -z $SES_ENDPOINT ]; then
    echo "SES Endpoint is not set"
    exit 1
fi


DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" >/dev/null && pwd)"

deploy_infra(){
    echo "Deploying Cloud Formation stack: \"${PROJECT_NAME}-infra\" containing ALB, ECS Tasks, and Services..."
    aws cloudformation deploy \
        --no-fail-on-empty-changeset \
        --stack-name "${PROJECT_NAME}-infra" \
        --template-file "${DIR}/01-infra.yaml" \
        --capabilities CAPABILITY_IAM \
        --parameter-overrides "ProjectName=${PROJECT_NAME}" "KeyPair=${KEY_PAIR}"
}
deploy_rds(){
    echo "Deploying Cloud Formation stack: \"${PROJECT_NAME}-rds\" containing RDS..."
    aws cloudformation deploy \
        --no-fail-on-empty-changeset \
        --stack-name "${PROJECT_NAME}-rds" \
        --template-file "${DIR}/02-rds.yaml" \
        --capabilities CAPABILITY_IAM \
        --parameter-overrides "ProjectName=${PROJECT_NAME}" "DBName=${DB_NAME}" "DBUser=${DB_USER}" "DBPassword=${DB_PWD}"
}
deploy_mesh(){
    echo "Deploying Cloud Formation stack: \"${PROJECT_NAME}-mesh\" containing Mesh configuration..."
    aws cloudformation deploy \
        --no-fail-on-empty-changeset \
        --stack-name "${PROJECT_NAME}-mesh" \
        --template-file "${DIR}/03-mesh.yaml" \
        --capabilities CAPABILITY_IAM \
        --parameter-overrides "ProjectName=${PROJECT_NAME}"
}
deploy_app(){
    echo "Deploying Cloud Formation stack: \"${PROJECT_NAME}-app\" containing app configuration..."
    aws cloudformation deploy \
        --no-fail-on-empty-changeset \
        --stack-name "${PROJECT_NAME}-app" \
        --template-file "${DIR}/04-app.yaml" \
        --capabilities CAPABILITY_IAM \
        --parameter-overrides "ProjectName=${PROJECT_NAME}" "SESSMTPEndPoint=${SES_ENDPOINT}" "SESSMTPUserName=${SES_USER}" "SESSMTPPassowrd=${SES_PWD}" "EmailFrom=${EMAIL_FROM}"
}
deploy_stacks() {
    deploy_infra
    deploy_rds
    deploy_mesh
    deploy_app
}

delete_cfn_stack() {
    stack_name=$1
    echo "Deleting Cloud Formation stack: \"${stack_name}\"..."
    aws cloudformation delete-stack --stack-name $stack_name
    echo 'Waiting for the stack to be deleted, this may take a few minutes...'
    aws cloudformation wait stack-delete-complete --stack-name $stack_name
    echo 'Done'
}


delete_stacks() {
    delete_cfn_stack "${PROJECT_NAME}-app"
    delete_cfn_stack "${PROJECT_NAME}-mesh"
    delete_cfn_stack "${PROJECT_NAME}-rds"
    delete_cfn_stack "${PROJECT_NAME}-infra"
    echo "all resources for primary account have been deleted"
}
load_values(){
    EC2EndPoint=$(aws cloudformation describe-stacks \
        --stack-name="${PROJECT_NAME}-infra" \
        --query="Stacks[0].Outputs[?OutputKey=='BastionIp'].OutputValue" \
        --output=text)

    ALBLoadBalancerDNSEndpoint=$(aws cloudformation describe-stacks \
        --stack-name="${PROJECT_NAME}-infra" \
        --query="Stacks[0].Outputs[?OutputKey=='ALBLoadBalancerDNSEndpoint'].OutputValue" \
        --output=text)
    RDSEndpoint=$(aws cloudformation describe-stacks \
        --stack-name="${PROJECT_NAME}-rds" \
        --query="Stacks[0].Outputs[?OutputKey=='RDSEndpoint'].OutputValue" \
        --output=text)

    echo "EC2 Public IP -----------> ${EC2EndPoint}"
    echo "ALB DNS Endpopint is -----------> ${ALBLoadBalancerDNSEndpoint}"
    echo "RDS DNS Endpoint is ----------> ${RDSEndpoint}"
}
action=${1:-"deploy"}

if [ "$action" == "delete" ]; then
    delete_stacks
    exit 0
fi

if [ "$action" == "deploy" ]; then
    deploy_stacks
    load_values
    exit 0
fi
