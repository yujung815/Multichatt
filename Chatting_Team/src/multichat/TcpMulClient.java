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
	}//send-������-end

	public void run() {
		Scanner in1 = new Scanner(System.in); //ä�ó����� Ű����� �Է�
		try {
			if(outputStream!=null)
				outputStream.writeUTF(nickname);//�г��� �������� send
			while(outputStream !=null) {
				//io��Ʈ���� ���� ���濡�� chat����
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
			try {//������ ���� (**�ʷϿ���**)���� �氡 �޾� ���
				System.out.println(inputStream.readUTF());
			}catch(IOException e) {
				//e.printStackTrace();
			}
		//�̺κп��� ä������ 
		}//while-end
	}//run-end
}//ThreadClientRcvClass-end








public class TcpMulClient {

	public static void main(String[] args) {
		if(args.length!=3) {
			System.out.println("����: Ŭ���̾�Ʈ ������  \'java"
					+ "��Ű����.���ϸ� ip�ּ� ��Ʈ��ȣ �г���\' �������� �Է�");
			System.exit(1);
		}
		try {					//ip �ּ�			//��Ʈ��ȣ
			Socket s1= new Socket(args[0], Integer.parseInt(args[1]));
			System.out.println("������ ����...");//connect


			//GUI�� �����ϴ� .java�� �ϳ� ����� �ű⼭ ó���ϵ��� �غ���
			//�׷��� ���� �ű⿡ �ʿ��� ������ ��� ���ڷ� ��������

			//i.o network stream: readUTF() writeUTF() ����� ���� �غ�
			DataOutputStream outputStream =new DataOutputStream(s1.getOutputStream());
			DataInputStream inputStream =new DataInputStream(s1.getInputStream());
			outputStream.writeUTF("##"+args[2]); //�г��� ���� ������ ���� ����

//KagaClientGUI.java�� GUI���� ��, main X
			//========================================================
			//client console -->client GUI
			new KajaClientGUI(outputStream,inputStream,args[2]) {//new Ŭ������(���ڵ�)
				//io��Ʈ�� �� �г����� ���ڷ� ����
				
				public void closeWork() throws IOException{ //ȣ��ÿ��� ����
					outputStream.close();
					inputStream.close();
					System.exit(0);
				}
			}; //new KajaClientGUI -end


			//===========console(dos���)�� ���======================
			/*ThreadClientSendClass  tcc1= new ThreadClientSendClass(s1,args[2]);	
		ThreadClientRcvClass trc1 =new ThreadClientRcvClass(s1);	//�г���

		Thread tsend1 =new Thread(tcc1);//������ chat����
		Thread trcv1 =new Thread(trc1);//�޴� chat����
		tsend1.start();
		trcv1.start();*/
			//=========================================================	
		}catch(Exception e){
			//e.printStackTrace(); //�ּ��޾ƾ� ������ ȭ���� �ε巴�� ����
		}
	}

}
