package Goods.Threads;

import java.util.List;

public class MyRunnable2 implements Runnable{
    private List<Object[]> lst;
    public void run(){}

    public synchronized List<Object[]> getLst() {
        return lst;
    }

    public synchronized void setLst(List<Object[]> lst) {
        this.lst = lst;
    }
}

