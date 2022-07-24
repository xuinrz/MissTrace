package misstrace.Entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(	name = "misspost")
public class MissPost {
    @Id
    private Integer id;
    //    绑定学号
    @ManyToOne
    @JoinColumn(name = "uid")
    private User user;

    private String text;
    private String img;
    //    是否待审核？建立帖子时设为待审核
    private Boolean isChecking = true;
    //    是否过审？建立帖子时设为未过审
    private Boolean isPassed = false;
    //    迷踪帖下是否有待审的匹配贴？建立帖子时设为没有
    private Boolean isMatching = false;
    //    是否被匹配成功？建立帖子时设为未匹配成功
    private Boolean isMatched = false;
    //    发帖位置
    private Double longitude;//精度
    private Double latitude ;//纬度
    //    发帖时间
    private String postTime;
    //    审核时间
    private String checkTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public Boolean getChecking() {
        return isChecking;
    }

    public void setIsChecking(Boolean checking) {
        isChecking = checking;
    }

    public Boolean getIsPassed() {
        return isPassed;
    }

    public void setIsPassed(Boolean passed) {
        isPassed = passed;
    }

    public Boolean getIsMatching() {
        return isMatching;
    }

    public void setIsMatching(Boolean matching) {
        isMatching = matching;
    }

    public Boolean getIsMatched() {
        return isMatched;
    }

    public void setIsMatched(Boolean matched) {
        isMatched = matched;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public String getPostTime() {
        return postTime;
    }

    public void setPostTime(String postTime) {
        this.postTime = postTime;
    }

    public String getCheckTime() {
        return checkTime;
    }

    public void setCheckTime(String checkTime) {
        this.checkTime = checkTime;
    }
}