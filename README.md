# services.events-admin-service

## Definition of Done

<details>
  <summary>This is what is needed to close out a feature branch and created/merge a PR.</summary>

- Contract created/updated
- Dependencies added to pom(s) are commented with what their usage is
- Layers are created/updated and follows naming conventions:
    - Controller
    - Service
    - DAO
    - DTO
- Features and tests are added/updated
- API collection (Bruno) is updated and committed to api-client repository
- Bump the version of the app in the pom
- Update the [change log](./CHANGELOG.md)

</details>

## Docker

<details>
  <summary>Running application dependency containers locally</summary>

#### Start the containers

```bash
dockerlocalup
```

```bash
docker compose -f ./docker-compose.local.yml up -d
```

#### Stop the containers

```bash
dockerlocaldown
```

```bash
docker compose -f ./docker-compose.local.yml down
```

</details>

<br />

<details>
  <summary>Running application in a container locally</summary>

This is how to create a new build of the Application and package it into a Docker container

```bash
mvn package -Dpackaging=docker
```

Start the Event Admin Service in Docker

```bash
docker run -d --name events-admin-service -e MICRONAUT_ENVIRONMENTS=dev -e AWS_REGION=us-east-1 -p 8080:8080 events-admin-service:latest
```

```bash
docker stop events-admin-service
```

</details>

## AWS

<details>
<summary>DynamoDB</summary>

**Note** There is an alias assumed if using the `awslocalddb` command below. The alias assumes you have set the
following:

```bash
awslocalddb=aws --profile=local --endpoint-url http://localhost:8000
```

List out the tables created

```bash
awslocalddb dynamodb list-tables
```

List out data in a table

```bash
awslocalddb dynamodb scan --table-name SERVICES_EVENTS_ADMIN_SERVICE_LOCAL
```

</details>

<br />

<details>
<summary>S3</summary>

**Note** There is an alias assumed if using the `awslocals3` command below. The alias assumes you have set the
following:

```
awslocals3=aws --profile=local --endpoint-url http://localhost:9090
```

List out the buckets that exits

```bash
awslocals3 s3 ls
```

List out the files in a bucket

```bash
awslocals3 s3 ls event-admin-service-file-storage
```

List out the content of a file in a bucket

```bash
awslocals3 s3 cp s3://event-admin-service-file-storage/4f2d25cc-cb66-4e29-ac36-c20ce83fb28a/2024-04-16T20:13:19.074960.csv -
```

</details>

<br />

<details>
<summary>ECR</summary>

#### Registry

654918520080.dkr.ecr.us-east-1.amazonaws.com

#### Repository

services.events-admin-service

#### Logging in

```bash
aws ecr get-login-password --region us-east-1 | docker login --username AWS --password-stdin 654918520080.dkr.ecr.us-east-1.amazonaws.com
```

#### List Repositories

```bash
aws ecr describe-repositories
```

#### Tag a version to publish

Run the below command to list the images and their identifiers

```bash
dokcer images
```

```bash
docker tag identifier_from_docker_images 654918520080.dkr.ecr.us-east-1.amazonaws.com/services.events-admin-service
docker tag 2c65d71c3137 654918520080.dkr.ecr.us-east-1.amazonaws.com/services.events-admin-service
docker tag 6aafecc82c31 654918520080.dkr.ecr.us-east-1.amazonaws.com/services.events-admin-service
```

#### Publish after tagging

```bash
docker push 654918520080.dkr.ecr.us-east-1.amazonaws.com/services.events-admin-service
```

</details>

## IntelliJ

<details>
<summary>Environment Variables</summary>

```bash
MICRONAUT_ENVIRONMENTS=local
```

</details>
