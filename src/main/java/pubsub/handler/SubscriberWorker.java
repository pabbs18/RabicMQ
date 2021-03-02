package pubsub.handler;

/*The SubscriberWorker works between a topic and the topic subscriber.

Each subscriber has a SubscriberWorker.
It gets the messages from the messageList of the given topic.
It then helps the subscriber consume that message.*/

import lombok.NonNull;
import lombok.SneakyThrows;
import pubsub.model.Message;
import pubsub.model.Topic;
import pubsub.model.TopicSubscriber;

public class SubscriberWorker implements Runnable{

    private final Topic topic;
    private final TopicSubscriber topicSubscriber;

    public SubscriberWorker(@NonNull final Topic topic, @NonNull final TopicSubscriber topicSubscriber){
        this.topic=topic;
        this.topicSubscriber=topicSubscriber;
    }
    /*This overridden run method takes a topicSubscriber and checks till where has it read the messages,
    by checking the offset.
    If the offset is at the end of the messageList, then it makes that therad wait, till new messages are available.
        Else, it gets the messages from the messageList of the topic and asks the subscriber to consume it.
        It then increments the messageOffset value to the next slot in the messageList
    but makes sure that the offset value is not reset. */
    @SneakyThrows
    @Override
    public void run() {
        synchronized (topicSubscriber){
            do{
                int currentMessageOffset = topicSubscriber.getMessageOffset().get();
                while(currentMessageOffset >= topic.getMessageList().size()){
                    topicSubscriber.wait();
                }
                Message message = topic.getMessageList().get(currentMessageOffset);
                topicSubscriber.getSubscriber().consume(message);

                //check for reset and then increment currentMessageOffset
                //instead of just set we are comparing and then setting the updated value
                topicSubscriber.getMessageOffset().compareAndSet(currentMessageOffset, currentMessageOffset+1);

            }while(true);//do this forever as threads only sleep and wakeup
        }
    }

    synchronized public void wakeUpIfNeeded(){
        synchronized (topicSubscriber){
            topicSubscriber.notify();
        }
    }
}
