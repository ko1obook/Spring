package ru.education.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.education.entity.SalesPeriod;
import ru.education.exceptions.EntityAlreadyExistsException;
import ru.education.exceptions.EntityConflictException;
import ru.education.exceptions.EntityIllegalArgumentException;
import ru.education.exceptions.EntityNotFoundException;
import ru.education.jpa.ProductRepository;
import ru.education.jpa.SalesPeriodRepository;

import java.util.List;

@Service
public class SalesPeriodService {

    private final SalesPeriodRepository salesPeriodRepository;
    private final ProductRepository productRepository;

    @Autowired
    public SalesPeriodService(SalesPeriodRepository salesPeriodRepository, ProductRepository productRepository) {
        this.salesPeriodRepository = salesPeriodRepository;
        this.productRepository = productRepository;
    }

    public List<SalesPeriod> findAll() {
        return salesPeriodRepository.findAll();
    }

    public SalesPeriod findById(Object id) {
        SalesPeriod salesPeriod;
        if (id == null) {
            throw new EntityIllegalArgumentException("Идентификатор объекта не может быть null");
        }
        Long parsedId;
        try {
            parsedId = Long.valueOf((String) id);
        } catch (NumberFormatException ex) {
            throw new EntityIllegalArgumentException(String.format("Не удалось преобразовать идентификатор" +
                    "к нужному типу, текст ошибки: %s", ex));
        }

        salesPeriod = salesPeriodRepository.findById(parsedId).orElse(null);

        if (salesPeriod == null) {
            throw new EntityNotFoundException(SalesPeriod.TYPE_NAME, parsedId);
        }
        return salesPeriod;
    }

    public SalesPeriod create(SalesPeriod salesPeriod) {
        if (salesPeriod == null) {
            throw new EntityIllegalArgumentException("Создаваемый объект не может быть null");
        }
        if (salesPeriod.getId() == null) {
            throw new EntityIllegalArgumentException("Идентификатор объекта не может быть null");
        }
        if (salesPeriod.getProduct() == null) {
            throw new EntityIllegalArgumentException("Продукт не может быть null");
        }
        if (salesPeriod.getProduct().getId() == null) {
            throw new EntityIllegalArgumentException("Идентификатор продукта не может быть null");
        }
        Integer productId = salesPeriod.getProduct().getId();
        if (!productRepository.existsById(productId)) {
            throw new EntityIllegalArgumentException(String.format("Продукта с идентификатором %d не существует", productId));
        }

        if (salesPeriod.getDateFrom() == null) {
            throw new EntityIllegalArgumentException("Дата начала периода не может быть null");
        }
        SalesPeriod existedSalesPeriod = salesPeriodRepository.findById(salesPeriod.getId()).orElse(null);
        if (existedSalesPeriod != null) {
            throw new EntityAlreadyExistsException(SalesPeriod.TYPE_NAME, existedSalesPeriod.getId());
        }
        List<SalesPeriod> lastSalesPeriod = salesPeriodRepository.findByDateToIsNullAndProductId(salesPeriod.getProduct().getId());
        if (lastSalesPeriod.size() > 0) {
            throw new EntityConflictException(String.format("В системе имются открытые торговые периоды для продукта с id %s", salesPeriod.getProduct().getId()));
        }
        return salesPeriodRepository.save(salesPeriod);
    }

    public void delete(Object id) {
        SalesPeriod salesPeriod = findById(id);
        salesPeriodRepository.deleteById(salesPeriod.getId());
    }
}
