package com.buaa.lottery.vm;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

public class ClazzUtils {
	/**
	 * ���ַ����룬��ִ����ķ���
	 * @param str
	 */
	public static void compileClass(String str){
		//��ȡ���ص�class�ļ��ڵ��ֽ��룬ת�����ֽ�������
		File file = new File(".");
		InputStream input;
		try {
			
			input = new FileInputStream(file.getCanonicalPath()+"\\bin\\Programmer.class");
			byte[] result = new byte[1024];
			int count = input.read(result);

			
			//���ֽ�����ת�����ַ�
			String temp =bytesToHexString(result);

		
			//���ַ�ת�����ֽ�����
			byte[] bt = hexStringToBytes(temp);
			
			// ʹ���Զ������������� byte�ֽ�������ת��Ϊ��Ӧ��class����
			MyClassLoader loader = new MyClassLoader();
			Class clazz = loader.defineMyClass( bt, 0, count);
			//���Լ����Ƿ�ɹ�����ӡclass ��������
			System.out.println(clazz.getCanonicalName());
	                
	               //ʵ��һ��Programmer����
	               Object o= clazz.newInstance();
	               
	                   //����Programmer��code����
	              clazz.getMethod("setName", String.class).invoke(o, "gaoqing");
	              clazz.getMethod("getName", null).invoke(o, null);
	              
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static String bytesToHexString(byte[] src){
		StringBuilder stringBuilder = new StringBuilder("");
		if(src==null||src.length<=0){
			return null;
		}
		for (int i = 0; i < src.length; i++) {
			int v = src[i] & 0xFF;
			String hv = Integer.toHexString(v);
			if (hv.length() < 2) {
				stringBuilder.append(0);
			}   
			stringBuilder.append(hv);
		}   
		return stringBuilder.toString();   
	}
	 
	/**
	 * Convert hex string to byte[]
	 * @param hexString
	 * @return
	 */
	public static byte[] hexStringToBytes(String hexString) {  
	    if (hexString == null || hexString.equals("")) {  
	        return null;  
	    }  
	    hexString = hexString.toUpperCase();  
	    int length = hexString.length() / 2;  
	    char[] hexChars = hexString.toCharArray();  
	    byte[] d = new byte[length];  
	    for (int i = 0; i < length; i++) {  
	        int pos = i * 2;  
	        d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));  
	    }  
	    return d;  
	}
	
	/**
	 * Convert char to byte
	 * @param c
	 * @return
	 */
	private static byte charToByte(char c) {  
	    return (byte) "0123456789ABCDEF".indexOf(c);  
	} 
	
	
	/**
	 * ��̬���һ��class�ļ�
	 */
	public static void saveClassFile(){

		//��ȡ���ص�class�ļ��ڵ��ֽ��룬ת�����ֽ�������
		File file = new File(".");
		InputStream input,input_two;
		try {
			
			input = new FileInputStream(file.getCanonicalPath()+"Programmer.class");
			byte[] result = new byte[1024];
			int count = input.read(result);

			
			//���ֽ�����ת�����ַ�
			String temp =bytesToHexString(result);

		
			//���ַ�ת�����ֽ�����
			byte[] bt = hexStringToBytes(temp);
			
			// ʹ���Զ������������� byte�ֽ�������ת��Ϊ��Ӧ��class����
			MyClassLoader loader = new MyClassLoader();
			Class clazz = loader.defineMyClass(bt, 0, count);
			//���Լ����Ƿ�ɹ�����ӡclass ��������
			System.out.println(clazz.getCanonicalName());
	                
	               //ʵ��һ��Programmer����
	         Object o= clazz.newInstance();
	               
	                   //����Programmer��code����
	         clazz.getMethod("setName", String.class).invoke(o, "wangfangda");
	        // clazz.getMethod("getName", null).invoke(o, null);
	         
	         input_two = new FileInputStream(file.getCanonicalPath()+"\\bin\\Programmer.class");
	         
			 byte[] result_two = new byte[1024];
			 int count_two = input_two.read(result_two);
				//���ֽ�����ת�����ַ�
			 String temp_two =bytesToHexString(result_two);
/*
			
				//���ַ�ת�����ֽ�����
				//byte[] bt = hexStringToBytes(temp_two);
				
				// ʹ���Զ������������� byte�ֽ�������ת��Ϊ��Ӧ��class����
				MyClassLoader loader_two = new MyClassLoader();
				Class clazz_two = loader_two.defineMyClass( result_two, 0, count_two);
				//���Լ����Ƿ�ɹ�����ӡclass ��������
				System.out.println(clazz_two.getCanonicalName());
				clazz.getMethod("getName", null).invoke(o, null);*/
	              
	              
	              
	              
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}
}
