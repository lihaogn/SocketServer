package com.lihaogn;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

public class ServerThread implements Runnable{

	// ���嵱ǰ�߳��������Socket
	private Socket mSocket=null;
	// ���߳��������Socket����Ӧ�������� 
	BufferedReader br=null;
	
	
	
	// ֪ͨ��Ϣ
	private String noticeString;
	// ������Ϣ
	String contentString;
	// �ͻ���־������ʶ�����ĸ��ͻ�
	private static final String USER_TAG="USER:";
	// ��Ž�ȡ�Ŀͻ���־
	String userNameString[];
	
	public ServerThread(Socket socket) throws IOException{
		// TODO Auto-generated constructor stub
		
		this.mSocket=socket;
		// ��ʼ����Socket��Ӧ��������
		br=new BufferedReader(new InputStreamReader(socket.getInputStream(),"utf-8"));
		
		// ����̨��� ֪ͨ��Ϣ
		noticeString="�û���"+socket.getInetAddress()+"������������\n"+"��ǰ������"+MyServer.socketList.size();
		System.out.println(noticeString);
		
	}
//	private boolean judgeContent(String contentString){
//		try {
//			// �жϷ���������
//			if (contentString.equals("exit this chat")) {// �˳���Ϣ
//				// ������ݣ���ֹ�´�ʶ��Ϊexit
//				contentString="";
//				// ���б���ɾȥ��Ϊ���жϻ��ж���������
//				MyServer.socketList.remove(mSocket);
//				// �ر�������
//				br.close();
//				// ���������Ϣ
//				noticeString="�û���"+mSocket.getInetAddress()+"������\n"+"��ǰ������"+MyServer.socketList.size();
//				System.out.println(noticeString);
//				sendMessage(false,null);
////				// �ر������----����Ҫ�رգ�ֻҪ���������رվͺ���
////				mPrintWriter.close();
//				// �ر����client��socket
//				mSocket.close();
//				return true;
//			}else if (contentString.startsWith(USER_TAG)) {// ������Ϣ
//				userNameString=contentString.split(":");
//				// ����һ�����ӳɹ���Ϣ
//				sendMessage(true,"ok");
//			}else {
//				// 
//				sendMessage(true,null);
//			}
//		} catch (Exception e) {
//			// TODO: handle exception
//			e.printStackTrace();
//		}
//		return false;
//		
//	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
			// ����ѭ�����ϴ�Socket�ж�ȡ�ͻ��˷��͹��������� 
			while ((contentString=readFromClient())!=null) {
				// �жϷ���������
//				boolean ju=judgeContent(contentString);
//				if (ju) {
//					break;
//				}
				try {
					// �жϷ���������
					if (contentString.equals("exit this chat")) {// �˳���Ϣ
						// ������ݣ���ֹ�´�ʶ��Ϊexit
						contentString="";
						// ���б���ɾȥ��Ϊ���жϻ��ж���������
						MyServer.socketList.remove(mSocket);
						// �ر�������
						br.close();
						// ���������Ϣ
						noticeString="�û���"+mSocket.getInetAddress()+"������\n"+"��ǰ������"+MyServer.socketList.size();
						System.out.println(noticeString);
						sendMessage(false,null);
//						// �ر������----����Ҫ�رգ�ֻҪ���������رվͺ���
//						mPrintWriter.close();
						// �ر����client��socket
						mSocket.close();
						break;
					}else if (contentString.startsWith(USER_TAG)) {// ������Ϣ
						userNameString=contentString.split(":");
						// ����һ�����ӳɹ���Ϣ
						sendMessage(true,"ok");
					}else {
						// 
						sendMessage(true,null);
					}
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
			}
	}
	
	// �����ȡ�ͻ������ݵķ���
	private String readFromClient() {
		// TODO Auto-generated method stub
		try {
			// ���տͻ��˴���������
			return br.readLine();
		} catch (Exception e) {
			// TODO: handle exception
			// �����׽���쳣��������Socket��Ӧ�Ŀͻ����Ѿ��ر� 
			// ɾ����Socket 
			MyServer.socketList.remove(mSocket);
			e.printStackTrace();
			
		}
		return null;
	}

	// ��client������Ϣ
	// Ŀǰ���ܣ������е�client������Ϣ
	private void sendMessage(boolean sendOrExit,String string){
		
		try {
			// trueΪ������Ϣ��falseΪ����������Ϣ
			if (sendOrExit) {
				// ture:��������
//				contentString=mSocket.getInetAddress()+"˵��"+contentString;
				if (string!=null) {// �����Ϊ�գ��������ӳɹ���Ϣ
					string=null;
					contentString="ok";
				}else {
					contentString=userNameString[1]+"˵��"+contentString;
				}
				
			}else {
				// false:����������Ϣ
//				contentString=mSocket.getInetAddress()+" ������.....";
				contentString=userNameString[1]+" ������.....";
			}
			
			// ����socketList�е�ÿ��Socket����������������ÿ��Socket(client)����һ�� 
			for (Socket s : MyServer.socketList) {
				// �������ݸ�client
//				OutputStream os=s.getOutputStream();
//				os.write((contentString+"\n").getBytes("utf-8"));
				// ��ʼ�������
				PrintWriter mPrintWriter=new PrintWriter(new OutputStreamWriter(s.getOutputStream(), "utf-8"));
				mPrintWriter.println(contentString);
				mPrintWriter.flush();
				
			}
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
	}
	
}
