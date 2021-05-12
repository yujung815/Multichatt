package multichat;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

class ServerClass{

	ArrayList <ThreadServerClass> threadList//제네릭
	=new ArrayList <ThreadServerClass>();

	Socket socket; //클라이언트 받아 저장할 곳
	DataOutputStream outputStream;//출력스트림

	//생성자를 만나게 해야 port 번호같은 초기치 처리
	public ServerClass(int portno) throws IOException{//생성자
		Socket s1 =null;
		ServerSocket ss1 =new ServerSocket(portno);
		System.out.println("서버가동......");//listen
		while(true) {
			s1=ss1.accept();
			System.out.println("접속주소:"+s1.getInetAddress()
			+",접속포트 "+s1.getPort());
			ThreadServerClass tServer1 = new ThreadServerClass(s1);//s1초기치
			tServer1.start();//run을 호출
			threadList.add(tServer1);//컬렉션에 add
			//arraylist의 길이
			System.out.println("접속자 수: "+threadList.size()+"명");

		}//while-true-end
	}//생성자 end

	//아래는 ServerClass 안에있음.//class in class
	class ThreadServerClass extends Thread{
		Socket socket1;
		DataInputStream inputStream;
		DataOutputStream outputStream;

		public ThreadServerClass(Socket s1) throws IOException {//생성자
			socket1=s1;
			inputStream=new DataInputStream(s1.getInputStream());
			outputStream= new DataOutputStream(s1.getOutputStream());
			//입출력스트림
		}//생성자-end

		public void run() {//java 패키지명... 9887 은빛공주
			String nickname="";
			try {//이부분이 진행되고 있다는 것은 안정적으로 채팅 중
				if(inputStream!=null) {
					nickname=inputStream.readUTF();
					sendChat(nickname+"님 입장~~(^ ^)");
					//메소드 2형식 호출문//클라이언트가 처음 보내는 것이 별명
				}
				while(inputStream!=null) {
					sendChat(inputStream.readUTF());
					// 클라이언트가 보낸 채팅 내용을 접속한 모두에게 보냄
				}
			}catch(IOException e) {
				//e.printStackTrace();
			}finally {//어쨋든 간에 채팅으로부터 나왔단 얘기
				//나간 쓰레드의 인덱스 찾기
				for(int i=0;i<threadList.size();i++) {
					//socket 비교로 찾음
					if(socket1.equals(threadList.get(i).socket1)) {
						threadList.remove(i);//퇴장시 remove
						try {
							sendChat(nickname+"님 퇴장~(ㅠㅠ )");
							//서버는 모든 클라이언트에게 누구누구님 퇴장이라고 알림
						}catch(IOException e) {
							//e.printStackTrace();
						}
					}
				}//for -end
				System.out.println("접속자 수: "+ threadList.size()+"명");
			}//finally-end
		}//run-end

		public void sendChat(String chat) throws IOException{
			for(int i=0;i<threadList.size();i++)
				threadList.get(i).outputStream.writeUTF(chat);
			//각각의 회원을 찾아가서 별명 or 채팅내용을 보냄

		}
	}//ThreadServerClass
}//ServerClass-end



public class TcpMulServer {
	public static void main(String[] args) throws IOException{
		if(args.length!=1) {
			System.out.println("사용법: 서버실행은 "
					+ "\'java 패키지명.파일명 포트번포\' 형식으로 입력");
		}
		new ServerClass(Integer.parseInt(args[0]));//port no.
		//객체 생성 -초기치 생성자
	}


}
