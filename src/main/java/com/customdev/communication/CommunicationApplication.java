package com.customdev.communication;

import com.amazon.sqs.javamessaging.ProviderConfiguration;
import com.amazon.sqs.javamessaging.SQSConnectionFactory;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.core.JmsTemplate;

import javax.annotation.PostConstruct;

@SpringBootApplication
@EnableJms
public class CommunicationApplication {

	@Value("${aws.region}")
	private String awsRegion;

	private SQSConnectionFactory connectionFactory;

	public static void main(String[] args) {
		SpringApplication.run(CommunicationApplication.class, args);
	}

	@PostConstruct
	public void init() {
		connectionFactory = createSQSConnectionFactory();
	}

	private SQSConnectionFactory createSQSConnectionFactory() {
		final AmazonSQS sqs = AmazonSQSClient.builder()
				.withRegion(awsRegion)
				.withCredentials(new ProfileCredentialsProvider())
				.build();
		return new SQSConnectionFactory(new ProviderConfiguration(), sqs);
	}

	@Bean
	public DefaultJmsListenerContainerFactory jmsListenerContainerFactory() {
		final DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
		factory.setConnectionFactory(connectionFactory);
		return factory;
	}

	@Bean
	public JmsTemplate defaultJmsTemplate() {
		return new JmsTemplate(connectionFactory);
	}
}
