package yeong.chatting.server.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.Vector;

import yeong.chatting.model.ChattingProfile;
import yeong.chatting.model.Member;
import yeong.chatting.model.Message;
import yeong.chatting.model.RoomInfo;
import yeong.chatting.model.SearchValue;
import yeong.chatting.server.thread.ServerThread;
import yeong.chatting.util.Log;

public class ServerDAO extends CommonDao{

	/**
	 *  싱글톤 패턴
	 */
	private static ServerDAO sDao;

	private ServerDAO() {}

	public static ServerDAO getInstance() {
		if(sDao == null) sDao = new ServerDAO();
		return sDao;
	}

	/**
	 * 입력된 정보 가지고 회원가입을 한다.
	 */
	public void insertMember(Member member) throws SQLException {

		PreparedStatement pstmt = openConnection("InsertMember");
		pstmt.setString(1, member.getId());
		pstmt.setString(2, member.getPassword());
		pstmt.setString(3, member.getName());
		pstmt.setString(4, member.getEmail());
		pstmt.executeUpdate();
		pstmt.close();
	}

	/**
	 * 요청된 멤버정보를 이용하여 DB의 데이터를 가져온다.
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
			loginMember.setIsLogin(rs.getString("LOGIN_YN"));
		}
		pstmt.close();

		return loginMember;
	}
	/**
	 *  입력한 정보를 가지고 방정보를 DB에 삽입한다.
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
		pstmt.close();

		return rInfo; 
	}
	/**
	 * DB에서 전체 방 목록을 가져온다.
	 * @return
	 * @throws SQLException
	 */
	public Vector<RoomInfo> getRooms() throws SQLException {
		Vector<RoomInfo> rooms = new Vector<>();
		PreparedStatement pstmt = openConnection("SelectRooms");
		ResultSet rs = pstmt.executeQuery();
		RoomInfo rInfo = null;


		while(rs.next()) {
			// DB로 부터 현재 방정보를 변수로 담는다.
			int index = rs.getInt("Rindex");
			String title = rs.getString("Rtitle");
			String password = rs.getString("Rpassword");
			String host = rs.getString("Rhost");

			//현재 방정보 Map에 있는 인원수를 DB의 index값으로 찾는다.
			int roomMemberListSize =0;
			if(!(ServerThread.roomMemberList.get(index) == null))
				roomMemberListSize = ServerThread.roomMemberList.get(index).size();

			rInfo = new RoomInfo(index, title, password, host,roomMemberListSize);

			rooms.add(rInfo);
		}
		pstmt.close();
		return rooms;
	}

	/**
	 * 특정 방을 검색한다.
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
		pstmt.close();
		return checkedRoomInfo;
	}

	public void deleteRoom(RoomInfo info) throws SQLException {
		PreparedStatement pstmt = openConnection("DeleteRoom");
		pstmt.setInt(1, info.getRoom_num());
		pstmt.setString(2, info.getRoom_host());
		pstmt.executeUpdate();
		pstmt.close();
	}

	public void deleteRooms() throws SQLException {
		PreparedStatement pstmt = openConnection("DeleteRooms");
		pstmt.executeUpdate();
		closeConnection();
	}

	/**
	 * String 값이면 DB에 해당 ID값이 있음
	 * null 값이면 DB에 해당 ID값이 없음 
	 */
	public String checkID(String msg) throws SQLException {
		PreparedStatement pstmt = openConnection("CheckID");
		pstmt.setString(1, msg);
		ResultSet rs =pstmt.executeQuery();

		String id = null;
		while(rs.next()) {
			id = rs.getString("id");
		}
		pstmt.close();
		return id;
	}

	public boolean updateLogin(Member member, boolean login_YN) throws SQLException {

		PreparedStatement pstmt = openConnection("UpdateLogin");
		pstmt.setString(1, login_YN ? "Y" : "N");
		pstmt.setString(2, member.getId());
		pstmt.setString(3, member.getPassword());

		int result = pstmt.executeUpdate();
		if(result==1) {
			pstmt.close();
			return true;
		} else {
			pstmt.close();
			return false;
		}
	}

	public void UpdateLogoutAll() throws SQLException {
		PreparedStatement pstmt = openConnection("UpdateLogoutAll");
		pstmt.executeUpdate();
		pstmt.close();
	}

	//	public Map<Integer, Vector<Member>> initMap(Map<Integer, Vector<Member>> map) {
	//		
	//		PreparedStatement pstmt = openConnection("InitMap")
	//		
	//		return map;
	//	}

