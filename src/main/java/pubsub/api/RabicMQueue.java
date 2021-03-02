package pubsub.api;

import lombok.NonNull;
import pubsub.handler.TopicHandler;
import pubsub.model.Message;
import pubsub.model.Topic;
import pubsub.model.TopicSubscriber;
import pubsub.publicInterface.ISubscriber;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class RabicMQueue {
    private final Map<String, TopicHandler> topic_Handler_Map;

    public RabicMQueue(){
        topic_Handler_Map = new HashMap<>();
    }

    //when you create a topic, you also create its corresponding topic handler
    public Topic createTopic(@NonNull final String topicName){
        Topic topic = new Topic(topicName, UUID.randomUUID().toString());
        TopicHandler topicHandler = new TopicHandler(topic);
        topic_Handler_Map.put(topic.getTopicId(), topicHandler);
        System.out.println("Created Topic:"+topic.getTopicName());
        return topic;
    }

    //to subscribe to a topic, you need a subscriber, a topic to subscribe.
    //the topic adds the subscriber to its subscribersList by wrapping a subscriber in a TopicSubscriber
    public void subscribe(@NonNull final ISubscriber subscriber, @NonNull final Topic topic){
        topic.addTopicSubscriber(new TopicSubscriber(subscriber));
        System.out.println(subscriber.getId()+" Subscribed to "+ topic.getTopicName());
    }

    //to publish a message to a topi, you need a topic and a message.
    //this method adds a message to the messageList in a topic and then calls the corresponding
    //topicHandler from the above topic_Handler_Map to publish the message
    public void publish(@NonNull final Topic topic, @NonNull final Message message){
        topic.addMessage(message);
        System.out.println(message+" published to "+topic.getTopicName());
        topic_Handler_Map.get(topic.getTopicId()).publish();
    }



}
