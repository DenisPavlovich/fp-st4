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
    <%@ include file="../../WEB-INF/jspf/menu/client_menu.jspf"%>
    <div class="sign in">
        <form action="/client/bookroom" method="post">
            <div class="form">
                <div class="login-form">
                    <input type="text" hidden="hidden" readonly name="id" value="${param.get("id")}">
                    <input type="text" hidden="hidden" readonly name="type" value="${param.get("type")}">
                        <p>from</p>
                    <input type="date" name="from">
                        <p>to</p>
                    <input type="date" name="to">
                    <button type="submit">ok</button>
                </div>
            </div>
        </form>
    </div>
</div>
</body>
</html>
