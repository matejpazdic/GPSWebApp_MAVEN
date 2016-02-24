<%-- 
    Document   : Login
    Created on : 9.10.2013, 19:09:14
    Author     : matej_000
--%>

<%@page import="sk.tuke.fei.kpi.Database.DBLoginFinder"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    //session.removeAttribute("trackFilename");
    //session.removeAttribute("trackName");
    //session.removeAttribute("trackDescr");
    //session.removeAttribute("trackActivity");
%>
<!DOCTYPE html>
<html>
    <%
        DBLoginFinder finder = new DBLoginFinder();
        boolean isGood = finder.isCorrectLogin(request.getParameter("Login"), request.getParameter("Pass"));
        
        //session.removeAttribute("correctRegistration");
        
        if (isGood) {
                DBLoginFinder finder1 = new DBLoginFinder();
                    boolean isUserStatus = finder1.isUserStatus(request.getParameter("Login"));
                    boolean isUserAccepted = finder1.isUserAccepted(request.getParameter("Login"));
                    if (isUserAccepted) {
                        session.setAttribute("username", request.getParameter("Login"));
                        session.setAttribute("isCorrectLogin", "True");
                        if (isUserStatus) {
                            session.setAttribute("Admin", "False");
                            response.sendRedirect("Logged/HomePage.jsp");
                        } else {
                            session.setAttribute("Admin", "True");
                            response.sendRedirect("Logged/HomePage.jsp");
                        }
                    } else {
                        session.setAttribute("isCorrectLogin", "True");
                        session.setAttribute("notAcceptedUser", "True");
                        response.sendRedirect("LoginPage.jsp");
                    }

                } else {
                    session.setAttribute("isCorrectLogin", "False");
                    session.setAttribute("userEmail", request.getParameter("Login"));
                    response.sendRedirect("LoginPage.jsp");
        } %>
</html>