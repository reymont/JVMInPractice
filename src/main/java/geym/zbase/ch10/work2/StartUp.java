package geym.zbase.ch10.work2;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;  
  
public class StartUp {  
      
    public static void main(String[] args) throws ClassNotFoundException {  
          
        int i = 0 ;  
          
        while(true){  
            MyClassLoader mcl = new MyClassLoader() ;  
            System.out.println(mcl.getParent());  
            Class<?> personClass =  mcl.findClass("geym.zbase.ch10.work2.Person");
              
            try {  
                Object person =  personClass.newInstance() ;  
                Method sayHelloMethod = personClass.getMethod("sayHello") ;  
                sayHelloMethod.invoke(person) ;  
                System.out.println(++i);  
            } catch (InstantiationException e1) {  
                e1.printStackTrace();  
            } catch (IllegalAccessException e1) {  
                e1.printStackTrace();  
            } catch (SecurityException e) {  
                e.printStackTrace();  
            } catch (NoSuchMethodException e) {  
                e.printStackTrace();  
            } catch (IllegalArgumentException e) {  
                e.printStackTrace();  
            } catch (InvocationTargetException e) {  
                e.printStackTrace();  
            }  
              
            try {  
                Thread.sleep(1000) ;  
            } catch (InterruptedException e) {  
                e.printStackTrace();  
            }  
        }  
          
    }  
  
}  