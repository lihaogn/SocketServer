package com.lihaogn;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

public class ServerThread implements Runnable{

	// 定义当前线程所处理的Socket
	private Socket mSocket=null;
	// 该线程所处理的Socket所对应的输入流 
	BufferedReader br=null;
	
	
	
	// 通知消息
	private String noticeString;
	// 聊天信息
	String contentString;
	// 客户标志，用来识别是哪个客户
	private static final String USER_TAG="USER:";
	// 存放截取的客户标志
	String userNameString[];
	
	public ServerThread(Socket socket) throws IOException{
		// TODO Auto-generated constructor stub
		
		this.mSocket=socket;
		// 初始化该Socket对应的输入流
		br=new BufferedReader(new InputStreamReader(socket.getInputStream(),"utf-8"));
		
		// 控制台输出 通知消息
		noticeString="用户："+socket.getInetAddress()+"加入了聊天室\n"+"当前人数："+MyServer.socketList.size();
		System.out.println(noticeString);
		
	}
//	private boolean judgeContent(String contentString){
//		try {
//			// 判断发来的内容
//			if (contentString.equals("exit this chat")) {// 退出消息
//				// 清空内容，防止下次识别为exit
//				contentString="";
//				// 从列表中删去，为了判断还有多少人在线
//				MyServer.socketList.remove(mSocket);
//				// 关闭输入流
//				br.close();
//				// 输出下线消息
//				noticeString="用户："+mSocket.getInetAddress()+"下线了\n"+"当前人数："+MyServer.socketList.size();
//				System.out.println(noticeString);
//				sendMessage(false,null);
////				// 关闭输出流----不需要关闭，只要把输入流关闭就好了
////				mPrintWriter.close();
//				// 关闭这个client的socket
//				mSocket.close();
//				return true;
//			}else if (contentString.startsWith(USER_TAG)) {// 连接消息
//				userNameString=contentString.split(":");
//				// 发送一个连接成功消息
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
			// 采用循环不断从Socket中读取客户端发送过来的数据 
			while ((contentString=readFromClient())!=null) {
				// 判断发来的内容
//				boolean ju=judgeContent(contentString);
//				if (ju) {
//					break;
//				}
				try {
					// 判断发来的内容
					if (contentString.equals("exit this chat")) {// 退出消息
						// 清空内容，防止下次识别为exit
						contentString="";
						// 从列表中删去，为了判断还有多少人在线
						MyServer.socketList.remove(mSocket);
						// 关闭输入流
						br.close();
						// 输出下线消息
						noticeString="用户："+mSocket.getInetAddress()+"下线了\n"+"当前人数："+MyServer.socketList.size();
						System.out.println(noticeString);
						sendMessage(false,null);
//						// 关闭输出流----不需要关闭，只要把输入流关闭就好了
//						mPrintWriter.close();
						// 关闭这个client的socket
						mSocket.close();
						break;
					}else if (contentString.startsWith(USER_TAG)) {// 连接消息
						userNameString=contentString.split(":");
						// 发送一个连接成功消息
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
	
	// 定义读取客户端数据的方法
	private String readFromClient() {
		// TODO Auto-generated method stub
		try {
			// 接收客户端传来的数据
			return br.readLine();
		} catch (Exception e) {
			// TODO: handle exception
			// 如果捕捉到异常，表明该Socket对应的客户端已经关闭 
			// 删除该Socket 
			MyServer.socketList.remove(mSocket);
			e.printStackTrace();
			
		}
		return null;
	}

	// 给client发送消息
	// 目前功能：给所有的client发送消息
	private void sendMessage(boolean sendOrExit,String string){
		
		try {
			// true为发送消息，false为发送下线消息
			if (sendOrExit) {
				// ture:发送内容
//				contentString=mSocket.getInetAddress()+"说："+contentString;
				if (string!=null) {// 如果不为空，则发送连接成功消息
					string=null;
					contentString="ok";
				}else {
					contentString=userNameString[1]+"说："+contentString;
				}
				
			}else {
				// false:发送下线消息
//				contentString=mSocket.getInetAddress()+" 下线了.....";
				contentString=userNameString[1]+" 下线了.....";
			}
			
			// 遍历socketList中的每个Socket，将读到的内容向每个Socket(client)发送一次 
			for (Socket s : MyServer.socketList) {
				// 发送数据给client
//				OutputStream os=s.getOutputStream();
//				os.write((contentString+"\n").getBytes("utf-8"));
				// 初始化输出流
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
