package ua.nure.romanenko.st4.dto;


import ua.nure.romanenko.st4.annotation.Column;
import ua.nure.romanenko.st4.annotation.Id;
import ua.nure.romanenko.st4.annotation.Table;

import java.util.Date;

/**
 * Created by denis on 11.09.17.
 */
@Table("accounts")
public class Accounts extends Dto {

    @Id
    @Column("id")
    private Integer id;
    @Column("userid")
    private Integer userId;
    @Column("login")
    private String login;
    @Column("password")
    private String password;
    @Column("type")
    private String type;
    @Column(value = "create_date", defaultValue = true)
    private Date createDate;

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

}
