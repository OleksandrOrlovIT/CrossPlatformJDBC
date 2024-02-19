package orlov641p.khai.edu.com.service;

import orlov641p.khai.edu.com.model.Flight;
import orlov641p.khai.edu.com.model.Ticket;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class TicketJDBCService implements JDBCService<Ticket> {

    private final Statement statement;
    private final Connection connection;

    public TicketJDBCService(Statement statement, Connection connection) {
        this.statement = statement;
        this.connection = connection;
    }

    @Override
    public List<Ticket> findAll() {
        try {
            ResultSet rs = statement.executeQuery("SELECT * FROM tickets");
            List<Ticket> list = new ArrayList<>();
            while (rs.next()) {
                Ticket ticket = new Ticket(
                        rs.getString("ticketId"),
                        rs.getString("clientId"),
                        rs.getString("flightId"),
                        rs.getString("orderId"),
                        rs.getInt("seatNumber")
                );
                list.add(ticket);
            }
            return list;
        } catch (SQLException e) {
            System.out.println("TicketJDBCService.findAll() " + e);
            return null;
        }
    }

    public List<Ticket> findAllBySpecificFieldId(String fieldName, String id) {
        try {
            ResultSet rs = statement.executeQuery("SELECT * FROM tickets WHERE " + fieldName + " = " + id);
            List<Ticket> list = new ArrayList<>();
            while (rs.next()) {
                Ticket ticket = new Ticket(
                        rs.getString("ticketId"),
                        rs.getString("clientId"),
                        rs.getString("flightId"),
                        rs.getString("orderId"),
                        rs.getInt("seatNumber")
                );
                list.add(ticket);
            }
            return list;
        } catch (SQLException e) {
            System.out.println("TicketJDBCService.findAll() " + e);
            return null;
        }
    }

    @Override
    public Ticket getById(String ticketId) {
        try {
            ResultSet rs = statement.executeQuery("SELECT * FROM tickets where ticketId = " + ticketId);
            rs.next();
            return new Ticket(
                    rs.getString("ticketId"),
                    rs.getString("clientId"),
                    rs.getString("flightId"),
                    rs.getString("orderId"),
                    rs.getInt("seatNumber")
            );
        } catch (SQLException e) {
            System.out.println("TicketJDBCService.getById() " + e);
            return null;
        }
    }

    @Override
    public boolean deleteById(String ticketId) {
        try {
            Ticket ticket = getById(ticketId);
            statement.execute("UPDATE flightSeats SET isTaken = 0 where flightId = " + ticket.getFlightId() +
                    " AND seatNumber = " + ticket.getSeatNumber());

            statement.execute("DELETE FROM tickets WHERE ticketId = " + ticketId);
            return true;
        } catch (SQLException e){
            System.out.println("TicketJDBCService.deleteById() " + e + " ticketId = " + ticketId);
            return false;
        }
    }

    @Override
    public boolean add(Ticket ticket) {
        try {
            Flight flight = getFlightById(ticket.getFlightId());
            if(checkAvailableSeat(flight, ticket.getSeatNumber())){
                return false;
            }

            statement.execute("UPDATE flightSeats SET isTaken = 1 WHERE flightId = " + ticket.getFlightId() +
                    " AND seatNumber = " + ticket.getSeatNumber());

            statement.execute("INSERT INTO tickets(ticketId, clientId, flightId, orderId, seatNumber) " +
                    "VALUES ('" + ticket.getTicketId() + "','" + ticket.getClientId() + "','" +
                    ticket.getFlightId() + "','" + ticket.getOrderId() + "','" + ticket.getSeatNumber() + "')");
            return true;
        } catch (SQLException e){
            deleteById(ticket.getTicketId());
            System.out.println("TicketJDBCService.add() " + e + " ticket = " + ticket);
            return false;
        }
    }

    @Override
    public boolean update(Ticket ticket) {
        Ticket foundTicket = getById(ticket.getTicketId());
        try {
            if(!foundTicket.getFlightId().equals(ticket.getFlightId()) || foundTicket.getSeatNumber() != ticket.getSeatNumber()){
                Flight flight = getFlightById(foundTicket.getFlightId());

                if(checkAvailableSeat(flight, ticket.getSeatNumber())){
                    System.out.println("Seat has already been taken for flight = " + flight);
                    System.out.println("Seat number = " + ticket.getSeatNumber());
                    return false;
                }

                statement.execute("UPDATE flightSeats SET isTaken = 0 where flightId = " + foundTicket.getFlightId() +
                        " AND seatNumber = " + foundTicket.getSeatNumber());

                statement.execute("UPDATE flightSeats SET isTaken = 1 where flightId = " + ticket.getFlightId() +
                        " AND seatNumber = " + ticket.getSeatNumber());
            }

            statement.execute("UPDATE tickets SET clientId = '" + ticket.getClientId() +
                    "', flightId = '" + ticket.getFlightId() +
                    "', orderId = '" + ticket.getOrderId() +
                    "', seatNumber = '" + ticket.getSeatNumber() +
                    "' WHERE ticketId = '" + ticket.getTicketId() + "'");
            return true;
        } catch (Exception e){
            System.out.println("TicketJDBCService.updateById() " + e + " ticket = " + ticket + " foundTicketById = " +
                    foundTicket);
            return false;
        }
    }

    public boolean checkAvailableSeat(Flight flight, int seatNumber){
        return flight.getSeats()[seatNumber];
    }

    @Override
    public boolean deleteAll() {
        try {
            statement.execute("DELETE FROM tickets");
            return true;
        } catch (SQLException e){
            System.out.println("TicketJDBCService.deleteAll() " + e);
            return false;
        }
    }

    public Flight getFlightById(String flightId) {
        try {
            ResultSet rs = statement.executeQuery("SELECT * FROM flights WHERE flightId = " + flightId);
            rs.next();
            return new Flight(
                    flightId,
                    rs.getString("origin"),
                    rs.getString("destination"),
                    DateUtil.convertToLocalDateTimeViaSqlTimestamp(rs.getDate("flightDate")),
                    getSeats(flightId)
            );
        } catch (SQLException e) {
            System.out.println("FlightJDBCService.getById() " + e + " flightId = " + flightId);
            return null;
        }
    }

    public Boolean[] getSeats(String flightId){
        try {
            Statement seatStatement = connection.createStatement();
            ResultSet rs = seatStatement.executeQuery("SELECT * FROM flightSeats where flightId = " + flightId);
            List<Boolean> booleans = new ArrayList<>();
            while (rs.next()){
                booleans.add(rs.getBoolean("isTaken"));
            }

            Boolean[] seats = new Boolean[booleans.size()];
            for(int i = 0; i < seats.length; i++){
                seats[i] = booleans.get(i);
            }

            return seats;
        } catch (SQLException e){
            System.out.println("FlightJDBCService.getSeats() " + e);
            return null;
        }
    }
}
