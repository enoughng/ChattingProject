package yeong.chatting.server.thread;

import java.awt.CheckboxGroup;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Vector;

import javafx.application.Platform;
import yeong.chatting.model.Member;
import yeong.chatting.model.Message;
import yeong.chatting.model.RoomInfo;
import yeong.chatting.server.dao.ServerDAO;
import yeong.chatting.server.main.MainController;
import yeong.chatting.util.Log;
import yeong.chatting.util.ProtocolType;

/**
 * 클라이언트에서 요청된 메시지를 분석하여 다시 보내주는 클래스이다.
 * 
 * @author Yeong
 *
 */

public class RequestCheck {

	private MainController controller = MainController.getMainController();

	private ServerDAO sDao;

	private Message request;
	private Message response;

	ProtocolType responseProtocol = null;

	private boolean isBroadType = false;
	
	private InputThread t;
	
	public RequestCheck(InputThread t, Message msg) throws SQLException, IOException {
		this.t = t;
		request = msg;
		sDao = ServerDAO.getInstance();
	}

	public Message result() throws SQLException {
		switch (request.getProtocol()) {
		case REQUEST_LOGIN:
			// 현재 사용자 수
			response = login();
			isBroadType = false;
			break;
		case REQUEST_IDCHECK:
			response = checkID();
			isBroadType = false;
			break;
		case REQUEST_REGISTRY:
			response = registry();
			isBroadType = false;
			break;
		case REQUEST_WAITINGROOM_MEMBER:
			response = getMembers();
			isBroadType = true;
			break;
		case REQUEST_LOGOUT:
			response = logout();
			isBroadType = false;
			break;
		case REQUEST_CREATEROOM:
			response = createRoom();
			isBroadType = false;
			break;
		case REQUEST_ENTERROOM:
			response = enterRoom();
			isBroadType = false;
			break;
		case REQUEST_EXITROOM:
			isBroadType = false;
			response = exitRoom();
			break;
		case REQUEST_SEND:
			isBroadType = false;
			response = send();
			break;
		default:
			response = null;
		}

		return response;
	}

	public boolean sendType() {
		return isBroadType;
	}

