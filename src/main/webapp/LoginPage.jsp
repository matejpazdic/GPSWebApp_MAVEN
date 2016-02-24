<%-- 
    Document   : LoginPage
    Created on : 9.10.2013, 20:27:56
    Author     : matej_000
--%>

<%@page import="sk.tuke.fei.kpi.Database.DBLoginFinder"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    //session.removeAttribute("trackFilename");
    //session.removeAttribute("trackName");
    //session.removeAttribute("trackDescr");
    //session.removeAttribute("trackActivity");
    //session.removeAttribute("access");
    session.removeAttribute("Admin");
    
%>
<%
            DBLoginFinder finder = new DBLoginFinder();
            if(session.getAttribute("username") != null){
                if(finder.isExistingLoginNonLog(session.getAttribute("username").toString())){
                    if(finder.isUserStatus(session.getAttribute("username").toString())){
                        session.setAttribute("Admin", "False");
                        response.sendRedirect("Logged/HomePage.jsp");
                    }else{
                        session.setAttribute("Admin", "True");
                        response.sendRedirect("Logged/HomePage.jsp");
                    }
                }
            }
            %>

<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="">
    <meta name="author" content="">

    <title>Login Page</title>

    <!-- Bootstrap core CSS -->
    <link href="HTMLStyle/LoginPageStyle/css/bootstrap.css" rel="stylesheet">

    
    <script type="text/javascript" src="HTMLStyle/LoginPageStyle/js/jquery.min.js"></script>
    <script type="text/javascript" src="HTMLStyle/LoginPageStyle/js/bootstrap.min.js"></script>
    <script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.0.5/angular.js"></script>
   
    <script>
    
    var app = angular.module('myapp', []); 
    
    </script>
    
    <!-- Add custom CSS here -->
    <link href="HTMLStyle/LoginPageStyle/css/stylish-portfolio.css" rel="stylesheet">
   
  </head>

  <body ng-app="myapp">  
    <!-- Full Page Image Header Area -->
    <div id="top" class="header">
     
       <div class="container">
                                
           <div id="top1"> 
                 <a id="modal-49447" href="#modal-container-49447" data-toggle="modal"></a>
			
                <div class="modal fade" id="modal-container-49447" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
			<div class="modal-dialog">
			<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-hidden="true">x</button>
					<h3 class="modal-title" id="myModalLabel">
                                            <b>Incorrect Login or Password!!!</b>
					</h3>
			</div>
			<div class="modal-body">
				Click on the "Sign in" button and write correct login and password...
                                If you forgotten your password, just click on "Send me password" button!
			</div>
			<div class="modal-footer">
                        <a class="btn btn-primary" href="RecoverYourPassword">Send me password</a>
			<button type="button" class="btn btn-primary" data-dismiss="modal">Sign in</button>
				</div>
			</div>
					
		</div></div></div>
           
            <div id="top2"> 
                 <a id="modal-49448" href="#modal-container-49448" data-toggle="modal"></a>
			
                <div class="modal fade" id="modal-container-49448" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
			<div class="modal-dialog">
			<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-hidden="true">x</button>
					<h3 class="modal-title" id="myModalLabel">
                                            <b>Congrats! We send you a activation email with Activation link! </b>
					</h3>
			</div>
			<div class="modal-body">
				After use our link you can sign in! 
			</div>
			<div class="modal-footer">
			<button type="button" class="btn btn-primary" data-dismiss="modal">Sign in</button>
				</div>
			</div>
					
		</div></div></div>
           
           <div id="top3"> 
                 <a id="modal-49448" href="#modal-container-49449" data-toggle="modal"></a>
			
                <div class="modal fade" id="modal-container-49449" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
			<div class="modal-dialog">
			<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-hidden="true">x</button>
					<h3 class="modal-title" id="myModalLabel">
                                            <b>Your account isn't activated! </b>
					</h3>
			</div>
			<div class="modal-body">
				Check your email for message with activation link and use it! 
			</div>
			<div class="modal-footer">
			<button type="button" class="btn btn-primary" data-dismiss="modal">I understand!</button>
				</div>
			</div>
					
		</div></div></div>
        
                        
           <form name = "form" action="Login.jsp" class="form-signin" method="POST">
        <h2 class="form-signin-heading">Please sign in</h2>
        <div id="1" name = "1" ng-class="{'has-error': !form.Login.$valid && form.Login.$dirty,  'has-success': form.Login.$valid}">
        <input type="email" name="Login" class="form-control" placeholder="Email address" autofocus ng-model="email" required>
        </div>
        <div id="2" name = "2" ng-class="{'has-error': !form.Pass.$valid && form.Pass.$dirty,  'has-success': form.Pass.$valid}">
        <input type="password" name="Pass" class="form-control" placeholder="Password" ng-model="text" required>
        </div>
        <label class="checkbox">
          <input type="checkbox" value="remember-me"> Remember me
        </label>
        <button ng-disabled="!form.$valid" class="btn btn-lg btn-primary btn-block" type="submit">Sign in</button>
      </form>
           <a href="RegisterPage.jsp" class="label">Or register!</a>
           <br>
           <%
            if (session.getAttribute("isCorrectLogin") != null && session.getAttribute("isCorrectLogin").equals("False")) {
                    //out.print("<script>alert(\"Incorrect Login or Password!\")</script>");
                    
                    out.print("<script>$(document).ready(function() {$('#top1').find('a').trigger('click');});</script>");
                    session.removeAttribute("isCorrectLogin");
                
                } else if (session.getAttribute("correctRegistration") != null && session.getAttribute("correctRegistration").toString().equals("True")) {
                    //out.print("<script>alert(\"Congrats you have been successfuly registered! You can now Sign in.\")</script>");
                    session.removeAttribute("correctRegistration");
                    out.print("<script>$(document).ready(function() {$('#top2').find('a').trigger('click');});</script>");
                    
                    
                } else if (session.getAttribute("isCorrectLogin") != null && session.getAttribute("isCorrectLogin").equals("True") && session.getAttribute("notAcceptedUser") != null && session.getAttribute("notAcceptedUser").equals("True")) {
                    //out.print("<script>alert(\"Congrats you have been successfuly registered! You can now Sign in.\")</script>");
                    
                    out.print("<script>$(document).ready(function() {$('#top3').find('a').trigger('click');});</script>");
                    session.removeAttribute("isCorrectLogin");
                    session.removeAttribute("notAcceptedUser");
                    }
            %>
           
    </div>
    </div>

  </body>

</html>