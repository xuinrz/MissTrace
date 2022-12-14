package misstrace.Service.Impl;

import misstrace.Entity.MissPost;
import misstrace.Repo.MissRepository;
import misstrace.Service.MissService;
import misstrace.Util.DataUtil;
import misstrace.Util.ImgUtil;
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
            m.put("sid", post.getUser().getSid());//前端用学号来判断是否添加发布匹配帖按钮
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
        missPost.setCheckTime(DataUtil.getTime());
        missRepository.save(missPost);


    }

    @Override
    public void refuseMissPostById(Integer missId) {
        MissPost missPost = missRepository.findById(missId).get();
        missPost.setIsChecking(false);
        missPost.setIsPassed(false);
        missPost.setCheckTime(DataUtil.getTime());
//        if(missPost.getImg()!=null) ImgUtil.deleteImg(missPost.getImg());//不过审，直接删除图片
        missRepository.save(missPost);
    }

    @Override
    public List showMyPostsByUserId(Integer userId) {
        List<MissPost> postsList = missRepository.findPostByUserId(userId);
        List dataList = new ArrayList();
        MissPost post;
        String status = "";
        Integer code = 0;
        Map m;
        Boolean isChecking,isPassed,isMatching,isMatched;
        for (int i = 0; i < postsList.size(); i++) {
            post = postsList.get(i);
            m =new HashMap();
            m.put("id", post.getId());
            m.put("text", post.getText());
            m.put("img", post.getImg());
            m.put("postTime",post.getPostTime());
            isChecking = post.getIsChecking();
            isPassed = post.getIsPassed();
            isMatching = post.getIsMatching();
            isMatched = post.getIsMatched();
            if (isChecking){
                status = "待审核";
                code = 0;
            }else{
                if (!isPassed){
                    status = "未过审";
                    code = -1;
                }else{
                    if (!isMatching&&!isMatched){
                        status = "已发布，待匹配";
                        code = 1;
                    }else if(isMatching&&!isMatched){
                        status = "有人正在匹配此帖，请等待审核结果";
                        code = 2;
                    }else if(isMatching&&isMatched){
                        status = "本帖已被成功匹配，获得1积分";
                        code = 3;
                    }
                }
            }
            m.put("status",status);
            m.put("code",code);
            dataList.add(m);
        }
        return dataList;
    }
}
