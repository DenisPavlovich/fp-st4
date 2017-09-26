<%@page contentType="text/html" pageEncoding="utf-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
<head>
    <link type="text/css" rel="stylesheet" href="../../css/body.css">
    <link type="text/css" rel="stylesheet" href="../../css/menu.css">
    <link type="text/css" rel="stylesheet" href="../../css/sign_in_out.css">
</head>
<body>
<div>
    <%@ include file="../../WEB-INF/jspf/menu/client_menu.jspf" %>
    <div class="sign in">
        <form action="/order" method="post">
            <div class="form">
                <input type="text" hidden="hidden" readonly name="accountid"
                       value="${sessionScope.get('accountid')}">
                <p class="message"> person count
                    <input class="input" type="number" required name="person_count">
                </p>
                <p class="message"> apartment type <br>
                    <input class="input" type="radio" name="apartmentType" value="ECONOMY">ECONOMY<br>
                    <input class="input" type="radio" name="apartmentType" checked value="BUSINESS">BUSINESS<br>
                    <input class="input" type="radio" name="apartmentType" value="PREMIUM">PREMIUM<br>
                </p>
                <p class="message"> from
                    <input class="input" type="date" name="from">
                </p>
                <p class="message"> to
                    <input class="input" type="date" name="to">
                </p>
                <button type="submit">ok</button>
            </div>
        </form>
    </div>
</div>
</body>
</html>
