<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>users</title>
    <meta charset="UTF-8"/>
    <link rel="stylesheet" href="../../css/style.css" type="text/css"/>
</head>
<body>
    <jsp:include page="../header.jsp"/>
    <ol>
        <li>
            Dodanie użytkownika
            <form action="/users" method="post">
                <input type="text" name="type" value="add" style="display: none;"/>
                <input type="text" name="username" placeholder="nazwa użytkownika"/><br/>
                <input type="email" name="email" placeholder="email"/><br/>
                <input type="password" name="password" placeholder="hasło"/><br/>
                <input type="number" name="user_group_id" placeholder="id grupy"/><br/>
                <input type="submit" value="Dodaj"/><br/>
            </form>
        </li>
        <li>
            Edycja użytkownika
            <form action="/users" method="post">
                <input type="text" name="type" value="edit" style="display: none;"/>
                Podaj id użytkownika do edycji<br/>
                <input type="number" name="id" placeholder="id"/><br/><br/>
                <input type="text" name="username" placeholder="nazwa użytkownika"/><br/>
                <input type="email" name="email" placeholder="email"/><br/>
                <input type="password" name="password" placeholder="hasło"/><br/>
                <input type="number" name="user_group_id" placeholder="id grupy"/><br/>
                <input type="submit" value="Edytuj"/><br/>
            </form>
        </li>
        <li>
            Usunięcie użytkownika
            <form action="/users" method="post">
                <input type="text" name="type" value="delete" style="display: none"/>
                Podaj id użytkownika do usunięcia
                <input type="number" name="id" placeholder="id"/><br/>
                <input type="submit" value="Usuń"/><br/>
            </form>
        </li>
    </ol>

    <table>
        <tr>
            <td>id</td>
            <td>username</td>
            <td>email</td>
            <td>password</td>
            <td>user_group_id</td>
        </tr>
        <c:forEach items="${users}" var="u">
            <tr>
                <td>${u.id}</td>
                <td>${u.username}</td>
                <td>${u.email}</td>
                <td>${u.password}</td>
                <td>${u.user_group_id}</td>
            </tr>
        </c:forEach>
    </table>
    <jsp:include page="../footer.jsp"/>
</body>
</html>
