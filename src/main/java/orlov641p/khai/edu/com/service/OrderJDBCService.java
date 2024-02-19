package orlov641p.khai.edu.com.service;

import orlov641p.khai.edu.com.model.Order;
import orlov641p.khai.edu.com.model.Ticket;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class OrderJDBCService implements JDBCService<Order> {

    private final Statement statement;
    private final TicketJDBCService ticketJDBCService;

    public OrderJDBCService(Statement statement, TicketJDBCService ticketJDBCService) {
        this.statement = statement;
        this.ticketJDBCService = ticketJDBCService;
    }

    @Override
    public List<Order> findAll() {
        try {
            ResultSet rs = statement.executeQuery("SELECT * FROM orders");
            List<Order> list = new ArrayList<>();
            while (rs.next()) {
                Order order = new Order(
                        rs.getString("orderId"),
                        rs.getString("creditCardId"),
                        rs.getString("deliveryAddress"),
                        DateUtil.convertToLocalDateTimeViaSqlTimestamp(rs.getDate("orderDate")),
                        DateUtil.convertToLocalDateTimeViaSqlTimestamp(rs.getDate("deliveryDate"))
                );
                list.add(order);
            }
            return list;
        } catch (SQLException e) {
            System.out.println("OrderJDBCService.findAll() " + e);
            return null;
        }
    }

    @Override
    public Order getById(String orderId) {
        try {
            ResultSet rs = statement.executeQuery("SELECT * FROM orders WHERE orderId = " + orderId);
            rs.next();
            return new Order(
                    rs.getString("orderId"),
                    rs.getString("creditCardId"),
                    rs.getString("deliveryAddress"),
                    DateUtil.convertToLocalDateTimeViaSqlTimestamp(rs.getDate("orderDate")),
                    DateUtil.convertToLocalDateTimeViaSqlTimestamp(rs.getDate("deliveryDate"))
            );
        } catch (SQLException e) {
            System.out.println("OrderJDBCService.getById() " + e + " orderId = " + orderId);
            return null;
        }
    }

    @Override
    public boolean deleteById(String orderId) {
        try {
            List<Ticket> list = ticketJDBCService.findAllBySpecificFieldId("orderId", orderId);
            for(Ticket ticket : list){
                ticketJDBCService.deleteById(ticket.getTicketId());
            }
            statement.execute("DELETE FROM orders WHERE orderId = " + orderId);
            return true;
        } catch (SQLException e){
            System.out.println("OrderJDBCService.deleteById() " + e + " orderId = " + orderId);
            return false;
        }
    }

    @Override
    public boolean add(Order order) {
        try {
            Date date1 = DateUtil.convertLocalDateTimeToSQLDate(order.getOrderDate());
            Date date2 = DateUtil.convertLocalDateTimeToSQLDate(order.getDeliveryDate());
            statement.execute("INSERT INTO orders(orderId, creditCardId, deliveryAddress, orderDate, deliveryDate) " +
                    "VALUES ('" + order.getOrderId() + "','" + order.getCreditCardId() + "','" +
                    order.getDeliveryAddress() + "','" + date1 + "','" + date2 + "')");
            return true;
        } catch (SQLException e){
            System.out.println("OrderJDBCService.add() " + e + " order = " + order);
            return false;
        }
    }

    @Override
    public boolean update(Order order) {
        try {
            Date date1 = DateUtil.convertLocalDateTimeToSQLDate(order.getOrderDate());
            Date date2 = DateUtil.convertLocalDateTimeToSQLDate(order.getDeliveryDate());
            statement.execute("UPDATE orders SET creditCardId = '" + order.getCreditCardId() +
                    "', deliveryAddress = '" + order.getDeliveryAddress() +
                    "', orderDate = '" + date1 +
                    "', deliveryDate = '" + date2 +
                    "' WHERE orderId = '" + order.getOrderId() + "'");
            return true;
        } catch (SQLException e){
            System.out.println("OrderJDBCService.updateById() " + e + " order = " + order);
            return false;
        }
    }

    @Override
    public boolean deleteAll() {
        try {
            statement.execute("DELETE FROM orders");
            return true;
        } catch (SQLException e){
            System.out.println("OrderJDBCService.deleteAll() " + e);
            return false;
        }
    }
}
