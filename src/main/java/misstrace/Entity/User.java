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
}
