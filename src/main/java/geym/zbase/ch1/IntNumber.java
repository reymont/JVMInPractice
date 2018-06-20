package geym.zbase.ch1;

public class IntNumber {
    public static void main(String[] args) {
        int a=-10;
        for(int i=0;i<32;i++){
            int t=(a & 0x80000000>>>i)>>>(31-i);
            System.out.print(t);
        }
        System.out.println();
        System.out.println(Integer.toBinaryString(a));
        System.out.println(Integer.toBinaryString(-99));
        System.out.println(Integer.toBinaryString(-105));
        System.out.println(Integer.toBinaryString(205));
        a=205;
        for(int i=0;i<32;i++){
            int t=(a & 0x80000000>>>i)>>>(31-i);
            System.out.print(t);
        }
    }
}
