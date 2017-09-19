package ua.nure.romanenko.st4.dto;


import ua.nure.romanenko.st4.annotation.Column;
import ua.nure.romanenko.st4.annotation.Id;
import ua.nure.romanenko.st4.annotation.Table;

/**
 * Created by denis on 11.09.17.
 */
@Table("apartments")
public class Apartments extends Dto {

    @Id
    @Column("id")
    private Integer id;
    @Column("number")
    private Integer number;
    @Column("rooms")
    private Integer rooms;
    @Column("person_count")
    private Integer personCount;
    @Column("price")
    private Double price;
    @Column("status")
    private String status;
    @Column("type")
    private String type;
    @Column("photo_path")
    private String photoPath;

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public Integer getRooms() {
        return rooms;
    }

    public void setRooms(Integer rooms) {
        this.rooms = rooms;
    }

    public Integer getPersonCount() {
        return personCount;
    }

    public void setPersonCount(Integer personCount) {
        this.personCount = personCount;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPhotoPath() {
        return photoPath;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }

}
