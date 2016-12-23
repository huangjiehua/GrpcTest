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
package com.buaa.lottery.vm;

/**
 * @author Administrator
 *
 */
public class Cloader extends ClassLoader {
	static int maxsize = 10000;

	public Class load(byte[] bt, String classname) throws java.lang.Exception {
		try {
			// 进行判断这个class是否已经调入,已经有就直接返回,不然就调入
			Class ctmp = this.findLoadedClass(classname);
			System.out.println(ctmp.getName() + " is load");
			return ctmp;
		} catch (Exception e) {
			// System.out.println(e);
		}
		return defineClass(classname, bt, 0, bt.length);
	}
	@Override
	public Class<?> findClass(String name) throws ClassNotFoundException {
	  return Thread.currentThread().getContextClassLoader().loadClass(name);
	}
}