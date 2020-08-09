## Overview

This section shows how to use App Mesh between multiple accounts for cross account communication. In this section, we will create a VPC and Mesh in one account (the primary account) and share those resources with another account (the secondary account). We will then deploy applications into the mesh that show they are communicating on a single network, configured by App Mesh.

![](./CrossAccount.png)

- Clone this repository and navigate to the 2-cross-account-appmesh folder, all commands should be run from this location.
    ```
    cd appmesh-workshop/2-cross-account-appmesh
    ```
- Edit the **vars.env** file and update values of variables as per your environment
    - **Account IDs**: This demo needs two accounts in the same AWS Organization.
        - AWS_PRIMARY_ACCOUNT_ID = primary account id e.g. 111111111111
        - AWS_SECONDARY_ACCOUNT_ID = secondary account id e.g. 222222222222
    - **AWS Profiles**: Set the profile name for Primary and Secondary AWS account, each profile should have credentials set:
        - To set the primary account profile with name "primary", use the following and provide the required details
            ```
                aws configure --profile primary
            ```
        - To set the secondary account profile with name "secondary", use the following and provide the required details
            ```
                aws configure --profile secondary
            ```
        - Verify if the profiles have been set correctly, check using

            ```
            cat ~/.aws/credentials
            ```
        - Set the profile names

            - AWS_PRIMARY_PROFILE=primary

            - AWS_SECONDARY_PROFILE=secondary

    - **Key Pair** : set to the name of an EC2 key pair. This key will be use to test dns lookup. key pair name from your primary account e.g. 
        - KEY_PAIR = key-pair-us-east-1

        See https://docs.aws.amazon.com/AWSEC2/latest/UserGuide/ec2-key-pairs.html
        
        Note: This key pair should be in the primary account. We will be creating a bastion host in the primary account.
   
4. Once you have set all the variables, your **vars.env** should be like (we have put some example values for your reference only)

    ```
    unset AWS_ACCESS_KEY_ID
    unset AWS_SECRET_ACCESS_KEY
    export AWS_PRIMARY_ACCOUNT_ID=111111111111
    export AWS_SECONDARY_ACCOUNT_ID=222222222222
    export AWS_PRIMARY_PROFILE=primary
    export AWS_SECONDARY_PROFILE=secondary
    export PROJECT_NAME=appmeshdemo
    export AWS_DEFAULT_REGION=us-east-1
    export KEY_PAIR=keypair-us-east-1
    export ENVOY_IMAGE=840364872350.dkr.ecr.us-east-1.amazonaws.com/aws-appmesh-envoy:v1.12.2.1-prod
    export BOOK_IMAGE=unleashcontainers/bookcatalogueservice:2.0
    export EMAIL_IMAGE=unleashcontainers/emailnotificationservice:2.0
    ```

5. To Source the `vars.env` file run the following location 

    ```
    decode-appmesh/cross-account-appmesh-basic
    
    ```

    ```
    $ source vars.env
    ```

    Check if environment variables have been setup by running the "env" on the terminal.

    ```
    env

    ```

6. Let's put things in action by deploying all stacks in primary and secondary accounts by running the following command from 

    Note: run it from this location : decode-appmesh/cross-account-appmesh-basic
           
    ```
    ./deploy.sh deploy

    ```
## Cross account mesh in action

1. After a few minutes, the applications should be deployed and you will see an output such as:
    ```
    Public endpoint:
    Application is available at http://test-Publi-1GPSQQLJIQ22I-188320172.us-east-1.elb.amazonaws.com
    ```
    http://test-Publi-1GPSQQLJIQ22I-188320172.us-east-1.elb.amazonaws.com is just an example, you will have a different DNS Endpoint

2. Try curling the Book API. Update the <DNS_ENDPOINT> in following command with your application endpoint
 
    ```
    curl -X POST -H "Content-Type: application/json" -d "{\"name\":\"Cross Account App mesh\",\"price\":13.5,\"genre\":\"Technical\",\"authorname\":\"appmesh demo\",\"authoremailid\":\"youremail@testmail.com\"}" <DNS_ENDPOINT>/bookcatalogue/books -v
    
    ```
    ```
    e.g.

    curl -X POST -H "Content-Type: application/json" -d "{\"name\":\"AWS Unleashed\",\"price\":15.5,\"genre\":\"Technical\",\"authorname\":\"ECS Workshop\",\"authoremailid\":\"testemail@gmail.com\"}" http://appme-Publi-1SCYCVAA02R5X-1518210722.us-east-1.elb.amazonaws.com/bookcatalogue/books -v

    ```

## Teardown
When you are done with the example you can delete everything we created by running:
    Note: run it from this location : decode-appmesh/cross-account-appmesh-basic

    ```
    ./deploy.sh delete
    ```


### Book Image

The book image is a microservice which listens on a **8081** port, exposes **/bookcatalogue/books** api which internally calls **/emailnotification/notify** api on email microservice on a **8083** port. 

### Email Image

The email image is a microservice which listens on a **8083** port, exposes **/emailnotification/notify** api. This is dummy api, only prints message "Email Sent", it doesn't actually send email. 

## Setup

------------------------------------------------------------
Once the installation is complete you will get ALB endpoint, Bastion IP and RDS Endpoint

# Application is available at <Application Load Balancer Endpoint>
# Bastion ip is <Bastion IP>
# RDS DNS Endpoint is <RDS DNS Endpoint>

####New Terminal to create required table in the database######
ssh -i your-region-ec2.pem ec2-user@<Bastion IP>
$ sudo yum install mysql
$ mysql --host=<RDS DNS Endpoint> --user=appmeshdemodb --password=appmeshdemodb appmeshdemodb
$ CREATE TABLE `appmeshdemodb`.`enquiry` (
    `id` INT NOT NULL AUTO_INCREMENT,
    `buyername` VARCHAR(45) NOT NULL,
    `buyeremailid` VARCHAR(100) NOT NULL,
    `cartype` VARCHAR(45) NOT NULL,
    `carbrand` VARCHAR(45) NOT NULL,
    `carid` VARCHAR(45) NOT NULL,
    PRIMARY KEY (`id`));

####New Terminal 
curl <Application Load Balancer Endpoint>/enquiry/healthcheck

curl -X POST -H "Content-Type: application/json" -d "{\"buyername\":\"Vikas Bajaj\",\"cartype\":\"SUV\",\"carid\":\"ABC10101\",\"carbrand\":\"Toyota\",\"buyeremailid\":\"testemail@gmail.com\"}" curl http://appme-Publi-UD9MCWWH53I-583636019.us-east-1.elb.amazonaws.com/enquiry/newenquiry -v

## you should receive an email from Dealer API version-1
./deploy.sh update-route

curl -X POST -H "Content-Type: application/json" -d "{\"buyername\":\"Vikas Bajaj\",\"cartype\":\"SUV\",\"carid\":\"ABC10101\",\"carbrand\":\"Toyota\",\"buyeremailid\":\"testemail@gmail.com\"}" curl <Application Load Balancer Endpoint>/enquiry/newenquiry -v
## you should receive an email from Dealer API version-2
---------------------------------------------------------------

https://docs.aws.amazon.com/cli/latest/reference/appmesh/update-route.html
