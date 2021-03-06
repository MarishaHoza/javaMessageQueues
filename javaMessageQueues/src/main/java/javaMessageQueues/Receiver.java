package javaMessageQueues;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.AmazonSQSException;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.SendMessageBatchRequest;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import com.amazonaws.services.lambda.runtime.events.SQSEvent.SQSMessage;

import java.util.List;

public class Receiver implements RequestHandler<SQSEvent, Void> {
    public static void receiveMessage() {
        AmazonSQS sqs = AmazonSQSClientBuilder.defaultClient();

        String QUEUE_NAMEA = "QueueA";
        String QUEUE_NAMEB = "QueueB";
        String QUEUE_NAMEC = "QueueC";

        String queueUrlA = sqs.getQueueUrl(QUEUE_NAMEA).getQueueUrl();
        String queueUrlB = sqs.getQueueUrl(QUEUE_NAMEB).getQueueUrl();
        String queueUrlC = sqs.getQueueUrl(QUEUE_NAMEC).getQueueUrl();

        List<Message> messages = sqs.receiveMessage(queueUrlA).getMessages();

        for ( Message message : messages ) {
            System.out.println(message.getBody());
            sqs.deleteMessage(queueUrlA, message.getReceiptHandle());
        }
    }

    public static void receiveManyMessages() throws InterruptedException{
        while (true) {
            receiveMessage();
        }
    }

    @Override
    public Void handleRequest(SQSEvent event, Context context)
    {
        for(SQSMessage msg : event.getRecords()){
            System.out.println(new String(msg.getBody()));
        }
        return null;
    }
}




