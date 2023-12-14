package com.planties.plantiesbackend.utils;

import org.springframework.beans.factory.annotation.Value;

public class ImageProcess {

    @Value("${aws.accessKeyId}")
    private String awsAccessKeyId;

    @Value("${aws.secretKey}")
    private String awsSecretKey;

    @Value("${aws.region}")
    private String awsRegion;

    @Value("${aws.s3.bucketName}")
    private String s3BucketName;



}
