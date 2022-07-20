package misstrace.Service.Impl;

import misstrace.Entity.MatchPost;
import misstrace.Repo.MatchRepository;
import misstrace.Service.MatchService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;


@Service
public class MatchServiceImlp implements MatchService {

    @Resource
    MatchRepository matchRepository;

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
}
