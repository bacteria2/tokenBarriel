package message.limiter;

import static java.util.concurrent.TimeUnit.*;


public abstract class RateLimiter {

    protected double maxTokens;

    protected double availableTokens;

    protected long startNanos;

    protected long nextGenTokenMicros;

    protected double stableIntervalTokenMicros;

    protected Object mutex = new Object();


    public void setRate(double tokenPerSecond) {
        if (tokenPerSecond < 0)
            throw new IllegalArgumentException("input must bigger than 0");
        synchronized (mutex) {
            doSetRate(tokenPerSecond);
        }
    }

    protected abstract void doSetRate(double tokenPerSecond);

    public abstract void syncAvailableToken(long nowMicros);

    public abstract double getToken(double requireToken);

    public static Builder builder(){
        return new Builder();
    }

    public static class Builder<T extends RateLimiter> {
        private double tokenPerSecond;
        private LimitType limitType;

        public Builder withType(LimitType type) {
            this.limitType = type;
            return this;
        }

        public Builder withTokenPerSecond(double perSecond) {
            this.tokenPerSecond = perSecond;
            return this;
        }

        public T build() {
            switch (limitType) {
                case TokenBucket:
                    return (T)buildSmoothTokenBucketLimiter();
               default:
                    return (T)buildSmoothTokenBucketLimiter();
            }
        }

        private SmoothTokenBucketLimiter buildSmoothTokenBucketLimiter() {
            SmoothTokenBucketLimiter limiter = new SmoothTokenBucketLimiter();
            limiter.startNanos = System.nanoTime();
            limiter.setRate(tokenPerSecond);
            return limiter;
        }

    }

    public long duration() {
        return MICROSECONDS.convert(System.nanoTime() - startNanos, NANOSECONDS);
    }

    public enum LimitType {
        TokenBucket,
        Default,
    }
}
