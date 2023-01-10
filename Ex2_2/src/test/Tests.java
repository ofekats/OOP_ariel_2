package test;

import main.CustomExecutor;
import main.Task;
import main.TaskType;
import org.junit.Test;
import org.junit.platform.commons.logging.Logger;
import org.junit.platform.commons.logging.LoggerFactory;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static java.lang.Thread.sleep;

public class Tests {
    public static final Logger logger = LoggerFactory.getLogger(Tests.class);

    /**
     * our_Test checks:
     * 1. maximum priority is changed
     * 2. submit(Task)
     * 3. submit(Callable)
     * 4. submit(Callable, TaskType)
     * 5. submit a lot of tasks so that they will be waiting in queue
     */
    @Test
    public void our_Test(){
        CustomExecutor customExecutor1 = new CustomExecutor();
        logger.info(()-> "Current maximum priority = " +
                customExecutor1.getCurrentMax());
        for(int i=0; i<5; i++)
        {
            customExecutor1.submit(()-> {
                System.out.println("3OTHER!!!");
                sleep(1000);
                return 1000 * Math.pow(1.02, 30);
            }, TaskType.OTHER);

            logger.info(()-> "Current maximum priority = " +
                    customExecutor1.getCurrentMax());

            customExecutor1.submit(()-> {
                System.out.println("1COMPUTATIONAL!!!");
                return 1000 * Math.pow(1.02, 29);
            }, TaskType.COMPUTATIONAL);

            logger.info(()-> "Current maximum priority = " +
                    customExecutor1.getCurrentMax());

            customExecutor1.submit(()-> {
                System.out.println("2IO!!!");
                return 1000 * Math.pow(1.02, 37);
            }, TaskType.IO);

            logger.info(()-> "Current maximum priority = " +
                    customExecutor1.getCurrentMax());

            customExecutor1.submit(()-> {
                System.out.println("3OTHER!!!");
                return 1000 * Math.pow(1.02, 30);
            }, TaskType.OTHER);

            logger.info(()-> "Current maximum priority = " +
                    customExecutor1.getCurrentMax());

            customExecutor1.submit(()-> {
                System.out.println("3OTHER!!!");
                return 1000 * Math.pow(1.02, 30);
            }, TaskType.OTHER);

            logger.info(()-> "Current maximum priority = " +
                    customExecutor1.getCurrentMax());

            customExecutor1.submit(()-> {
                System.out.println("2IO!!!");
                return 1000 * Math.pow(1.02, 35);
            }, TaskType.IO);

            logger.info(()-> "Current maximum priority = " +
                    customExecutor1.getCurrentMax());
            Callable<String> c = () -> {
                StringBuilder sb = new StringBuilder("ABCDEFGHIJKLMNOPQRSTUVWXYZ");
                System.out.println("just Callable");
                return sb.reverse().toString();
            };
            customExecutor1.submit(c);

            logger.info(()-> "Current maximum priority = " +
                    customExecutor1.getCurrentMax());
            Task<?> task = Task.createTask(() -> {
                int sum = 0;
                System.out.println("as task");
                for (int j = 1; j <= 10; j++) {
                    sum += j;
                }
                return sum;
            }, TaskType.COMPUTATIONAL);
            customExecutor1.submit(task);

            logger.info(()-> "Current maximum priority = " +
                    customExecutor1.getCurrentMax());
        }

        customExecutor1.gracefullyTerminate();
        logger.info(()-> "Current maximum priority = " +
                customExecutor1.getCurrentMax());
    }

    @Test
    public void partialTest() {
        CustomExecutor customExecutor = new CustomExecutor();
        var task = Task.createTask(() -> {
            int sum = 0;
            for (int i = 1; i <= 10; i++) {
                sum += i;
            }
            return sum;
        }, TaskType.COMPUTATIONAL);
        var sumTask = customExecutor.submit(task);
        final int sum;
        try {
            //(int
            sum =(int) sumTask.get(1, TimeUnit.MILLISECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            throw new RuntimeException(e);
        }
        logger.info(() -> "Sum of 1 through 10 = " + sum);
        Callable<Double> callable1 = () -> {
            return 1000 * Math.pow(1.02, 5);
        };
        Callable<String> callable2 = () -> {
            StringBuilder sb = new StringBuilder("ABCDEFGHIJKLMNOPQRSTUVWXYZ");
            return sb.reverse().toString();
        };
        // var is used to infer the declared type automatically
        var priceTask = customExecutor.submit(() -> {
            return 1000 * Math.pow(1.02, 5);
        }, TaskType.COMPUTATIONAL);
        var reverseTask = customExecutor.submit(callable2, TaskType.IO);
        final Double totalPrice;
        final String reversed;
        try {
            //(double)
            totalPrice = (double) priceTask.get();
            //(String)
            reversed = (String) reverseTask.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
        logger.info(() -> "Reversed String = " + reversed);
        logger.info(() -> String.valueOf("Total Price = " + totalPrice));
        logger.info(() -> "Current maximum priority = " +
                customExecutor.getCurrentMax());
        customExecutor.gracefullyTerminate();
    }

    }


