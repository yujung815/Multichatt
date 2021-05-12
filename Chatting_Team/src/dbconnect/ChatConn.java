package dbconnect;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ChatConn {
	private Connection con;
	public Connection getConnection() {
		return con;
	}
	public ChatConn() //공통을 만들어 놓고 다른데서 부를때는 getConnection()으로 부르겠다.
			throws ClassNotFoundException,SQLException{
		Class.forName("oracle.jdbc.driver.OracleDriver");
con=DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe","hr","hr");
	}

}