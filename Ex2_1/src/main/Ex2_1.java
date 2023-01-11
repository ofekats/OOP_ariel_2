package main;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.io.File;
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.concurrent.*;
import java.time.*;

public class Ex2_1 {

    /**
     * Creat n text file with random number fo lines
     * and in each line write "Hello, World!"
     * @param n
     * @param seed
     * @param bound
     * @return array of the files names that was created
     */
    public static String[] createTextFiles(int n, int seed, int bound)
    {
        if(n >= 0)
        {
            String files_names [] = new String [n];
            Random rand = new Random(seed);
            for (int i = 0; i < n; i++) {
                //generate number of line in a file
                int x = rand.nextInt(bound);
                try {

                    String name = "file_"+(i+1)+".txt";
                    // Create a new file
                    File file = new File(name);
                    files_names[i] = name;
                    // Create a new PrintWriter object for writing to the file
                    PrintWriter writer = new PrintWriter(file);
                    for (int j = 0; j < x; j++) {
                        // Write some text to the file
                        if (j == x - 1) {
                            //the last line will not have \n
                            writer.print("Hello, World!");
                            break;
                        }
                        writer.println("Hello, World!");
                    }
                    // Close the PrintWriter
                    writer.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return files_names;
        }else {
            System.out.println("number of files need to be positive!");
        }
        return null;
    }

    /**
     * Calculate num of lines in one file (auxiliary function)
     * @param fileNames
     * @return num of lines
     */
    public static int getNumOfLines_OneFile(String fileNames)
    {
        int sum = 0;
        try {
            FileReader fr = new FileReader(fileNames);
            BufferedReader br = new BufferedReader(fr);
            String line = br.readLine();
            while (line != null)
            {
                sum++;
                line = br.readLine();
            }
            br.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return sum;
    }

    /**
     * Calculate the number of lines in all the given files,
     * and count the time it took and print that time.
     * @param fileNames -array of names of files
     * @return num of lines (sum of all the files lines)
     */
    public static int getNumOfLines(String[] fileNames)
    {
        Instant start = Instant.now();
        int sum = 0;
        for (int i =0; i < fileNames.length; i++)
        {
            sum += getNumOfLines_OneFile(fileNames[i]);
        }
        Instant end = Instant.now();
        Duration elapsed = Duration.between(start, end);
        long elapsedMillis = elapsed.toMillis();
        System.out.println("func 2, without threads: "+elapsedMillis +" Millis");
        return sum;
    }

    /**
     * Calculate the number of lines in all the given files.
     * each file being Calculate in different thread,
     * and count the time it took and print that time.
     * @param fileNames -array of names of files
     * @return num of lines (sum of all the files lines)
     */
    public int getNumOfLinesThreads(String[] fileNames)
    {
        Instant start = Instant.now();
        int sum = 0;
        Mythread [] threads = new Mythread[fileNames.length];
        try {
            for(int i=0; i < fileNames.length; i++) {
                threads[i] = new Mythread(fileNames[i]);
                threads[i].start();
            }
            for(int i=0; i < fileNames.length; i++) {
                threads[i].join();
                sum += threads[i].getNumOfLines();
            }
            } catch (Exception e) {
                e.printStackTrace();
            }
        Instant end = Instant.now();
        Duration elapsed = Duration.between(start, end);
        long elapsedMillis = elapsed.toMillis();
        System.out.println("func 3, with threads: "+elapsedMillis +" Millis");
        return sum;
    }

    /**
     * Calculate the number of lines in all the given files.
     * each file being Calculate in a thread that executes by thread-pool,
     * and count the time it took and print that time.
     * @param fileNames -array of names of files
     * @return num of lines (sum of all the files lines)
     */
    public int getNumOfLinesThreadPool(String[] fileNames)
    {
        Instant start = Instant.now();
        int sum = 0;
        try {
            MyTreadPool [] threads = new MyTreadPool[fileNames.length];
            List<Future<Integer>> futures = new ArrayList<>();
            ExecutorService pool = Executors.newFixedThreadPool(fileNames.length);
            for(int i=0; i < threads.length; i++)
            {
                threads[i] = new MyTreadPool(fileNames[i]);
                Future<Integer> future = pool.submit(threads[i]);
                futures.add(future);
            }
            for(Future f : futures)
            {
                sum += (int) f.get();
            }
            pool.shutdown();
        }catch (Exception e) {
            e.printStackTrace();
        }
        Instant end = Instant.now();
        Duration elapsed = Duration.between(start, end);
        long elapsedMillis = elapsed.toMillis();
        System.out.println("func 4, with thread-pool: "+elapsedMillis +" Millis");
        return sum;
    }

    /**
     * delete files
     * @param fileNames
     */
    public static void delete_files(String[] fileNames)
    {
        try {
            for (int i =0; i< fileNames.length; i++) {
                File f = new File(fileNames[i]);
                if (f.exists()) {
                    if (f.delete()) {

                    } else {
                        System.out.println("Delete operation is failed.");
                    }
                } else {
                    System.out.println(fileNames[i] + " doesn't exist.");
                }
            }
        } catch (SecurityException e) {
                e.printStackTrace();
            }
    }
}

