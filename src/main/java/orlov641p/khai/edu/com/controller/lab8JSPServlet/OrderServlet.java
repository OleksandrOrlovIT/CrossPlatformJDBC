package orlov641p.khai.edu.com.controller.lab8JSPServlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static orlov641p.khai.edu.com.controller.lab7JDBC.BaseClient.makeConnection;

@WebServlet(name = "OrderServlet", urlPatterns = "/OrderServlet")
public class OrderServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private static String baseUrl = "http://localhost:8080/ORDER_REQUEST/";

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        String result = "";

        switch (action) {
            case "findAll":
                result = makeConnection(baseUrl + "FIND_ALL");
                break;
            case "getById":
                String orderId = request.getParameter("orderId");
                result = makeConnection( baseUrl + "GET_BY_ID/" + orderId);
                break;
            case "add":
                result = makeConnection(getAddOrUpdateRequest("ADD", request));
                break;
            case "update":
                result = makeConnection(getAddOrUpdateRequest("UPDATE", request));
                break;
            case "deleteById":
                String deleteOrderId = request.getParameter("orderId");
                result = makeConnection( baseUrl + "DELETE_BY_ID/" + deleteOrderId);
                break;
        }

        request.setAttribute("result", result);
        request.getRequestDispatcher("/order.jsp").forward(request, response);
    }

    private String getAddOrUpdateRequest(String operation, HttpServletRequest request) {
        String orderId = request.getParameter("orderId");
        String creditCardId = request.getParameter("creditCardId");
        String deliveryAddress = request.getParameter("deliveryAddress");
        String orderDate = request.getParameter("orderDate");
        String deliveryDate = request.getParameter("deliveryDate");

        return baseUrl + operation + "/" +
                orderId + "/" + creditCardId + "/" + deliveryAddress + "/" + orderDate + "/" + deliveryDate;
    }
}

