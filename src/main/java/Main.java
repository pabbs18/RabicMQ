import pubsub.api.RabicMQueue;
import pubsub.model.Message;
import pubsub.model.Subscriber;
import pubsub.model.Topic;

public class Main {
    public static void main(String[] args) throws InterruptedException{
        final RabicMQueue rabicMQueue = new RabicMQueue();

        final Topic topic1 = rabicMQueue.createTopic("topic1");
        final Topic topic2 = rabicMQueue.createTopic("topic2");
        Thread.sleep(10000);
        final Subscriber subscriber1 = new Subscriber("subscriber1", 10000);
        final Subscriber subscriber2 = new Subscriber("subscriber2", 10000);

        rabicMQueue.subscribe(subscriber1,topic1);
        rabicMQueue.subscribe(subscriber2,topic1);
        Thread.sleep(10000);
        final Subscriber subscriber3 = new Subscriber("subscriber3", 10000);
        rabicMQueue.subscribe(subscriber3,topic2);
        Thread.sleep(10000);
        rabicMQueue.publish(topic1,new Message("message1"));
        rabicMQueue.publish(topic1,new Message("message2"));
        Thread.sleep(10000);
        rabicMQueue.publish(topic2,new Message("message3"));

        Thread.sleep(10000);

        rabicMQueue.publish(topic2,new Message("message4"));
        rabicMQueue.publish(topic1,new Message("message5"));

        rabicMQueue.resetOffset(topic1, subscriber1, 0);

    }
}
