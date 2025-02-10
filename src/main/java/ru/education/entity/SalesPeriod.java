package ru.education.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "sales_period")
@NoArgsConstructor
@Getter
@Setter
public class SalesPeriod {

    public static String TYPE_NAME = "Торговый период";

    @Getter
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sales_periods_id_seq")
    @SequenceGenerator(name = "sales_periods_id_seq", sequenceName = "sales_periods_id_seq", allocationSize = 1)
    private Long id;

    @Column(name = "price")
    private Long price;

    @Getter
    @Setter
    @Column(name = "date_from")
    private Date dateFrom;

    @Getter
    @Setter
    @Column(name = "date_to")
    private Date dateTo;

    @Getter
    @Setter
    @OneToOne
    @JoinColumn(name = "product", referencedColumnName = "id", nullable = false)
    private Product product;

    public void setId(Long id) {
        this.id = id;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

}
