package Goods.json;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import java.io.Serializable;
import java.util.Date;

public class DataChanges implements Serializable {
    @JsonProperty("date")
    private Date date;
    @JsonProperty("frequency")
    private Long frequency;

    DataChanges() {
    }

    public  DataChanges(Date date, Long frequency){
        this.date = date;
        this.frequency = frequency;
    }

    public Long getFrequency() {
        return frequency;
    }

    public void setFrequency(Long frequency) {
        this.frequency = frequency;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}

