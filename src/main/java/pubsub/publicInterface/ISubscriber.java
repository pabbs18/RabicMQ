package pubsub.publicInterface;
/*Each subscriber has an Id and consumes the published messages*/

import pubsub.model.Message;

public interface ISubscriber {
    String getId();
    void consume(Message message) throws InterruptedException;

}
