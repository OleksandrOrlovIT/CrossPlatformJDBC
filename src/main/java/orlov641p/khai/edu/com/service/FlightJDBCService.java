package orlov641p.khai.edu.com.service;

import orlov641p.khai.edu.com.model.Flight;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FlightJDBCService implements JDBCService<Flight> {

    private final Statement statement;
    private final TicketJDBCService ticketJDBCService;

    public FlightJDBCService(Statement statement, TicketJDBCService ticketJDBCService) {
        this.statement = statement;
        this.ticketJDBCService = ticketJDBCService;
    }

    @Override
    public List<Flight> findAll() {
        try {
            ResultSet rs = statement.executeQuery("SELECT * FROM flights");
            List<Flight> list = new ArrayList<>();
            while (rs.next()) {
                String flightId = rs.getString("flightId");
                Flight flight = new Flight(
                        flightId,
                        rs.getString("origin"),
                        rs.getString("destination"),
                        DateUtil.convertToLocalDateTimeViaSqlTimestamp(rs.getDate("flightDate")),
                        ticketJDBCService.getSeats(flightId)
                );
                list.add(flight);
            }
            return list;
        } catch (SQLException e) {
            System.out.println("FlightJDBCService.findAll() " + e);
            return null;
        }
    }

    @Override
    public Flight getById(String flightId) {
        return ticketJDBCService.getFlightById(flightId);
    }

    @Override
    public boolean deleteById(String flightId) {
        try {
            statement.execute("DELETE FROM tickets WHERE flightId = " + flightId);
            statement.execute("DELETE FROM flights WHERE flightId = " + flightId);
            statement.execute("DELETE FROM flightSeats WHERE flightId = " + flightId);
            return true;
        } catch (SQLException e) {
            System.out.println("FlightJDBCService.deleteById() " + e + " flightId = " + flightId);
            return false;
        }
    }

    @Override
    public boolean add(Flight flight) {
        try {
            Date date1 = DateUtil.convertLocalDateTimeToSQLDate(flight.getFlightDate());
            Boolean[] seats = flight.getSeats();
            for (int i = 0; i < seats.length; i++) {
                int tempBool = seats[i] ? 1 : 0;
                statement.execute("INSERT INTO flightSeats(flightId, seatNumber, isTaken) " +
                        "VALUES ('" + flight.getFlightId() + "','" + i + "','" + tempBool + "')");
            }
            statement.execute("INSERT INTO flights(flightId, origin, destination, flightDate) " +
                    "VALUES ('" + flight.getFlightId() + "','" + flight.getOrigin() + "','" +
                    flight.getDestination() + "','" + date1 + "')");
            return true;
        } catch (SQLException e) {
            System.out.println("FlightJDBCService.add() " + e + " flight = " + flight);
            return false;
        }
    }

    @Override
    public boolean update(Flight flight) {
        try {
            Flight foundFlight = getById(flight.getFlightId());
            if (foundFlight != null) {
                Boolean[] oldBooleans = foundFlight.getSeats();
                Boolean[] newBooleans = flight.getSeats();
                if (oldBooleans.length < newBooleans.length) {
                    for (int i = oldBooleans.length; i < newBooleans.length; i++) {
                        statement.execute("INSERT INTO flightSeats(flightId, seatNumber, isTaken) " +
                                "VALUES ('" + flight.getFlightId() + "','" + i + "','" + 0 + "')");
                    }
                } else if (oldBooleans.length > newBooleans.length) {
                    for (int i = oldBooleans.length - 1; i >= newBooleans.length; i--) {
                        if(oldBooleans[i]) {
                            ResultSet rs = statement.executeQuery("SELECT * FROM tickets WHERE flightId = " +
                                    flight.getFlightId() + " AND seatNumber = " + i);
                            rs.next();
                            ticketJDBCService.deleteById(rs.getString("ticketId"));
                        }
                        statement.execute("DELETE FROM flightSeats WHERE flightId = " + flight.getFlightId() +
                                " AND seatNumber = " + i);
                    }
                }
                Date date1 = DateUtil.convertLocalDateTimeToSQLDate(flight.getFlightDate());
                statement.execute("UPDATE flights SET flightId = '" + flight.getFlightId() +
                        "', origin = '" + flight.getOrigin() +
                        "', destination = '" + flight.getDestination() +
                        "', flightDate = '" + date1 +
                        "' WHERE flightId = '" + flight.getFlightId() + "'");
                return true;
            }
        } catch (SQLException e) {
            System.out.println("FlightJDBCService.update() " + e + " flight = " + flight);
            return false;
        }
        return false;
    }

    @Override
    public boolean deleteAll() {
        try {
            statement.execute("DELETE FROM flightSeats");
            statement.execute("DELETE FROM flights");
            return true;
        } catch (SQLException e) {
            System.out.println("FlightJDBCService.deleteAll() " + e);
            return false;
        }
    }
}
