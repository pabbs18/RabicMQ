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
    but makes sure that the offset value is not reset.
    topicSubscriber object is synchronized because: In the first line of do-while loop, the
    currentMessageOffset reads the offset value from messageOffset of the topicSubscriber.
    The do-while loop increases the value of messageOffset of topicSubscriber object/class.
    During a loop run, if we do a parallel call of the api 'resetOffset', it resets the messageOffset of the
    topicSubscriber object/class. So for the next loop, the value read by  currentMessageOffset of the first line
    of do-while loop, will have changed value instead of incremented by one.
    So to avoid that, we synchronize the topicSubscriber object/class. Here Synchronize an object means,
    for that object, all the variables and methods of that object's class are locked, till wait() is called,
    at which point the thread releases lock on the object on its own.
     */
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
