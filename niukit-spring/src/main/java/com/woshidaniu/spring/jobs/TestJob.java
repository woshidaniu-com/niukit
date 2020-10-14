package com.woshidaniu.spring.jobs;

public class TestJob implements Runnable {

	private static int count = 0;
	
	@Override
	public void run() {
		System.out.println("定时任务打印："+(count++));
	}

}
