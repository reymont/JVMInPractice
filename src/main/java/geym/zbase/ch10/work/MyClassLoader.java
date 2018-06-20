package geym.zbase.ch10.work;

import java.io.IOException;
import java.net.MalformedURLException;  
import java.net.URL;  
import java.net.URLClassLoader;  
import java.util.Date;  
  
public class MyClassLoader extends URLClassLoader {  
  
    private  MyClassLoader loader = null;  
    Date startDate = new Date();  
    public MyClassLoader(URL[] urls) {  
        super(urls);  
    }  
  
    public MyClassLoader(ClassLoader parent) {  
        super(new URL[0], parent);  
    }  
  
    @Override  
    public void close() throws IOException {  
        // TODO Auto-generated method stub  
        super.close();  
    }  
    /** 
     * Adds a jar file from the filesystems into the jar loader list. 
     *  
     * @param jarfile 
     *            The full path to the jar file. 
     * @throws MalformedURLException 
     */  
    public void addJarFile(String jarfile) throws MalformedURLException {  
        URL url = new URL("file:" + jarfile);  
        addURL(url);  
    }  
      
    public void addDir(String path) throws MalformedURLException{  
        path= "file:"+path;  
        URL url = new URL(path);  
        addURL(url);  
    }  
  
  
      
    @Override  
    public String toString() {  
        // TODO Auto-generated method stub  
        return super.toString() + ",time:"+startDate.toLocaleString();  
    }  
      
}  