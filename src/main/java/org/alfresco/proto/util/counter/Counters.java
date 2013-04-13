package org.alfresco.proto.util.counter;

import java.util.HashMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;


public class Counters
{
    public static Counters instance = new Counters();
    
    private HashMap<String, Counter> counters = new HashMap<String, Counter>();
    private HashMap<String, SumCounter> sumCounters = new HashMap<String, SumCounter>();
    
    private ReentrantReadWriteLock countersLock = new ReentrantReadWriteLock();

    public void reset()
    {
        instance = new Counters();
    }
    
    public Counter createThreadCounter(String name)
    {
        countersLock.writeLock().lock();
        try
        {
            String key = createKey(name);
            Counter counter = counters.get(key);
            if (counter != null)
            {
                return counter;
            }
            counter = new Counter(name);
            counters.put(key, counter);
            
            SumCounter sum = sumCounters.get(name);
            if (sum == null)
            {
                sum = new SumCounter(name);
                sumCounters.put(name, sum);
            }
            sum.addCounter(counter);
            
            return counter;
        }
        finally
        {
            countersLock.writeLock().unlock();
        }
    }

    public Counter getThreadCounter(String name)
    {
        countersLock.readLock().lock();
        try
        {
            Counter counter = counters.get(createKey(name));
            return counter;
        }
        finally
        {
            countersLock.readLock().unlock();
        }
    }
    
    private String createKey(String name)
    {
        return Thread.currentThread().getId() + "." + name;
    }
    
    public Thread startLogging()
    {
        return startLogging(1000);
    }
    
    public Thread startLogging(long millis)
    {
        Thread counter = new Thread(new CountersLog(millis));
        counter.start();
        return counter;
    }
    
    public void logCounters()
    {
        long toMillis = System.currentTimeMillis();
        
        Object[] countersArray;
        Object[] sumCountersArray;
        countersLock.readLock().lock();
        try
        {
             countersArray = counters.values().toArray();
             sumCountersArray = sumCounters.values().toArray();
        }
        finally
        {
            countersLock.readLock().unlock();
        }
        
        logDivider();
        boolean logged = false;
        for (Object counterObject : countersArray)
        {
            Counter counter = (Counter)counterObject;
            String msg = String.format("%1$s: cnt=%2$d, avg=%3$.2f/s", counter.getName(), counter.getCount(), counter.getAvgPerSecond(toMillis));
            log(msg);
            logged = true;
        }
        for (Object counterObject : sumCountersArray)
        {
            SumCounter counter = (SumCounter)counterObject;
            if (counter.getSize() > 1)
            {
                String msg = String.format("%1$s: cnt=%2$d, avg=%3$.2f/s", counter.getName(), counter.getCount(), counter.getAvgPerSecond(toMillis));
                log(msg);
                logged = true;
            }
        }
        if (!logged)
            log("<no counters>");
    }
    
    private void logDivider()
    {
        log("-------");
    }
    
    private void log(String msg)
    {
        System.out.println(msg);
    }

    private class CountersLog implements Runnable
    {
        private long millis;
        
        public CountersLog(long millis)
        {
            this.millis = millis;
        }
        
        public void run()
        {
            log("CountersLog: start");
            logCounters();
            while(true)
            {
                try
                {
                    Thread.sleep(millis);
                    logCounters();
                }
                catch(InterruptedException e)
                {
                    logDivider();
                    log("CountersLog: end");
                    break;
                }
            }
        }
    }

}
