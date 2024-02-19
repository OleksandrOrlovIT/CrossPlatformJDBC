package orlov641p.khai.edu.com.controller.lab7JDBC;

import orlov641p.khai.edu.com.model.Client;
import orlov641p.khai.edu.com.model.Flight;
import orlov641p.khai.edu.com.model.Order;
import orlov641p.khai.edu.com.model.Ticket;
import orlov641p.khai.edu.com.service.ClientJDBCService;
import orlov641p.khai.edu.com.service.FlightJDBCService;
import orlov641p.khai.edu.com.service.OrderJDBCService;
import orlov641p.khai.edu.com.service.TicketJDBCService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

public class ServerHTTP {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public static void main(String[] args) {
        int port = 8080;

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            Connection connection = DriverManager
                    .getConnection("jdbc:mysql://localhost:3306/mydatabase", "root", "lab6Orlov");

            Statement statement = connection.createStatement();
            TicketJDBCService ticketJDBCService = new TicketJDBCService(statement, connection);
            FlightJDBCService flightJDBCService = new FlightJDBCService(statement, ticketJDBCService);
            ClientJDBCService clientJDBCService = new ClientJDBCService(statement, ticketJDBCService);
            OrderJDBCService orderJDBCService = new OrderJDBCService(statement, ticketJDBCService);

            bootStrap(clientJDBCService, orderJDBCService, flightJDBCService, ticketJDBCService);
            System.out.println("Server is listening on port " + port);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected: " + clientSocket.getInetAddress());

                BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                String requestLine = reader.readLine();
                String url = requestLine.substring(5, requestLine.length()-9);
                String[] urlParts = url.split("/");
                System.out.println("url parts = " + Arrays.toString(urlParts));
                switch (urlParts[0]) {
                    case "CLIENT_REQUEST" -> new Thread(() -> handleClientRequest(clientSocket, clientJDBCService, urlParts)).start();
                    case "ORDER_REQUEST" ->  new Thread(() -> handleOrderRequest(clientSocket, orderJDBCService, urlParts)).start();
                    case "FLIGHT_REQUEST" -> new Thread(() -> handleFlightRequest(clientSocket, flightJDBCService, urlParts)).start();
                    case "TICKET_REQUEST" -> new Thread(() -> handleTicketRequest(clientSocket, ticketJDBCService, urlParts)).start();
                }

            }

        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }

    private static void handleClientRequest(Socket clientSocket, ClientJDBCService clientJDBCService, String[] urlParts) {
        String operation = urlParts[1];
        try {
            StringBuilder response = new StringBuilder("HTTP/1.1 200 OK\r\n\r\nCLIENT_REQUEST " + operation + "\n");
            switch (operation) {
                case "FIND_ALL" -> {
                    for (Client client : clientJDBCService.findAll()) {
                        response.append(client).append("\n");
                    }
                }
                case "GET_BY_ID" -> {
                    response.append(clientJDBCService.getById(urlParts[2]));
                }
                case "DELETE_BY_ID" -> {
                    if(clientJDBCService.deleteById(urlParts[2])){
                        response.append("Successfully deleted client with id = ");
                    } else {
                        response.append("Couldn`t delete client with id = ");
                    }
                    response.append(urlParts[2]);
                }
                case "ADD" -> {
                    Client client = new Client(urlParts[2], urlParts[3], urlParts[4], urlParts[5]);

                    boolean add = clientJDBCService.add(client);
                    if (add) {
                        response.append(clientJDBCService.getById(urlParts[2]));
                    } else {
                        response.append("Couldn`t add this client = ").append(client);
                    }
                }
                case "UPDATE" -> {
                    Client client = new Client(urlParts[2], urlParts[3], urlParts[4], urlParts[5]);

                    clientJDBCService.update(client);
                    response.append(clientJDBCService.getById(urlParts[2]));
                }
                default -> {
                    response.append("No operation found = ").append(operation);
                }
            }

            OutputStream outputStream = clientSocket.getOutputStream();
            System.out.println("Response = " + response);

            outputStream.write(response.toString().getBytes());

            clientSocket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void handleOrderRequest(Socket clientSocket, OrderJDBCService orderJDBCService, String[] urlParts) {
        String operation = urlParts[1];
        try {
            StringBuilder response = new StringBuilder("HTTP/1.1 200 OK\r\n\r\nORDER_REQUEST " + operation + "\n");
            switch (operation) {
                case "FIND_ALL" -> {
                    for (Order order : orderJDBCService.findAll()) {
                        response.append(order).append("\n");
                    }
                }
                case "GET_BY_ID" -> {
                    response.append(orderJDBCService.getById(urlParts[2]));
                }
                case "DELETE_BY_ID" -> {
                    if(orderJDBCService.deleteById(urlParts[2])){
                        response.append("Successfully deleted order with id = ");
                    } else {
                        response.append("Couldn`t delete order with id = ");
                    }
                    response.append(urlParts[2]);
                }
                case "ADD" -> {
                    Order order = getOrder(urlParts);

                    boolean add = orderJDBCService.add(order);
                    if (add) {
                        response.append(orderJDBCService.getById(urlParts[2]));
                    } else {
                        response.append("Couldn`t add this order = ").append(order);
                    }
                }
                case "UPDATE" -> {
                    Order order = getOrder(urlParts);

                    orderJDBCService.update(order);
                    response.append(orderJDBCService.getById(urlParts[2]));
                }
                default -> {
                    response.append("No operation found = ").append(operation);
                }
            }

            OutputStream outputStream = clientSocket.getOutputStream();
            System.out.println("Response = " + response);

            outputStream.write(response.toString().getBytes());

            clientSocket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void handleFlightRequest(Socket clientSocket, FlightJDBCService flightJDBCService, String[] urlParts) {
        String operation = urlParts[1];
        try {
            StringBuilder response = new StringBuilder("HTTP/1.1 200 OK\r\n\r\nFLIGHT_REQUEST " + operation + "\n");
            switch (operation) {
                case "FIND_ALL" -> {
                    for (Flight flight : flightJDBCService.findAll()) {
                        response.append(flight).append("\n");
                    }
                }
                case "GET_BY_ID" -> {
                    response.append(flightJDBCService.getById(urlParts[2]));
                }
                case "DELETE_BY_ID" -> {
                    if(flightJDBCService.deleteById(urlParts[2])){
                        response.append("Successfully deleted flight with id = ");
                    } else {
                        response.append("Couldn`t delete flight with id = ");
                    }
                    response.append(urlParts[2]);
                }
                case "ADD" -> {
                    Flight flight = getFlight(urlParts);

                    boolean add = flightJDBCService.add(flight);
                    if (add) {
                        response.append(flightJDBCService.getById(urlParts[2]));
                    } else {
                        response.append("Couldn`t add this flight = ").append(flight);
                    }
                }
                case "UPDATE" -> {
                    Flight flight = getFlight(urlParts);

                    flightJDBCService.update(flight);
                    response.append(flightJDBCService.getById(urlParts[2]));
                }
                default -> {
                    response.append("No operation found = ").append(operation);
                }
            }

            OutputStream outputStream = clientSocket.getOutputStream();
            System.out.println("Response = " + response);

            outputStream.write(response.toString().getBytes());

            clientSocket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void handleTicketRequest(Socket clientSocket, TicketJDBCService ticketService, String[] urlParts) {
        String operation = urlParts[1];
        try {
            StringBuilder response = new StringBuilder("HTTP/1.1 200 OK\r\n\r\nTICKET_REQUEST " + operation + "\n");
            switch (operation) {
                case "FIND_ALL" -> {
                    for(Ticket ticket : ticketService.findAll()){
                        response.append(ticket).append("\n");
                    }
                }
                case "GET_BY_ID" -> {
                    response.append(ticketService.getById(urlParts[2]));
                }
                case "DELETE_BY_ID" -> {
                    if(ticketService.deleteById(urlParts[2])){
                        response.append("Successfully deleted ticket with id = ");
                    } else {
                        response.append("Couldn`t delete ticket with id = ");
                    }
                    response.append(urlParts[2]);
                }
                case "ADD" -> {
                    Ticket ticket = getTicket(urlParts);

                    boolean add = ticketService.add(ticket);
                    if (add) {
                        response.append(ticketService.getById(urlParts[2]));
                    } else {
                        response.append("Couldn`t add this ticket = ").append(ticket);
                    }
                }
                case "UPDATE" -> {
                    Ticket ticket = getTicket(urlParts);

                    ticketService.update(ticket);
                    response.append(ticketService.getById(urlParts[2]));
                }
                default -> {
                    response.append("No operation found = ").append(operation);
                }
            }

            OutputStream outputStream = clientSocket.getOutputStream();
            System.out.println("Response = " + response);

            outputStream.write(response.toString().getBytes());

            clientSocket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Order getOrder(String[] urlParts) {
        try {
            LocalDateTime orderDate = LocalDateTime.parse(urlParts[5], formatter);
            LocalDateTime deliveryDate = LocalDateTime.parse(urlParts[6], formatter);
            return new Order(urlParts[2], urlParts[3], urlParts[4], orderDate, deliveryDate);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static Flight getFlight(String[] urlParts) {
        try {
            LocalDateTime date = LocalDateTime.parse(urlParts[5], formatter);
            int numberOfSeats = Integer.parseInt(urlParts[6]);
            return new Flight(urlParts[2], urlParts[3], urlParts[4], date, numberOfSeats);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static Ticket getTicket(String[] parts){
        try {
            return new Ticket(parts[2], parts[3], parts[4], parts[5], Integer.parseInt(parts[6]));
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    private static void bootStrap(ClientJDBCService clientJDBCService, OrderJDBCService orderJDBCService,
                                  FlightJDBCService flightJDBCService, TicketJDBCService ticketJDBCService) {
        ticketJDBCService.deleteAll();
        clientJDBCService.deleteAll();
        orderJDBCService.deleteAll();
        flightJDBCService.deleteAll();

        Client client1 = new Client("1", "Orlov", "Sasha", "Oleksandrovich");
        Client client2 = new Client("2", "Semenova", "Vika", "Oleksandrivna");
        clientJDBCService.add(client1);
        clientJDBCService.add(client2);

        Order order1 = new Order("1", "12345678910", "Kharkiv",
                LocalDateTime.now(), LocalDateTime.now().plusDays(1));
        Order order2 = new Order("2", "10123345466", "Kiyv",
                LocalDateTime.now(), LocalDateTime.now().plusDays(2));
        orderJDBCService.add(order1);
        orderJDBCService.add(order2);

        Flight flight1 = new Flight("1", "Kharkiv", "Kiyv",
                LocalDateTime.now().plusDays(10), 5);

        Flight flight2 = new Flight("2", "Berlin", "Madrid",
                LocalDateTime.now().plusDays(30), 3);

        flightJDBCService.add(flight1);
        flightJDBCService.add(flight2);

        Ticket ticket1 = new Ticket("1", client1.getClientId(), flight1.getFlightId(), order1.getOrderId(), 1);
        Ticket ticket2 = new Ticket("2", client2.getClientId(), flight2.getFlightId(), order2.getOrderId(), 2);

        ticketJDBCService.add(ticket1);
        ticketJDBCService.add(ticket2);
    }
}