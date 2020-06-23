<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: VCOM
  Date: 23/06/2020
  Time: 11:24 SA
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Create User</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<h1>Create new user</h1>
<p>
    <c:if test="${requestScope['message']!=null}">
        <span class="message">${requestScope["message"]}</span>
    </c:if>
</p>

<p><a href="/users">Back to user list</a></p>

<form method="post">
    <fieldset>
        <legend>User information</legend>
        <table>
            <tr>
                <th>Name:</th>
                <td><input type="text" name="name" id="name"></td>
            </tr>
            <tr>
                <th>Email:</th>
                <td><input type="text" name="email" id="email"></td>
            </tr>
            <tr>
                <th>Country:</th>
                <td><input type="text" name="country" id="country"></td>
            </tr>
            <tr>
                <td></td>
                <td><input type="submit" value="Create user"></td>
            </tr>
        </table>
    </fieldset>
</form>
</body>
</html>
