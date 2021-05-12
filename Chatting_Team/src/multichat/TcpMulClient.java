package multichat;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

class ThreadClientSendClass implements Runnable{
	Socket socket;
	DataOutputStream outputStream;
	String nickname;

	public ThreadClientSendClass(Socket socket, String nickname) throws IOException{
		this.socket=socket;
		outputStream =new DataOutputStream(socket.getOutputStream());
		this.nickname=nickname;
	}//send-생성자-end

	public void run() {
		Scanner in1 = new Scanner(System.in); //채팅내용을 키보드로 입력
		try {
			if(outputStream!=null)
				outputStream.writeUTF(nickname);//닉네임 은빛공주 send
			while(outputStream !=null) {
				//io스트림을 통해 상대방에게 chat보냄
				outputStream.writeUTF("(**"+nickname+"**)"+in1.nextLine());
			}
		}catch(IOException e) {
			//e.printStackTrace();
		}
	}//run()-end
}//ThreadClientSendClass- end

class ThreadClientRcvClass implements Runnable{
	Socket socket;
	DataInputStream inputStream;

	public ThreadClientRcvClass(Socket socket) throws IOException{
		this.socket=socket;
		inputStream =new DataInputStream(socket.getInputStream());
	}


	public void run() {
		while (inputStream!=null) {
			try {//서버가 보낸 (**초록왕자**)나도 방가 받아 출력
				System.out.println(inputStream.readUTF());
			}catch(IOException e) {
				//e.printStackTrace();
			}
		//이부분에서 채팅저장 
		}//while-end
	}//run-end
}//ThreadClientRcvClass-end








public class TcpMulClient {

	public static void main(String[] args) {
		if(args.length!=3) {
			System.out.println("사용법: 클라이언트 실행은  \'java"
					+ "패키지명.파일명 ip주소 포트번호 닉네임\' 형식으로 입력");
			System.exit(1);
		}
		try {					//ip 주소			//포트번호
			Socket s1= new Socket(args[0], Integer.parseInt(args[1]));
			System.out.println("서버에 연결...");//connect


			//GUI를 전담하는 .java를 하나 만들어 거기서 처리하도록 준비함
			//그러기 위해 거기에 필요한 내용을 모두 인자로 보내야함

			//i.o network stream: readUTF() writeUTF() 사용을 위해 준비
			DataOutputStream outputStream =new DataOutputStream(s1.getOutputStream());
			DataInputStream inputStream =new DataInputStream(s1.getInputStream());
			outputStream.writeUTF("##"+args[2]); //닉네임 먼저 보내는 것은 같다

//KagaClientGUI.java는 GUI파인 단, main X
			//========================================================
			//client console -->client GUI
			new KajaClientGUI(outputStream,inputStream,args[2]) {//new 클래스명(인자들)
				//io스트림 및 닉네임을 인자로 전달
				
				public void closeWork() throws IOException{ //호출시에만 수행
					outputStream.close();
					inputStream.close();
					System.exit(0);
				}
			}; //new KajaClientGUI -end


			//===========console(dos모드)일 경우======================
			/*ThreadClientSendClass  tcc1= new ThreadClientSendClass(s1,args[2]);	
		ThreadClientRcvClass trc1 =new ThreadClientRcvClass(s1);	//닉네임

		Thread tsend1 =new Thread(tcc1);//보내는 chat위해
		Thread trcv1 =new Thread(trc1);//받는 chat위해
		tsend1.start();
		trcv1.start();*/
			//=========================================================	
		}catch(Exception e){
			//e.printStackTrace(); //주석달아야 에러시 화면이 부드럽게 진행
		}
	}

}
