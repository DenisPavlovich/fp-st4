package ua.nure.romanenko.st4.dao.impl;

import ua.nure.romanenko.st4.dbcp.Mutator;
import ua.nure.romanenko.st4.dto.Dto;
import ua.nure.romanenko.st4.dto.OrderStatus;
import ua.nure.romanenko.st4.dto.Orders;
import ua.nure.romanenko.st4.dto.Users;

import java.sql.SQLException;

import static ua.nure.romanenko.st4.dbcp.Mutator.getTransactionWriter;

/**
 * Created by denis on 25.09.17.
 */
public class OrderDaoImpl extends DaoBase<Orders> {

    public Orders read(int id) throws SQLException {
        Orders order = new Orders();
        order.setId(id);
        return (Orders) query.readRow(order);
    }

    public Orders insert(Orders dto, OrderStatus status) throws SQLException {
        Mutator.TransactionWriter tw = Mutator.getTransactionWriter();
        tw.open();

        tw.write(dto);
        dto.setStatus(status);
        tw.update(dto);

        tw.close();
        return dto;
    }

    public void update(Integer id, OrderStatus status) throws SQLException {
        Orders filter = new Orders();

        filter.setId(id);
        filter.setStatus(status);

        mutator.update(filter);
    }
}
