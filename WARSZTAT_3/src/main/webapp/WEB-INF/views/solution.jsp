<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>solution</title>
    <meta charset="UTF-8"/>
    <link rel="stylesheet" href="../../css/style.css" type="text/css"/>
</head>
<body>
    <jsp:include page="../header.jsp"/>
    <ol>
        <li>
            Dodanie rozwiązania
            <form action="/solution" method="post">
                <input type="text" name="type" value="add" style="display: none;"/>
                <input type="text" name="description" placeholder="opis"/><br/>
                <input type="number" name="exercise_id" placeholder="id zadania"/><br/>
                <input type="number" name="users_id" placeholder="id użytkownika"/><br/>
                <input type="submit" value="Dodaj"/><br/>
            </form>
        </li>
        <li>
            Edycja rozwiązania
            <form action="/solution" method="post">
                <input type="text" name="type" value="edit" style="display: none;"/>
                Podaj id rozwiązania do edycji<br/>
                <input type="number" name="id" placeholder="id"/><br/><br/>
                <input type="text" name="description" placeholder="opis"/><br/>
                <input type="number" name="exercise_id" placeholder="id zadania"/><br/>
                <input type="number" name="users_id" placeholder="id użytkownika"/><br/>
                <input type="submit" value="Edytuj"/><br/>
            </form>
        </li>
        <li>
            Usunięcie rozwiązania
            <form action="/solution" method="post">
                <input type="text" name="type" value="delete" style="display: none;"/>
                Podaj id rozwiązania do usunięcia
                <input type="number" name="id" placeholder="id"/><br/>
                <input type="submit" value="Usuń"/><br/>
            </form>
        </li>
    </ol>

    <table>
        <tr>
            <td>id</td>
            <td>created</td>
            <td>updated</td>
            <td>description</td>
            <td>exercise_id</td>
            <td>users_id</td>
        </tr>
        <c:forEach items="${solutions}" var="s">
            <tr>
                <td>${s.id}</td>
                <td>${s.created}</td>
                <td>${s.updated}</td>
                <td>${s.description}</td>
                <td>${s.exercise_id}</td>
                <td>${s.users_id}</td>
            </tr>
        </c:forEach>
    </table>
    <jsp:include page="../footer.jsp"/>
</body>
</html>
