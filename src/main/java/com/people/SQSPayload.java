package com.people;

import com.amazonaws.services.sns.model.MessageAttributeValue;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SQSPayload {

    private String notificationMode;

    private String message;

    private String recipient;

    private Map<String, MessageAttributeValue> messageAttributes;


    public String getNotificationMode() {
        return notificationMode;
    }

    public void setNotificationMode(String notificationMode) {
        this.notificationMode = notificationMode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public Map<String, MessageAttributeValue> getMessageAttributes() {
        return messageAttributes;
    }

    public void setMessageAttributes(Map<String, MessageAttributeValue> messageAttributes) {
        this.messageAttributes = messageAttributes;
    }
}
