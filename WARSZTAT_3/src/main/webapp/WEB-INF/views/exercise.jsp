<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>exercise</title>
    <meta charset="UTF-8"/>
</head>
<body>
    <jsp:include page="../header.jsp"/>
    <br/>

    <ol>
        <li>
            Dodaj zadanie
            <form method="post">

                <input type="hidden" name="type" value="add"/>

                <label>
                    Tytuł:<br/>
                    <input type="text" name="title" placeholder="tytuł"/>
                </label><br/>

                <label>
                    Opis:<br/>
                    <input type="text" name="description" placeholder="opis"/>
                </label><br/>

                <input type="submit" value="Dodaj"/><br/>
            </form>
        </li>
        <li>
            Edytuj zadanie
            <form method="post">

                <input type="hidden" name="type" value="edit"/>

                <label>
                    Id zadania:<br/>
                    <input type="number" name="id" placeholder="id zadania"/>
                </label><br/>

                <label>
                    Nowy tytuł:<br/>
                    <input type="text" name="title" placeholder="Nowy tytuł"/>
                </label><br/>

                <label>
                    Nowy opis:<br/>
                    <input type="text" name="description" placeholder="Nowy opis"/>
                </label><br/>

                <input type="submit" value="Edytuj"/><br/>
            </form>
        </li>
        <li>
            Usuń zadanie
            <form method="post">

                <input type="hidden" name="type" value="delete"/>

                <label>
                    Id zadania:<br/>
                    <input type="number" name="id" placeholder="id zadania"/>
                </label><br/>

                <input type="submit" value="Usuń"/><br/>
            </form>
        </li>
    </ol>

    <table>
        <tr>
            <td>id</td>
            <td>title</td>
            <td>description</td>
        </tr>
        <c:forEach items="${exercises}" var="e">
            <tr>
                <td>${e.id}</td>
                <td>${e.title}</td>
                <td>${e.description}</td>
            </tr>
        </c:forEach>
    </table>
</body>
</html>
