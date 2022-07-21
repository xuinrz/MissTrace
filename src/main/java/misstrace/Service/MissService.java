package misstrace.Service;

import misstrace.Entity.MissPost;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface MissService {
    void addMissPost(MissPost missPost);
    void updateMissPost(MissPost missPost);
    Integer getNewId();
    Optional<MissPost> findMissPostById(Integer id);
    List<MissPost> showPosts();
}
