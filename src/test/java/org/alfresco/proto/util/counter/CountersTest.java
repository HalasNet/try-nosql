package org.alfresco.proto.util.counter;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;

import java.util.Random;

import org.junit.Before;
import org.junit.Test;

public class CountersTest
{
    private Counters counters;
    
    @Before public void setUp() throws Exception
    {
        counters = new Counters();
    }
    
    @Test public void exists()
    {
        Counter c1 = counters.getThreadCounter("test");
        assertNull(c1);
    }

    @Test public void create()
    {
        Counter c1 = counters.createThreadCounter("test");
        assertNotNull(c1);
        assertNotNull(c1.getName());
        Counter c2 = counters.getThreadCounter("test");
        assertEquals(c1, c2);
    }
    
    @Test public void duplicates()
    {
        Counter c1 = counters.createThreadCounter("test");
        assertNotNull(c1);
        Counter c2 = counters.createThreadCounter("test");
        assertEquals(c1, c2);
    }

    @Test public void count()
    {
        Counter c1 = counters.createThreadCounter("test");
        assertNotNull(c1);
        assertEquals(0, c1.getCount());
        for (int i = 0; i < 10000; i++)
        {
            c1.incrementCount();
        }
        assertEquals(10000, c1.getCount());
    }

    @Test public void log() throws InterruptedException
    {
        Thread l = counters.startLogging();
        Thread t1 = new Thread(new IncrementCounterRunnable());
        Thread t2 = new Thread(new IncrementCounterRunnable());
        Thread t3 = new Thread(new IncrementCounterRunnable());
        t1.start();
        t2.start();
        t3.start();
        Thread.sleep(4000);
        t1.interrupt();
        t2.interrupt();
        t3.interrupt();
        l.interrupt();
    }
    
    public class IncrementCounterRunnable implements Runnable
    {
        public void run()
        {
            Counter c = counters.createThreadCounter("increment");
            c.start();
            
            while(true)
            {
                for (int i = 0; i < new Random().nextInt(1000000); i++)
                {
                    c.incrementCount();
                }
                try
                {
                    Thread.sleep((long)new Random().nextInt((int)1000));
                }
                catch(InterruptedException e)
                {
                    return;
                }
            }
        }
    }
    
}
