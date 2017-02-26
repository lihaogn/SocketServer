package com.lihaogn;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class MyServer {
	
	// ���屣�����е�socket����
	public static ArrayList<Socket> socketList=new ArrayList<Socket>();
	
	public static void main(String[] args) throws IOException{
		// TODO Auto-generated method stub
		
		//��ȡ���أ�����������ip��ַ��������
		InetAddress mAddress=InetAddress.getLocalHost();
		String ip=mAddress.getHostAddress();
		
		// �����˿�
		ServerSocket mSerSocket=new ServerSocket(52121);
		// ������أ�����������ip��ַ��������
		System.out.println(ip);
		System.out.println("service created success!");
		System.out.println("waiting for connect with client......");
		// �������ӳ�ʱʱ��
		//mSerSocket.setSoTimeout(20000);
		
		
		while (true) {
			// ����ʽ�ȴ�
			Socket mSocket=mSerSocket.accept();
			System.out.println("there is a client coming!");
			// �����msocket�����б���
			socketList.add(mSocket);
			// ÿ���ͻ������Ӻ�����һ��ServerThread�߳�Ϊ�ÿͻ��˷���
			new Thread(new ServerThread(mSocket)).start();
		}
		
	}

}
