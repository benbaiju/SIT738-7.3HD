<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="edu.deakin.sit738.finsight.entity.User" %>
<%@ page import="edu.deakin.sit738.finsight.security.UserRoles" %>
<%@ page import="org.springframework.web.util.HtmlUtils" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Admin Users | FinSight</title>
    <style>
        body { margin: 0; font-family: Arial, sans-serif; background: #f5f7fb; color: #172033; }
        .layout { display: flex; min-height: 100vh; }
        .sidebar { width: 250px; background: #172033; color: white; padding: 28px 20px; }
        .brand { font-size: 24px; font-weight: bold; margin-bottom: 38px; }
        .brand span { color: #6ee7b7; }
        .nav a { display: block; color: #cbd5e1; text-decoration: none; padding: 13px 12px; border-radius: 8px; margin-bottom: 5px; font-size: 14px; }
        .nav a:hover, .nav a.active { background: #26344d; color: white; }
        .nav form.logout-form { margin-top: 25px; }
        .nav form.logout-form button.logout {
            display: block; width: 100%; text-align: left; border: none; border-radius: 8px;
            padding: 13px 12px; font-size: 14px; cursor: pointer; background: #7f1d1d; color: white;
        }
        .main { flex: 1; padding: 34px; }
        .card { background: white; border: 1px solid #e2e8f0; border-radius: 16px; padding: 26px; overflow-x: auto; }
        table { width: 100%; border-collapse: collapse; margin-top: 18px; min-width: 900px; }
        th, td { text-align: left; padding: 12px 8px; border-bottom: 1px solid #e2e8f0; font-size: 14px; vertical-align: top; }
        th { color: #64748b; font-size: 12px; text-transform: uppercase; }
        select, button { padding: 8px 10px; border-radius: 8px; font-size: 13px; }
        select { border: 1px solid #cbd5e1; }
        button { border: none; background: #2563eb; color: white; cursor: pointer; margin-top: 6px; }
        form.inline { display: inline-block; margin-right: 8px; }
    </style>
</head>
<body>
<%
    List<User> users = (List<User>) request.getAttribute("users");
    List<User> advisors = (List<User>) request.getAttribute("advisors");
%>
<div class="layout">
    <aside class="sidebar">
        <div class="brand">Fin<span>Sight</span></div>
        <nav class="nav">
            <a class="active" href="${pageContext.request.contextPath}/admin/users">Manage users</a>
            <form class="logout-form" action="${pageContext.request.contextPath}/logout" method="post">
                <%@ include file="includes/spring-csrf.jsp" %>
                <input type="hidden" name="csrfToken" value="${sessionScope.csrfToken}">
                <button class="logout" type="submit">Logout</button>
            </form>
        </nav>
    </aside>
    <main class="main">
        <div class="card">
            <h1>User and advisor management</h1>
            <p>Update roles and assign advisors to clients.</p>

            <table>
                <thead>
                <tr>
                    <th>ID</th>
                    <th>Name</th>
                    <th>Email</th>
                    <th>Role</th>
                    <th>Advisor assignment</th>
                </tr>
                </thead>
                <tbody>
                <% if (users != null) {
                    for (User user : users) {
                        String role = user.getRole() == null ? UserRoles.USER : user.getRole();
                %>
                    <tr>
                        <td><%= user.getId() %></td>
                        <td><%= HtmlUtils.htmlEscape(user.getFullName()) %></td>
                        <td><%= HtmlUtils.htmlEscape(user.getEmail()) %></td>
                        <td>
                            <form class="inline"
                                  action="${pageContext.request.contextPath}/admin/users/role"
                                  method="post">
                                <%@ include file="includes/spring-csrf.jsp" %>
                                <input type="hidden" name="csrfToken" value="${sessionScope.csrfToken}">
                                <input type="hidden" name="userId" value="<%= user.getId() %>">
                                <select name="role">
                                    <option value="USER" <%= "USER".equals(role) ? "selected" : "" %>>USER</option>
                                    <option value="ADVISOR" <%= "ADVISOR".equals(role) ? "selected" : "" %>>ADVISOR</option>
                                    <option value="ADMIN" <%= "ADMIN".equals(role) ? "selected" : "" %>>ADMIN</option>
                                </select>
                                <button type="submit">Update role</button>
                            </form>
                        </td>
                        <td>
                            <% if (UserRoles.isUser(role)) { %>
                                <form class="inline"
                                      action="${pageContext.request.contextPath}/admin/users/assign-advisor"
                                      method="post">
                                    <%@ include file="includes/spring-csrf.jsp" %>
                                    <input type="hidden" name="csrfToken" value="${sessionScope.csrfToken}">
                                    <input type="hidden" name="userId" value="<%= user.getId() %>">
                                    <select name="advisorId">
                                        <option value="0">No advisor</option>
                                        <% if (advisors != null) {
                                            for (User advisor : advisors) {
                                                boolean selected = user.getAdvisorId() != null
                                                        && user.getAdvisorId().intValue() == advisor.getId();
                                        %>
                                            <option value="<%= advisor.getId() %>" <%= selected ? "selected" : "" %>>
                                                <%= HtmlUtils.htmlEscape(advisor.getFullName()) %>
                                            </option>
                                        <% } } %>
                                    </select>
                                    <button type="submit">Assign</button>
                                </form>
                            <% } else { %>
                                N/A
                            <% } %>
                        </td>
                    </tr>
                <% } } %>
                </tbody>
            </table>
        </div>
    </main>
</div>
</body>
</html>
