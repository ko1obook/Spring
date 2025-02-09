package ru.education.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.education.entity.Product;
import ru.education.service.ProductService;

import java.util.List;

@RestController
@RequestMapping("api/v1/product")
public class ProductController {

    public final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public List<Product> findAll() {
        return productService.findAll();
    }

    @GetMapping("/{id}")
    public Product findById(@PathVariable String id) {
        return productService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Product create(@RequestBody Product product) {
        return productService.create(product);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public Product update(@RequestBody Product product) {
        return productService.update(product);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String id) {
        productService.delete(id);
    }
}
