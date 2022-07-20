package misstrace.Service.Impl;

import misstrace.Entity.MissPost;
import misstrace.Repo.MissRepository;
import misstrace.Service.MissService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;

@Service
public class MissServiceImlp implements MissService {

    @Resource
    MissRepository missRepository;

    @Override
    public void addMissPost(MissPost missPost) {
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
    public List<MissPost> findPassedMissPosts() {
        return missRepository.findPassedMissPosts();
    }
}
