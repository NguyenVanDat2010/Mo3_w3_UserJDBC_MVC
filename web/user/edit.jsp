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
    <title>Edit USer</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<h1>Edit User</h1>
<%--<p>--%>
<%--    <c:if test="${requestScope['message']!=null}">--%>
<%--        <span class="message">${requestScope['message']}</span>--%>
<%--    </c:if>--%>
<%--</p>--%>

<p><a href="/users">Back to user list</a></p>

<form method="post">
    <fieldset>
        <legend>User Information</legend>
        <table>
            <tr>
                <th>Name:</th>
                <td><input type="text" name="name" id="name" value="${requestScope['user'].getName()}"></td>
            </tr>
            <tr>
                <th>Email:</th>
                <td><input type="text" name="email" id="email" value="${requestScope['user'].getEmail()}"></td>
            </tr>
            <tr>
                <th>Address:</th>
                <td><input type="text" name="country" id="country" value="${requestScope['user'].getCountry()}"></td>
            </tr>
            <tr>
                <td></td>
                <td><input type="submit" value="Update user"></td>
            </tr>
        </table>
    </fieldset>
</form>
</body>
</html>
