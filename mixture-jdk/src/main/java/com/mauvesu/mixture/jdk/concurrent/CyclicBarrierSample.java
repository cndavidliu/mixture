package com.mauvesu.mixture.jdk.concurrent;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CyclicBarrierSample {
	
	private int size = 5;
	private CyclicBarrier barrier = new CyclicBarrier(size);
	private CountDownLatch latch = new CountDownLatch(size);
	
	private class CyclicTask implements Runnable {
		
		private int id;
		
		public CyclicTask(int id) {
			this.id = id;
		}
		
		@Override
		public void run() {
			System.out.println(String.format("Thread[%d] started!", this.id));
			try {
				Thread.sleep(100l * id);
				System.out.println(String.format("Thread[%d] wait for barrier", id));
				barrier.await();
				System.out.println(String.format("Thread[%d] end!", id));
				latch.countDown();
			} catch (InterruptedException | BrokenBarrierException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void run() {
		ExecutorService service = Executors.newFixedThreadPool(size);
		for (int i = 0; i < size; i ++) {
			service.execute(new CyclicTask(i));
		}
		try {
			latch.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		new CyclicBarrierSample().run();
	}
}
