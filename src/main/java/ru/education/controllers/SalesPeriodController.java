package ru.education.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.education.entity.SalesPeriod;
import ru.education.service.impl.DefaultSalesPeriodService;

import java.util.List;

@RestController
@RequestMapping("api/v1/sales_period")
public class SalesPeriodController {

    public final DefaultSalesPeriodService defaultSalesPeriodService;

    @Autowired
    public SalesPeriodController(DefaultSalesPeriodService defaultSalesPeriodService) {
        this.defaultSalesPeriodService = defaultSalesPeriodService;
    }

    // Получение списка торговых периодов
    @GetMapping
    public List<SalesPeriod> findAll() {
        return defaultSalesPeriodService.findAll();
    }

    // Получение торгового периода по идентификатору
    @GetMapping("/{id}")
    public SalesPeriod findBy(@PathVariable String id) {
        return defaultSalesPeriodService.findById(id);
    }

    // Создание нового торгового периода
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SalesPeriod create(@RequestBody SalesPeriod salesPeriod) {
        return defaultSalesPeriodService.create(salesPeriod);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public SalesPeriod update(@RequestBody SalesPeriod salesPeriod) {
        return defaultSalesPeriodService.update(salesPeriod);
    }

    // Удаление торгового периода
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String id) {
        defaultSalesPeriodService.delete(id);
    }
}








