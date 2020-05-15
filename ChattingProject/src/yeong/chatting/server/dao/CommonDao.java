package yeong.chatting.server.dao;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import yeong.chatting.util.CommonPathAddress;
import yeong.chatting.util.Log;

public class CommonDao {

	private static final String DRIVER = "oracle.jdbc.driver.OracleDriver";
	private static final String URL = "jdbc:oracle:thin:@localhost:1521/XEPDB1";
	private static final String ID = "messenger";
	private static final String PW = "messenger";


	private Connection con = null;
	private PreparedStatement pstmt = null;
	
	private Properties SQLProperties;
	
	
	public PreparedStatement openConnection(String sql) {
		try {
			init();
			initProperties();
			pstmt = con.prepareStatement(SQLProperties.getProperty(sql));
		} catch (ClassNotFoundException e) {
			Log.e(getClass(), e);			
		} catch (SQLException e) {
			Log.e(getClass(), e);
		}
		return pstmt;
	}

	public void closeConnection() {
		try { if(!pstmt.isClosed()) { pstmt.close(); }  } catch(SQLException e) {};
		try { if(!con.isClosed()) { con.close(); }  } catch(SQLException e) {};
	}


	private void init() throws ClassNotFoundException, SQLException{
		Class.forName(DRIVER);
		if(con == null) {
			con = DriverManager.getConnection(URL,ID,PW);
		}
	}

	private void initProperties() {
		try {
			SQLProperties = new Properties();
			if(SQLProperties.isEmpty()) {
				SQLProperties.load(new FileReader(new File(CommonPathAddress.SQLProperties)));
			}
		} catch (FileNotFoundException e) {
			Log.e(getClass(), e);
		} catch (IOException e) {
			Log.e(getClass(), e);
		}
	}
	
}
