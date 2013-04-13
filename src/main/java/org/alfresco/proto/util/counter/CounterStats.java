package org.alfresco.proto.util.counter;

public interface CounterStats
{
    public String getName();

    public long getCount();

    public double getAvgPerSecond(long toMillis);

}
