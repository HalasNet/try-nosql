package org.alfresco.proto.util.counter;

import java.util.concurrent.atomic.AtomicLong;

public class Counter implements CounterStats
{
    private String name;
    private long startTime = 0;
    private long stopTime = 0;
    private AtomicLong count = new AtomicLong();
    
    public Counter(String name)
    {
        this.name = name + " thread=" + Thread.currentThread().getName();
    }
    
    public void start()
    {
        startTime = System.currentTimeMillis();
    }

    public void stop()
    {
        stopTime = System.currentTimeMillis();
    }
    
    public long incrementCount()
    {
        return count.incrementAndGet();
    }

    @Override
    public String getName()
    {
        return name;
    }

    @Override
    public long getCount()
    {
        return count.get();
    }

    @Override
    public double getAvgPerSecond(long toMillis)
    {
        long duration = getDuration(toMillis);
        long lCount = getCount();
        double avg = (duration == 0) ? 0 : (lCount == 0) ? 0 : ((double)lCount / duration) * 1000d;
        return avg;
    }
    
    private long getDuration(long toMillis)
    {
        long endTime = (stopTime == 0) ? toMillis : stopTime; 
        return (startTime == 0 || endTime <= startTime) ? 0 : endTime - startTime;
    }

}
