package misstrace.Service;

import misstrace.Entity.MatchPost;
import org.springframework.stereotype.Service;

@Service
public interface MatchService {
    void addMatchPost(MatchPost matchPost);
    Integer getNewId();

}
