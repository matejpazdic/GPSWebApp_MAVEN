<%-- 
    Document   : TryToAcceptUser
    Created on : 27.2.2014, 12:27:43
    Author     : matej_000
--%>

<%@page import="sk.tuke.fei.kpi.Database.DBLoginUpdater"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <%
            String userToken = request.getParameter("token");
            String email = request.getParameter("email");
            
            DBLoginUpdater updater = new DBLoginUpdater();
            boolean accept = updater.acceptUser(email, userToken);
            
            if(accept){
                response.sendRedirect("LoginPage.jsp");
            }else{
                response.sendRedirect("LoginPage.jsp");
            }
        %>
    </body>
</html>
