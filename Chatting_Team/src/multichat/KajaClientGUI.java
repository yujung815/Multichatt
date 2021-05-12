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
	//GUI			//thread위해	//event
	//console모드에서 넘어오는 3개 인자를 받아 저장할 준비필드
	DataOutputStream outputStream;
	DataInputStream inputStream;
	String nickname;

	//그래픽디자인 컴포넌트를 준비
	JLabel jlabel1 =new JLabel("우리반 채팅~~");
	JTextArea jtarea1 =new JTextArea();
	JTextField jtfield1 =new JTextField();
	JScrollPane jScrollPane = new JScrollPane(jtarea1);


	//클라이언트의 new KajaClient에서 찾아오는 곳
	public KajaClientGUI(DataOutputStream outputStream, DataInputStream inputStream, String nickname)
			throws HeadlessException {
		super();
		this.outputStream = outputStream;
		this.inputStream = inputStream;
		this.nickname = nickname;
		setLayout(new BorderLayout());

		jlabel1.setFont(new Font("굴림",Font.BOLD,22));
		add("North",jlabel1);//우리반채팅

		//chat 문자열이 출력되는 곳
		jtarea1.setBackground(Color.YELLOW);//보색대비
		jtarea1.setForeground(Color.blue);//노랑배경색 위에 파랑색 글씨
		jtarea1.setFont(new Font("굴림",Font.BOLD, 22));

		jtarea1.setEditable(false);//편집 x
		add("Center",jScrollPane);

		jtfield1.setBackground(Color.white);
		jtfield1.setForeground(Color.BLACK);
		jtfield1.setFont(new Font("굴림",Font.BOLD, 25));
		add("South",jtfield1);
		jtfield1.addActionListener(this);//이벤트 등록
		setSize(800,800);//채팅창 크기
		setVisible(true);//항상보여줘

		addWindowListener(new WindowAdapter() {//swing x, awt x
			public void windowClosing(WindowEvent e) {
				dispose();
				System.exit(0);
				setVisible(false);
			}
		});
		//서버로부터 받아 textarea에 뿌려주는 쓰레드
		Thread  th1 =new Thread(this);
		th1.start();
		//--------------------
	}//생성자 -end
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==jtfield1) {
			try {
				outputStream.writeUTF(nickname+"--> "+jtfield1.getText());
				//nickname과 client의 chat을 서버로
				ChatDAO dao =new ChatDAO() ;
				dao.SaveChat(nickname,nickname+"--> "+jtfield1.getText());
			}catch(IOException ee) {
				//ee.printStackTrace();
			}catch(Exception ex) {
				//ex.printStackTrace();
			}
			
			jtfield1.setText("");//서버로 보내고 해당 칸을 클리어
		}
	}//actionPerformed -end

	Toolkit tk1 = Toolkit.getDefaultToolkit();
	//chat올때마다 beep음 울림

	public void run() {//for 받는 thread
		try {
			while(true) {
				String strServer1 = inputStream.readUTF();//서버로부터
				if(strServer1==null) {
					jtarea1.append("\n"+"종료");
					return;
				}
				jtarea1.append("\n"+strServer1);//서버에서 온 것 textarea에 추가
				//---------이것해야 스크롤바가 생길 후 맨 마지막 내용이 잘보임
				int kkeut = jtarea1.getText().length();
				jtarea1.setCaretPosition(kkeut);//커서를 맨뒤로
				//jtarea1.setCaretPosition(0);커서를 맨처음에
				tk1.beep();//chat이 올때마다 beep음 

			}
		}catch(Exception eee) {
			jtarea1.append("\n"+eee);
		}

	}//run-end
}//class-end
