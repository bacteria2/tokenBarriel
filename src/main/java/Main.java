import message.Consumer;
import message.Producer;
import message.impl.StringConsumer;
import message.impl.StringMessage;
import message.impl.StringProducer;
import message.queue.SpeedLimitedMessageQueue;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {

    public static void main(String[] args) {
        final String mockContent = "类似的心理测试很多，但测试的结果仅供参考，心理测试不是心理健康的唯一标准，心理测试细节应由专家再分析；因为心理测试是以心理健康社会常模为标准测试，心理测试的常模标准是动态的，比如国外的测试常模和过去的测试常模不一定符合当代社会的普遍现象；另一方面，当事人在心理测试的时候是一种心态，测试以后可能又是一种心态；还有心理测试题是否恰当、是否有暗示性、当事人对心理测试的态度、测试答案怎么分析都是影响心理测试的因素，心理测试在专业领域叫心理测量，网络上的心理测试多是娱乐游戏性质。";
        Random random = new Random();
        SpeedLimitedMessageQueue<StringMessage> messageQueue = new SpeedLimitedMessageQueue<>();

        Runnable stringProducer = () -> {
            Producer<String> producer = new StringProducer(messageQueue);
            int repeatTime=3;
            int start=0;
            try {
                while (start<repeatTime) {
                    int stringEnd = random.nextInt(mockContent.length());
                    String message = String.format("producerId:%d,time:%s,content:%s", Thread.currentThread().getId(),LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME),mockContent.substring(0, stringEnd));

                    System.out.println(String.format("#生产者:生产一条消息:%s",message));

                    producer.produce(message);
                    start++;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        };

        Runnable stringConsumer = () -> {
            Consumer<String> consumer = new StringConsumer(messageQueue);
            try {
                while (true) {
                    String message = consumer.consume();
                    System.out.println(String.format("#消费者:content:%s",message));
                    System.out.println("");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        };

        ExecutorService producerExecutor = Executors.newFixedThreadPool(4);
        producerExecutor.submit(stringProducer);
        producerExecutor.submit(stringProducer);
        //producerExecutor.submit(stringProducer);

        ExecutorService consumerExecutor = Executors.newFixedThreadPool(2);
        consumerExecutor.submit(stringConsumer);

    }
}
