<%--
  Created by IntelliJ IDEA.
  User: VCOM
  Date: 23/06/2020
  Time: 11:25 SA
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Delete User</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<h1>Delete user</h1>
<p>
    <a href="/users">Back to user list</a>
</p>
<form method="post">
    <h3>Are you sure?</h3>
    <fieldset>
        <legend>User information</legend>
        <table>
            <tr>
                <th>Name:</th>
                <td>${requestScope["user"].getName()}</td>
            </tr>
            <tr>
                <th>Email:</th>
                <td>${requestScope["user"].getEmail()}</td>
            </tr>
            <tr>
                <th>Address:</th>
                <td>${requestScope["user"].getCountry()}</td>
            </tr>
            <tr>
                <td></td>
                <td><input type="submit" value="Delete user"></td>
            </tr>
        </table>
    </fieldset>
</form>
</body>
</html>
