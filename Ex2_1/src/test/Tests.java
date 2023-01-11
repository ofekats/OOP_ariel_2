package test;

import main.Ex2_1;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class Tests {

    @Test
    public void Test(){
        int n = 3000;
        int expected = 1507237;
        String files_names [] = Ex2_1.createTextFiles(n, 3, 1000);
        int num1 = Ex2_1.getNumOfLines(files_names);
        assertEquals(expected ,num1,0);
        System.out.println("num of lines: "+num1);
        Ex2_1 e1 = new Ex2_1(){
            @Override
            public int getNumOfLinesThreads(String[] fileNames){
                return super.getNumOfLinesThreads(fileNames);
            }
        };
        int num2 = e1.getNumOfLinesThreads(files_names);
        assertEquals(expected , num2,0);
        System.out.println("num of lines: "+num2);

        Ex2_1 e2 = new Ex2_1(){
            @Override
            public int getNumOfLinesThreadPool(String[] fileNames){
                return super.getNumOfLinesThreadPool(fileNames);
            }
        };
        int num3 = e2.getNumOfLinesThreadPool(files_names);
        assertEquals(expected ,num3,0);
        System.out.println("num of lines: "+num3);
        Ex2_1.delete_files(files_names);
    }

    /**
     * if n < 0 exit
     */
    @Test
    public void Test_n0(){
        String files_names [] = Ex2_1.createTextFiles(-1, 3, 1000);
        assertEquals(null ,files_names);
    }

}
