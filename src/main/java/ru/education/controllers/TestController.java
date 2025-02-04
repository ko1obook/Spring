package ru.education.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;
import ru.education.entity.SalesPeriodJdbcDemo;
import ru.education.entity.SalesPeriodJpaDemo;
import ru.education.jdbc.SalesPeriodJdbcRepository;
import ru.education.jpa.Product;
import ru.education.jpa.ProductRepository;
import ru.education.jpa.SalesPeriodRepository;
import ru.education.model.Formatter;

import java.util.List;

@RestController
@RequestMapping("api/v1")
public class TestController {

    private final Formatter formatter;

    private final ProductRepository productRepository;

    private final SalesPeriodJdbcRepository salesPeriodJdbcRepository;

    private SalesPeriodRepository salesPeriodRepository;

    @Autowired
    public TestController(@Qualifier("fooFormatter") Formatter formatter, ProductRepository productRepository, SalesPeriodJdbcRepository salesPeriodJdbcRepository, SalesPeriodRepository salesPeriodRepository) {
        this.formatter = formatter;
        this.productRepository = productRepository;
        this.salesPeriodJdbcRepository = salesPeriodJdbcRepository;
        this.salesPeriodRepository = salesPeriodRepository;
    }

    @GetMapping("/hello")
    public String getHello(){
        return "Hello, World!";
    }

    @GetMapping("/format")
    public String getFormat(){
        return formatter.format();
    }

    @GetMapping("/products")
    public List<Product> getProducts() {
        return productRepository.findAll();
    }

    @GetMapping("sales/count")
    public Integer getSalesCount() {
        return salesPeriodJdbcRepository.count() ;
    }

    @GetMapping("/sales")
    public List<SalesPeriodJdbcDemo> getSalesPeriods() {
        return salesPeriodJdbcRepository.getSalesPeriods();
    }

    @GetMapping("/sales/byhigherprice")
    public List<SalesPeriodJdbcDemo> getSalesPeriodsByHigherPrice() {
        return salesPeriodJdbcRepository.getSalesPeriodsPriceIsHigher(90);}

    @GetMapping("/products/sales/active")
    public List<Product> getProductsWithActivePeriod() {
        return salesPeriodJdbcRepository.getProductsWithActivePeriod();}

    @GetMapping("/sales/jpa")
    public List<SalesPeriodJpaDemo> getSalesPeriodsJpa() {return salesPeriodRepository.findAll();}

    @PostMapping("/sales/jpa")
    public SalesPeriodJpaDemo addSalesPeriodsJpa(@RequestBody SalesPeriodJpaDemo salesPeriodJpaDemo) {
        return salesPeriodRepository.save(salesPeriodJpaDemo);}

    @GetMapping("/sales/jpa/max/price")
    public Integer getMaxPriceByProductId() {return salesPeriodRepository.getMaxPriceByProductId(1);}

    @GetMapping("/sales/jpa/exists/price")
    public boolean existsByPrice() {return salesPeriodRepository.existsByPrice(60);}

    @GetMapping("/sales/jpa/active")
    public List<SalesPeriodJpaDemo> findByDateToIsNull() {return salesPeriodRepository.findByDateToIsNull();}

    @GetMapping("/sales/jpa/byproductname")
    public List<SalesPeriodJpaDemo> findByProductName() {return salesPeriodRepository.findByProductName("bycicle");}
}
