<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>user_group</title>
    <meta charset="UTF-8"/>
    <link rel="stylesheet" href="style.css" type="text/css"/>
</head>
<body>
    <jsp:include page="WEB-INF/header.jsp"/>
    <ol>
        <li>
            Dodanie grupy
            <form action="/user_group" method="post">
                <input type="text" name="type" value="add" style="display: none;"/>
                <input type="text" name="name" placeholder="nazwa"/><br/>
                <input type="submit" value="Dodaj"/><br/>
            </form>
        </li>
        <li>
            Edycja grupy
            <form action="/user_group" method="post">
                <input type="text" name="type" value="edit" style="display: none;"/>
                Podaj id grupy do edycji<br/>
                <input type="number" name="id" placeholder="id"/><br/><br/>
                <input type="text" name="name" placeholder="nazwa"/><br/>
                <input type="submit" value="Edytuj"/><br/>
            </form>
        </li>
        <li>
            Usunięcie grupy
            <form action="/user_group" method="post">
                <input type="text" name="type" value="delete" style="display: none;"/>
                Podaj id grupy do usunięcia<br/>
                <input type="number" name="id" placeholder="id"/><br/>
                <input type="submit" value="Usuń"/><br/>
            </form>
        </li>
    </ol>

    <table>
        <tr>
            <td>id</td>
            <td>title</td>
        </tr>
        <c:forEach items="${groups}" var="g">
            <tr>
                <td>${g.id}</td>
                <td>${g.name}</td>
            </tr>
        </c:forEach>
    </table>
    <jsp:include page="WEB-INF/footer.jsp"/>
</body>
</html>
