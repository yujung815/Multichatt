package multichat;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

class ServerClass{

	ArrayList <ThreadServerClass> threadList//���׸�
	=new ArrayList <ThreadServerClass>();

	Socket socket; //Ŭ���̾�Ʈ �޾� ������ ��
	DataOutputStream outputStream;//��½�Ʈ��

	//�����ڸ� ������ �ؾ� port ��ȣ���� �ʱ�ġ ó��
	public ServerClass(int portno) throws IOException{//������
		Socket s1 =null;
		ServerSocket ss1 =new ServerSocket(portno);
		System.out.println("��������......");//listen
		while(true) {
			s1=ss1.accept();
			System.out.println("�����ּ�:"+s1.getInetAddress()
			+",������Ʈ "+s1.getPort());
			ThreadServerClass tServer1 = new ThreadServerClass(s1);//s1�ʱ�ġ
			tServer1.start();//run�� ȣ��
			threadList.add(tServer1);//�÷��ǿ� add
			//arraylist�� ����
			System.out.println("������ ��: "+threadList.size()+"��");

		}//while-true-end
	}//������ end

	//�Ʒ��� ServerClass �ȿ�����.//class in class
	class ThreadServerClass extends Thread{
		Socket socket1;
		DataInputStream inputStream;
		DataOutputStream outputStream;

		public ThreadServerClass(Socket s1) throws IOException {//������
			socket1=s1;
			inputStream=new DataInputStream(s1.getInputStream());
			outputStream= new DataOutputStream(s1.getOutputStream());
			//����½�Ʈ��
		}//������-end

		public void run() {//java ��Ű����... 9887 ��������
			String nickname="";
			try {//�̺κ��� ����ǰ� �ִٴ� ���� ���������� ä�� ��
				if(inputStream!=null) {
					nickname=inputStream.readUTF();
					sendChat(nickname+"�� ����~~(^ ^)");
					//�޼ҵ� 2���� ȣ�⹮//Ŭ���̾�Ʈ�� ó�� ������ ���� ����
				}
				while(inputStream!=null) {
					sendChat(inputStream.readUTF());
					// Ŭ���̾�Ʈ�� ���� ä�� ������ ������ ��ο��� ����
				}
			}catch(IOException e) {
				//e.printStackTrace();
			}finally {//��¶�� ���� ä�����κ��� ���Դ� ���
				//���� �������� �ε��� ã��
				for(int i=0;i<threadList.size();i++) {
					//socket �񱳷� ã��
					if(socket1.equals(threadList.get(i).socket1)) {
						threadList.remove(i);//����� remove
						try {
							sendChat(nickname+"�� ����~(�Ф� )");
							//������ ��� Ŭ���̾�Ʈ���� ���������� �����̶�� �˸�
						}catch(IOException e) {
							//e.printStackTrace();
						}
					}
				}//for -end
				System.out.println("������ ��: "+ threadList.size()+"��");
			}//finally-end
		}//run-end

		public void sendChat(String chat) throws IOException{
			for(int i=0;i<threadList.size();i++)
				threadList.get(i).outputStream.writeUTF(chat);
			//������ ȸ���� ã�ư��� ���� or ä�ó����� ����

		}
	}//ThreadServerClass
}//ServerClass-end



public class TcpMulServer {
	public static void main(String[] args) throws IOException{
		if(args.length!=1) {
			System.out.println("����: ���������� "
					+ "\'java ��Ű����.���ϸ� ��Ʈ����\' �������� �Է�");
		}
		new ServerClass(Integer.parseInt(args[0]));//port no.
		//��ü ���� -�ʱ�ġ ������
	}


}
