package pers.kanarien.chatroom.dao.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import org.springframework.stereotype.Repository;

import pers.kanarien.chatroom.dao.UserInfoDao;
import pers.kanarien.chatroom.model.po.GroupInfo;
import pers.kanarien.chatroom.model.po.UserInfo;
import pers.kanarien.chatroom.util.Constant;

@Repository
public class UserInfoDaoImpl implements UserInfoDao {

    /**
     * Dead data is used here, no database is used
     */
    @Override
    public void loadUserInfo() {
        // Set user basic information, a total of 4 users
        UserInfo userInfo = new UserInfo("001", "wang", "000", "static/img/avatar/Member001.jpg");
        UserInfo userInfo2 = new UserInfo("002", "nikolas", "000", "static/img/avatar/Member002.jpg");
        UserInfo userInfo3 = new UserInfo("003", "saab", "000", "static/img/avatar/Member003.jpg");
        UserInfo userInfo4 = new UserInfo("004", "semilogoolusola", "000", "static/img/avatar/Member005.jpg");

        // Set user friend list
        userInfo.setFriendList(generateFriendList("001"));
        userInfo2.setFriendList(generateFriendList("002"));
        userInfo3.setFriendList(generateFriendList("003"));
        userInfo4.setFriendList(generateFriendList("004"));

        // Set the user group list, a total of 1 group
        GroupInfo groupInfo = new GroupInfo("01", "Group01", "static/img/avatar/Group01.jpg", null);
        List<GroupInfo> groupList = new ArrayList<GroupInfo>();
        groupList.add(groupInfo);
        userInfo.setGroupList(groupList);
        userInfo2.setGroupList(groupList);
        userInfo3.setGroupList(groupList);
        userInfo4.setGroupList(groupList);

        
        Constant.userInfoMap.put("wang", userInfo);
        Constant.userInfoMap.put("nikolas", userInfo2);
        Constant.userInfoMap.put("saab", userInfo3);
        Constant.userInfoMap.put("semilogoolusola", userInfo4);

    }

    @Override
    public UserInfo getByUsername(String username) {
        return Constant.userInfoMap.get(username);
    }
    
    @Override
    public UserInfo getByUserId(String userId) {
        UserInfo result = null;
        Iterator<Entry<String, UserInfo>> iterator = Constant.userInfoMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Entry<String, UserInfo> entry = iterator.next();
            if (entry.getValue().getUserId().equals(userId)) {
                result = entry.getValue();
                break;
            }
        }
        return result;
    }
    
    private List<UserInfo> generateFriendList(String userId) {
        UserInfo userInfo = new UserInfo("001", "wang", "000", "static/img/avatar/Member001.jpg");
        UserInfo userInfo2 = new UserInfo("002", "nikolas", "000", "static/img/avatar/Member002.jpg");
        UserInfo userInfo3 = new UserInfo("003", "Saab", "000", "static/img/avatar/Member003.jpg");
        UserInfo userInfo4 = new UserInfo("004", "semilogoolusola", "000", "static/img/avatar/Member005.jpg");

        List<UserInfo> friendList = new ArrayList<UserInfo>();
        friendList.add(userInfo);
        friendList.add(userInfo2);
        friendList.add(userInfo3);
        friendList.add(userInfo4);

        Iterator<UserInfo> iterator = friendList.iterator();
        while(iterator.hasNext()) {
            UserInfo entry = iterator.next();
            if (userId.equals(entry.getUserId())) {
                iterator.remove();
            }
        }
        return friendList;
    }


}
