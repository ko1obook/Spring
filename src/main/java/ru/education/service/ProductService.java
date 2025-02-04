package ru.education.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.education.jpa.ProductRepository;
import ru.education.jpa.SalesPeriodRepository;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    private final SalesPeriodRepository salesPeriodRepository;

    public ProductService(ProductRepository productRepository, SalesPeriodRepository salesPeriodRepository) {
        this.productRepository = productRepository;
        this.salesPeriodRepository = salesPeriodRepository;
    }

    
}
