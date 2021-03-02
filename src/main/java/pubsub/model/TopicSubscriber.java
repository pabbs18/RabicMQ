package pubsub.model;
/*Topic subscriber is a subscriber who has subscribed to a topic
 and is reading from the messageList at an offset*/

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import pubsub.publicInterface.ISubscriber;

import java.util.concurrent.atomic.AtomicInteger;

@Getter
@AllArgsConstructor
public class TopicSubscriber {
    private final AtomicInteger messageOffset;//till where this subscriber has read the message in the message list of the topic
    private final ISubscriber subscriber;

    public TopicSubscriber(@NonNull final ISubscriber subscriber){
        this.subscriber=subscriber;
        this.messageOffset = new AtomicInteger(0);
    }
}
