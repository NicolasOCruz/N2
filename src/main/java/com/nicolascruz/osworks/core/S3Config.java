package com.nicolascruz.osworks.core;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

@Configuration
public class S3Config {
	
	@Value("${aws.access_key_id}")
	private String awsId;

	@Value("${aws.secret_access_key}")
	private String awsKey;

	@Value("${s3.region}")
	private String region;
	
	@Bean
	public AmazonS3 s3client() {
		System.out.println("Aws ID: " + awsId);
		System.out.println("Aws Key: " + awsKey);
		BasicAWSCredentials awsCred = new BasicAWSCredentials(awsId, awsKey); //cria um objeto passando id e senha (lá do properties)
		AmazonS3 s3client = AmazonS3ClientBuilder.standard().withRegion(Regions.fromName(region)) //instancia o Amazon S3 de acordo com a regiao
							.withCredentials(new AWSStaticCredentialsProvider(awsCred)).build();
		return s3client;
	}
	

}
