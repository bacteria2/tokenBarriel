package message.impl;

import message.Producer;
import message.queue.SpeedLimitedMessageQueue;

public class StringProducer implements Producer<String> {

    private SpeedLimitedMessageQueue<StringMessage> queue;

    public StringProducer(SpeedLimitedMessageQueue<StringMessage> queue) {
        this.queue = queue;
    }

    @Override
    public void produce(String message) throws Exception {
        StringMessage msg = new StringMessage(message);
        queue.put(msg);
    }
}
