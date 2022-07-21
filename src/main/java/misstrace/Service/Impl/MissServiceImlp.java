package misstrace.Service.Impl;

import misstrace.Entity.MissPost;
import misstrace.Repo.MissRepository;
import misstrace.Service.MissService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

@Service
public class MissServiceImlp implements MissService {

    @Resource
    MissRepository missRepository;

    @Override
    public void addMissPost(MissPost missPost) {
        missRepository.save(missPost);
    }

    @Override
    public void updateMissPost(MissPost missPost) {
        missRepository.save(missPost);
    }

    @Override
    public synchronized Integer getNewId() {
        Integer newId = missRepository.getMaxId();
        if(newId==null)return 1;
        else return newId+1;
    }

    @Override
    public Optional<MissPost> findMissPostById(Integer id) {
        return missRepository.findById(id);
    }

    @Override
    public List showPosts() {
        List<MissPost> postsList = missRepository.findDisplayablePost();
        List dataList = new ArrayList();
        MissPost post;
        Map m;
        for (int i = 0; i < postsList.size(); i++) {
            post = postsList.get(i);
            m =new HashMap();
            m.put("id", post.getId());
            m.put("text", post.getText());
            m.put("img", post.getImg());
            m.put("avatar", post.getUser().getAvatar());
            m.put("nickName", post.getUser().getNickName());
            m.put("sid", post.getUser().getSid());
            m.put("postTime",post.getPostTime());
            dataList.add(m);
        }
        return dataList;
    }

    @Override
    public List showCheckingMissPost() {
        List<MissPost> postsList = missRepository.findCheckingPost();
        List dataList = new ArrayList();
        MissPost post;
        Map m;
        for (int i = 0; i < postsList.size(); i++) {
            post = postsList.get(i);
            m =new HashMap();
            m.put("id", post.getId());
            m.put("text", post.getText());
            m.put("img", post.getImg());
            m.put("avatar", post.getUser().getAvatar());
            m.put("nickName", post.getUser().getNickName());
            m.put("sid", post.getUser().getSid());
            m.put("postTime",post.getPostTime());
            dataList.add(m);
        }
        return dataList;
    }

    @Override
    public void passMissPostById(Integer missId) {
        MissPost missPost = missRepository.findById(missId).get();
        missPost.setIsChecking(false);
        missPost.setIsPassed(true);
        missRepository.save(missPost);


    }

    @Override
    public void refuseMissPostById(Integer missId) {
        MissPost missPost = missRepository.findById(missId).get();
        missPost.setIsChecking(false);
        missPost.setIsPassed(false);
        missRepository.save(missPost);
    }
}
