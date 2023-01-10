package main;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

/**
 * this class implements adapter design pattern
 * between FutureTask (Runnable) and Task (Callable)
 */
public class TaskAdaptFuture<T> extends FutureTask<T> implements Comparable<TaskAdaptFuture<T>>{

    private Task<T> task;

    /**
     * constructor
     * if c is not Task (just Callable object)- first creat a new Task
     * @param c
     */
    public TaskAdaptFuture(Callable<T> c)
    {
        super(c);
        if(!(c instanceof Task<T>))
        {
            this.task = (Task<T>) Task.createTask(c);
        }else {
            this.task = (Task<T>) c;
        }
    }

    public Callable<T> getCallable()
    {
        return this.task;
    }

    public int getPriority()
    {
        return (((Task<T>)task)).getPriority();
    }

    /**
     * (Override from Comparable)
     * makes TaskAdaptFuture object compared by the Priority Value that in Task.TaskType .
     * the lower priority comes first.
     * @param o the object to be compared.
     */
    @Override
    public int compareTo(TaskAdaptFuture o) {
        //System.out.println("com: "+ ((Task<T>)this.task).compareTo((Task<T>)o.task));
        return ((Task<T>)this.task).compareTo((Task<T>)o.task);
    }

}


