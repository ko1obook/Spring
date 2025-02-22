package service;

import config.TestConfig;
import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import ru.education.entity.Product;
import ru.education.entity.SalesPeriod;
import ru.education.exceptions.EntityAlreadyExistsException;
import ru.education.exceptions.EntityHasDetailsException;
import ru.education.exceptions.EntityIllegalArgumentException;
import ru.education.exceptions.EntityNotFoundException;
import ru.education.jpa.ProductRepository;
import ru.education.jpa.SalesPeriodRepository;
import ru.education.service.impl.DefaultProductService;
import ru.education.service.impl.DefaultSalesPeriodService;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;


@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = TestConfig.class)
@Transactional
public class DefaultProductServiceTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private DefaultProductService defaultProductService;

    @Autowired
    private DefaultSalesPeriodService defaultSalesPeriodService;

    @Autowired
    private SalesPeriodRepository salesPeriodRepository;

    @Test
    public void findAllTest() {
        List<Product> products = defaultProductService.findAll();
        Assert.assertEquals(2, products.size());
    }

    @Test
    public void findByIdValidTest() {
        Product product = defaultProductService.findById("1");
        Assert.assertNotNull(product);
        Assert.assertEquals(Integer.valueOf(1), product.getId());
    }

    @Test(expected = EntityIllegalArgumentException.class)
    public void findByIdNullTest() {
        defaultProductService.findById(null);
    }

    @Test(expected = EntityIllegalArgumentException.class)
    public void findByNonNumericTest() {
        defaultProductService.findById("abc");
    }

    @Test(expected = EntityNotFoundException.class)
    public void findByIdNotFoundTest() {
        defaultProductService.findById("9999");
    }

    @Test(expected = EntityIllegalArgumentException.class)
    public void createNullProductException() {
        defaultProductService.create(null);
    }

    @Test(expected = EntityIllegalArgumentException.class)
    public void createProductWithNullIdException() {
        Product product = new Product();
        product.setId(null);
        defaultProductService.create(product);
    }

    @Test
    public void createNewProductTest() {
        Product product = new Product();
        product.setId(8888);
        product.setName("new_airplane");
        Product saved = defaultProductService.create(product);
        Assert.assertNotNull(saved);
        Assert.assertEquals("new_airplane", saved.getName());
    }

    @Test(expected = EntityAlreadyExistsException.class)
    public void createProductAlreadyExistsExceptionTest() {
        Product existing = defaultProductService.findById("1");
        Product duplicate = new Product();
        duplicate.setId(existing.getId());
        duplicate.setName("Duplicate");
        defaultProductService.create(duplicate);
    }

    @Test
    public void updateTest() {
        // Продукт с id = 1 уже существует в тестовой БД
        Product product = defaultProductService.findById("1");
        String originalName = product.getName();

        // Изменяем имя продукта
        product.setName("Updated_transport");
        Product updated = defaultProductService.update(product);

        Assert.assertNotNull(updated);
        Assert.assertEquals("Updated_transport", updated.getName());

        // Возвращаем имя продукта к исходному
        product.setName(originalName);
        defaultProductService.update(product);
    }

    @Test(expected = EntityIllegalArgumentException.class)
    public void updateNullProductTest() {
        defaultProductService.update(null);
    }

    @Test(expected = EntityHasDetailsException.class)
    public void updateProductWithOpenSalesPeriod() {
        Product product = defaultProductService.findById("2");
        defaultProductService.update(product);
    }

    @Test(expected = EntityNotFoundException.class)
    public void updateNonExistentProduct() {
        Product product = defaultProductService.findById("33");
        defaultProductService.update(product);
    }

    @Test
    public void deleteProductWithoutSalesPeriodsTest() {
        Product product = new Product();
        product.setId(3333);
        product.setName("del_boat");
        productRepository.save(product);
        defaultProductService.delete("3333");
        try {
            defaultProductService.findById("3333");
            Assert.fail("Ожидалось исключение EntityNotFoundException");
        } catch (EntityNotFoundException ex) {
            // исключение ожидается
        }
    }

    @Test(expected = EntityHasDetailsException.class)
    public void deleteProductWithOpenSalesPeriodTest() {
        defaultProductService.delete("2");
    }

    @Test(expected = EntityIllegalArgumentException.class)
    public void deleteProductWithIllegalArgumentTest() {
        defaultProductService.delete("abc");
    }

    @Test(expected = EntityIllegalArgumentException.class)
    public void deleteProductWithNullArgumentTest() {
        defaultProductService.delete(null);
    }

    @Test(expected = EntityNotFoundException.class)
    public void deleteNotFoundProductTest() {
        defaultProductService.delete("33");
    }
}











