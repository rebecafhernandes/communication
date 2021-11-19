package com.customdev.communication.queue.consumer;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class SendEmailConsumer {

    @JmsListener(destination = "${aws.sqs.queue.send.email.name}")
    public void messageConsumer(@Payload String message) {
        System.out.println("Message received: " + message);
    }
}
