package geym.zbase.ch10.work3;

public class Worker {
    public void doit() {
        System.out.println(this.getClass().getClassLoader().toString() + " ---> doit" );
    }
}