<%@page contentType="text/html" pageEncoding="utf-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="my" uri="/WEB-INF/viewrooms.tld" %>

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
        <div class="form">
            <div class="login-form">
                <my:client_free_rooms action="../../index.jsp" buttonName="cancel" withAccount="true"/>
            </div>
        </div>
    </div>
</div>
</body>
</html>
