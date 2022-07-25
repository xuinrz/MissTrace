package misstrace.Service.Impl;

import misstrace.Entity.MatchPost;
import misstrace.Entity.MissPost;
import misstrace.Entity.User;
import misstrace.Payload.Result;
import misstrace.Repo.MatchRepository;
import misstrace.Repo.MissRepository;
import misstrace.Repo.UserRepository;
import misstrace.Service.UserService;
import misstrace.Util.DataUtil;
import misstrace.Util.ImgUtil;
import misstrace.Util.JwtUtil;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.*;

@Service
public class UserServiceImpl implements UserService {

    @Resource
    UserRepository userRepository;
    @Resource
    MissRepository missRepository;
    @Resource
    MatchRepository matchRepository;



    @Override
    public Optional<User> findUserBySid(String sid) {
        return userRepository.findBySid(sid);
    }

    @Override
    public synchronized Integer getNewId() {
        Integer newId = userRepository.getMaxId();
        if (newId == null) return 1;
        else return newId + 1;
    }

    @Override
    public void addUser(User user) {
        userRepository.save(user);
    }

    @Override
    public void updateUser(User user) {
        userRepository.save(user);
    }

    @Override
    public void modifyUser(User user) {
        userRepository.save(user);
    }

    @Override
    public User getUserByToken(String token) {
        Integer userId = Integer.parseInt(JwtUtil.getUserId(token));
        User user = userRepository.findById(userId).get();
        return user;
    }

    @Override
    public List getMsgByUserId(Integer userId) {
        List<HashMap> dataList = new ArrayList();
        List<MissPost> missList = missRepository.getMissMessageByUserId(userId);
        List<MatchPost> matchList = matchRepository.getMatchMessageByUserId(userId);
        MissPost missPost;
        MatchPost matchPost;
        HashMap m;
//        处理迷踪帖消息（不过审、过审、被成功匹配）
        for (int i = 0; i < missList.size(); i++) {
            m = new HashMap();
            missPost = missList.get(i);
            if (!missPost.getIsPassed()) {//不通过
                m.put("code", -1);
                m.put("msg", "迷踪帖未通过审核");
            } else {//过审了，要分为（只是过审）和（匹配成功）
                if (!missPost.getIsMatched()) {//只是过审了
                    m.put("code", 1);
                    m.put("msg", "迷踪帖已过审");
                } else {//匹配成功了
                    m.put("code", 2);
                    m.put("msg", "迷踪帖已被成功匹配，获得1积分");
                }
            }
            m.put("text", missPost.getText());//迷踪帖文字
            m.put("checkTime", missPost.getCheckTime());//审核时间
            dataList.add(m);
        }
//        处理匹配贴消息（匹配成功、匹配失败）
        for (int i = 0; i < matchList.size(); i++) {
            m = new HashMap();
            matchPost = matchList.get(i);
            if (!matchPost.getIsMatched()) {//匹配失败
                m.put("code", -2);
                m.put("msg", "匹配帖匹配失败");
            } else {//匹配成功
                    m.put("code", 3);
                    m.put("msg", "匹配帖匹配成功，获得1积分");
            }
            m.put("text", matchPost.getMissPost().getText());//所匹配的迷踪帖的文字
            m.put("checkTime", matchPost.getCheckTime());//审核时间
            dataList.add(m);
        }
        //利用匿名类排序
//        System.out.println(dataList);
        dataList.sort(Comparator.comparing((HashMap a) -> DataUtil.parseString((String) a.get("checkTime"))));
//        System.out.println(dataList);
        return dataList;
    }

    @Override
    public void changeAvatar(User user, MultipartFile avatar) {
        String avatarPath = ImgUtil.uploadAvatar(avatar);
        if(avatarPath!=null&&user.getAvatar()!=null)ImgUtil.deleteImg(user.getAvatar());//存在旧头像则删除
        user.setAvatar(avatarPath);
        modifyUser(user);
    }


}
