<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>user_group</title>
    <meta charset="UTF-8"/>
</head>
<body>
    <jsp:include page="../header.jsp"/>
    <ol>
        <li>
            Dodaj grupę
            <form method="post">

                <input type="hidden" name="type" value="add"/>

                <labe>
                    Nazwa:<br/>
                    <input type="text" name="name" placeholder="nazwa"/>
                </labe><br/>

                <input type="submit" value="Dodaj"/><br/>
            </form>
        </li>
        <li>
            Edytuj grupę
            <form method="post">

                <input type="hidden" name="type" value="edit"/>

                <label>
                    Id grupy:<br/>
                    <input type="number" name="id" placeholder="id grupy"/>
                </label><br/>

                <label>
                    Nowa nazwa:<br/>
                    <input type="text" name="name" placeholder="nowa nazwa"/>
                </label><br/>

                <input type="submit" value="Edytuj"/><br/>
            </form>
        </li>
        <li>
            Usuń grupę
            <form method="post">

                <input type="hidden" name="type" value="delete"/>

                <label>
                    Id grupy<br/>
                    <input type="number" name="id" placeholder="id grupy"/>
                </label><br/>

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
</body>
</html>
