package misstrace.Entity;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(	name = "goods")
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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public Integer getCost() {
        return cost;
    }

    public void setCost(Integer cost) {
        this.cost = cost;
    }

    public Boolean getOnSale() {
        return onSale;
    }

    public void setOnSale(Boolean onSale) {
        this.onSale = onSale;
    }

    public List<UserProperty> getUserProperties() {
        return userProperties;
    }

    public void setUserProperties(List<UserProperty> userProperties) {
        this.userProperties = userProperties;
    }
}
