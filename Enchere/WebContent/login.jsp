<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Page Connexion</title>
</head>
<body>
 
<h1><a href="/Enchere/AccueilNonConnectePagination6Servlet">ENI-ENCHERE</a></h1>
 
 
<h2 style="color:red;">${messageNonTrouve}</h2>
	<form method="post" action="LoginServlet">
		Identifiant: <input type="text" name="login" placeholder="Pseudo ou email"  required/> <br>
		Password:  <input type="password" name="password" placeholder="Mot de passe" required /><br>
		<input type="submit" value="Connexion">
		
	</form>
	<input type="checkbox" value=""> Se souvenir de moi <br>
<a href="/Enchere/PasswordOublieServlet"> Mot de passe oublié </a><br>
  <a href="/Enchere/UtilisateurServlet"> <input type="button" value="Créer un compte">  </a>
</body>
</html>