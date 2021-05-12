package multichat;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import javax.swing.*;
import dbconnect.ChatDAO;

public class PastChatUI extends JFrame implements Runnable {
	DataOutputStream outputStream;
	DataInputStream inputStream;
	String name;
	String chat;
	
	JLabel jlabel1=new JLabel("ÀÌÀü Ã¤ÆÃ ºÒ·¯¿À±â");
	JTextArea jtarea1=new JTextArea();
	JScrollPane jScrollPane=new JScrollPane(jtarea1);
	JButton jbt1=new JButton("´Ý±â");
	
	//gui »ý¼ºÀÚ
	public PastChatUI(DataOutputStream outputStream, DataInputStream inputStream, String name, String chat) {
		this.outputStream=outputStream;
		this.inputStream=inputStream;
		this.name=name;
		this.chat=chat;
		getContentPane().setLayout(new BorderLayout());
		
		jlabel1.setFont(new Font("±¼¸²", Font.BOLD, 22));
		getContentPane().add("North", jlabel1);
		
		jtarea1.setBackground(Color.pink);
		jtarea1.setFont(new Font("±¼¸²",Font.BOLD,22));
		
		jtarea1.setEditable(false);
		getContentPane().add("Center",jScrollPane);
		
		jbt1.addActionListener(new ActionListener() { //´Ý±â
			public void actionPerformed(ActionEvent e) {
				dispose();
				setVisible(false);
			}
		});
		
		jbt1.setFont(new Font("±¼¸²", Font.BOLD, 22));
		getContentPane().add("South",jbt1);
		
		setSize(700,700);
		setVisible(true);
	
		Thread th1=new Thread(this);
		th1.start();
	
	}//gui-»ý¼ºÀÚ-end
	
	public void run() {
		try {
			while(true) {
			ChatDAO dao =new ChatDAO();
			for(int i=0; i<dao.chatAllInfo(name).size();i++) {
				jtarea1.append(dao.chatAllInfo(name).get(i).getChat());
			}
			
				
			}
		}catch(Exception eee) {
			
		}
	}//run-end
	
	
}//class-end
