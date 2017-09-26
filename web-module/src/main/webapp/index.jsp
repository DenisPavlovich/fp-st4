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
        <c:set var="hRooms" value="FREE ROOMS" scope="application"/>
    <c:if test="${empty sessionScope.get('auth')}">
        <%@ include file="jspf/not_auth_menu.jspf" %>

        <div class="book_list">
            <h3>${hRooms}</h3>
            <my:free_rooms/>
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
                    <h3>{hRooms}</h3>
                    <my:client_free_rooms action="pages/client/book_room.jsp" buttonName="book" apartmentStatus="FREE"/>
                </div>
            </c:when>
            <%--CLIENT VIEW END--%>

            <%--MANAGER VIEW START--%>
            <c:when test="${accountType.equals('MANAGER')}">
                <%@ include file="WEB-INF/jspf/menu/manager_menu.jspf" %>
            </c:when>
            <%--MANAGER VIEW END--%>
        </c:choose>
    </c:if>

</div>
</body>
</html>
