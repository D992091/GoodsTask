package Goods.Threads;

public class MyRunnable1 implements Runnable{
    private Long count;
    public synchronized void setCount(Long count){this.count = count;}
    public synchronized Long getCount(){
        return count;
    }
    public void run(){}
}
