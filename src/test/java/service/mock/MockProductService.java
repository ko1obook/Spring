package service.mock;

import org.springframework.stereotype.Service;
import ru.education.entity.Product;
import ru.education.service.ProductService;

import java.util.ArrayList;
import java.util.List;

@Service
public class MockProductService implements ProductService {
    @Override
    public List<Product> findAll() {
        return new ArrayList<>();
    }

    @Override
    public Product findById(Object id) {

        return new Product(Integer.valueOf((String) id), "testProduct");
    }

    @Override
    public Product create(Product product) {
        return product;
    }

    @Override
    public Product update(Product product) {
        return product;
    }

    @Override
    public void delete(Object id) {

    }
}
