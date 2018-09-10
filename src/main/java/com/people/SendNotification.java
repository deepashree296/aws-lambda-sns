package com.people;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSAsync;
import com.amazonaws.services.sns.AmazonSNSAsyncClient;
import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.PublishResult;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.List;

public class SendNotification implements RequestHandler<SQSEvent, Void> {

    private static final String SMS = "SMS";

    private static final String NOTIFICATION = "NOTIFICATION";

    @Override
    public Void handleRequest(SQSEvent event, Context context)

    {
        LambdaLogger logger = context.getLogger();
        String messageId = null;
        logger.log("event is " + event);

        try {

            for(SQSEvent.SQSMessage message : event.getRecords()) {

                logger.log("payload" + message.getBody());

                SQSPayload payload = new ObjectMapper().readerFor(SQSPayload.class)
                        .readValue(message.getBody());
                String notificationMode = payload.getNotificationMode();
                if(SMS.equals(notificationMode)) {
                    messageId = callSNSToSendSMS(payload);
                } else if (NOTIFICATION.equals(notificationMode)) {
                    messageId = callSNSToSendNotification(payload);
                } else {
                    logger.log("Unable to invoke SNS. Invalid notification mode.");
                }

                logger.log("messageId" + messageId);
            }

        } catch(IOException e) {
            logger.log("exception" + e);
        }
        return null;
    }

    private String callSNSToSendSMS(SQSPayload payload) {

        AmazonSNSAsync sns = new AmazonSNSAsyncClient();
        PublishResult result = sns.publish(new PublishRequest()
                .withMessage(payload.getMessage())
                .withPhoneNumber(payload.getRecipient())
                .withMessageAttributes(payload.getMessageAttributes()));

        return result != null ? result.getMessageId() : null;
    }

    private String callSNSToSendNotification(SQSPayload payload) {

        AmazonSNSAsync sns = new AmazonSNSAsyncClient();
        PublishResult result = sns.publish(new PublishRequest()
                .withMessage(payload.getMessage())
                .withTargetArn(payload.getRecipient())
        );

        return result != null ? result.getMessageId() : null;
    }
}
