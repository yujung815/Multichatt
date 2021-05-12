package multichat;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.sql.SQLException;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.*;
import dbconnect.ChatDAO;
public class KajaClientGUI extends JFrame implements Runnable, ActionListener {
	//GUI			//thread����	//event
	//console��忡�� �Ѿ���� 3�� ���ڸ� �޾� ������ �غ��ʵ�
	DataOutputStream outputStream;
	DataInputStream inputStream;
	String nickname;

	//�׷��ȵ����� ������Ʈ�� �غ�
	JLabel jlabel1 =new JLabel("�츮�� ä��~~");
	JTextArea jtarea1 =new JTextArea();
	JTextField jtfield1 =new JTextField();
	JScrollPane jScrollPane = new JScrollPane(jtarea1);


	//Ŭ���̾�Ʈ�� new KajaClient���� ã�ƿ��� ��
	public KajaClientGUI(DataOutputStream outputStream, DataInputStream inputStream, String nickname)
			throws HeadlessException {
		super();
		this.outputStream = outputStream;
		this.inputStream = inputStream;
		this.nickname = nickname;
		setLayout(new BorderLayout());

		jlabel1.setFont(new Font("����",Font.BOLD,22));
		add("North",jlabel1);//�츮��ä��

		//chat ���ڿ��� ��µǴ� ��
		jtarea1.setBackground(Color.YELLOW);//�������
		jtarea1.setForeground(Color.blue);//������� ���� �Ķ��� �۾�
		jtarea1.setFont(new Font("����",Font.BOLD, 22));

		jtarea1.setEditable(false);//���� x
		add("Center",jScrollPane);

		jtfield1.setBackground(Color.white);
		jtfield1.setForeground(Color.BLACK);
		jtfield1.setFont(new Font("����",Font.BOLD, 25));
		add("South",jtfield1);
		jtfield1.addActionListener(this);//�̺�Ʈ ���
		setSize(800,800);//ä��â ũ��
		setVisible(true);//�׻󺸿���

		addWindowListener(new WindowAdapter() {//swing x, awt x
			public void windowClosing(WindowEvent e) {
				dispose();
				System.exit(0);
				setVisible(false);
			}
		});
		//�����κ��� �޾� textarea�� �ѷ��ִ� ������
		Thread  th1 =new Thread(this);
		th1.start();
		//--------------------
	}//������ -end
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==jtfield1) {
			try {
				outputStream.writeUTF(nickname+"--> "+jtfield1.getText());
				//nickname�� client�� chat�� ������
				ChatDAO dao =new ChatDAO() ;
				dao.SaveChat(nickname,nickname+"--> "+jtfield1.getText());
			}catch(IOException ee) {
				//ee.printStackTrace();
			}catch(Exception ex) {
				//ex.printStackTrace();
			}
			
			jtfield1.setText("");//������ ������ �ش� ĭ�� Ŭ����
		}
	}//actionPerformed -end

	Toolkit tk1 = Toolkit.getDefaultToolkit();
	//chat�ö����� beep�� �︲

	public void run() {//for �޴� thread
		try {
			while(true) {
				String strServer1 = inputStream.readUTF();//�����κ���
				if(strServer1==null) {
					jtarea1.append("\n"+"����");
					return;
				}
				jtarea1.append("\n"+strServer1);//�������� �� �� textarea�� �߰�
				//---------�̰��ؾ� ��ũ�ѹٰ� ���� �� �� ������ ������ �ߺ���
				int kkeut = jtarea1.getText().length();
				jtarea1.setCaretPosition(kkeut);//Ŀ���� �ǵڷ�
				//jtarea1.setCaretPosition(0);Ŀ���� ��ó����
				tk1.beep();//chat�� �ö����� beep�� 

			}
		}catch(Exception eee) {
			jtarea1.append("\n"+eee);
		}

	}//run-end
}//class-end
