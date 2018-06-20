package geym.zbase.ch10.work3;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.WritableByteChannel;

public class HotClassLoader extends ClassLoader {

    public HotClassLoader() {
        super(ClassLoader.getSystemClassLoader());
    }

    private File objFile;

    public File getObjFile() {
        return objFile;
    }

    public void setObjFile(File objFile) {
        this.objFile = objFile;
    }

    @Override
    protected Class<?> findClass(String name) {
        System.out.println("findClass");
        Class clazz = null;
        try {
            byte[] data = getClassFileBytes(getObjFile());
            clazz = defineClass(name, data, 0, data.length);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return clazz;

    }

    /**
     * 把CLASS文件转成BYTE
     */
    private byte[] getClassFileBytes(File file) throws Exception {
        //采用NIO读取  
        FileInputStream fis = new FileInputStream(file);
        FileChannel fileC = fis.getChannel();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        WritableByteChannel outC = Channels.newChannel(baos);
        ByteBuffer buffer = ByteBuffer.allocateDirect(1024);
        while (true) {
            int i = fileC.read(buffer);
            if (i == 0 || i == -1) {
                break;
            }
            buffer.flip();
            outC.write(buffer);
            buffer.clear();
        }
        fis.close();
        return baos.toByteArray();
    }

} 