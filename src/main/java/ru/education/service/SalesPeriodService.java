package ru.education.service;

import ru.education.entity.SalesPeriod;

import java.util.List;

public interface SalesPeriodService {

    List<SalesPeriod> findAll();

    SalesPeriod findById(Object Id);

    SalesPeriod create(SalesPeriod salesPeriod);

    SalesPeriod update(SalesPeriod salesPeriod);

    void delete(Object Id);
}
