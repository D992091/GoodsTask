package Goods.repositories;

import Goods.model.Price;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

public interface PriceRepo extends JpaRepository<Price, Double> {
    List<Price> findAllByDate(Date date);
    List<Price> findAllById(Integer id);

    @Query(value = "select g.name, count(g.name) frequency from price p join goods g on p.good_id = g.id group by p.good_id order by p.good_id" ,nativeQuery = true)
    List<Object[]> getGoodChanges();

    @Query(value = "select date, count(date) frequency from price group by date order by date" ,nativeQuery = true)
    List<Object[]> getDataChanges();
}

