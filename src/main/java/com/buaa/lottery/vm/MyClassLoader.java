package com.buaa.lottery.vm;

public class MyClassLoader extends ClassLoader{

	public Class defineMyClass ( byte[] b, int off, int len) 
	{
		return super.defineClass( b, off, len);
	}
}
