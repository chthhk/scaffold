package com.chthhk.zk.curator.testcurator;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.BackgroundCallback;
import org.apache.curator.framework.api.CuratorEvent;
import org.apache.curator.framework.api.CuratorEventType;
import org.apache.curator.framework.api.GetDataBuilder;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.retry.RetryNTimes;
import org.apache.curator.retry.RetryUntilElapsed;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;

public class checkexists {

	public static void main(String[] args) throws Exception {
		
		ExecutorService es = Executors.newFixedThreadPool(5);
		
		//RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
		//RetryPolicy retryPolicy = new RetryNTimes(5, 1000);
		RetryPolicy retryPolicy = new RetryUntilElapsed(5000, 1000);
//		CuratorFramework client = CuratorFrameworkFactory
//				.newClient("192.168.1.105:2181",5000,5000, retryPolicy);
		
		CuratorFramework client = CuratorFrameworkFactory
				.builder()
				.connectString("127.0.0.1:2181")
				.sessionTimeoutMs(5000)
				.connectionTimeoutMs(5000)
				.retryPolicy(retryPolicy)
				.build();
		
		client.start();
		
		client.checkExists().inBackground(new BackgroundCallback() {
			
			public void processResult(CuratorFramework arg0, CuratorEvent arg1)
					throws Exception {
				Stat stat = arg1.getStat();
				System.out.println(stat);
				System.out.println(arg1.getContext());
				
		
				
				
			}
		},"123",es).forPath("/jike5");
		
		Thread.sleep(Integer.MAX_VALUE);
		
		
		
	}
	
}