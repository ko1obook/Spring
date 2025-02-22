package ru.education.service.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.education.annotation.Loggable;
import ru.education.entity.Product;
import ru.education.entity.SalesPeriod;
import ru.education.exceptions.EntityAlreadyExistsException;
import ru.education.exceptions.EntityHasDetailsException;
import ru.education.exceptions.EntityIllegalArgumentException;
import ru.education.exceptions.EntityNotFoundException;
import ru.education.jpa.ProductRepository;
import ru.education.jpa.SalesPeriodRepository;
import ru.education.service.ProductService;

import java.util.List;

@Service
public class DefaultProductService implements ProductService {

    private final ProductRepository productRepository;

    private final SalesPeriodRepository salesPeriodRepository;

    @Autowired
    public DefaultProductService(ProductRepository productRepository, SalesPeriodRepository salesPeriodRepository) {
        this.productRepository = productRepository;
        this.salesPeriodRepository = salesPeriodRepository;
    }

    @Loggable
    public List<Product> findAll() {
        return productRepository.findAll();
    }

    @Loggable
    public Product findById(Object id) {
        Product product;
        if (id == null) {
            throw new EntityIllegalArgumentException("Идентификатор объекта не может быть null");
        }
        Integer parsedId;
        try {
            parsedId = Integer.valueOf((String) id);
        } catch (NumberFormatException ex) {
            throw new EntityIllegalArgumentException(String.format("Не удалось преобразовать идентификатор" +
                    "к нужному типу, текст ошибки: %s", ex));
        }

        product = productRepository.findById(parsedId).orElse(null);

        if (product == null) {
            throw new EntityNotFoundException(Product.TYPE_NAME, parsedId);
        }
        return product;
    }

    @Loggable
    public Product create(Product product) {
        if (product == null) {
            throw new EntityIllegalArgumentException("Создаваемый объект не может быть null");
        }
        if (product.getId() == null) {
            throw new EntityIllegalArgumentException("Идентификатор объекта не может быть null");
        }
        Product existedProduct = productRepository.findById(product.getId()).orElse(null);
        if (existedProduct != null) {
            throw new EntityAlreadyExistsException(Product.TYPE_NAME, product.getId());
        }
        return productRepository.save(product);
    }

    @Loggable
    public Product update(Product product) {
        if (product == null) {
            throw new EntityIllegalArgumentException("Создаваемый объект не может быть null");
        }
        if (product.getId() == null) {
            throw new EntityIllegalArgumentException("Идентификатор объекта не может быть null");
        }
        Product existedProduct = productRepository.findById(product.getId()).orElse(null);
        if (existedProduct == null) {
            throw new EntityNotFoundException(Product.TYPE_NAME, product.getId());
        }
        // Проверяем открытые торговые периоды
        List<SalesPeriod> openSalesPeriods = salesPeriodRepository.findByDateToIsNullAndProductId(product.getId());
        if (!openSalesPeriods.isEmpty()) {
            throw new EntityHasDetailsException(String.format(
                    "Нельзя обновить продукт с id %d, так как у него есть открытые торговые периоды", product.getId()));
        }
        return productRepository.save(product);
    }

    @Loggable
    public void delete(Object id) {
        Product product = findById(id);
        List<SalesPeriod> salesPeriods = salesPeriodRepository.findByProduct(product);
        if (!salesPeriods.isEmpty()) {
            throw new EntityHasDetailsException(SalesPeriod.TYPE_NAME, product.getId());
        }
        productRepository.deleteById(product.getId());
    }
}
