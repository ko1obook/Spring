package service.mock;

import org.springframework.stereotype.Service;
import ru.education.entity.Product;
import ru.education.entity.SalesPeriod;
import ru.education.service.SalesPeriodService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Service
public class MockSalesPeriodService implements SalesPeriodService {
    @Override
    public List<SalesPeriod> findAll() { return new ArrayList<>(); }

    @Override
    public SalesPeriod findById(Object id) {
        SalesPeriod sp = new SalesPeriod();
        // Идентификаторо получаем в виде строки
        sp.setId(Long.valueOf((String) id));
        sp.setPrice(100L);
        sp.setDateFrom(new Date());
        sp.setDateTo(null);
        // Для поля product создаём тестовый продукт
        Product product = new Product();
        product.setId(1111);
        product.setName("testProduct");
        sp.setProduct(product);
        return sp;
    }

    @Override
    public SalesPeriod create(SalesPeriod salesPeriod) {
        return salesPeriod;
    }

    @Override
    public SalesPeriod update(SalesPeriod salesPeriod) {
        return salesPeriod;
    }

    @Override
    public void delete(Object Id) {
        // Ничего не делаем (mock-реализация)
    }
}
