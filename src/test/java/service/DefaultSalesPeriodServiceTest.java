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


    public void findByValidTest() {
        // Сначала создаём торговый период для существующего продукта.
        Product product = productRepository.findById(1).orElse(null);
        Assert.assertNotNull("Продукт с id=1 должен существовать", product);

        SalesPeriod sp = new SalesPeriod();
        sp.setId(200L);
        sp.setProduct(product);
        sp.setDateFrom(new Date());
        salesPeriodRepository.save(sp);

        SalesPeriod found = defaultSalesPeriodService.findById("200");
        Assert.assertNotNull(found);
        Assert.assertEquals(Long.valueOf(200), found.getId());
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
        sp.setId(302L);
        Product product = new Product();
        product.setId(5555); // Данный продукт отсутствует
        sp.setProduct(product);
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
    public void createSalesPeriodAlreadyExistsException() {
        Product product = productRepository.findById(1).orElse(null);
        Assert.assertNotNull(product);
        SalesPeriod sp = new SalesPeriod();
        sp.setId(304L);
        sp.setProduct(product);
        sp.setDateFrom(new Date());
        salesPeriodRepository.save(sp);
        // Попытка создать ещё один торговый период с тем же id должна привести к исключению.
        SalesPeriod spDuplicate = new SalesPeriod();
        spDuplicate.setId(304L);
        spDuplicate.setProduct(product);
        spDuplicate.setDateFrom(new Date());
        defaultSalesPeriodService.create(spDuplicate);
    }

    @Test(expected = EntityConflictException.class)
    public void createSalesPeriodOpenConflictException() {
        // Для продукта создаём открытый торговый период (dateTo == null).
        Product product = productRepository.findById(1).orElse(null);
        Assert.assertNotNull(product);
        SalesPeriod spOpen = new SalesPeriod();
        spOpen.setId(305L);
        spOpen.setProduct((product));
        spOpen.setDateFrom(java.sql.Date.valueOf(LocalDate.now().minusDays(10)));
        spOpen.setDateTo(null);
        salesPeriodRepository.save(spOpen);
        // Попытка создать новый торговый период для того же продукта приводит к конфликту.
        SalesPeriod spNew = new SalesPeriod();
        spNew.setId(306L);
        spNew.setProduct(product);
        spNew.setDateFrom(new Date());
        spNew.setDateTo(java.sql.Date.valueOf(LocalDate.now().plusDays(10)));
        defaultSalesPeriodService.create(spNew);
    }

    @Test
    public void createValidSalesPeriodTest() {
        // Создаём корректный торговый период
        Product product = productRepository.findById(1).orElse(null);
        Assert.assertNotNull(product);

        // Если ранее для продукта был создан открытый период, его можно закрыть.
        List<SalesPeriod> openPeriods = salesPeriodRepository.findByDateToIsNullAndProductId(product.getId());
        for (SalesPeriod sp : openPeriods) {
            sp.setDateTo(new Date());
            salesPeriodRepository.save(sp);
        }

        SalesPeriod sp = new SalesPeriod();
        sp.setId(307L);
        sp.setProduct(product);
        sp.setDateFrom(new Date());
        sp.setDateTo(java.sql.Date.valueOf(LocalDate.now().plusDays(10)));
        SalesPeriod created = defaultSalesPeriodService.create(sp);
        Assert.assertNotNull(created);
        Assert.assertEquals(Long.valueOf(307), created.getId());
    }

    @Test
    public void deleteSalesPeriod() {
        // Создаём торговый период, а затем удаляем его
        Product product = productRepository.findById(1).orElse(null);
        Assert.assertNotNull(product);

        SalesPeriod sp = new SalesPeriod();
        sp.setId(308L);
        sp.setProduct(product);
        sp.setDateFrom(new Date());
        salesPeriodRepository.save(sp);

        defaultSalesPeriodService.delete("308");

        try {
            defaultSalesPeriodService.findById("308");
            Assert.fail("Ожидалось исключение EntityNotFoundException");
        } catch (EntityNotFoundException ex) {
            // исключение ожидается
        }
    }
}



