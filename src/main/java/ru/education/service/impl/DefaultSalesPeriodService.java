package ru.education.service.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.education.entity.SalesPeriod;
import ru.education.exceptions.EntityAlreadyExistsException;
import ru.education.exceptions.EntityConflictException;
import ru.education.exceptions.EntityIllegalArgumentException;
import ru.education.exceptions.EntityNotFoundException;
import ru.education.jpa.ProductRepository;
import ru.education.jpa.SalesPeriodRepository;
import ru.education.service.SalesPeriodService;

import java.util.List;

@Service
public class DefaultSalesPeriodService implements SalesPeriodService {

    private final SalesPeriodRepository salesPeriodRepository;
    private final ProductRepository productRepository;

    @Autowired
    public DefaultSalesPeriodService(SalesPeriodRepository salesPeriodRepository, ProductRepository productRepository) {
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

    @Override
    public SalesPeriod update(SalesPeriod salesPeriod) {
        if (salesPeriod == null) {
            throw new EntityIllegalArgumentException("Обновляемый объект не может быть null");
        }
        if (salesPeriod.getId() == null) {
            throw new EntityIllegalArgumentException("Идентификатор объекта не может быть null");
        }

        SalesPeriod existingSalesPeriod = salesPeriodRepository.findById(salesPeriod.getId())
                .orElseThrow(() -> new EntityNotFoundException(SalesPeriod.TYPE_NAME, salesPeriod.getId()));

        // Обновляем поле product если оно передано
        if (salesPeriod.getProduct() != null) {
            if (salesPeriod.getProduct().getId() == null) {
                throw new EntityIllegalArgumentException("Идентификатор продукта не может быть null");
            }
            if (!productRepository.existsById(salesPeriod.getProduct().getId())) {
                throw new EntityIllegalArgumentException(
                        String.format("Продукт с идентификатором %d не существует", salesPeriod.getProduct().getId())
                );
            }
            existingSalesPeriod.setProduct(salesPeriod.getProduct());
        }

        // Обновляем DateFrom, если предана новая дата
        if (salesPeriod.getDateFrom() != null) {
            existingSalesPeriod.setDateFrom(salesPeriod.getDateFrom());
        }

        // Обновляем DateTo, если передана новая дата
        if (salesPeriod.getDateTo() != null) {
            existingSalesPeriod.setDateTo(salesPeriod.getDateTo());
        }

        // Обновляем Price, если цена изменилась
        if (salesPeriod.getPrice() != null) {
            existingSalesPeriod.setPrice(salesPeriod.getPrice());
        }

        return salesPeriodRepository.save(existingSalesPeriod);
    }

    public void delete(Object id) {
        SalesPeriod salesPeriod = findById(id);
        salesPeriodRepository.deleteById(salesPeriod.getId());
    }
}
