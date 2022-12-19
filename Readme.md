# Microservice Label Detector
This microservice allows you to detect labels in an image by providing the image's URL, the maximum number of labels to detect, and the confidence level. It uses the AWS Rekognition service to detect labels in the image.

It has been developped in the context of the AMT Lab along with :
* [The DataObject Microservice](https://github.com/Nelson-Jnrnd/AMT-Microservice-DataObject)
* [The main application](https://github.com/Nelson-Jnrnd/AMT-Microservice-Main)

## Prerequisites
* A valid AWS account with the Rekognition service enabled
* Java 8 or higher
* Maven

## Installation
* Clone the repository
* mvn install to install the dependencies

## Configuration
In order to use the AWS Rekognition service, you will need to provide your AWS credentials. You can do this in one of two ways:

1. Set the **AWS_ACCESS_KEY_ID** and **AWS_SECRET_ACCESS_KEY** environment variables with your AWS access key and secret key, respectively.
2. Create a file at *~/.aws/credentials* with the following format: 
```
[default]
aws_access_key_id = YOUR_ACCESS_KEY
aws_secret_access_key = YOUR_SECRET_KEY
```

##  Usage
To use the microservice, send a POST request to /labels with the following parameters:

* **imageURL**: the URL of the image to detect labels in
* **maxLabels**: the maximum number of labels to detect (optional, default is 10)
* **confidence**: the minimum confidence level for detected labels (optional, default is 90)

The response will be a JSON object with the following format:

```
{
    "labels": {
        "label1": confidence1,
        "label2": confidence2,
        ...
    },
    "nbLabels": nbLabels
```

## Errors
If an error occurs, the response will have an HTTP status code indicating the error type. Possible error codes are:

* **400 Bad Request**: The request was invalid or the image URL was malformed.
* **403 Forbidden**: Access to the AWS Rekognition service was denied.
* **500 Internal Server Error**: An unexpected error occurred.
