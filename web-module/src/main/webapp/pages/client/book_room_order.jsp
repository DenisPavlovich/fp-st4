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
    <%@ include file="../../jspf/menu/client_menu.jspf" %>
    <div class="sign in">
        <form action="/client/bookroom" method="post">
            <div class="form">
                <input type="text" hidden="hidden" readonly name="accountid"
                       value="${sessionScope.get('accountid')}">
                <p class="message"> person count
                    <input type="number" required name="person_count">
                </p>
                <p class="message"> apartment type <br>
                    <input type="checkbox" name="opt1" value="ECONOMY">ECONOMY<br>
                    <input type="checkbox" name="opt2" checked value="BUSINESS">BUSINESS<br>
                    <input type="checkbox" name="opt3" value="PREMIUM">PREMIUM<br>
                </p>
                <p class="message"> from
                    <input type="date" name="from">
                </p>
                <p class="message"> to
                    <input type="date" name="to">
                </p>
                <button type="submit">ok</button>
            </div>
        </form>
    </div>
</div>
</body>
</html>
