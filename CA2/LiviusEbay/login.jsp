<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<%@taglib uri="/struts-tags" prefix="s" %>

<html>
    <head>
        <title>LiviusEbay - Login</title>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    </head>

    <body>

        <h1>Login to LiviusEbay</h1>

        <s:form action="loginUser">
            Username: <s:textfield name="username" /> </br>
            Password: <s:password name="password" /> </br>
            <s:submit value="Login" />
        </s:form>

        </br>
        <a href="index.jsp">Back to Homepage</a>

    </body>
</html>
