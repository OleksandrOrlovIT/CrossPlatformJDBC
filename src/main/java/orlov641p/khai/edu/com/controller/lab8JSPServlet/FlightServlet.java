package orlov641p.khai.edu.com.controller.lab8JSPServlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static orlov641p.khai.edu.com.controller.lab7JDBC.BaseClient.makeConnection;

@WebServlet(name = "FlightServlet", urlPatterns = "/FlightServlet")
public class FlightServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private static String baseUrl = "http://localhost:8080/FLIGHT_REQUEST/";

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        String result = "";

        switch (action) {
            case "findAll":
                result = makeConnection(baseUrl + "FIND_ALL");
                break;
            case "getById":
                String flightId = request.getParameter("flightId");
                result = makeConnection( baseUrl + "GET_BY_ID/" + flightId);
                break;
            case "add":
                result = makeConnection(getAddOrUpdateRequest("ADD", request));
                break;
            case "update":
                result = makeConnection(getAddOrUpdateRequest("UPDATE", request));
                break;
            case "deleteById":
                String deleteFlightId = request.getParameter("flightId");
                result = makeConnection( baseUrl + "DELETE_BY_ID/" + deleteFlightId);
                break;
        }

        request.setAttribute("result", result);
        request.getRequestDispatcher("/flight.jsp").forward(request, response);
    }

    private String getAddOrUpdateRequest(String operation, HttpServletRequest request) {
        String flightId = request.getParameter("flightId");
        String origin = request.getParameter("origin");
        String destination = request.getParameter("destination");
        String flightDate = request.getParameter("flightDate");
        String numberOfSeats = request.getParameter("numberOfSeats");

        return baseUrl + operation + "/" +
                flightId + "/" + origin + "/" + destination + "/" + flightDate + "/" + numberOfSeats;
    }
}

