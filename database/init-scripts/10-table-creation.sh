aws configure set aws_access_key_id "accessKeyId"
aws configure set aws_secret_access_key "secretAccessKey"
aws configure set region "us-east-1"

aws dynamodb create-table \
--endpoint-url=http://database:8000 \
--table-name SERVICES_SPRING_JUNIT_LOCAL \
--attribute-definitions \
  AttributeName=partitionKey,AttributeType=S  \
  AttributeName=sortKey,AttributeType=S \
--key-schema \
  AttributeName=partitionKey,KeyType=HASH \
  AttributeName=sortKey,KeyType=RANGE \
--billing-mode PAY_PER_REQUEST;

sleep 2