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
    @ManyToOne
    @JoinColumn(name = "uid")
    private User user;
    @ManyToOne
    @JoinColumn(name = "gid")
    private Goods goods;
//    拥有数量
    private Integer number = 1;

}