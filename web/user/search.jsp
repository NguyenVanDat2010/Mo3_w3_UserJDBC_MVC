<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
    <title>Search User</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<h1>Search User</h1>
<p><a href="/users?action=create">Create new user</a></p>
<p><a href="/users">Back to user list</a></p>

<form method="post" action="/users?action=search">
    <input type="text" name="searchValue" placeholder="Enter your word">
    <input type="submit" name="search" value="Search user">
</form>

<table>
    <tr>
        <th>Name</th>
        <th>Email</th>
        <th>Country</th>
        <th>Edit</th>
        <th>Delete</th>
    </tr>
    <c:forEach items="${requestScope['users']}" var="user">
        <tr>
            <td>${user.getName()}</td>
            <td>${user.getEmail()}</td>
            <td>${user.getCountry()}</td>
            <td><a href="/users?action=edit&id=${user.getId()}"><button type="button">Edit</button></a></td>
            <td><a href="/users?action=delete&id=${user.getId()}"><button type="button">Delete</button></a></td>
        </tr>
    </c:forEach>
</table>
</body>
</html>
