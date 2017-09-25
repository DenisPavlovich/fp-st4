package ua.nure.romanenko.st4.dao.impl;

import ua.nure.romanenko.st4.dto.Apartments;
import ua.nure.romanenko.st4.dto.Orders;

import java.sql.SQLException;

/**
 * Created by denis on 25.09.17.
 */
public class ApartmentDaoImpl extends DaoBase<Apartments> {
    public Apartments read(int id) throws SQLException {
        Apartments apartment = new Apartments();
        apartment.setId(id);
        return (Apartments) query.readRow(apartment);
    }
}
