package main;

import java.util.Comparator;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.*;

import static java.lang.Thread.sleep;

public class CustomExecutor {//extends ThreadPoolExecutor{

    //tor
    private ThreadPoolExecutor new_pool;
    private PriorityBlockingQueue P_queue;

    private int Max_pri = -1;

    public CustomExecutor() {
        int min = Runtime.getRuntime().availableProcessors() / 2;
        int max = Runtime.getRuntime().availableProcessors() - 1;
        P_queue = new PriorityBlockingQueue<>(min, new Comparator<Object>(){
            @Override
            public int compare(Object o1, Object o2) {
                if (o1 instanceof Task && o2 instanceof Task) {
                    return ((Task<?>) o1).compareTo((Task<?>) o2);
                } else {
                    int h1 = o1.hashCode();
                    int h2 = o2.hashCode();
                    return Integer.compare(h1, h2);
                }
            }});
        new_pool = new ThreadPoolExecutor(min, max, 300, TimeUnit.MILLISECONDS, P_queue);
    }

    public <T> Future<?> submit(Task<T> t) {
//        if(t.getPriority() < Max_pri)
//        {
//            this.Max_pri = t.getPriority();
//        }
        Future<?> future = null;
        try {
            future = new_pool.submit((Callable)t);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(!new_pool.getQueue().isEmpty())
        {
            if(new_pool.getQueue().peek() instanceof Task<?>)
                this.Max_pri = ((Task<?>) new_pool.getQueue().peek()).getPriority();
//            if(new_pool.getQueue().peek() instanceof FutureTask<?>)
//                new FutureTask<T>(){
//                    public int getPriority(){
//
//                    }
//                };
//                this.Max_pri = ((Task<?>) new_pool.getQueue().peek()).getPriority();
            for(Runnable r: new_pool.getQueue())
            {
                System.out.print(r+ ", ");
            }
        }else {

            for(Runnable r: new_pool.getQueue())
            {
                System.out.print(r+ ", ");
            }
        }
        return future;
    }

    public <T> Future<?> submit(Callable<?> c, TaskType taskType)
    {
        Task<?> t = Task.createTask(c , taskType);
        return this.submit(t);
    }

    //@Override
    public <T> Future<T> submit(Callable<T> c)
    {
        Task<?> t = Task.createTask(c , TaskType.OTHER);
        return (Future<T>) this.submit(t);
    }

    public int getCurrentMax()
    {
        return this.Max_pri;
    }

    public void gracefullyTerminate()
    {
        if(new_pool.getQueue().isEmpty())
        {
            new_pool.shutdown();
        }
    }

    //@Override
//    protected void afterExecute(Runnable r, Throwable t) {
//        if (!new_pool.getQueue().isEmpty())
//        {
//            if(r instanceof Task<?>)
//            {
//                if(((Task<?>) r).getPriority() < this.Max_pri)
//                {
//                    this.Max_pri = ((Task<?>) r).getPriority();
//                }
//            }
//        }
//    }

}
