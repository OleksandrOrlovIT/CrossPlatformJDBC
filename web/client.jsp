<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Client Application</title>
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
    <h1>Client Application</h1>

    <form action="ClientServlet" method="post">
        <label for="clientId">Client ID:</label>
        <input type="text" id="clientId" name="clientId" size="25" />

        <label for="lastName">Last Name:</label>
        <input type="text" id="lastName" name="lastName" size="25" />

        <label for="firstName">First Name:</label>
        <input type="text" id="firstName" name="firstName" size="25" />

        <label for="secondName">Second Name:</label>
        <input type="text" id="secondName" name="secondName" size="25" />

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