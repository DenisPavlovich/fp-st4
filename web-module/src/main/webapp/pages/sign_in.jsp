<%@page contentType="text/html" pageEncoding="utf-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
<head>
    <link type="text/css" rel="stylesheet" href="../css/body.css">
    <link type="text/css" rel="stylesheet" href="../css/menu.css">
    <link type="text/css" rel="stylesheet" href="../css/sign_in_out.css">
</head>
<body>
<div>
    <%@ include file="../jspf/sign_in.jspf"%>
    <div class="sign in">
        <form action="/signin" method="post">
            <div class="form">
                <div class="login-form">
                    <input class="input" type="text" name="username" placeholder="username" required/>
                    <input class="input" type="password" name="password" placeholder="password" required/>
                    <button type="submit">sing in</button>
                    <a href="sign_up.jsp" class="message">Have no account?</a>
                </div>
            </div>
        </form>
    </div>
</div>
</body>
</html>
