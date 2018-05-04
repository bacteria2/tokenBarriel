package message.queue;

import message.Message;
import message.limiter.RateLimiter;

import java.util.concurrent.*;

public class SpeedLimitedMessageQueue<T extends Message> implements MessageQueue<T> {

    private final LinkedBlockingQueue<T> queue = new LinkedBlockingQueue<>();

    //每块令牌允许传送字节数 byte
    private static final int perTokenAllowSize = 20;

    private RateLimiter limiter;

    public SpeedLimitedMessageQueue() {
        this.limiter = RateLimiter.builder().
                withTokenPerSecond(5).
                withType(RateLimiter.LimitType.TokenBucket).
                build();
    }

    public void put(T message) throws Exception {
        Integer removeTokenCount = new Double(Math.ceil(message.getSize() / perTokenAllowSize)).intValue();
        try {
            limiter.getToken(removeTokenCount);
            queue.put(message);
            System.out.println(String.format("#插入消息,移除token:%d", removeTokenCount));
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public T pop() throws Exception {
        return queue.take();
    }


}
