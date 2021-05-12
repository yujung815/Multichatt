package multichat;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;

import javax.swing.*;
							
public class NowChatUI extends JFrame implements Runnable, ActionListener {
	DataOutputStream outputStream;
	DataInputStream inputStream;
	String name;
	
	JTextArea jtarea1=new JTextArea();
	JTextField jtfield1=new JTextField();
	JScrollPane jScrollPane=new JScrollPane(jtarea1);
	JButton jbt1=new JButton("이전 채팅 불러오기");
	
	//gui생성자
	public NowChatUI(DataOutputStream outputStream, DataInputStream inputStream, String name) {
		this.outputStream=outputStream;
		this.inputStream=inputStream;
		this.name=name;
		getContentPane().setLayout(new BorderLayout());
		jbt1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//이전 채팅 팝업으로 열기
				//예외:저장된 내용이 없습니다
			}
		});
		
		jbt1.setFont(new Font("굴림", Font.BOLD, 22));
		getContentPane().add("North",jbt1);
		
		jtarea1.setBackground(Color.YELLOW);
		jtarea1.setFont(new Font("굴림",Font.BOLD,22));
		
		jtarea1.setEditable(false);
		getContentPane().add("Center",jScrollPane);
		
		jtfield1.setBackground(Color.white);
		jtfield1.setForeground(Color.BLACK);
		jtfield1.setFont(new Font("굴림",Font.BOLD,25));
		getContentPane().add("South",jtfield1);
		jtfield1.addActionListener(this);
		
		setSize(800,800);
		setVisible(true);
		
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				dispose();
				System.exit(0);
				
				setVisible(false);
			}
		}); //window-end
	
	
		Thread th1=new Thread(this);
		th1.start();
	}//gui-생성자-end
	
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == jtfield1) {
			try {
				outputStream.writeUTF(name+" --> "+jtfield1.getText());
			}catch(IOException ee) {
				//ee.printStackTrace();
			}
			jtfield1.setText("");
		}
	}//action-end

	public void run() {
		try {
			while(true) {
				String strServer1 = inputStream.readUTF();
				if(strServer1 == null) {
					jtarea1.append("\n"+"종료");
					return;
				}
				jtarea1.append("\n"+strServer1);
				
				int kkeut=jtarea1.getText().length();
				jtarea1.setCaretPosition(kkeut);
			}
		}catch(Exception eee) {
			jtarea1.append("\n"+eee);
		}
	}//run-end
}//class-end