	public SearchValue selectSearchID(SearchValue sv) throws SQLException {
		PreparedStatement pstmt = openConnection("SearchID");
		pstmt.setString(1, sv.getName());
		pstmt.setString(2, sv.getEmail());

		SearchValue newValue = new SearchValue(sv);

		ResultSet rs = pstmt.executeQuery();

		while(rs.next()) {
			newValue.setId(rs.getString("id"));
		}
		pstmt.close();
		return newValue;
	}

	public SearchValue selectSearchPW(SearchValue sv) throws SQLException {
		PreparedStatement pstmt = openConnection("SearchPW");
		pstmt.setString(1, sv.getName());
		pstmt.setString(2, sv.getEmail());
		pstmt.setString(3,  sv.getId());

		SearchValue newValue = new SearchValue(sv);

		ResultSet rs = pstmt.executeQuery();

		while(rs.next()) {
			newValue.setPassword(rs.getString("password"));
		}
		pstmt.close();
		return newValue;
	}

	public ChattingProfile selectProfile(Member to) throws SQLException {
		ChattingProfile selectResult = null;
		PreparedStatement pstmt = openConnection("SelectProfile");
		pstmt.setString(1, to.getId());
		ResultSet rs = pstmt.executeQuery();
		if(rs.next()) {
			String id = rs.getString("id");
			String name = rs.getString("name");
			String introduce = rs.getString("introduce");
			selectResult = new ChattingProfile(name, id, introduce);
		}

		if(selectResult == null) {
			insertProfile(to);
			selectResult = selectProfile(to);
		}
		pstmt.close();
		return selectResult;
	}

	public void insertProfile(Member from) throws SQLException {
		PreparedStatement pstmt = openConnection("InsertProfile");
		pstmt.setString(1, from.getId());
		pstmt.executeUpdate();
	}

	public boolean updateProfile(Member from, ChattingProfile p) throws SQLException {
		PreparedStatement pstmt = openConnection("UpdateProfile");
		pstmt.setString(1, p.getIntroduce());
		pstmt.setString(2, from.getId());

		int index = pstmt.executeUpdate();

		pstmt = openConnection("UpdateMemberID");
		pstmt.setString(1, p.getNickname());
		pstmt.setString(2, from.getId());
		int index2 = pstmt.executeUpdate();

		pstmt.close();

		return (index+index2) == 2;
	}

	public boolean deleteAccount(Member from) throws SQLException {

		PreparedStatement pstmt = openConnection("DeleteAccount");
		pstmt.setString(1, from.getId());
		int i = pstmt.executeUpdate();
		pstmt.close();

		return i==1;
	}

	/**
	 * ID값으로 member 찾기
	 * @param id
	 * @return
	 * @throws SQLException
	 */
	public Member selectMember(String id) throws SQLException {
		
		PreparedStatement pstmt = openConnection("SelectMember");
		pstmt.setString(1, id);
		ResultSet rs = pstmt.executeQuery();
		Member result = null;
		while(rs.next()) {
			result = new Member(rs);
		}
		pstmt.close();

		return result;
	}

	public boolean addFriend(Member from, Member to) throws SQLException {
		
		PreparedStatement pstmt = openConnection("SelectFriend");
		pstmt = openConnection("InsertFriend");
		pstmt.setString(1, to.getId());
		pstmt.setString(2, from.getId());
		pstmt.setString(3, from.getId());
		pstmt.setString(4, to.getId());
		pstmt.close();

		return (pstmt.executeUpdate() == 2);
	}

	public boolean friendCheck(Member from, Member to) throws SQLException {
		
		PreparedStatement pstmt = openConnection("SelectFriend");
		pstmt.setString(1, to.getId());
		pstmt.setString(2, from.getId());
		ResultSet rs =pstmt.executeQuery();
		int res = 0;
		while(rs.next()) {
			res = rs.getInt("friend_cnt");
		}
		pstmt.close();

		return (res==1);
	}
	
	public Vector<Member> friendList(Member from) throws SQLException {
		Vector<Member> list = new Vector<>();
		PreparedStatement pstmt = openConnection("SelectFriendMembers");
		pstmt.setString(1, from.getId());
		ResultSet rs = pstmt.executeQuery();
		while (rs.next()) {
			String id = rs.getString("id");
			String password = rs.getString("password");
			String name = rs.getString("name");
			String email = rs.getString("email");
			String loginYN = rs.getString("Login_YN");
			Member m = new Member(id,password,name,email,loginYN);
			list.add(m);
		}
		pstmt.close();

		return list;
	}
	
	public boolean removeFriend(Member from, Member to) throws SQLException {
		PreparedStatement pstmt = openConnection("DeleteFriend");
		pstmt.setString(1, from.getId());
		pstmt.setString(2, to.getId());
		pstmt.setString(3, to.getId());
		pstmt.setString(4, from.getId());
		pstmt.close();

		return pstmt.executeUpdate() == 2;
	}
	
}
