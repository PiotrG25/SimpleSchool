<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>solution</title>
    <meta charset="UTF-8"/>
</head>
<body>
    <jsp:include page="../header.jsp"/>
    <ol>
        <li>
            Dodaj rozwiązanie
            <form action="/solution" method="post">

                <input type="hidden" name="type" value="add"/>

                <label>
                    Opis:<br/>
                    <input type="text" name="description" placeholder="opis"/>
                </label><br/>

                <label>
                    Id zadania:<br/>
                    <input type="number" name="exerciseId" placeholder="id zadania"/>
                </label><br/>

                <label>
                    Id użytkownika:<br/>
                    <input type="number" name="userId" placeholder="id użytkownika"/>
                </label><br/>

                <input type="submit" value="Dodaj"/><br/>
            </form>
        </li>
        <li>
            Edytuj rozwiązanie:
            <form action="/solution" method="post">

                <input type="hidden" name="type" value="edit"/>

                <label>
                    Id rozwiązania:<br/>
                    <input type="number" name="id" placeholder="id rozwiązania"/>
                </label>

                <label>
                    Opis:<br/>
                    <input type="text" name="description" placeholder="opis"/>
                </label><br/>

                <label>
                    Id zadania:<br/>
                    <input type="number" name="exerciseId" placeholder="id zadania"/>
                </label><br/>

                <label>
                    Id użytkownika:<br/>
                    <input type="number" name="userId" placeholder="id użytkownika"/>
                </label><br/>

                <input type="submit" value="Edytuj"/><br/>
            </form>
        </li>
        <li>
            Usunięcie rozwiązania
            <form action="/solution" method="post">

                <input type="hidden" name="type" value="delete"/>

                <label>
                    Id rozwiązania:<br/>
                    <input type="number" name="id" placeholder="id rozwiązania"/>
                </label><br/>

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
            <td>exerciseId</td>
            <td>userId</td>
        </tr>
        <c:forEach items="${solutions}" var="s">
            <tr>
                <td>${s.id}</td>
                <td>${s.created}</td>
                <td>${s.updated}</td>
                <td>${s.description}</td>
                <td>${s.exerciseId}</td>
                <td>${s.userId}</td>
            </tr>
        </c:forEach>
    </table>
</body>
</html>
