package orlov641p.khai.edu.com.controller.lab8JSPServlet;


import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static orlov641p.khai.edu.com.controller.lab7JDBC.BaseClient.makeConnection;

@WebServlet(name = "ClientServlet", urlPatterns = "/ClientServlet")
public class ClientServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private static String baseUrl = "http://localhost:8080/CLIENT_REQUEST/";

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        String result = "";

        switch (action) {
            case "findAll":
                result = makeConnection(baseUrl + "FIND_ALL");
                break;
            case "getById":
                String clientId = request.getParameter("clientId");
                result = makeConnection( baseUrl + "GET_BY_ID/" + clientId);
                break;
            case "add":
                result = makeConnection(getAddOrUpdateRequest("ADD", request));
                break;
            case "update":
                result = makeConnection(getAddOrUpdateRequest("UPDATE", request));
                break;
            case "deleteById":
                String deleteClientId = request.getParameter("clientId");
                result = makeConnection( baseUrl + "DELETE_BY_ID/" + deleteClientId);
                break;
        }

        request.setAttribute("result", result);
        request.getRequestDispatcher("/client.jsp").forward(request, response);
    }

    private String getAddOrUpdateRequest(String operation, HttpServletRequest request) {
        String clientId = request.getParameter("clientId");
        String lastName = request.getParameter("lastName");
        String firstName = request.getParameter("firstName");
        String secondName = request.getParameter("secondName");

        return baseUrl + operation + "/" +
                clientId + "/" + lastName + "/" + firstName + "/" + secondName;
    }
}
