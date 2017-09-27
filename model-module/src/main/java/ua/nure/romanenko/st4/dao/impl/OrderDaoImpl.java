package ua.nure.romanenko.st4.dao.impl;

import ua.nure.romanenko.st4.dbcp.Mutator;
import ua.nure.romanenko.st4.dto.Dto;
import ua.nure.romanenko.st4.dto.OrderStatus;
import ua.nure.romanenko.st4.dto.Orders;
import ua.nure.romanenko.st4.dto.Users;

import java.sql.SQLException;
import java.util.ResourceBundle;

import static ua.nure.romanenko.st4.dbcp.Mutator.getTransactionWriter;

/**
 * Created by denis on 25.09.17.
 */
public class OrderDaoImpl extends DaoBase<Orders> {

    private ResourceBundle rb = ResourceBundle.getBundle("fieldNameKeys");

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

    public void update(Orders order) throws SQLException {
        mutator.update(order);
    }

    public void update(String status, Orders filter) throws SQLException {
        Orders newOrder = new Orders();
        newOrder.setStatus(OrderStatus.valueOf(status));
        mutator.update(newOrder, filter);
    }
    public void update(OrderStatus status, Orders filter) throws SQLException {
        Orders newOrder = new Orders();

        Orders order = (Orders) query.getQueryBuilder(Orders.class)
                .setWhere(filter)
                .setOrderDesc(rb.getString("order.createTime"))
                .setLimit(1)
                .readDtos()
                .get(0);

        System.out.println(order.getStatus());

        newOrder.setStatus(status);
        newOrder.setId(order.getId());
        mutator.update(newOrder);
    }
}
