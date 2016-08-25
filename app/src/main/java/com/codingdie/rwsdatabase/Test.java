package com.codingdie.rwsdatabase;

/**
 * Created by xupen on 2016/8/22.
 */
public class Test {
    public static void main(String[] args) {
        final Test test=new Test();
        test.test2();
        for (int i=0;i<10;i++){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    test.test1();
                }
            }).start();
        }


    }
    public  synchronized void   test1(){
        try {
            Thread.sleep(5000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("test1");
    }
    public  synchronized  void   test2(){
        System.out.println("test1");
    }
}
