<%@page contentType="text/html" pageEncoding="utf-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="my" uri="/WEB-INF/viewrooms.tld" %>

<html>
<head>
    <link type="text/css" rel="stylesheet" href="../../css/body.css">
    <link type="text/css" rel="stylesheet" href="../../css/menu.css">
    <link type="text/css" rel="stylesheet" href="../../css/sign_in_out.css">
    <link type="text/css" rel="stylesheet" href="../../css/room_list.css">
</head>
<body>
<div>
    <%@ include file="../../WEB-INF/jspf/menu/client_menu.jspf" %>
    <h3>YOU ORDERS :</h3>
    <div class="sign in">
        <div class="form">
            <div class="login-form">
                <my:smart_rooms action="/orderstatus" buttonName="pay"
                                apartmentStatus="BOOKED" withAccount="true"
                                updateOrderStatus="PAID"/>
            </div>
        </div>
    </div>
</div>
</body>
</html>
