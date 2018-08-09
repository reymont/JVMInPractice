package geym.zbase.ch9.work2;

import java.io.IOException;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.Opcodes;

public class TestVisitor extends ClassVisitor{

	public TestVisitor(int asmVersion) {
		super(asmVersion);
	}
	
	public FieldVisitor visitField(int access, String name, String desc,
                                        String sig, Object value) {
		//如果字段加 final ,则可以有默认值value,否则为null
		System.out.println(access+"\t"+name+"\t"+desc+"\t"+sig+"\t"+value);
		return super.visitField(access, name, desc, sig, value);
	}
	
	public static void main(String[] args) throws IOException {
		TestBean t = new TestBean();
		t.setIn(5);
//		String p = t.getClass().getName();
//		ClassReader creader = new ClassReader(p);
		ClassReader creader = new ClassReader(
                    ClassLoader.getSystemResourceAsStream(
                    t.getClass().getName().replace(".", "/")+".class"));
		TestVisitor visitor = new TestVisitor(Opcodes.ASM5);
		creader.accept(visitor, 0);
	}
}