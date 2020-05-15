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

	public Member checkLogin(Member member) throws SQLException {
		PreparedStatement pstmt = openConnection("CheckLogin"); 
		pstmt.setString(1, member.getId());
		pstmt.setString(2, member.getPassword());
		ResultSet rs = pstmt.executeQuery();
		Member loginMember = null;
		while(rs.next()) {
			loginMember = new Member(null, null);
			loginMember.setId(rs.getString("id"));
			loginMember.setPassword(rs.getString("password"));
			loginMember.setName(rs.getString("name"));
			loginMember.setEmail(rs.getString("email"));
		}
		return loginMember;
	}

}
