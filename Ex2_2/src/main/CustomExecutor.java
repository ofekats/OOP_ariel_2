package main;

import java.util.Comparator;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.*;

/**
 * this class creat a new ThreadPoolExecutor that support Callable Task with priority.
 * (the lower priority comes first)
 */
public class CustomExecutor extends ThreadPoolExecutor{

    //saves the max priority (the lowest priority), if the queue null: -1
    private int Max_pri = -1;

    /**
     * constructor uses ThreadPoolExecutor constructor with:
     * corePoolSize: Runtime.getRuntime().availableProcessors()/2
     * maximumPoolSize: Runtime.getRuntime().availableProcessors() - 1
     * keepAliveTime: 300 MILLISECONDS
     * and the blocking queue is PriorityBlockingQueue with:
     * initialCapacity: Runtime.getRuntime().availableProcessors()/2
     * and comparator by the compareTo that in TaskAdaptFuture (the lower priority comes first)
     */
    public CustomExecutor() {
        super(Runtime.getRuntime().availableProcessors()/2 , Runtime.getRuntime().availableProcessors() - 1,
                300 , TimeUnit.MILLISECONDS , new PriorityBlockingQueue<>(Runtime.getRuntime().availableProcessors()/2
                        ,Comparator.comparing(t2 ->((TaskAdaptFuture)t2))));
    }

    /**
     * submit get Task or Callable object and calls ThreadPoolExecutor submit
     * also - change the maximum priority (if the queue null: -1)
     * @param t
     * @return Future object that contains the return value of call func
     */
    public <T> Future<T> submit(Task<T> t) {
        try {
            if(t.taskType != null)
            {
                this.Max_pri = t.getPriority();
            }
            if(this.getQueue().isEmpty())
            {
                this.Max_pri = -1;
            }
            //Callable or task - super know to handle -> newTaskFor()
            return super.submit(t);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * submit get Callable object and a TaskType
     * creat new task and calls submit(Task<T> t)
     * @param c
     * @param taskType
     * @return Future object that contains the return value of call func
     */
    public <T> Future<?> submit(Callable<?> c, TaskType taskType)
    {
        Task<?> t = Task.createTask(c , taskType);
        return submit(t);
    }

    /**
     * @return the maximum priority (if the queue null: -1)
     */
    public int getCurrentMax()
    {
        return this.Max_pri;
    }

    /**
     * for TaskAdaptFuture use
     * @param callable the callable task being wrapped
     */
    @Override
    protected <T> RunnableFuture<T> newTaskFor(Callable<T> callable)
    {
        return new TaskAdaptFuture<T>(callable);
    }

    /**
     * shutdown after finished all the Tasks in the queue
     * change the maximum priority to -1 (the queue is null)
     */
    public void gracefullyTerminate()
    {
        while(true)
        {
            if(this.getQueue().isEmpty())
            {
                this.Max_pri = -1;
                break;
            }
        }
        super.shutdown();
    }

}