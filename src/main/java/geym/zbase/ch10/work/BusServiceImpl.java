package geym.zbase.ch10.work;

import java.util.Date;
  
public class BusServiceImpl implements BusService{
  
    boolean close = false;  
    Date date= new Date();  
    String version ="5.0";  
    {  
        Thread thread = new Thread(){  
            public void run() {  
                setName("buserviceimplThread:"+date.toLocaleString());  
                System.out.println(this.toString() + ",time:"+date.toLocaleString());  
                while(!close){  
                    doIt("");  
                    try {  
                        System.out.println(getName() + ",org time:"+date.toLocaleString() + "  ,curtime:"+ new Date().toLocaleString());  
                        Thread.sleep(5000);  
                    } catch (InterruptedException e) {  
                        // TODO Auto-generated catch block  
                        e.printStackTrace();  
                    }  
                }  
                  
            };  
        };  
        thread.start();  
    }  
    @Override  
    public String doIt(String name) {  
        String res = version + name+",hello";  
  
        return res;  
    }  
    @Override  
    public void close() {  
        close = true;  
        System.out.println("close invoked !,"+this.toString() + ",time:"+date.toLocaleString());  
    }  
  
}  