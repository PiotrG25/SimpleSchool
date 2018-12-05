<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <style>
        td{
            border: 1px solid black;
        }
        ol > li{
            width: 300px;
            background-color: chartreuse;
            border: 1px solid olivedrab;
            list-style-type: none;
            cursor: pointer;
            display: inline-block;
        }
        ol > li > form{
            width: 290px;
            padding: 5px;
            background-color: grey;
            display: none;
            position: fixed;
            cursor: default;
        }
        ol > li:hover{
            background-color: grey;
        }
        ol > li:hover > form{
            display: block;
        }
    </style>
</head>
<body>
    <a href="/">Strona główna</a><br/>
    <a href="/exercise">Exercise</a> - obsługa zadań<br/>
    <a href="/solution">Solution</a> - obsługa rozwiązań<br/>
    <a href="/userGroup">User_group</a> - obsługa grup<br/>
    <a href="/user">Users</a> - obsługa użytkowników<br/>
</body>
</html>
