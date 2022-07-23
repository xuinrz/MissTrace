package misstrace.Service;

import misstrace.Entity.User;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
@Service
public interface UserService {
    String casLogin(String sid,String password);
    Optional<User> findUserBySid(String sid);
    Integer getNewId();
    void addUser(User user);
    void updateUser(User user);
    void modifyUser(User user);
    User getUserByToken(String token);
    List getMsgByUserId(Integer userId);
    void changeAvatar(User user, MultipartFile avatar);



}
