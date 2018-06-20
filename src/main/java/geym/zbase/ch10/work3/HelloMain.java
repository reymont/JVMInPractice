package geym.zbase.ch10.work3;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.lang.reflect.Method;

public class HelloMain {

    private static Logger logger = LoggerFactory.getLogger(HelloMain.class);

    private static MethodExcuteThread methodExcuteThread = new MethodExcuteThread();
    private static ClassFileChangeListenerThread classFileChangeListenerThread = new ClassFileChangeListenerThread();

    private static volatile Class desClazz;//共享变量

    public static void main(String[] args) {
        classFileChangeListenerThread.start();
        methodExcuteThread.start();
    }

    /**
     * 通过比较class文件，来判断class是否有改变
     */
    private static class ClassFileChangeListenerThread extends Thread {
        @Override
        public void run() {
            try {
                File file = new File(HelloMain.class.getResource("").getFile() + "Worker.class");
                long lastTime = file.lastModified();
                boolean isFirst = true;
                while (true) {
                    Thread.sleep(2000);
                    File newFile = new File(HelloMain.class.getResource("").getFile() + "Worker.class");
                    long nowModified = newFile.lastModified();
                    if (lastTime != nowModified) {
                        logger.info("worker is changed ---> " + nowModified);
                        lastTime = nowModified;
                        reloadFile(newFile, methodExcuteThread);
                    } else {
                        if (isFirst) {
                            reloadFile(newFile, methodExcuteThread);
                            isFirst = false;
                        } else {
                            logger.debug("worker is not changed");
                        }
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 方法执行线程
     */
    private static class MethodExcuteThread extends Thread {
        volatile InheritableThreadLocal<Class> excuteClassLocal = new InheritableThreadLocal<>();

        @Override
        public void run() {
            while (true) {
                try {
                    Class excuteClazz = desClazz;
                    if (null == excuteClazz) {
                        Thread.sleep(2000);
                        System.out.println("no class");
                        continue;
                    }
                    Thread.sleep(1000);
                    Object objObject = excuteClazz.getConstructor(new Class[]{}).newInstance(new Object[]{});
                    Method excuteClazzMethod = excuteClazz.getMethod("doit", null);
                    excuteClazzMethod.invoke(objObject, null);//执行
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        public InheritableThreadLocal<Class> getExcuteClassLocal() {
            return excuteClassLocal;
        }

        public void setExcuteClassLocal(InheritableThreadLocal<Class> excuteClassLocal) {
            this.excuteClassLocal = excuteClassLocal;
        }
    }


    /**
     * 重新加载FILE
     */
    private static void reloadFile(File newFile, MethodExcuteThread methodExcuteThread) {
        HotClassLoader hotClassLoader = new HotClassLoader();
        hotClassLoader.setObjFile(newFile);
        try {
            Class<?> objClass = hotClassLoader.findClass("geym.zbase.ch10.work3.Worker");
            // 获取当前线程的上下文加载器，并加载Worker对象
            methodExcuteThread.getExcuteClassLocal().set(objClass);
            desClazz = objClass;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}