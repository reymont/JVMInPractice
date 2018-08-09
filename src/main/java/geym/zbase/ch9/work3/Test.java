package geym.zbase.ch9.work3;

import org.objectweb.asm.*;

public class Test {
    public static void main(String[] args) throws Exception {
        ClassPrinter printer = new ClassPrinter();
        ClassReader cr = new ClassReader("geym.zbase.ch9.FullUser");
        cr.accept(printer, 0);
    }

    static class ClassPrinter extends ClassVisitor {
        public ClassPrinter() {
            super(Opcodes.ASM5);
        }

        @Override
        public FieldVisitor visitField(int access, String name, String desc,
                                       String signature, Object value) {
            //打印出字段
            System.out.println(access + " " + name + " " + desc + " " + signature + " " + value);
            if (cv != null) {
                return cv.visitField(access, name, desc, signature, value);
            }
            return null;
        }

        @Override
        public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
            super.visit(version, access, name, signature, superName, interfaces);
            //打印出父类name和本类name
            System.out.println(superName + " " + name);
        }

        @Override
        public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
            //打印出方法名和类型签名
            System.out.println(name + " " + desc);
            System.out.println(access + " " + name + " " + desc + " " + signature + " " + exceptions);
            return super.visitMethod(access, name, desc, signature, exceptions);
        }
    }
}