package misstrace.Service;

import misstrace.Entity.User;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
public interface UserService {
    String casLogin(String sid,String password);
    Optional<User> findUserBySid(String sid);
    Integer getNewId();
    void addUser(User user);
    void modifyUser(User user);



}
