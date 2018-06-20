package geym.zbase.ch4.work;

import java.util.ArrayList;
import java.util.List;

/**
 * https://github.com/GerogeLeon/Practice/blob/master/jvm/jvmDemo/src/main/java/GenerateSTW.java
 * java -Xms20M -Xmx20M -XX:+PrintGCDetails -XX:+PrintGCTimeStamps GenerateSTW
 */
public class GenerateSTW {
    /**
     * 通过集合引用对象，保证对象不被gc回收
     */
    private List<byte[]> content = new ArrayList<byte[]>();

    public static void main(String[] args) {
        GenerateSTW stw = new GenerateSTW();
        stw.start();
    }

    private void start() {
        while (true) {
            try {
                content.add(new byte[1024]);
            } catch (OutOfMemoryError e) {
                e.printStackTrace();
                //在不可以分配的时候，进行清理部分空间,继续运行，这样会很快产生下一次垃圾回收

                content.clear();

            }

        }
    }

}