package misstrace.Service.Impl;

import misstrace.Entity.MatchPost;
import misstrace.Entity.MissPost;
import misstrace.Entity.User;
import misstrace.Repo.MatchRepository;
import misstrace.Repo.MissRepository;
import misstrace.Service.MatchService;
import misstrace.Service.MissService;
import misstrace.Service.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class MatchServiceImlp implements MatchService {

    @Resource
    MatchRepository matchRepository;
    @Resource
    MissService missService;
    @Resource
    UserService userService;

    @Override
    public void addMatchPost(MatchPost matchPost) {
        matchRepository.save(matchPost);
    }

    @Override
    public Integer getNewId() {
        Integer newId = matchRepository.getMaxId();
        if(newId==null)return 1;
        else return newId+1;
    }

    @Override
    public List showCheckingMatchPost() {
        List<MatchPost> postsList = matchRepository.findCheckingPost();
        List dataList = new ArrayList();
        MatchPost post;
        Map m;
        for (int i = 0; i < postsList.size(); i++) {
            post = postsList.get(i);
            m =new HashMap();
            m.put("id", post.getId());
            m.put("img", post.getImg());
            m.put("missImg",post.getMissPost().getImg());
            m.put("avatar", post.getUser().getAvatar());
            m.put("nickName", post.getUser().getNickName());
            m.put("sid", post.getUser().getSid());
            m.put("postTime",post.getPostTime());
            dataList.add(m);
        }
        return dataList;
    }

    @Override
    public void passMatchPostById(Integer matchId) {
        MatchPost matchPost = matchRepository.findById(matchId).get();
        matchPost.setIsChecking(false);
        matchPost.setIsMatched(true);
        MissPost missPost = matchPost.getMissPost();
        missPost.setIsMatched(true);
        missService.updateMissPost(missPost);
        matchRepository.save(matchPost);
//        给两人添加一个积分
        User missUser = missPost.getUser();
        User matchUser = matchPost.getUser();
        missUser.addCoin();
        matchUser.addCoin();
        userService.updateUser(missUser);
        userService.updateUser(matchUser);

    }

    @Override
    public void refuseMatchPostById(Integer matchId) {
        MatchPost matchPost = matchRepository.findById(matchId).get();
        matchPost.setIsChecking(false);
        matchPost.setIsMatched(false);
        MissPost missPost = matchPost.getMissPost();
        missPost.setIsMatching(false);
        missService.updateMissPost(missPost);
        matchRepository.save(matchPost);
    }
}
