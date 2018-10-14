package cn.rumoss.ts.friend.service;

import cn.rumoss.ts.friend.client.UserClient;
import cn.rumoss.ts.friend.dao.FriendDao;
import cn.rumoss.ts.friend.dao.NoFriendDao;
import cn.rumoss.ts.friend.pojo.Friend;
import cn.rumoss.ts.friend.pojo.NoFriend;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FriendService {

    @Autowired
    private FriendDao friendDao;

    @Autowired
    private NoFriendDao noFriendDao;

    @Autowired
    private UserClient userClient;

    /**
     * 添加好友
     *
     * @param userid
     * @param friendid
     * @return
     */
    @Transactional
    public int addFriend(String userid, String friendid) {

        //判断是否添加过该好友
        if (friendDao.selectCount(userid, friendid) > 0) {
            //已经添加过了
            return 0;
        }

        //没有添加过,则添加好友
        Friend friend = new Friend();
        friend.setUserid(userid);
        friend.setFriendid(friendid);
        friend.setIslike("0");

        friendDao.save(friend);

        //判断对方是否也喜欢你
        if(friendDao.selectCount(userid,friendid)>0){
            //更新双方的islike为1
            friendDao.updateLike(userid,friendid,"1");
            friendDao.updateLike(friendid,userid,"1");
        }

        //远程更新用户的关注数和粉丝数
        //关注数
        userClient.updateFollowcount(userid,1);
        //粉丝数
        userClient.updateFanscount(friendid,1);

        return 1;
    }

    /**
     * 添加非好友
     * @param userid
     * @param friendid
     */
    @Transactional
    public void addNoFriend(String userid, String friendid) {
        NoFriend noFriend = new NoFriend();
        noFriend.setUserid(userid);
        noFriend.setFriendid(friendid);
        noFriendDao.save(noFriend);
    }

    /**
     * 删除好友
     * @param userid
     * @param friendid
     */
    @Transactional
    public void deleteFriend(String userid, String friendid) {

        //删除当前好友记录
        friendDao.deleteFriend(userid,friendid);

        //更新对方的lslike为0

        friendDao.updateLike(friendid,userid,"0");

        //更新关注数和粉丝数
        userClient.updateFanscount(friendid,-1);
        userClient.updateFollowcount(userid,-1);
    }
}
