package misstrace.Service.Impl;

import misstrace.Entity.User;
import misstrace.Repo.UserRepository;
import misstrace.Service.UserService;
import misstrace.Util.JwtUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;
import java.util.Optional;
@Service
public class UserServiceImpl implements UserService {

    @Resource
    UserRepository userRepository;


    @Override
    public String casLogin(String sid, String password) {
        System.out.println("假设统一认证登录成功");
        return "假设统一认证登录成功";
    }

    @Override
    public Optional<User> findUserBySid(String sid) {
        return userRepository.findBySid(sid);
    }

    @Override
    public synchronized Integer getNewId() {
        Integer newId = userRepository.getMaxId();
        if(newId==null)return 1;
        else return newId+1;
    }

    @Override
    public void addUser(User user) {
        userRepository.save(user);
    }

    @Override
    public void updateUser(User user) {
        userRepository.save(user);
    }

    @Override
    public void modifyUser(User user) {
        userRepository.save(user);
    }

    @Override
    public User getUserByToken(String token) {
        Map<String, Object> info = JwtUtil.getInfo(token);
        User user = findUserBySid((String)info.get("sid")).get();
        return user;
    }


}
