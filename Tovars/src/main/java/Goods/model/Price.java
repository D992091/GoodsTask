package Goods.model;


import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Date;

@Entity

public class Price {
    @Id
    @JsonIgnore
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "good_id")
    @JsonProperty("name")
    private Goods goods;

    @JsonProperty("price")
    private double price;

    @Type(type = "date")
    @JsonIgnore
    private Date date;


    public Price(){

    }

    public Price(Integer id, double price, Date date, Goods goods){
        this.id = id;
        this.price = price;
        this.date = date;
        this.goods = goods;
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Goods getGoods() {
        return goods;
    }

    public void setGoods(Goods goods) {
        this.goods = goods;
    }
}
