package com.caiqiang.simple.test.asmTest;

import org.springframework.asm.ClassReader;
import org.springframework.asm.ClassWriter;
import org.springframework.asm.Opcodes;

import java.io.IOException;

import static org.springframework.asm.Opcodes.*;

/**
 * @program: spring
 * @description:
 * @author: caiqiang
 * @create: 2022-09-01 16:00
 **/
public class AsmTest {

	private static final int PARSING_OPTIONS = ClassReader.SKIP_DEBUG | ClassReader.SKIP_CODE | ClassReader.SKIP_FRAMES;

	public static void main(String[] args) throws IOException {
       MyClassVisitor myClassVisitor = new MyClassVisitor(Opcodes.ASM5);

		ClassReader cr = new ClassReader(MyClassVisitor.class.getClassLoader().getResourceAsStream("com/caiqiang/simple/test/asmTest/AsmBean.class"));//读取测试类内容

		cr.accept(myClassVisitor,0);
	}

//	public static void main(String[] args) {
//		/*自定义一个ClassWriter对象*/
//		ClassWriter cw = new ClassWriter(0);
//		/*生成1.5版本,名为comparable,父类为Object*/
//		cw.visit(V1_5,ACC_PUBLIC+ACC_ABSTRACT+ACC_INTERFACE,"pkg/comparable",null,"java/long/Object",null);
//		/*生成Field,名字是"LESS",类型是int,值是-1*/
//		cw.visitField(ACC_PUBLIC + ACC_FINAL + ACC_STATIC,"LESS","I",null,-1).visitEnd();
//		/*最后生成一个方法,方法名为"compareTo",参数是java/lang/Object,*/
//		cw.visitMethod(ACC_PUBLIC+ACC_ABSTRACT,"compareTo","(Ljava/lang/Object;)I",null,null).visitEnd();
//		/*将其转换为二进制数组*/
//		byte[] b = cw.toByteArray();
//		/*使用这个字节数组*/
//
//		/*load为一个Class对象,这里会涉及到Java的反射*/
//		Class c = AsmTest.class.getClassLoader().defineClass("pkg.Comparable",b);
//		/*然后将其打印出来,由于是一个这是一个接口,所以他没有构造方法,第0个方法就是"comparable"*/
//		System.out.println(c.getMethods()[0].getName());
//	}
}
