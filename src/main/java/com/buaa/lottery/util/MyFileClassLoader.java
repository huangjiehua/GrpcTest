/**
 * Copyright (c) 2015-2016, Tao wang 王涛 (wtsoftware@163.com).
 *
 * Licensed under the GNU Lesser General Public License (LGPL) ,Version 3.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.gnu.org/licenses/lgpl-3.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.buaa.lottery.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 文件加载类 可根据MyFileClassLoader 从文件中动态生成类
 * 
 * @author chengmingwei
 *
 */
public class MyFileClassLoader extends ClassLoader {

	private String classPath;

	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws ClassNotFoundException, InstantiationException,
			IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		MyFileClassLoader fileClsLoader = new MyFileClassLoader();
		fileClsLoader.setClassPath("c:\\");
		Class cls = fileClsLoader.loadClass("com.buaa.lottery.vm.Project");
		Object obj = cls.newInstance();
		Method[] mthds = cls.getMethods();
		for (Method mthd : mthds) {
			String methodName = mthd.getName();
			System.out.println("mthd.name=" + methodName);
		}
		System.out.println("obj.class=" + obj.getClass().getName());
		System.out.println("obj.class=" + cls.getClassLoader().toString());
		System.out.println("obj.class=" + cls.getClassLoader().getParent().toString());
	}

	/**
	 * 根据类名字符串从指定的目录查找类，并返回类对象
	 */
	@Override
	protected Class<?> findClass(String name) throws ClassNotFoundException {
		byte[] classData = null;
		try {
			classData = loadClassData(name);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return super.defineClass(name, classData, 0, classData.length);
	}

	/**
	 * 根据类名字符串加载类 byte 数据流
	 * 
	 * @param name
	 *            类名字符串 例如： com.cmw.entity.SysEntity
	 * @return 返回类文件 byte 流数据
	 * @throws IOException
	 */
	private byte[] loadClassData(String name) throws IOException {
		File file = getFile(name);
		FileInputStream fis = new FileInputStream(file);
		byte[] arrData = new byte[(int) file.length()];
		fis.read(arrData);
		return arrData;
	}

	/**
	 * 根据类名字符串返回一个 File 对象
	 * 
	 * @param name
	 *            类名字符串
	 * @return File 对象
	 * @throws FileNotFoundException
	 */
	private File getFile(String name) throws FileNotFoundException {
		File dir = new File(classPath);
		if (!dir.exists())
			throw new FileNotFoundException(classPath + " 目录不存在！");
		String _classPath = classPath.replaceAll("[\\\\]", "/");
		int offset = _classPath.lastIndexOf("/");
		name = name.replaceAll("[.]", "/");
		if (offset != -1 && offset < _classPath.length() - 1) {
			_classPath += "/";
		}
		_classPath += name + ".class";
		dir = new File(_classPath);
		if (!dir.exists())
			throw new FileNotFoundException(dir + " 不存在！");
		return dir;
	}

	public String getClassPath() {
		return classPath;
	}

	public void setClassPath(String classPath) {
		this.classPath = classPath;
	}

}