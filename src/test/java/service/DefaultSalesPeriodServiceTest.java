package service;

import config.TestConfig;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import ru.education.entity.Product;
import ru.education.entity.SalesPeriod;
import ru.education.exceptions.EntityAlreadyExistsException;
import ru.education.exceptions.EntityConflictException;
import ru.education.exceptions.EntityIllegalArgumentException;
import ru.education.exceptions.EntityNotFoundException;
import ru.education.jpa.ProductRepository;
import ru.education.jpa.SalesPeriodRepository;
import ru.education.service.impl.DefaultSalesPeriodService;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = TestConfig.class)
@Transactional
public class DefaultSalesPeriodServiceTest {

    @Autowired
    private DefaultSalesPeriodService defaultSalesPeriodService;

    private DefaultSalesPeriodService defaultProductService;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private SalesPeriodRepository salesPeriodRepository;

    @Test
    public void findAllTest() {
        // Если в тестовой БД торговых периодов нет, список может быть пустым.
        List<SalesPeriod> salesPeriods = defaultSalesPeriodService.findAll();
        Assert.assertNotNull(salesPeriods);
    }

    @Test
    public void findByValidTest() {
        SalesPeriod found = defaultSalesPeriodService.findById("5");
        Assert.assertNotNull(found);
        Assert.assertEquals(Long.valueOf(5), found.getId());
    }

    @Test(expected = EntityIllegalArgumentException.class)
    public void findByIdNullTest() {
        defaultSalesPeriodService.findById(null);
    }

    @Test(expected = EntityIllegalArgumentException.class)
    public void findByIdNotNumericTest() {
        defaultSalesPeriodService.findById("abc");
    }

    @Test(expected = EntityNotFoundException.class)
    public void findByIdNotFoundTest() {
        defaultSalesPeriodService.findById("6666");
    }

    @Test(expected = EntityIllegalArgumentException.class)
    public void createNullSalesPeriodException() {
        defaultSalesPeriodService.create(null);
    }

    @Test(expected = EntityIllegalArgumentException.class)
    public void createSalesPeriodWithNullIdException() {
        SalesPeriod sp = new SalesPeriod();
        sp.setId(null);
        Product product = new Product();
        product.setId(1);

        sp.setProduct(product);
        sp.setDateFrom(new Date());

        defaultSalesPeriodService.create(sp);
    }

    @Test(expected = EntityIllegalArgumentException.class)
    public void createSalesPeriodWithNullProductException() {
        SalesPeriod sp = new SalesPeriod();
        sp.setId(300L);
        sp.setProduct(null);
        defaultSalesPeriodService.create(sp);
    }

    @Test(expected = EntityIllegalArgumentException.class)
    public void createSalesPeriodWithProductNullIdException() {
        SalesPeriod sp = new SalesPeriod();
        sp.setId(301L);
        Product product = new Product();
        product.setId(null);
        sp.setProduct(product);
        defaultSalesPeriodService.create(sp);
    }

    @Test(expected = EntityIllegalArgumentException.class)
    public void createSalesPeriodWithNonExistingProductException() {
        SalesPeriod sp = new SalesPeriod();
        Product nonExistingProduct = new Product();
        nonExistingProduct.setId(5555); // Просто задаём ID, но в БД такого нет
        sp.setProduct(nonExistingProduct);
        sp.setDateFrom(new Date());
        defaultSalesPeriodService.create(sp);
    }

    @Test(expected = EntityIllegalArgumentException.class)
    public void createSalesPeriodWithNullDateFromException() {
        Product product = productRepository.findById(1).orElse(null);
        Assert.assertNotNull("Продукт с id=1 должен существовать", product);
        SalesPeriod sp = new SalesPeriod();
        sp.setId(303L);
        sp.setProduct(product);
        sp.setDateFrom(null);
        defaultSalesPeriodService.create(sp);
    }

    @Test(expected = EntityAlreadyExistsException.class)
    public void createAlreadyExistsSalesPeriod() {
        Product product = productRepository.findById(1).orElse(null);
        SalesPeriod sp = new SalesPeriod();
        sp.setId(1L);
        sp.setProduct(product);
        sp.setDateFrom(new Date());
        defaultSalesPeriodService.create(sp);
    }

    @Test(expected = EntityConflictException.class)
    public void createSalesPeriodTwice() {
        Product product = productRepository.findById(2).orElse(null);
        SalesPeriod sp = new SalesPeriod();
        sp.setId(111L);
        sp.setProduct(product);
        sp.setDateFrom(new Date());
        defaultSalesPeriodService.create(sp);
    }

    @Test
    public void updateValidTest() {
        SalesPeriod sp = defaultSalesPeriodService.findById("5");
        sp.setPrice(370L);
        sp.setDateTo(new Date());
        defaultSalesPeriodService.update(sp);
    }

    @Test(expected = EntityConflictException.class)
    public void updateSalesPeriodForProductWithAlreadyOpenSP() {
        // Загружаем закрытый торговый период для продукта с другим открытым торговым периодом (id=5)
        SalesPeriod sp = defaultSalesPeriodService.findById("4");
        // Открываем торговый период
        sp.setDateTo(null);
        defaultSalesPeriodService.update(sp);
    }

    @Test(expected = EntityIllegalArgumentException.class)
    public void updateSalesPeriodNullTest() {
        defaultSalesPeriodService.update(null);
    }

    @Test(expected = EntityIllegalArgumentException.class)
    public void updateSalesPeriodWithNullTest() {
        SalesPeriod sp = new SalesPeriod();
        sp.setId(null);
        defaultSalesPeriodService.update(sp);
    }

    @Test(expected = EntityNotFoundException.class)
    public void updateNonExisingSalesPeriodTest() {
        SalesPeriod sp = new SalesPeriod();
        sp.setId(1111L); // Торговый период с таким Id отсутствует
        defaultSalesPeriodService.update(sp);
    }

    @Test
    public void deleteValidTest() {
        defaultSalesPeriodService.delete("4");  // Торговый период с таким Id существует
    }

    @Test(expected = EntityIllegalArgumentException.class)
    public void deleteNullSalesPeriod() {
        defaultSalesPeriodService.delete(null);
    }

    @Test(expected = EntityNotFoundException.class)
    public void deleteNotFoundSalesPeriod() {
        defaultSalesPeriodService.delete("37"); // Торговый период с таким Id отсутствует
    }
}




