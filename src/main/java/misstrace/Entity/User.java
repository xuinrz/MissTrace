package misstrace.Entity;


import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(	name = "user")
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
    public synchronized void addCoin(){coin++;}//获得积分时调用
    public synchronized void costCoin(Integer cost){coin-=cost;}//购买物品时调用

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public Integer getCoin() {
        return coin;
    }

    public void setCoin(Integer coin) {
        this.coin = coin;
    }

    public Boolean getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(Boolean admin) {
        isAdmin = admin;
    }

    public List<MissPost> getMissPostList() {
        return missPostList;
    }

    public void setMissPostList(List<MissPost> missPostList) {
        this.missPostList = missPostList;
    }

    public List<MatchPost> getMatchPostList() {
        return matchPostList;
    }

    public void setMatchPostList(List<MatchPost> matchPostList) {
        this.matchPostList = matchPostList;
    }

    public List<UserProperty> getUserPropertyList() {
        return userPropertyList;
    }

    public void setUserPropertyList(List<UserProperty> userPropertyList) {
        this.userPropertyList = userPropertyList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
