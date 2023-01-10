package main;

import java.util.concurrent.*;

/**
 * this class provides Task that includes TaskType and Callable.
 * implement Comparable - so that Task objects can be compared.
 * also includes- toString, get functions, set functions.
 */
public class Task<V>  implements Callable<V>, Comparable<Task>{
    public TaskType taskType;
    private Callable<V> c;


    /**
     * private constructor
     * @param c
     * @param taskType
     */
    private Task(Callable<V> c, TaskType taskType){
        this.taskType = taskType;
        this.c = c;
    }

    /**
     * factory method
     * @param c
     * @param taskType
     * @return new Task Object
     */
    public static Task<?> createTask(Callable<?> c, TaskType taskType){
        Task<?> t = new Task<>(c, taskType);
        return t;
    }

    /**
     * factory method.
     * @param c
     * @return new Task Object
     */
    public static Task<?> createTask(Callable<?> c){
        //???
        TaskType taskType = TaskType.OTHER;
        Task<?> t = new Task<>(c, taskType);
        return t;
    }

    /**
     * call method (Override from Callable)
     * do the function that in c - If an exception is thrown, return null.
     * @return the return value of the Callable in the Task.
     */
    @Override
    public V call(){
        try {
            return this.c.call();
        }catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     *(Override from Comparable)
     * makes Task object compared by the Priority Value that in TaskType.
     * the lower priority comes first.
     * @param t the object to be compared.
     */
    @Override
    public int compareTo(Task t) {
        if (this.taskType.getPriorityValue() > t.taskType.getPriorityValue())
        {
            //System.out.println("com: (1)"+this.taskType.getPriorityValue() +" , "+ t.taskType.getPriorityValue());
            return 1;
        }
        if (this.taskType.getPriorityValue() < t.taskType.getPriorityValue())
        {
            //System.out.println("com: (-1)"+this.taskType.getPriorityValue() +" , "+ t.taskType.getPriorityValue());
            return -1;
        }
        //System.out.println("com: (0)"+this.taskType.getPriorityValue() +" , "+ t.taskType.getPriorityValue());
        return 0;
    }

    //more function- toString, get functions, set functions.

    @Override
    public String toString()
    {
        String s = "Type: " + this.taskType.getType() + ", Priority: " + this.taskType.getPriorityValue();
        return s;
    }

    public int getPriority()
    {
        return this.taskType.getPriorityValue();
    }

    public Callable<V> getCallable()
    {
        return this.c;
    }

    public void setTaskType(TaskType tasktype)
    {
        this.taskType = tasktype;
    }

    public void setCallable(Callable c)
    {
        this.c = c;
    }


}
