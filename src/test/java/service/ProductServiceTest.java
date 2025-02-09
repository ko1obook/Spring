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
import ru.education.exceptions.EntityIllegalArgumentException;
import ru.education.exceptions.EntityNotFoundException;
import ru.education.jpa.ProductRepository;
import ru.education.jpa.SalesPeriodRepository;
import ru.education.service.ProductService;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

@RunWith( SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = TestConfig.class)
@Transactional
public class ProductServiceTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductService productService;

    @Autowired
    private SalesPeriodRepository salesPeriodRepository;

    @Test
    public void findAllTest() {
        List<Product> products = productService.findAll();
        Assert.assertEquals(2, products.size());
    }

    @Test
    public void findByIdValidTest() {
        Product product = productService.findById("1");
        Assert.assertNotNull(product);
        Assert.assertEquals(Integer.valueOf(1), product.getId());
    }

    @Test(expected = EntityIllegalArgumentException.class)
    public void findByIdNullTest() {
        productService.findById(null);
    }

    @Test(expected = EntityIllegalArgumentException.class)
    public void findByNonNumericTest() {
        productService.findById("abc");
    }

    @Test(expected = EntityNotFoundException.class)
    public void findByIdNotFoundTest() {
        productService.findById("9999");
    }

    @Test(expected = EntityIllegalArgumentException.class)
    public void createNullProductException() {
        productService.create(null);
    }

    @Test(expected = EntityIllegalArgumentException.class)
    public void createProductWithNullIdException() {
        Product product = new Product();
        product.setId(null);
        productService.create(product);
    }

    @Test
    public void createNewProductTest() {
        Product product = new Product();
        product.setId(8888);
        product.setName("new_airplane");
        Product saved = productService.create(product);
        Assert.assertNotNull(saved);
        Assert.assertEquals("new_airplane", saved.getName());
    }

    @Test(expected = EntityAlreadyExistsException.class)
    public void createProductAlreadyExistsExceptionTest() {
        Product existing = productService.findById("1");
        Product duplicate = new Product();
        duplicate.setId(existing.getId());
        duplicate.setName("Duplicate");
        productService.create(duplicate);
    }

    @Test
    public void deleteProductWithoutSalesPeriodsTest() {
        Product product = new Product();
        product.setId(3333);
        product.setName("del_boat");
        productRepository.save(product);
        productService.delete("3333");
        try {
            productService.findById("3333");
            Assert.fail("Ожидалось исключение EntityNotFoundException");
        } catch (EntityNotFoundException ex) {
            // исключение ожидается
        }
    }


    public void deleteProductWithSalesPeriodTest() {
        Product product = new Product();
        product.setId(4444);
        product.setName("trade_rocket");
        productRepository.save(product);

        SalesPeriod sp = new SalesPeriod();
        sp.setId(100L);
        sp.setProduct(product);
        sp.setDateFrom(new Date());
        // Оставляем sp.dateTo == null (открытый период)
        salesPeriodRepository.save(sp);

        // При попытке удалить продукт должно быть выброшено исключение
        productService.delete("4444");
    }
}









