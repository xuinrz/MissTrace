package misstrace.Service.Impl;

import misstrace.Entity.MatchPost;
import misstrace.Entity.MissPost;
import misstrace.Entity.User;
import misstrace.Repo.MatchRepository;
import misstrace.Repo.MissRepository;
import misstrace.Service.MatchService;
import misstrace.Service.MissService;
import misstrace.Service.UserService;
import misstrace.Util.DataUtil;
import misstrace.Util.ImgUtil;
import misstrace.Util.LocationUtil;
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
        MissPost miss;
        Map m;
        for (int i = 0; i < postsList.size(); i++) {
            post = postsList.get(i);
            miss = post.getMissPost();
            m =new HashMap();
            m.put("id", post.getId());
            m.put("img", post.getImg());
            m.put("missImg",post.getMissPost().getImg());
            Double distance = LocationUtil.getDistance(miss.getLatitude(),miss.getLongitude(),post.getLatitude(),post.getLongitude());
            m.put("distance",distance);
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
        matchPost.setCheckTime(DataUtil.getTime());
        MissPost missPost = matchPost.getMissPost();
        missPost.setIsMatched(true);
        missPost.setCheckTime(DataUtil.getTime());
//        ???????????????????????????????????????
//        if(missPost.getImg()!=null) ImgUtil.deleteImg(missPost.getImg());
        if(matchPost.getImg()!=null) ImgUtil.deleteImg(matchPost.getImg());
        missService.updateMissPost(missPost);
        matchRepository.save(matchPost);
//        ???????????????????????????
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
        matchPost.setCheckTime(DataUtil.getTime());
        if(matchPost.getImg()!=null) ImgUtil.deleteImg(matchPost.getImg());//??????????????????????????????????????????
        MissPost missPost = matchPost.getMissPost();
        missPost.setIsMatching(false);
        missService.updateMissPost(missPost);
        matchRepository.save(matchPost);
    }
}
