<%@page import="in.co.rays.bean.RoleBean"%>
<%@page import="in.co.rays.ctl.LoginCtl"%>
<%@page import="in.co.rays.bean.UserBean"%>
<%@page import="in.co.rays.ctl.ORSView"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Insert title here</title>
</head>
<body>

	<%
		UserBean userBean = (UserBean) session.getAttribute("user");
		boolean userLoggedIn = userBean != null;
		String welcomeMsg = "Hi, "
				+ (userLoggedIn ? userBean.getFirstName() + " (" + session.getAttribute("role") + ")" : "Guest");
	%>

	<table>
		<tr>
			<td width="90%"><a style="text-decoration: none;"
				href="<%=ORSView.WELCOME_CTL%>"><b>Welcome</b></a> | <%
				if (userLoggedIn) {
			%> <a style="text-decoration: none;"
				href="<%=ORSView.LOGIN_CTL%>?operation=<%=LoginCtl.OP_LOG_OUT%>"><b>Logout</b></a>
				<%
					} else {
				%> <a style="text-decoration: none;" href="<%=ORSView.LOGIN_CTL%>"><b>Login</b></a>
				<%
					}
				%></td>
			<td rowspan="2" align="right"><img
				src="<%=ORSView.APP_CONTEXT%>/img/customLogo.jpg" width="318"
				height="90"></td>
		</tr>
		<tr>
			<td>
				<h3><%=welcomeMsg%></h3>
			</td>
		</tr>
		<tr>
			<td colspan="2">
				<%
					if (userLoggedIn) {
				%> <%
 	if (userBean.getRoleId() == RoleBean.ADMIN) {
 %> <a href="<%=ORSView.USER_CTL%>">Add User</a> | <a
				href="<%=ORSView.USER_LIST_CTL%>">User List</a> <%
 	}
 %> <%
 	if (userBean.getRoleId() == RoleBean.STUDENT) {
 %> <%
 	}
 %> <%
 	}
 %>
			</td>
		</tr>
	</table>
	<hr>

</body>
</html>