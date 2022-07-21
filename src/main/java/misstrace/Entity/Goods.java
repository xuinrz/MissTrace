package misstrace.Entity;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(	name = "goods")
@Data
public class Goods {
    @Id
    private Integer id;
    private String description;
    private String img;
//    商品积分价值
    private Integer cost;
    private Boolean onSale = true;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "user")
    private List<UserProperty> userProperties;
}
