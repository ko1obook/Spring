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
import ru.education.exceptions.EntityIllegalArgumentException;
import ru.education.service.ProductService;

import java.util.List;

@RunWith( SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = TestConfig.class)
public class ProductServiceTest {

    @Autowired
    private ProductService productService;

    @Test
    public void findAllTest() {
        List<Product> products = productService.findAll();
        Assert.assertEquals(2, products.size());
    }

    @Test(expected = EntityIllegalArgumentException.class)
    public void createNullProductException() {
        productService.create(null);
    }
}
