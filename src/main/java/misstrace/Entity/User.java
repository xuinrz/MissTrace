package misstrace.Entity;


import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(	name = "user")
@Data
public class User {
    @Id
    private Integer id;
    private String sid;
    private String avatar;
    private String nickName;
//    因用户积分为0
    private Integer coin = 0;
    private Boolean isAdmin = false;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "user")
    private List<MissPost> missPostList;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "user")
    private List<MatchPost> matchPostList;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "user")
    private List<UserProperty> userPropertyList;
    private String name;
    public void addCoin(){coin++;}//获得积分时调用
    public void costCoin(Integer cost){coin-=cost;}//购买物品时调用
}
