package org.alfresco.proto.util.counter;

import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class SumCounter implements CounterStats
{
    private String name;
    private String description;
    private ArrayList<CounterStats> counters = new ArrayList<CounterStats>();
    
    private ReentrantReadWriteLock countersLock = new ReentrantReadWriteLock();
    
    public SumCounter(String name)
    {
        this.name = name;
        this.description = name;
    }
    
    public void addCounter(CounterStats counter)
    {
        countersLock.writeLock().lock();
        try
        {
            counters.add(counter);
            description = name + " threads x " + counters.size();
        }
        finally
        {
            countersLock.writeLock().unlock();
        }
    }

    public int getSize()
    {
        countersLock.readLock().lock();
        try
        {
            return counters.size();
        }
        finally
        {
            countersLock.readLock().unlock();
        }
    }
    
    @Override
    public String getName()
    {
        return description;
    }

    @Override
    public long getCount()
    {
        countersLock.readLock().lock();
        try
        {
            long c = 0;
            for (CounterStats counter : counters)
            {
                c += counter.getCount();
            }
            return c;
        }
        finally
        {
            countersLock.readLock().unlock();
        }
    }

    @Override
    public double getAvgPerSecond(long toMillis)
    {
        countersLock.readLock().lock();
        try
        {
            double avg = 0;
            for (CounterStats counter : counters)
            {
                avg += counter.getAvgPerSecond(toMillis);
            }
            return avg;
        }
        finally
        {
            countersLock.readLock().unlock();
        }
    }
}
