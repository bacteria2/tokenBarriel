import message.limiter.RateLimiter;
import org.junit.jupiter.api.Test;

public class RateLimiterTest {
    long a;
    long b=10L;

    @Test
    public void testSmoothTokenBucket(){

        System.out.println(b>a);
        System.out.println(a);
        System.out.println(System.nanoTime());
    }

    @Test
    public void test(){
        Runnable a=()-> System.out.println("this is a");
        Runnable b=()->System.out.println("this is b");

        new Thread(a).join();

    }
}
