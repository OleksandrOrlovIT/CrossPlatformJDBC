<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Flight Application</title>
    <style>
        body {
            font-size: 18px;
            display: flex;
            flex-direction: column;
            align-items: center;
        }

        .form-container {
            width: 60%;
            margin-bottom: 20px;
        }

        .result-container {
            width: 60%;
        }

        label {
            display: block;
            margin-bottom: 5px;
        }

        input, textarea {
            width: calc(100% - 16px);
            padding: 8px;
            margin-bottom: 10px;
        }

        button {
            font-size: 18px;
            padding: 10px;
            margin-top: 10px;
        }

        h1 {
            margin-bottom: 10px;
        }
    </style>
</head>
<body>

<div class="form-container">
    <h1>Flight Application</h1>

    <form action="FlightServlet" method="post">
        <label for="flightId">Flight ID:</label>
        <input type="text" id="flightId" name="flightId" size="25" />

        <label for="origin">Origin :</label>
        <input type="text" id="origin" name="origin" size="25" />

        <label for="destination">Destination :</label>
        <input type="text" id="destination" name="destination" size="25" />

        <label for="flightDate">FlightDate format(YYYY-MM-DD MM-SS) :</label>
        <input type="text" id="flightDate" name="flightDate" size="25" />

        <label for="numberOfSeats">Seats :</label>
        <input type="text" id="numberOfSeats" name="numberOfSeats" size="25" />

        <br/>

        <button type="submit" name="action" value="findAll">Find All</button>
        <button type="submit" name="action" value="getById">Get by Id</button>
        <button type="submit" name="action" value="add">Add</button>
        <button type="submit" name="action" value="update">Update by Id</button>
        <button type="submit" name="action" value="deleteById">Delete by Id</button>
    </form>
</div>

<div class="result-container">
    <pre>
        <%= request.getAttribute("result") %>
    </pre>
</div>

</body>
</html>