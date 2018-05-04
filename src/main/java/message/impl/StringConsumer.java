package message.impl;

import message.Consumer;
import message.queue.MessageQueue;

import java.util.concurrent.TimeUnit;

public class StringConsumer implements Consumer<String> {
    private MessageQueue<StringMessage> queue;

    public StringConsumer(MessageQueue<StringMessage> queue){
        this.queue=queue;
    }

    @Override
    public String consume() throws Exception {
        TimeUnit.NANOSECONDS.sleep(10);
        return queue.pop().getMessage();
    }
}
