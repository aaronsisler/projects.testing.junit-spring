aws configure set aws_access_key_id "accessKeyId"
aws configure set aws_secret_access_key "secretAccessKey"
aws configure set region "us-east-1"

#aws dynamodb batch-write-item \
#  --endpoint-url=http://database:8000 \
#  --request-items file:///data/10_clients.json