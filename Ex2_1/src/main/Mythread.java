package main;

/**
 * this class extends Thread. MyTread object contains file name and number of line of this file.
 * Mythread calculate the num of lines in the file in run function.
 */
public class Mythread extends Thread{

    private String filename;
    private int num_of_lines;

    /**
     * constructor
     * @param filename
     */
    public Mythread(String filename)
    {
        this.filename = filename;
    }

    @Override
    public void run()
    {
        this.num_of_lines = Ex2_1.getNumOfLines_OneFile(filename);
    }

    public int getNumOfLines()
    {
        return this.num_of_lines;
    }


}
