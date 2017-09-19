package ua.nure.romanenko.st4.dto;

import ua.nure.romanenko.st4.annotation.Column;
import ua.nure.romanenko.st4.annotation.Id;
import ua.nure.romanenko.st4.annotation.Table;

import java.util.Date;

/**
 * Created by denis on 11.09.17.
 */
@Table("orders")
public class Orders extends Dto {
    @Id
    @Column("id")
    private Integer id;
    @Column("accountid")
    private Integer accountId;
    @Column("apartmentid")
    private Integer apartmentId;
    @Column("status")
    private String status;
    @Column("person_count")
    private Integer personCount;
    @Column("apartment_type")
    private String apartmentType;
    @Column("\"from\"")
    private Date from;
    @Column("\"to\"")
    private Date to;
    @Column("create_date")
    private Date create_date;

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getAccountId() {
        return accountId;
    }

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }

    public Integer getApartmentId() {
        return apartmentId;
    }

    public void setApartmentId(Integer apartmentId) {
        this.apartmentId = apartmentId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getPersonCount() {
        return personCount;
    }

    public void setPersonCount(Integer personCount) {
        this.personCount = personCount;
    }

    public String getApartmentType() {
        return apartmentType;
    }

    public void setApartmentType(String apartmentType) {
        this.apartmentType = apartmentType;
    }

    public Date getFrom() {
        return from;
    }

    public void setFrom(Date from) {
        this.from = from;
    }

    public Date getTo() {
        return to;
    }

    public void setTo(Date to) {
        this.to = to;
    }

    public Date getCreate_date() {
        return create_date;
    }

    public void setCreate_date(Date create_date) {
        this.create_date = create_date;
    }

    public static class Status {
        public final static String UNCHECKED = "UNCHECKED";
        public final static String WAITED = "WAITED";
        public final static String PAID = "PAID";
        public final static String IN_PROCESS = "IN PROCESS";
        public final static String CANCELED = "CANCELED";
        public final static String DONE = "DONE";
    }
}
