package message.queue;

import message.Message;

public interface MessageQueue <T extends Message> {
    void put(T message) throws Exception;
    T pop() throws Exception;
}
