package ru.education.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.education.entity.SalesPeriod;
import ru.education.service.SalesPeriodService;

import java.util.List;

@RestController
@RequestMapping("api/v1/sales_period")
public class SalesPeriodController {

    public final SalesPeriodService salesPeriodService;

    @Autowired
    public SalesPeriodController(SalesPeriodService salesPeriodService) {
        this.salesPeriodService = salesPeriodService;
    }

    // Получение списка торговых периодов
    @GetMapping
    @PreAuthorize("hasPermission('sales_period', 'read')")
    public List<SalesPeriod> findAll() {
        return salesPeriodService.findAll();
    }

    // Получение торгового периода по идентификатору
    @GetMapping("/{id}")
    @PreAuthorize("hasPermission('sales_period', 'readById')")
    public SalesPeriod findBy(@PathVariable String id) {
        return salesPeriodService.findById(id);
    }

    // Создание нового торгового периода
    @PostMapping
    @PreAuthorize("hasPermission('sales_period', 'create')")
    @ResponseStatus(HttpStatus.CREATED)
    public SalesPeriod create(@RequestBody SalesPeriod salesPeriod) {
        return salesPeriodService.create(salesPeriod);
    }

    @PutMapping
    @PreAuthorize("hasPermission('sales_period', 'update')")
    @ResponseStatus(HttpStatus.OK)
    public SalesPeriod update(@RequestBody SalesPeriod salesPeriod) {
        return salesPeriodService.update(salesPeriod);
    }

    // Удаление торгового периода
    @DeleteMapping("/{id}")
    @PreAuthorize("hasPermission('sales_period', 'delete')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String id) {
        salesPeriodService.delete(id);
    }
}
