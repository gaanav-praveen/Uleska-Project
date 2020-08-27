# Uleska-Project

This is a scheduling project which is used to schedule multiple jobs via a REST API calls
The actions that can be performed are:
1. create jobs
2. Pause jobs 
3. Resume jobs
4. Delete jobs
5. Status of the Job

## Create Jobs

It is a POST operation which ask for 2 basic information
1. jobId : a unique alphanumeric input given to the job
2. jobName: a name given to the job for better understanding and readability.

Sample POST request 
```
curl --location --request POST 'localhost:8080/create' \
--header 'Content-Type: application/json' \
--data-raw '{
    "jobId" : "1",
    "jobName" : "Job1"
}'
```

## Pause Jobs

It is a GET operation which ask for 1 basic information
1. jobId : a unique alphanumeric input given to the job

Sample GET request 
```
curl --location --request POST 'http://localhost:8080/paused' \
--header 'Content-Type: application/json' \
--data-raw '{
    "jobId" : "1"
}'
```

## Resume Jobs

It is a POST operation which ask for 1 basic information
1. jobId : a unique alphanumeric input given to the job
Sample GET request 
```
curl --location --request POST 'http://localhost:8080/resume' \
--header 'Content-Type: application/json' \
--data-raw '{
    "jobId" : "1"
}'
```

## Delete Jobs

It is a POST operation which ask for 1 basic information
1. jobId : a unique alphanumeric input given to the job

Sample GET request 
```
curl --location --request POST 'http://localhost:8080/delete' \
--header 'Content-Type: application/json' \
--data-raw '{
    "jobId" : "1"
}'
```

## Status of Jobs

It is a GET operation which ask for 1 basic information
1. jobId : a unique alphanumeric input given to the job

Sample GET request 
```
curl --location --request GET 'http://localhost:8080/status?jobId=1'
```
