package message.limiter;

import java.util.concurrent.TimeUnit;

public class SmoothTokenBucketLimiter extends AbstractLimtedTokenBucket {

    @Override
    public double getToken(double requireToken) {
        long waitMicros;
        long sleepTimes;
        long oldNextGenTokenMicros;
        long nowMicros = duration();
        synchronized (mutex){
            syncAvailableToken(nowMicros);
            oldNextGenTokenMicros=nextGenTokenMicros;
            double tokenPermited = Math.min(requireToken,availableTokens);
            double needNewToken = requireToken - tokenPermited;
            waitMicros = (long) (needNewToken * stableIntervalTokenMicros);
            nextGenTokenMicros = nextGenTokenMicros +waitMicros;
            availableTokens -=tokenPermited;
        }
        sleepTimes =Math.max(oldNextGenTokenMicros - nowMicros,0);
        uninterruptibleSleep(sleepTimes,TimeUnit.MICROSECONDS);
        return sleepTimes;
    }

    private void uninterruptibleSleep(long sleepTime,TimeUnit unit){
        boolean interrupted = false;
        try {
            long remainingNanos = unit.toNanos(sleepTime);
            long end = System.nanoTime() + remainingNanos;
            while (true){
                try {
                    TimeUnit.NANOSECONDS.sleep(remainingNanos);
                    return;
                } catch (InterruptedException e) {
                    interrupted = true;
                    remainingNanos = end - System.nanoTime();
                }
            }
        } finally {
            if (interrupted){
                Thread.currentThread().interrupt();
            }
        }
    }
}
