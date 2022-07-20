package misstrace.Entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(	name = "userproperty")
@Data
public class UserProperty {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)//id自增
    private Integer id;
//    绑定学号
    private String sid;
//    绑定商品
    private String gid;
//    拥有数量
    private Integer number;

}