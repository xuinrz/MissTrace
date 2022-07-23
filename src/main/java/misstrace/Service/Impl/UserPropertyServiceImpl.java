package misstrace.Service.Impl;


import misstrace.Entity.Goods;
import misstrace.Entity.User;
import misstrace.Entity.UserProperty;
import misstrace.Repo.UserPropertyRepository;
import misstrace.Repo.UserRepository;
import misstrace.Service.UserPropertyService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserPropertyServiceImpl implements UserPropertyService {

    @Resource
    UserPropertyRepository userPropertyRepository;
    @Resource
    UserRepository userRepository;

    @Override
    public synchronized Integer getNewId() {
        Integer newId = userPropertyRepository.getMaxId();
        if(newId==null)return 1;
        else return newId+1;
    }

    @Override
    public List myPropertyList(Integer userId) {
        List<UserProperty> propertyList = userPropertyRepository.findByUser_Id(userId);
        List dataList = new ArrayList();
        UserProperty userProperty;
        Goods goods;
        Map m;
        for (int i = 0; i < propertyList.size(); i++) {
            userProperty = propertyList.get(i);
            goods = userProperty.getGoods();
            m = new HashMap();
            m.put("id", goods.getId());
            m.put("description", goods.getDescription());
            m.put("img", goods.getImg());
            m.put("cost", goods.getCost());
            m.put("number",userProperty.getNumber());
            dataList.add(m);
        }
        return dataList;
    }

    @Override
    public String download() {

        List<User> userList = userRepository.findAll();
        StringBuilder stringBuilder = new StringBuilder();
        User user;
        Map m;
        for (int i = 0; i < userList.size(); i++) {
            user = userList.get(i);
            stringBuilder.append(user.getSid()+"\t"+ user.getName()+"\t");
            stringBuilder.append(myPropertyForDownload(user.getId())+"\n");
        }


        return stringBuilder.toString();
    }


    public String myPropertyForDownload(Integer userId) {
        List<UserProperty> propertyList = userPropertyRepository.findByUser_Id(userId);
        StringBuilder myProperty = new StringBuilder();
        UserProperty userProperty;
        Goods goods;
        for (int i = 0; i < propertyList.size(); i++) {
            userProperty = propertyList.get(i);
            goods = userProperty.getGoods();
            myProperty.append(goods.getDescription()+"*"+userProperty.getNumber()+",");
        }
        return myProperty.toString();
    }

}
