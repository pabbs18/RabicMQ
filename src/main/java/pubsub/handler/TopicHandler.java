package pubsub.handler;

/*Each topic has a topic handler whose job is to publish the messages
to all the subscribers of that particular topic*/

import lombok.NonNull;
import pubsub.model.Topic;
import pubsub.model.TopicSubscriber;

import java.util.HashMap;
import java.util.Map;

public class TopicHandler {
    private final Topic topic;
    private final Map<String , SubscriberWorker> subscriber_WorkerMap;

    public TopicHandler(@NonNull Topic topic){
        this.topic=topic;
        this.subscriber_WorkerMap = new HashMap<>();
    }

    /*In the publish method, TopicHandler gets all the topicSubscribers from the topicSubscribersList
    for the given topic and hands each topicSubscriber over to their respective worker*/
    public void publish(){
        for(TopicSubscriber topicSubscriber: topic.getTopicSubscribersList()){
            startSubscriberWorker(topicSubscriber);
        }
    }

    /*This method takes a topicSubscriber, gets the id of that subscriber and fetches the corresponding
    worker form the above map: subscriber_WorkerMap. It then notifies the thread of that worker object
    to start consuming the messages for this subscriber.
            If a subscriber doesn't have a worker then, it creates a new worker and asks that worker to start
    on a new thread.*/
    public void startSubscriberWorker(@NonNull final TopicSubscriber topicSubscriber){
        final String subscriberId = topicSubscriber.getSubscriber().getId();
        if(subscriber_WorkerMap.containsKey(subscriberId)){
            final SubscriberWorker subscriberWorker = new SubscriberWorker(topic, topicSubscriber);
            subscriber_WorkerMap.put(subscriberId, subscriberWorker);
            new Thread(subscriberWorker).start();
        }
        final SubscriberWorker subscriberWorker = subscriber_WorkerMap.get(subscriberId);
        subscriberWorker.wakeUpIfNeeded();
    }
}
