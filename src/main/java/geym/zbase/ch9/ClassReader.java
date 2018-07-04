package geym.zbase.ch9;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class ClassReader {
    static int[] tag = {
            1, 7, 9, 10, 12
    };
    static int[] tagLen = {
            0, 2, 4, 4, 4
    };
    static Map<Integer, Integer> map = new HashMap<Integer, Integer>();

    public static String format(byte[] bt) {
        int line = 0;
        StringBuilder buf = new StringBuilder();
        int constNum = 0;    //常量池表项的个数
        for (byte d : bt) {
            if (line == 8)
                constNum = d;   //第9、10个字节为常量池大小
            if (line == 9)
                constNum += d - 1;//化为10进制-1为常量池表项的个数
            line++;
        }
        //System.out.println(constNum);
        String[] constant = new String[constNum];  //常量池表项
        int[] constLen = new int[constNum];  //对应长度
        for (int i = 0; i < tag.length; i++)    //添加各tag长度
            map.put(tag[i], tagLen[i]);    //为了方便 只添加了部分
        int lastConst = 10;  //第一个表项的位置
        boolean flag = false; //是否为utf8
        for (int i = 0; i < constNum; i++) {
            //暂时改为 1 进行测试 实际为 constNum
            if (i > 0) {
                if (flag)
                    lastConst += constLen[i - 1] + 3; //utf8 有两个字节记录长度
                else
                    lastConst += constLen[i - 1] + 1;  //之后每一个表项的起始位置
            }
            line = 0;   //记录长度
            for (byte d : bt) {
                if (line == lastConst) {
                    if ((int) d != 1) {
                        constLen[i] = map.get((int) d);
                        flag = false;
                        break;
                    }
                }
                if (line == lastConst + 2) {
                    //utf8长度
                    constLen[i] = (int) d;
                    flag = true;
                }
                line++;
            }
            int temp = 0;
            //System.out.println(constLen[i]);   //记录其长度
            byte[] bs = new byte[constLen[i]];   //
            int bsCount = 0;
            line = 0;
            for (byte d : bt) {
                //记录内容
                if (flag) {
                    //utf8内容 存入string
                    if (line > lastConst + 2 && line < lastConst + constLen[i] + 1 + 2) {
                        bs[bsCount] = d;
                        bsCount++;
                    }
                    if (line == lastConst + constLen[i] + 1 + 2) {
                        constant[i] = new String(bs);
                        break;
                    }
                } else {
                    //简单化分 存入id
                    if (line > lastConst && line < lastConst + constLen[i] + 1) {
                        temp += (int) d;
                    }
                    if (line == lastConst + constLen[i] + 1) {
                        constant[i] = String.valueOf(temp);
                        break;
                    }
                }
                line++;
            }
        }
        //for(String i : constant)
        //System.out.println(i);   //打印constant
        int sumOfConst = line; //最后一次循环走完 此时line为Class的访问标记
        line = 0;
        int AccessFlag = 2;
        int numOfInterface = 0;  //接口的数量
        int numOfField = 0;      //字段的数量
        int numOfMethod = 0;     //方法的数量
        for (int i = 0; i < bt.length; i++) {
            //找到CLass访问标记
            if (i == sumOfConst + AccessFlag) {
                //当前位置走过标志位后两位 对应着常量池项目标号为 constant[bt[i] + bt[++i] - 1]
                //再从标号处找到所对应的String名称， 注意下标要-1
                //每一次都++i 继续往下扫描
                System.out.println("当前类为 : " + constant[Integer.parseInt(constant[bt[i] + bt[++i] - 1]) - 1]);
                System.out.println(" 超  类 为 : " + constant[Integer.parseInt(constant[bt[++i] + bt[++i] - 1]) - 1]);
                numOfInterface += bt[++i];
                numOfInterface += bt[++i];
                //打印接口
                if (numOfInterface == 0) {
                    System.out.println(" - - - 无接口 - - -");
                } else {
                    //每个接口占有2个字节
                    System.out.println("接口数为： " + numOfInterface);
                    for (int noI = 0; noI < numOfInterface; noI++) {
                        System.out.println("接口分别为： " + constant[bt[++i] + bt[++i] - 1]);
                    }
                }
                numOfField += bt[++i];
                numOfField += bt[++i];
                //打印字段
                if (numOfField == 0) {
                    System.out.println(" - - - 无字段 - - -");
                } else {
                    //每个字段 前2个字节为 flag 之后2个字节为 name 之后2个字节为描述
                    //还有2个字节描述其他属性的个数
                    System.out.println("字段数为： " + numOfField);
                    for (int noF = 0; noF < numOfField; noF++) {
                        i += 2;
                        System.out.print("字段名字为： " + constant[bt[++i] + bt[++i] - 1] +
                                " 字段描述为 : " + constant[bt[++i] + bt[++i] - 1]);
                        int numOfAttrOfFiled = (int) (bt[++i] + bt[++i]);  //属性的数量
                        System.out.println(" 属性数量为 : " + numOfAttrOfFiled);
                        //每个属性占有8个字节
                        for (int noAoF = 0; noAoF < numOfAttrOfFiled; noAoF++) {
                            System.out.println("属性分别为： " + constant[bt[++i] + bt[++i] - 1] +
                                    constant[bt[++i] + bt[++i] - 1] + constant[bt[++i] + bt[++i] - 1] +
                                    constant[bt[++i] + bt[++i] - 1]);
                        }
                    }
                }
                numOfMethod += bt[++i];
                numOfMethod += bt[++i];
                //打印方法
                if (numOfMethod == 0) {
                    System.out.println(" - - - 无方法 - - - ");
                } else {
                    System.out.println("方法数为：" + numOfMethod);
                    //每个方法 前2个字节为 flag 之后2个字节为 name 之后2个字节为描述
                    //还有2个字节描述其他属性的个数
                    for (int noM = 0; noM < numOfMethod; noM++) {
                        i += 2;
                        System.out.print("方法名字为： " + constant[bt[++i] + bt[++i] - 1] +
                                " 方法描述为 : " + constant[bt[++i] + bt[++i] - 1]);
                        int numOfAttrOfMethod = (int) (bt[++i] + bt[++i]);  //属性的数量
                        System.out.println(" 属性数量为 : " + numOfAttrOfMethod);
                        //对于属性应该再进行判断 大部分名字都是Code
                        //前两位为名字 中间四位代表长度 取得长度
                        //由于系统的需求 这里不做详细的处理
                        for (int noAOM = 0; noAOM < numOfAttrOfMethod; noAOM++) {
                            i += 2;
                            int attributr_length = (int) (bt[++i] + bt[++i] + bt[++i] + bt[++i]);//取得属性长度
                            i += attributr_length;
                        }
                    }
                }
            }
        }
        line = 0;
        for (byte d : bt) {
            if (line % 16 == 0)
                buf.append(String.format("%05x: ", line));
            buf.append(String.format("%02x  ", d));
            line++;
            if (line % 16 == 0)
                buf.append("\n");
        }
        buf.append("\n");
        return buf.toString();
    }

    public static byte[] readFile(String file) throws IOException {
        InputStream is = new FileInputStream(file);
        int length = is.available();
        byte bt[] = new byte[length];
        is.read(bt);
        return bt;
    }

    public static void main(String[] agrs) throws Exception {
        String path = "C:\\workspace\\java\\java\\JVMInPractice\\target\\classes\\geym\\zbase\\ch9\\SimpleUser.class";
        FileInputStream inputStream = new FileInputStream(path);
        System.out.println("readMagic is: "+readMagic(inputStream));
        System.out.println("readMinorVersion is: "+readMinorVersion(inputStream));
        System.out.println("readMajorVersion is: "+readMajorVersion(inputStream));


        byte[] bt = ClassReader.readFile(path);
        String hexData = ClassReader.format(bt);
        System.out.println(hexData);
    }

    public static long readMagic(InputStream inputStream) throws Exception {

        byte[] bytes = new byte[4];
        try {
            inputStream.read(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
        long num = 0;
        for (int i= 0; i < bytes.length; i++) {
            num <<= 8;
            num |= (bytes[i] & 0xff);
        }
        return num;
    }

    public static int readMinorVersion(InputStream inputStream) {
        byte[] bytes = new byte[2];
        try {
            inputStream.read(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
        int num = 0;
        for (int i= 0; i < bytes.length; i++) {
            num <<= 8;
            num |= (bytes[i] & 0xff);
        }
        return num;
    }

    public static short readMajorVersion (InputStream inputStream) {
        byte[] bytes = new byte[1];
        try {
            inputStream.read(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
        short value = (short) (bytes[0] & 0xFF);
        return value;
    }

}