	/** 
	 *  공통 기능들
	 *  
	 *  서버 프로그램에 Log남기기
	 */
	private void appendLog(String msg) {

		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		String time = sdf.format(Calendar.getInstance().getTime());

		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				controller.getLog().appendText("[" + time + "] " + msg + "\n");
			}
		});
	}

	/**
	 * switch case 문에 따른 메소드들
	 * 
	 * 로그인 요청에 따른 Select조회 및 리턴
	 * 
	 * @throws SQLException
	 */
	private Message login() throws SQLException {
		Message response;
		ProtocolType responseProtocol;
		Member loginMember = sDao.checkLogin(request.getFrom()); // DB 조회한 멤버
		if (loginMember != null) { // 조회한 멤버가 있다면
			t.setCurrentID(loginMember.getId());
			appendLog(loginMember.getId() + "(" + loginMember.getName() + ")님이 로그인 하셨습니다.");
			responseProtocol = ProtocolType.RESPONSE_LOGIN_SUCCESS;
			ServerThread.memberList.add(loginMember);
			ServerThread.isLogout=false;
			response = new Message(responseProtocol, loginMember);
			appendLog("현재 인원 : " + ServerThread.serverThreads + "\nMember size : " + ServerThread.serverThreads.size());
		} else {
			responseProtocol = ProtocolType.RESPONSE_LOGIN_FAIL;
			response = new Message(responseProtocol, request.getFrom());
		}

		return response;
	}

	/**
	 * 로그아웃
	 */
	private Message logout() throws SQLException {

		ProtocolType responseProtocol = ProtocolType.RESPONSE_LOGOUT;
		ServerThread.isLogout =true;
		t.setCurrentID("");
		ServerThread.memberList.remove(request.getFrom());
		appendLog(request.getFrom().getId() + "님이 로그아웃 하셨습니다.");
		appendLog("현재 인원 : " + ServerThread.memberList + "\n 인원수 : " +ServerThread.memberList.size());

		Message response = new Message(responseProtocol, request.getFrom());
		response.setMemberList(new Vector<Member>(ServerThread.memberList));
		return  response;
	}

	/**	
	 * 회원등록
	 */
	private Message registry() throws SQLException {
		Message response;

		sDao.insertMember(request.getFrom()); // 데이터베이스 insert문 삽입
		responseProtocol = ProtocolType.RESPONSE_REGISTRY_SUCCESS;
		response = new Message(responseProtocol, request.getFrom());

		return response;
	}

	/**
	 * 현재 들어와있는 멤버 수 및 만들어져있는 방정보
	 * @return
	 * @throws SQLException
	 */
	private Message getMembers() throws SQLException {
		ProtocolType responseProtocol = ProtocolType.RESPONSE_WAITINGROOM_MEMBER;

		//대기실 방 인원수 정보
		Message response = new Message(responseProtocol, request.getFrom(), new Vector<Member>(ServerThread.memberList)); // 대기실 멤버
//		response.getFrom(); // 대기실 
		
		Vector<RoomInfo> list = sDao.getRooms(); // 대기실 방정보 DB를 통해서 가져옴
		response.setRoomList(new Vector<RoomInfo>(list)); // 대기실 방 정보를 Message에 담아줌
		return response;
	}

	/**
	 * 방 만들기
	 * 
	 * 1. 입력받은 방정보와 로그인 정보를 통해서 DB에 방을 생성한다.
	 * 2. 생성된 방 정보를 response에 담아준다.
	 * 3. 대기실 목록에서 요청한 멤버를 삭제해준다. ( memberList.remove(request.geFrom())
	 * 4. 새로운 벡터틀 만들어서 요청한 멤버를 넣어준다.
	 * 5. 4번에서 만든 벡터를 ServerThread.roomMemberList(해쉬맵)에 방번호를 키값으로 넣어준다.
	 * 6. 4번에서 만든 벡터를 Response값으로 보내준다.
	 * 
	 * 
	 * @return result(DB roomInfo), from(요청한 멤버), roomMemberList( rInfo에 해당하는 Vector배열 )
	 * @throws SQLException
	 */
	private Message createRoom() throws SQLException { 
		RoomInfo result = sDao.insertRoom(request.getrInfo(), request.getFrom());
		if(result != null) {	
			response = new Message(ProtocolType.RESPONSE_CREATEROOM, request.getFrom());
			response.setrInfo(result);
			//대기실목록에서 해당 사용자 삭제
			ServerThread.memberList.remove(request.getFrom());
			
			// 방이 정상적으로 만들어졌으면 해당 인원을 담을 백터를 생성해주고, 해당 백터에 방을 만든 멤버를 넣고 이를 메세지에 담아서 보내준다.
			Vector<Member> newList = new Vector<>();
			newList.add(request.getFrom());
			ServerThread.roomMemberList.put(result.getRoom_num(), newList);
			response.setRoomMemberList(newList);
		} else {
			response = new Message(ProtocolType.RESPONSE_CREATEROOM_FAIL, request.getFrom());
		}
		return response;
	}

	/**
	 * 방 들어가기
	 * 
	 * 요청값 : 선택한 방 정보, 요청한 멤버
	 * 1. 선택한 방 정보를 통해 DB에 방정보를 가져온다.
	 * 2. 요청한 멤버를 대기실 멤버리스트에서 제거한다.
	 * 3. DB에서 조회한 방 번호를 가지고 roomMemberList에서 Vector배열을 가져온다.
	 * 4. 3번에서 Vector에 현재 정보를 넣어준다.
	 * 5. response에 Vector를 넣어준다.
	 * 6. DB에서 조회한 방 정보를 넣는다.
	 */
	private Message enterRoom() throws SQLException {

		Member from = request.getFrom();
		RoomInfo requestRoomInfo = request.getrInfo();	
		/* 선택된 값이 없을 때*/
		if(requestRoomInfo == null) {
			responseProtocol = ProtocolType.RESPONSE_ENTERROOM_FAIL;
			Message response = new Message(responseProtocol, from);
			return response;
		}
		/* 요청된 방정보를 이용하여 DB에서 있는지 조회 */
		RoomInfo checkedRoomInfo = sDao.hasRoom(requestRoomInfo);
		ProtocolType responseProtocol;
		/* 있으면 success 없으면 fail로 응답 */
		if(checkedRoomInfo != null) {
			responseProtocol = ProtocolType.RESPONSE_ENTERROOM_SUCCESS;
		} else {
			responseProtocol = ProtocolType.RESPONSE_ENTERROOM_FAIL;
		}
		Message response = new Message(responseProtocol, from);
		ServerThread.memberList.remove(request.getFrom());
		//요청한 멤버를 현재 맵 데이터에 넣어준다.
		Vector<Member> roomMemberList = ServerThread.roomMemberList.get(checkedRoomInfo.getRoom_num());
		roomMemberList.add(request.getFrom());
		// index값을 이용하여 ServerThread에 있는 Map을 조회하여 해당 방번호에 존재하는 Vector를 반환해준다.
		response.setRoomMemberList(new Vector<Member>(roomMemberList));
		response.setrInfo(checkedRoomInfo); // RoomInfo
		return response;
	}

	/**
	 * 나가는 작업 방정보랑 방장이 만약 같다면? 
	 * 방을 삭제하는 쿼리문 작성 및 접속자들 전부 퇴장
	 * 요청자와 방장이 다르다면? 목록에서 삭제만하고 화면전환
	 * 1. 입력된 방정보를 통해 DB의 방정보를 가져온다.
	 * 2. 요청한 멤버와 DB의 방정보의 주인이 같다면 방을 삭제한다.
	 * 2-1 그렇지 않다면 Map에 있는 멤버중 요청한 멤버를 제거한다.
	 * 3. 대기실에 현재 멤버를 넣어준다.
	 * 4. 멤버정보와 DB조회정보 방정보를 Message에 넣어준다.
	 */
	private Message exitRoom() throws SQLException {
		Message response = null;
		ProtocolType responseProtocol;

		RoomInfo DBrInfo = sDao.hasRoom(request.getrInfo());

		
		if(DBrInfo.getRoom_host().equals(request.getFrom().getName())) { //나가는사람이 방장이면 방폭파
			isBroadType = false;
			responseProtocol = ProtocolType.RESPONSE_EXITROOM_HOST;
			sDao.deleteRoom(DBrInfo);
		} else { // 나가는사람이 방장이 아니면 방 나가기
			isBroadType = false;
			responseProtocol = ProtocolType.RESPONSE_EXITROOM;
			ServerThread.roomMemberList.get(DBrInfo.getRoom_num()).remove(request.getFrom());
		}
		
		ServerThread.memberList.add(request.getFrom()); // 요청된값 대기실인원으로 추가
		response = new Message(responseProtocol, request.getFrom(), new Vector<Member>(ServerThread.memberList)); // 멤버정보
		response.setrInfo(new RoomInfo(DBrInfo)); 
		Vector<RoomInfo> list = sDao.getRooms(); // 대기실 방정보 DB를 통해서 가져옴
		response.setRoomList(list); // 방정보 
//		response.setRoomMemberList(new Vector(list);
		return response;
	}
	
	/**
	 * 접속해 있는 방정보와 사용자정보, MSG값을 입력받아 방에 접속해있는 사람에게 돌려주는 메소드이다.
	 */

	private Message send() throws SQLException {
		ProtocolType responseProtocol = ProtocolType.RESPONSE_SEND;
		RoomInfo dbRoomInfo = sDao.hasRoom(request.getrInfo());
		// 프로토콜과 방정보만 바꿔준다.
		request.setProtocol(responseProtocol);
		request.setrInfo(dbRoomInfo);
		return request;
	}
	
	private Message checkID() throws SQLException {
		ProtocolType responseProtocol = ProtocolType.RESPONSE_IDCHECK;
		String isHave = sDao.checkID(request.getMsg());

		Message response = new Message(responseProtocol,isHave);
		response.setFrom(new Member(request.getMsg(),null));
		return response;
	}
}
