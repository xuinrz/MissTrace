package misstrace.Entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(	name = "matchpost")
@Data
public class MatchPost {
    @Id
    private Integer id;
    //    绑定迷踪帖id
    @OneToOne
    @JoinColumn(name = "missid")
    private MissPost missPost;
    //    绑定学号
    @ManyToOne
    @JoinColumn(name = "uid")
    private User user;

    private String img;
    //    是否待审核？建立帖子时设为待审核
    private Boolean isChecking = true;
    //    是否被匹配成功？建立帖子时设为未匹配成功
    private Boolean isMatched = false;
    //    发帖位置
    private String position;
//    发帖时间
    private String postTime;
}