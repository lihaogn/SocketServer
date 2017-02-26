package com.lihaogn;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class MyServer {
	
	// 定义保存所有的socket集合
	public static ArrayList<Socket> socketList=new ArrayList<Socket>();
	
	public static void main(String[] args) throws IOException{
		// TODO Auto-generated method stub
		
		//获取本地（服务器）的ip地址和主机名
		InetAddress mAddress=InetAddress.getLocalHost();
		String ip=mAddress.getHostAddress();
		
		// 监听端口
		ServerSocket mSerSocket=new ServerSocket(52121);
		// 输出本地（服务器）的ip地址和主机名
		System.out.println(ip);
		System.out.println("service created success!");
		System.out.println("waiting for connect with client......");
		// 设置连接超时时间
		//mSerSocket.setSoTimeout(20000);
		
		
		while (true) {
			// 阻塞式等待
			Socket mSocket=mSerSocket.accept();
			System.out.println("there is a client coming!");
			// 将这个msocket加入列表中
			socketList.add(mSocket);
			// 每当客户端连接后启动一条ServerThread线程为该客户端服务
			new Thread(new ServerThread(mSocket)).start();
		}
		
	}

}
