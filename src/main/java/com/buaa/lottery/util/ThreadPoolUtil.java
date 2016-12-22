package com.buaa.lottery.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadPoolUtil {
	
	private static class InstanceHolder{
		private static ExecutorService cachedThreadPool = Executors.newCachedThreadPool(); 
	}
	
	public static ExecutorService getThreadPool(){
		return InstanceHolder.cachedThreadPool;
	}
}
