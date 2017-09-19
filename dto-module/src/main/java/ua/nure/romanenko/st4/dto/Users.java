package ua.nure.romanenko.st4.dto;


import ua.nure.romanenko.st4.annotation.Column;
import ua.nure.romanenko.st4.annotation.Id;
import ua.nure.romanenko.st4.annotation.Table;

/**
 * Created by denis on 11.09.17.
 */
@Table("users")
public class Users extends Dto {
    @Id
    @Column("id")
    private Integer id;
    @Column("name")
    private String name;
    @Column("age")
    private Integer age;
    @Column("phone_number")
    private String phoneNumber;
    @Column("email")
    private String email;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
