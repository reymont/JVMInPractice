package geym.zbase.ch8.work;

public class DeadLockSynchronized implements Runnable {
    public int flag = 1;
    //静态对象是类的所有对象共享的  
    private static Object A = new Object(), B = new Object(), C = new Object(), D = new Object();
    @Override 
    public void run() {  
        System.out.println("flag=" + flag);  
        if (flag == 0) {
            synchronized (A) {
                try {  
                    Thread.sleep(500);  
                } catch (Exception e) {  
                    e.printStackTrace();  
                }  
                synchronized (B) {
                    System.out.println("0");
                }  
            }  
        }  
        if (flag == 1) {
            synchronized (B) {
                try {  
                    Thread.sleep(500);  
                } catch (Exception e) {  
                    e.printStackTrace();  
                }  
                synchronized (C) {
                    System.out.println("1");
                }  
            }  
        }
        if (flag == 2) {
            synchronized (C) {
                try {
                    Thread.sleep(500);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                synchronized (D) {
                    System.out.println("2");
                }
            }
        }
        if (flag == 3) {
            synchronized (D) {
                try {
                    Thread.sleep(500);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                synchronized (A) {
                    System.out.println("3");
                }
            }
        }
    }  
   
    public static void main(String[] args) {  
           
        DeadLockSynchronized td1 = new DeadLockSynchronized();
        DeadLockSynchronized td2 = new DeadLockSynchronized();
        DeadLockSynchronized td3 = new DeadLockSynchronized();
        DeadLockSynchronized td4 = new DeadLockSynchronized();
        td1.flag = 0;
        td2.flag = 1;
        td3.flag = 2;
        td4.flag = 3;
        new Thread(td1).start();
        new Thread(td2).start();
        new Thread(td3).start();
        new Thread(td4).start();
    }  
}