package Goods.repositories;

import Goods.model.Goods;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GoodsRepo extends JpaRepository<Goods,Long> {
    List<Goods> findAllById(Integer id);
}
