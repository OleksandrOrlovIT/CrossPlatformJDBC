package orlov641p.khai.edu.com.controller.lab8JSPServlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static orlov641p.khai.edu.com.controller.lab7JDBC.BaseClient.makeConnection;

@WebServlet(name = "TicketServlet", urlPatterns = "/TicketServlet")
public class TicketServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private static String baseUrl = "http://localhost:8080/TICKET_REQUEST/";

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        String result = "";

        switch (action) {
            case "findAll":
                result = makeConnection(baseUrl + "FIND_ALL");
                break;
            case "getById":
                String ticketId = request.getParameter("ticketId");
                result = makeConnection( baseUrl + "GET_BY_ID/" + ticketId);
                break;
            case "add":
                result = makeConnection(getAddOrUpdateRequest("ADD", request));
                break;
            case "update":
                result = makeConnection(getAddOrUpdateRequest("UPDATE", request));
                break;
            case "deleteById":
                String deleteTicketId = request.getParameter("ticketId");
                result = makeConnection( baseUrl + "DELETE_BY_ID/" + deleteTicketId);
                break;
        }

        request.setAttribute("result", result);
        request.getRequestDispatcher("/ticket.jsp").forward(request, response);
    }

    private String getAddOrUpdateRequest(String operation, HttpServletRequest request) {
        String ticketId = request.getParameter("ticketId");
        String clientId = request.getParameter("clientId");
        String flightId = request.getParameter("flightId");
        String orderId = request.getParameter("orderId");
        String seatNumber = request.getParameter("seatNumber");

        return baseUrl + operation + "/" +
                ticketId + "/" + clientId + "/" + flightId + "/" + orderId + "/" + seatNumber;
    }
}
