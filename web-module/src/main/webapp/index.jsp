<%@ page import="ua.nure.romanenko.st4.dto.Accounts" %>
<%@ page contentType="text/html" pageEncoding="utf-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="my" uri="/WEB-INF/viewrooms.tld" %>
<html>
<head>
    <link type="text/css" rel="stylesheet" href="css/body.css">
    <link type="text/css" rel="stylesheet" href="css/menu.css">
    <link type="text/css" rel="stylesheet" href="css/apartment.css">
    <link type="text/css" rel="stylesheet" href="css/sign_in_out.css">
    <link type="text/css" rel="stylesheet" href="css/button.css">
    <link type="text/css" rel="stylesheet" href="css/room_list.css">
</head>
<body>
<div>

    <%--USER VIEW START--%>
    <c:set var="freeRooms" value="FREE ROOMS" scope="application"/>
    <c:set var="allRooms" value="ROOMS" scope="application"/>
    <c:if test="${empty sessionScope.get('auth')}">
        <%@ include file="jspf/not_auth_menu.jspf" %>

        <div class="book_list">
            <h3>${allRooms}</h3>
            <%@include file="WEB-INF/jspf/util/sort.jspf" %>
            <my:rooms/>
        </div>
    </c:if>
    <%--USER VIEW END--%>

    <c:if test="${not empty sessionScope.get('auth')}">
        <c:set var="accountId" scope="session" value="${sessionScope.get('auth').getId()}"/>
        <c:set var="accountType" scope="session" value="${sessionScope.get('auth').getType()}"/>

        <c:choose>
            <%--CLIENT VIEW START--%>
            <c:when test="${accountType.equals('CLIENT')}">
                <%@ include file="WEB-INF/jspf/menu/client_menu.jspf" %>

                <div class="book_list">
                    <h3>${freeRooms}</h3>
                    <%@include file="WEB-INF/jspf/util/sort.jspf" %>
                    <my:smart_rooms action="pages/client/book_room.jsp" buttonName="book" apartmentStatus="FREE"/>
                </div>
            </c:when>
            <%--CLIENT VIEW END--%>

            <%--MANAGER VIEW START--%>
            <c:when test="${accountType.equals('MANAGER')}">
                <%@ include file="WEB-INF/jspf/menu/manager_menu.jspf" %>
                <%@include file="WEB-INF/jspf/util/sort.jspf" %>
                <my:rooms apartmentStatus="FREE"/>
            </c:when>
            <%--MANAGER VIEW END--%>
        </c:choose>
    </c:if>

        <input class="input" name="apartmentId" type="number">
</div>
</body>
</html>
