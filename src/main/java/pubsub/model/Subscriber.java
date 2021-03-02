package pubsub.model;

import pubsub.publicInterface.ISubscriber;

public class Subscriber implements ISubscriber {
    private final String id;
    private final int sleepTimeInMilliSeconds;

    public Subscriber(String id, int sleepTimeInMilliSeconds){
        this.id=id;
        this.sleepTimeInMilliSeconds=sleepTimeInMilliSeconds;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void consume(Message message) throws InterruptedException {
        System.out.println("Subscriber: "+id+ " is consuming "+message.getMsg());
        Thread.sleep(sleepTimeInMilliSeconds);
        System.out.println("Subscriber: "+id+ " is done consuming "+message.getMsg());

    }
}
