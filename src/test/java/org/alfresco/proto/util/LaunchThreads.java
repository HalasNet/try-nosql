package org.alfresco.proto.util;

import java.util.ArrayList;
import java.util.List;

public class LaunchThreads
{
    public static Threads start(Runnable runnable, int number)
    {
        List<Thread> threads = new ArrayList<Thread>(number);
        for (int i = 0; i < number; i++)
        {
            Thread t = new Thread(runnable);
            threads.add(t);
        }
        for (Thread thread : threads)
        {
            thread.start();
        }
        return new Threads(threads);
    }
    
    public static class Threads
    {
        private List<Thread> threads;
        
        public Threads(List<Thread> threads)
        {
            this.threads = threads;
        }
        
        public void stop() throws Exception
        {
            for (Thread thread : threads)
            {
                thread.interrupt();
                thread.join();
            }
        }
    }
}
