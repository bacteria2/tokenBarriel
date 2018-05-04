package message.limiter;

import java.util.concurrent.TimeUnit;

public abstract class AbstractLimtedTokenBucket extends RateLimiter {
    @Override
    protected void doSetRate(double tokenPerSecond) {
        syncAvailableToken(duration());
        this.maxTokens=tokenPerSecond;
        this.stableIntervalTokenMicros= TimeUnit.SECONDS.toMicros(1L)/tokenPerSecond;
    }

    @Override
    public void syncAvailableToken(long nowMicros) {
        if(nowMicros > nextGenTokenMicros){
            double newToken=(nowMicros - nextGenTokenMicros)/stableIntervalTokenMicros;
            availableTokens=Math.min(maxTokens,availableTokens+newToken);
            nextGenTokenMicros=nowMicros;
        }
    }
}
