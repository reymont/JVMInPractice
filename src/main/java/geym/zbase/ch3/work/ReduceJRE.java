package geym.zbase.ch3.work;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarInputStream;
import java.util.jar.JarOutputStream;

public class ReduceJRE {

        public static void main(String[] args) throws Exception {
                String mainJar = null;
                String classDenpdencyFile = null;
                if (args != null && args.length == 2) {
                        mainJar = args[0];
                        classDenpdencyFile = args[1];
                } else {
                        mainJar = "F:\\Program Files\\Java\\jre7\\lib\\rt.jar";
                        classDenpdencyFile = "F:\\Program Files\\Java\\jre7\\lib\\classdepency.txt";
                }
                List depencyClass = new ArrayList();
                BufferedReader br = new BufferedReader(new InputStreamReader(
                                new FileInputStream(classDenpdencyFile)));
                String templine = br.readLine();
                // load all the dependency class and store them in a array list;
                while (templine != null) {
                        int end = templine.lastIndexOf("from");
                        int begin = templine.lastIndexOf("[Loaded") + 7;
                        String className = templine.substring(begin, end).replace(".", "/")
                                        .trim();
                        depencyClass.add(className);
                        templine = br.readLine();
                }
                JarFile zipIn = new JarFile(mainJar);
                InputStream readin = null;
                JarOutputStream jos = new JarOutputStream(
                                new FileOutputStream("rt.jar"));
                JarInputStream jis = new JarInputStream(new FileInputStream(mainJar));
                JarEntry entry = jis.getNextJarEntry();
                while (entry != null) {
                        String name = entry.getName();
                        // remove the .class suffix.
                        name = name.substring(0, name.lastIndexOf("."));
                        if (depencyClass.contains(name)) {
                                // put an entry record and write the binary data
                                jos.putNextEntry(entry);
                                readin = zipIn.getInputStream(entry);
                                byte[] temp = new byte[4096];
                                int count = readin.read(temp);
                                while (count != -1) {
                                        jos.write(temp, 0, count);
                                        count = readin.read(temp);
                                }
                                readin.close();
                        }
                        entry = jis.getNextJarEntry();
                }
                jis.close();
                jos.close();
        }
}