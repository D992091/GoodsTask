package Goods.json;

import com.fasterxml.jackson.annotation.*;

import java.io.Serializable;
import java.util.List;


public class Statistic implements Serializable {
    @JsonProperty
    private Long count;

    @JsonProperty
    private List<GoodChanges> goodChanges;

    @JsonProperty
    private List<DataChanges> dataChanges;

    private Statistic(){

    }

    public Statistic(Long count, List<GoodChanges> goodChanges, List<DataChanges> dataChanges){
            this.count = count;
            this.goodChanges = goodChanges;
            this.dataChanges = dataChanges;
    }



    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public List<GoodChanges> getGoodChanges() {
        return goodChanges;
    }

    public void setGoodChanges(List<GoodChanges> goodChanges) {
        this.goodChanges = goodChanges;
    }

    public List<DataChanges> getDataChanges() {
        return dataChanges;
    }

    public void setDataChanges(List<DataChanges> dataChanges) {
        this.dataChanges = dataChanges;
    }
}
