package com.chthhk.zk.zkclient.testzkclient;

import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.serialize.SerializableSerializer;

public class CreateSession {

	public static void main(String[] args) {
		ZkClient zc = new ZkClient("127.0.0.1:2181",10000,10000,new SerializableSerializer());
		System.out.println("conneted ok!");
	}
	
}
