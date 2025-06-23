package com.caiqiang.simple.test.asmTest;

import org.springframework.asm.ClassVisitor;
import org.springframework.asm.FieldVisitor;
import org.springframework.asm.MethodVisitor;
import org.springframework.core.type.AnnotationMetadata;

import java.util.ArrayList;
import java.util.List;

/**
 * @program: spring
 * @description:
 * @author: caiqiang
 * @create: 2022-09-01 16:00
 **/
public class MyClassVisitor extends ClassVisitor {

	public MyClassVisitor(int api) {
		super(api);
	}

	@Override
	public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
		System.out.println("version:"+version);
		System.out.println("name:"+name);
		System.out.println("superName:"+superName);
		for (int i = interfaces.length - 1; i >= 0; i--) {
			System.out.println("interfaces:"+interfaces[i]);
		}
	}

	@Override
	public FieldVisitor visitField(int access, String name, String desc, String signature, Object value) {
		System.out.println("visitField:"+name);
		return null;
	}

	@Override
	public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
		System.out.println("visitMethod:"+name);
		return null;
	}

	@Override
	public void visitEnd() {
		super.visitEnd();
	}
}
