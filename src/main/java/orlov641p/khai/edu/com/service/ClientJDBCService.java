package orlov641p.khai.edu.com.service;

import orlov641p.khai.edu.com.model.Client;
import orlov641p.khai.edu.com.model.Ticket;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ClientJDBCService implements JDBCService<Client>{

    private final Statement statement;
    private final TicketJDBCService ticketJDBCService;

    public ClientJDBCService(Statement statement, TicketJDBCService ticketJDBCService) {
        this.statement = statement;
        this.ticketJDBCService = ticketJDBCService;
    }

    @Override
    public List<Client> findAll() {
        try {
            ResultSet rs = statement.executeQuery("SELECT * FROM clients");
            List<Client> list = new ArrayList<>();
            while (rs.next()) {
                Client client = new Client(
                        rs.getString("clientId"),
                        rs.getString("lastName"),
                        rs.getString("firstName"),
                        rs.getString("secondName")
                );
                list.add(client);
            }
            return list;
        } catch (SQLException e) {
            System.out.println("ClientJDBCService.findAll() " + e);
            return null;
        }
    }

    @Override
    public Client getById(String clientId) {
        try {
            ResultSet rs = statement.executeQuery("SELECT * FROM clients WHERE clientId = " + clientId);
            rs.next();
            return new Client(
                    rs.getString("clientId"),
                    rs.getString("lastName"),
                    rs.getString("firstName"),
                    rs.getString("secondName")
            );
        } catch (SQLException e) {
            System.out.println("ClientJDBCService.getById() " + e + " clientId = " + clientId);
            return null;
        }
    }

    @Override
    public boolean deleteById(String clientId){
        try {
            List<Ticket> list = ticketJDBCService.findAllBySpecificFieldId("clientId", clientId);
            for(Ticket ticket : list){
                ticketJDBCService.deleteById(ticket.getTicketId());
            }
            statement.execute("DELETE FROM clients WHERE clientId = " + clientId);
            return true;
        } catch (SQLException e){
            System.out.println("ClientJDBCService.deleteById() " + e + " clientId = " + clientId);
            return false;
        }
    }

    @Override
    public boolean add(Client client){
        try {
            statement.execute("INSERT INTO clients(clientId, lastName, firstName, secondName) " +
                    "VALUES ('" + client.getClientId() + "','" + client.getLastName() + "','" +
                    client.getFirstName() + "','" + client.getSecondName() + "')");
            return true;
        } catch (SQLException e){
            System.out.println("ClientJDBCService.add() " + e + " client = " + client);
            return false;
        }
    }

    @Override
    public boolean update(Client client) {
        try {
            statement.execute("UPDATE clients SET lastName = '" + client.getLastName() +
                    "', firstName = '" + client.getFirstName() +
                    "', secondName = '" + client.getSecondName() + "' WHERE clientId = '" + client.getClientId() + "'");
            return true;
        } catch (SQLException e){
            System.out.println("ClientJDBCService.updateById() " + e + " client = " + client);
            return false;
        }
    }

    @Override
    public boolean deleteAll() {
        try {
            statement.execute("DELETE FROM clients");
            return true;
        } catch (SQLException e){
            System.out.println("ClientJDBCService.deleteAll() " + e);
            return false;
        }
    }
}