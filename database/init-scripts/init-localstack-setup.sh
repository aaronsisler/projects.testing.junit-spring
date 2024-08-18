#!/bin/sh
echo "Begin: DynamoDB -> Create Table"
awslocal dynamodb create-table \
    --cli-input-json file://dynamodb-table-definition.json \
    --region us-east-1
echo "End: DynamoDB -> Create Table"
#
echo "Begin: DynamoDB -> List Tables"
#
awslocal dynamodb list-tables
#
echo "End: DynamoDB -> List Tables"
#
# The below isn't used currently but it was working before
# Starting and using Cloudformation took longer than
# just doing create-table using JSON template
#echo "Begin: Cloudformation -> Deploy"
#awslocal cloudformation deploy \
#    --stack-name aws-dynamodb-cfn-table \
#    --template-file "dynamo-db.yaml"
#echo "End: Cloudformation -> Deploy"
