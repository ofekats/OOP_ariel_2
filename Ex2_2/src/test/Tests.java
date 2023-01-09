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
    @Test
    public void partialTest(){
        CustomExecutor customExecutor = new CustomExecutor();
        var task = Task.createTask(()->{
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
        logger.info(()-> "Sum of 1 through 10 = " + sum);
        Callable<Double> callable1 = ()-> {
            return 1000 * Math.pow(1.02, 5);
        };
        Callable<String> callable2 = ()-> {
            StringBuilder sb = new StringBuilder("ABCDEFGHIJKLMNOPQRSTUVWXYZ");
            return sb.reverse().toString();
        };
        // var is used to infer the declared type automatically
        var priceTask = customExecutor.submit(()-> {
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
        logger.info(()-> "Reversed String = " + reversed);
        logger.info(()->String.valueOf("Total Price = " + totalPrice));
        logger.info(()-> "Current maximum priority = " +
                customExecutor.getCurrentMax());
        for(int i=0; i<15; i++)
        {
            customExecutor.submit(()-> {
                System.out.println("3OTHER!!!");
                return 1000 * Math.pow(1.02, 30);
            }, TaskType.OTHER);
            customExecutor.submit(()-> {
                System.out.println("1COMPUTATIONAL!!!");
                return 1000 * Math.pow(1.02, 29);
            }, TaskType.COMPUTATIONAL);
            customExecutor.submit(()-> {
                System.out.println("2IO!!!");
                return 1000 * Math.pow(1.02, 3);
            }, TaskType.IO);
            customExecutor.submit(()-> {
                System.out.println("3OTHER!!!");
                return 1000 * Math.pow(1.02, 30);
            }, TaskType.OTHER);
            customExecutor.submit(()-> {
                System.out.println("3OTHER!!!");
                return 1000 * Math.pow(1.02, 30);
            }, TaskType.OTHER);
            customExecutor.submit(()-> {
                System.out.println("2IO!!!");
                return 1000 * Math.pow(1.02, 3);
            }, TaskType.IO);
            logger.info(()-> "Current maximum priority = " +
                    customExecutor.getCurrentMax());
        }
//        customExecutor.submit(()-> {
//            System.out.println("COMPUTATIONAL!!!");
//            return 1000 * Math.pow(1.02, 29);
//        }, TaskType.COMPUTATIONAL);
//        customExecutor.submit(()-> {
//            System.out.println("COMPUTATIONAL!!!");
//            return 1000 * Math.pow(1.02, 14);
//        }, TaskType.COMPUTATIONAL);
//        customExecutor.submit(()-> {
//            System.out.println("COMPUTATIONAL!!!");
//            return 1000 * Math.pow(1.02, 9);
//        }, TaskType.COMPUTATIONAL);
//        customExecutor.submit(()-> {
//            System.out.println("IO!!!");
//            return 1000 * Math.pow(1.02, 3);
//        }, TaskType.IO);
//        customExecutor.submit(()-> {
//            System.out.println("COMPUTATIONAL!!!");
//            return 1000 * Math.pow(1.02, 3);
//        }, TaskType.COMPUTATIONAL);
//        customExecutor.submit(()-> {
//            System.out.println("IO!!!");
//            return 1000 * Math.pow(1.02, 3);
//        }, TaskType.IO);
//        customExecutor.submit(()-> {
//            System.out.println("OTHER!!!");
//            return 1000 * Math.pow(1.02, 30);
//        }, TaskType.OTHER);
//        customExecutor.submit(()-> {
//            System.out.println("IO!!!");
//            return 1000 * Math.pow(1.02, 30);
//        }, TaskType.IO);
//        customExecutor.submit(()-> {
//            return 1000 * Math.pow(1.02, 3);
//        }, TaskType.COMPUTATIONAL);
//        customExecutor.submit(()-> {
//            return 1000 * Math.pow(1.02, 3);
//        }, TaskType.COMPUTATIONAL);
//        customExecutor.submit(()-> {
//            sleep(300000);
//            return 1000 * Math.pow(1.02, 3);
//        }, TaskType.COMPUTATIONAL);
//        customExecutor.submit(()-> {
//            sleep(30000);
//            return 1000 * Math.pow(1.02, 3);
//        }, TaskType.COMPUTATIONAL);
//        customExecutor.submit(()-> {
//            sleep(30000);
//            return 1000 * Math.pow(1.02, 3);
//        }, TaskType.COMPUTATIONAL);
//        logger.info(()-> "Current maximum priority = " +
//                customExecutor.getCurrentMax());
//        customExecutor.submit(callable2, TaskType.IO);
//        var reverseTask1 = customExecutor.submit(callable2, TaskType.IO);
//        logger.info(()-> "Current maximum priority = " +
//                customExecutor.getCurrentMax());
//        customExecutor.submit(()-> {
//            sleep(30000);
//            return 1000 * Math.pow(1.02, 2);
//        }, TaskType.COMPUTATIONAL);
//        logger.info(()-> "Current maximum priority = " +
//                customExecutor.getCurrentMax());

        customExecutor.gracefullyTerminate();
    }
}

