package main;

import java.util.concurrent.Callable;

/**
 * this class implement Callable. MyTreadPool object contains file name.
 * MythreadPool calculate the num of lines in the file in call function.
 */
public class MyTreadPool implements Callable<Integer>{

    private String fileName;

    public MyTreadPool(String filename)
    {
        this.fileName = filename;
    }

    @Override
    public Integer call()
    {
        int num_line = Ex2_1.getNumOfLines_OneFile(fileName);
        return num_line;
    }
}
