package yeong.chatting.server.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.Vector;

import yeong.chatting.model.Member;
import yeong.chatting.model.Message;
import yeong.chatting.model.RoomInfo;
import yeong.chatting.server.thread.ServerThread;
import yeong.chatting.util.Log;

public class ServerDAO extends CommonDao{

	/**
	 *  �̱��� ����
	 */
	private static ServerDAO sDao;

	private ServerDAO() {}

	public static ServerDAO getInstance() {
		if(sDao == null) sDao = new ServerDAO();
		return sDao;
	}

	/**
	 * �Էµ� ���� ������ ȸ�������� �Ѵ�.
	 */
	public void insertMember(Member member) throws SQLException {

		PreparedStatement pstmt = openConnection("InsertMember");
		pstmt.setString(1, member.getId());
		pstmt.setString(2, member.getPassword());
		pstmt.setString(3, member.getName());
		pstmt.setString(4, member.getEmail());
		pstmt.executeUpdate();
	}

	/**
	 * ��û�� ��������� �̿��Ͽ� DB�� �����͸� �����´�.
	 */
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
			loginMember.setLogin(rs.getString("LOGIN_YN"));
		}
		return loginMember;
	}
	/**
	 *  �Է��� ������ ������ �������� DB�� �����Ѵ�.
	 */
	public RoomInfo insertRoom(RoomInfo room, Member from) throws SQLException {
		PreparedStatement pstmt;
		/** INSERT */
		if(room.isChk()) {
			pstmt  = openConnection("InsertRoomY");
		} else {
			pstmt  = openConnection("InsertRoomN");			
		}
		pstmt.setString(1, room.getRoom_title());
		pstmt.setString(2, room.getRoom_pwd()); 
		pstmt.setString(3, room.getRoom_host());
		pstmt.executeUpdate();
		/** SELECT */
		pstmt = openConnection("InsertRoomResult");
		pstmt.setString(1, room.getRoom_host());
		ResultSet rs = pstmt.executeQuery();
		RoomInfo rInfo = null;
		Vector<Member> roomMemberList  = new Vector<>();
		roomMemberList.add(from);
		while(rs.next()) {
			int index = rs.getInt("Rindex");
			String title = rs.getString(	"rTitle");
			String password = rs.getString("rPassword");
			String host = rs.getString("rHost");
			boolean chk = rs.getString("rSecret").equals("Y") ? true : false;
			ServerThread.roomMemberList.put(index, roomMemberList);
			rInfo = new RoomInfo(index,title,password,host);
			rInfo.setChk(chk);
			rInfo.setRoom_members(ServerThread.roomMemberList.size());
		}
		return rInfo; 
	}
	/**
	 * DB���� ��ü �� ����� �����´�.
	 * @return
	 * @throws SQLException
	 */
	public Vector<RoomInfo> getRooms() throws SQLException {
		Vector<RoomInfo> rooms = new Vector<>();
		PreparedStatement pstmt = openConnection("SelectRooms");
		ResultSet rs = pstmt.executeQuery();
		RoomInfo rInfo = null;


		while(rs.next()) {
			// DB�� ���� ���� �������� ������ ��´�.
			int index = rs.getInt("Rindex");
			String title = rs.getString("Rtitle");
			String password = rs.getString("Rpassword");
			String host = rs.getString("Rhost");

			//���� ������ Map�� �ִ� �ο����� DB�� index������ ã�´�.
			int roomMemberListSize =0;
			if(!(ServerThread.roomMemberList.get(index) == null))
				roomMemberListSize = ServerThread.roomMemberList.get(index).size();

			rInfo = new RoomInfo(index, title, password, host,roomMemberListSize);

			rooms.add(rInfo);
		}
		return rooms;
	}

	/**
	 * Ư�� ���� �˻��Ѵ�.
	 * 	 */
	public RoomInfo hasRoom(RoomInfo info) throws SQLException {

		RoomInfo checkedRoomInfo = null;
		PreparedStatement pstmt = openConnection("CheckRoom");
		pstmt.setInt(1, info.getRoom_num());
		ResultSet rs = pstmt.executeQuery();
		while(rs.next()) {
			int index = rs.getInt("Rindex");
			String title = rs.getString("Rtitle");
			String password = rs.getString("Rpassword");
			String host = rs.getString("Rhost");
			String rSecret = rs.getString("RSecret");
			RoomInfo DBroomInfo = new RoomInfo(index, title, password, host, (rSecret.equals("Y"))? true : false);
			checkedRoomInfo = DBroomInfo;
		}

		return checkedRoomInfo;
	}

	public void deleteRoom(RoomInfo info) throws SQLException {
		PreparedStatement pstmt = openConnection("DeleteRoom");
		pstmt.setInt(1, info.getRoom_num());
		pstmt.setString(2, info.getRoom_host());
		pstmt.executeUpdate();
	}

	public void deleteRooms() throws SQLException {
		PreparedStatement pstmt = openConnection("DeleteRooms");
		pstmt.executeUpdate();
		closeConnection();
	}

	/**
	 * String ���̸� DB�� �ش� ID���� ����
	 * null ���̸� DB�� �ش� ID���� ���� 
	 */
	public String checkID(String msg) throws SQLException {
		PreparedStatement pstmt = openConnection("CheckID");
		pstmt.setString(1, msg);
		ResultSet rs =pstmt.executeQuery();

		String id = null;
		while(rs.next()) {
			id = rs.getString("id");
		}
		return id;
	}

	public boolean updateLogin(Member member, boolean login_YN) throws SQLException {

		PreparedStatement pstmt = openConnection("UpdateLogin");
		pstmt.setString(1, login_YN ? "Y" : "N");
		pstmt.setString(2, member.getId());
		pstmt.setString(3, member.getPassword());

		int result = pstmt.executeUpdate();
		if(result==1) {
			return true;
		} else {
			return false;
		}
	}

	public void UpdateLogoutAll() throws SQLException {
		PreparedStatement pstmt = openConnection("UpdateLogoutAll");
		pstmt.executeUpdate();
	}

	//	public Map<Integer, Vector<Member>> initMap(Map<Integer, Vector<Member>> map) {
	//		
	//		PreparedStatement pstmt = openConnection("InitMap")
	//		
	//		return map;
	//	}

}
