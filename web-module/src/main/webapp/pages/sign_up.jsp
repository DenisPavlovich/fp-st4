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
        <form action="/signout" method="post">
            <div class="form">
                <div class="login-form">
                    <p>Введите данные о себе</p>
                    <input class="input" required type="text" name="name" placeholder="name (required)" pattern="[A-zА-яЁё0-9_]{6,26}"/>
                    <p style="font-size: 8px"> пример: +00(000)000-0000</p>
                    <input class="input" required type="text" name="phone_number" placeholder="phone number (required)" pattern="[\+]\d{1,2}[\(]\d{2,3}[\)]\d{3,4}[\-]\d{4}"/>
                    <input class="input" required type="text"  name="email" placeholder="email (required)" pattern="[a-z0-9._%+-]+@[a-z0-9.-]+\.[a-z]{2,3}$"/>
                    <input class="input" type="text" name="age" pattern="[0-9]" placeholder="age"/>
                    <p>Данные идентификации</p>
                    <input class="input" required type="text" name="username" pattern="[A-z0-9_]{1,64}" placeholder="username (required)"/>
                    <input class="input" required type="password" name="password" pattern="[A-z0-9_]{6,64}" placeholder="password (required)"/>
                    <p>
                    <button type="submit">sing up</button>
                    <p class="message">Have no account?</p>
                </div>
            </div>
        </form>
    </div>
</div>
</body>
</html>
