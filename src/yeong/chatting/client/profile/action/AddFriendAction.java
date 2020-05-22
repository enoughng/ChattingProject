package yeong.chatting.client.profile.action;

import yeong.chatting.client.base.action.ActionInfo;
import yeong.chatting.client.base.action.CommonAction;
import yeong.chatting.util.Log;

public class AddFriendAction implements CommonAction{
	@Override
	public void action(ActionInfo info) {

		Log.i(getClass(), "친구추가 액션 추가됨");
	}
}
