package ru.education.jdbc;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.education.entity.SalesPeriodJdbcDemo;
import ru.education.entity.Product;

import java.util.List;

@Repository
public class SalesPeriodJdbcRepository {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public SalesPeriodJdbcRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public int count() {
        return jdbcTemplate.queryForObject("select count(*) from public.sales_period", Integer.class);
    }

    public List<SalesPeriodJdbcDemo> getSalesPeriods() {
        return jdbcTemplate.query("select * from public.sales_period", (rs, rowNum) ->
                new SalesPeriodJdbcDemo(
                        rs.getLong("id"),
                        rs.getInt("price"),
                        rs.getDate("date_from"),
                        rs.getDate("date_to"),
                        rs.getInt("product")
                ));
    }

    public List<SalesPeriodJdbcDemo> getSalesPeriodsPriceIsHigher(long price) {
        return jdbcTemplate.query(String.format("select * from public.sales_period where price >= %s", price),
                (rs, rowNum) ->
                        new SalesPeriodJdbcDemo(
                                rs.getLong("id"),
                                rs.getInt("price"),
                                rs.getDate("date_from"),
                                rs.getDate("date_to"),
                                rs.getInt("product")
                        ));
    }

    public List<Product> getProductsWithActivePeriod() {
        return jdbcTemplate.query("select p.id product_id, p.name product_name from public.product p inner join " +
                "public.sales_period sp on p.id = sp.product where sp.date_to is null",
                (rs, rowNum) ->
                        new Product(
                                rs.getInt("product_id"),
                                rs.getString("product_name")
                        ));
    }
}
