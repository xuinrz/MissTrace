package misstrace.Entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(	name = "misspost")
@Data
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
}