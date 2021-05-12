package dbconnect;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;


public class ChatDAO {
	
	
	private Connection con ;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	
	public ChatDAO() throws ClassNotFoundException , SQLException {
		con = new ChatConn().getConnection();
	}
		
	public boolean insert_namechat(ArrayList<ChatVO> chatnamelog) {
		String sql = "insert into chatlog values(?,?)";
		for(int i=0; i<chatnamelog.size();i++) {
		
		try {
			pstmt=con.prepareStatement(sql);
			pstmt.setString(1, chatnamelog.get(i).getName());
			pstmt.setString(2, chatnamelog.get(i).getChat());
			pstmt.executeUpdate();
		}catch(SQLException e) {
			System.out.println("insert Exception");
			return false;
		}
		}//forend
		return true;
	}//insert-end
	
	
	public ArrayList<ChatVO> chatAllInfo(String id) throws SQLException {
		ArrayList<ChatVO> carray = new ArrayList<ChatVO>();
		String sql = "SELECT * FROM chatlog where name='id'";//넘겨줄 인자id
		
		pstmt = con.prepareStatement(sql);
		rs = pstmt.executeQuery();
		
		while (rs.next()) {
			String name = rs.getString("name");
			String chat = rs.getString("chat");
			ChatVO cv = new ChatVO (name , chat);
			carray.add(cv);
		}
		return carray;//DB에서 arrayList불러오는 기능
	}//chatAllInfo-end
	
	public void SaveChat(String name,String chat){
		
		ArrayList<ChatVO> chatnamelog = new ArrayList <ChatVO>();
		ChatVO vo1=new ChatVO();
		vo1.setName(name);
		vo1.setChat(chat);
		chatnamelog.add(vo1);//받은 이름과 채팅을 arrayLsit만든후에
		insert_namechat(chatnamelog);// DB에넣음
	}
	
	
	
	
	
}
