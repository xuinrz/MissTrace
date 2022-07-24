package misstrace.Entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(	name = "userproperty")
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

    public Goods getGoods() {
        return goods;
    }

    public void setGoods(Goods goods) {
        this.goods = goods;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }
}