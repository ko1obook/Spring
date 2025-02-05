package ru.education.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.education.entity.Product;
import ru.education.entity.SalesPeriod;

import java.util.List;

@Repository
public interface SalesPeriodRepository extends JpaRepository<SalesPeriod, Long> {

    @Query(value = "select max(price) from sales_period where product = :productId", nativeQuery = true)
    Integer getMaxPriceByProductId(@Param("productId") long productId);

    boolean existsByPrice(long price);

    List<SalesPeriod> findByDateToIsNull();

    List<SalesPeriod> findByProductName(String productName);

    List<SalesPeriod> findByProduct(Product product);

    List<SalesPeriod> findByDateToIsNullAndProductId(Integer productId);
}
