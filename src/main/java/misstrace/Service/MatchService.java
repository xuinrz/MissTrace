package misstrace.Service;

import misstrace.Entity.MatchPost;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface MatchService {
    void addMatchPost(MatchPost matchPost);
    Integer getNewId();
    List showCheckingMatchPost();
    void passMatchPostById(Integer matchId);
    void refuseMatchPostById(Integer matchId);

}
