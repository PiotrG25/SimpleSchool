<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>users</title>
    <meta charset="UTF-8"/>
</head>
<body>
    <jsp:include page="../header.jsp"/>
    <ol>
        <li>
            Dodaj użytkownika
            <form method="post">

                <input type="hidden" name="type" value="add"/>

                <label>
                    Nazwa użytkownika:<br/>
                    <input type="text" name="name" placeholder="nazwa użytkownika"/>
                </label><br/>

                <label>
                    Email:<br/>
                    <input type="email" name="email" placeholder="email"/>
                </label><br/>

                <label>
                    Hasło:<br/>
                    <input type="password" name="password" placeholder="hasło"/>
                </label><br/>

                <label>
                    Id grupy:<br/>
                    <input type="number" name="userGroupId" placeholder="id grupy"/>
                </label><br/>

                <input type="submit" value="Dodaj"/><br/>
            </form>
        </li>
        <li>
            Edytuj użytkownika
            <form method="post">

                <input type="hidden" name="type" value="edit"/>

                <label>
                    Id użytkownika:<br/>
                    <input type="number" name="id" placeholder="id użytkownika"/>
                </label><br/>

                <label>
                    Nowa nazwa użytkownika:<br/>
                    <input type="text" name="name" placeholder="nowa nazwa użytkownika"/>
                </label><br/>

                <label>
                    Nowy email:<br/>
                    <input type="email" name="email" placeholder="nowy email"/>
                </label><br/>

                <label>
                    Nowe hasło:<br/>
                    <input type="password" name="password" placeholder="nowe hasło"/>
                </label><br/>

                <label>
                    Nowe id grupy:<br/>
                    <input type="number" name="userGroupId" placeholder="nowe id grupy"/>
                </label><br/>

                <input type="submit" value="Edytuj"/><br/>
            </form>
        </li>
        <li>
            Usuń użytkownika
            <form method="post">

                <input type="hidden" name="type" value="delete"/>

                <label>
                    Id użytkownika:<br/>
                    <input type="number" name="id" placeholder="id użytkownika"/>
                </label><br/>

                <input type="submit" value="Usuń"/><br/>
            </form>
        </li>
    </ol>

    <table>
        <tr>
            <td>id</td>
            <td>name</td>
            <td>email</td>
            <td>password</td>
            <td>userGroupId</td>
        </tr>
        <c:forEach items="${users}" var="u">
            <tr>
                <td>${u.id}</td>
                <td>${u.name}</td>
                <td>${u.email}</td>
                <td>${u.password}</td>
                <td>${u.userGroupId}</td>
            </tr>
        </c:forEach>
    </table>
</body>
</html>
