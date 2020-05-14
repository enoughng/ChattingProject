package yeong.chatting.server.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import yeong.chatting.model.Member;
import yeong.chatting.model.Message;

public class ServerDAO extends CommonDao{

	/**
	 *  ΩÃ±€≈Ê ∆–≈œ
	 */
	private static ServerDAO sDao;

	private ServerDAO() {}

	public static ServerDAO getInstance() {
		if(sDao == null) sDao = new ServerDAO();
		return sDao;
	}

	public void insertMember(Member member) throws SQLException {

		PreparedStatement pstmt = openConnection("InsertMember");
		pstmt.setString(1, member.getId());
		pstmt.setString(2, member.getPassword());
		pstmt.setString(3, member.getName());
		pstmt.setString(4, member.getEmail());
		pstmt.executeUpdate();

	}

	public boolean checkLogin(Member member) throws SQLException {
		PreparedStatement pstmt = openConnection("CheckLogin"); 
		pstmt.setString(1, member.getId());
		pstmt.setString(2, member.getPassword());
		boolean hasInfo = false;
		ResultSet rs = pstmt.executeQuery();
		while(rs.next()) {
			hasInfo = true;
		}
		return hasInfo;
	}

}
