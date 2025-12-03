<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<%@taglib uri="/struts-tags" prefix="s" %>

<html>
    <head>
        <title>Login Successful</title>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    </head>

    <body>
        <h1>Welcome, <s:property value="username" /></h1>
        </br>

        <a href="logoutUser.action">Logout</a> </br>
        <a href="index.jsp">Back to Homepage</a>
    </body>
</html>
