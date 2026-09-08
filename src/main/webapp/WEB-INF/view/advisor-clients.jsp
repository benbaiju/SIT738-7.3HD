<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="edu.deakin.sit738.finsight.entity.User" %>
<%@ page import="org.springframework.web.util.HtmlUtils" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>My Clients | FinSight</title>
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
        .card { background: white; border: 1px solid #e2e8f0; border-radius: 16px; padding: 26px; }
        table { width: 100%; border-collapse: collapse; margin-top: 18px; }
        th, td { text-align: left; padding: 12px 8px; border-bottom: 1px solid #e2e8f0; font-size: 14px; }
        th { color: #64748b; font-size: 12px; text-transform: uppercase; }
        .button { display: inline-block; background: #2563eb; color: white; text-decoration: none; padding: 8px 12px; border-radius: 8px; font-size: 13px; }
        .error { background: #fee2e2; color: #b91c1c; padding: 12px; border-radius: 8px; margin-bottom: 16px; }
        .empty { color: #64748b; padding: 20px 0; }
    </style>
</head>
<body>
<%
    List<User> clients = (List<User>) request.getAttribute("clients");
%>
<div class="layout">
    <aside class="sidebar">
        <div class="brand">Fin<span>Sight</span></div>
        <nav class="nav">
            <a class="active" href="${pageContext.request.contextPath}/advisor/clients">My Clients</a>
            <form class="logout-form" action="${pageContext.request.contextPath}/logout" method="post">
                <input type="hidden" name="csrfToken" value="${sessionScope.csrfToken}">
                <input type="hidden"
                       name="${springCsrfParameterName}"
                       value="${springCsrfToken}">
                <button class="logout" type="submit">Logout</button>
            </form>
        </nav>
    </aside>
    <main class="main">
        <div class="card">
            <h1>My Clients</h1>
            <p>View financial insights for clients assigned to you.</p>

            <% if (request.getAttribute("error") != null) { %>
                <div class="error"><%= HtmlUtils.htmlEscape(String.valueOf(request.getAttribute("error"))) %></div>
            <% } %>

            <% if (clients == null || clients.isEmpty()) { %>
                <div class="empty">No clients are currently assigned to you.</div>
            <% } else { %>
                <table>
                    <thead>
                    <tr>
                        <th>Name</th>
                        <th>Email</th>
                        <th>Action</th>
                    </tr>
                    </thead>
                    <tbody>
                    <% for (User client : clients) { %>
                        <tr>
                            <td><%= HtmlUtils.htmlEscape(client.getFullName()) %></td>
                            <td><%= HtmlUtils.htmlEscape(client.getEmail()) %></td>
                            <td>
                                <a class="button"
                                   href="${pageContext.request.contextPath}/advisor/client-insights?clientId=<%= client.getId() %>">
                                    View insights
                                </a>
                            </td>
                        </tr>
                    <% } %>
                    </tbody>
                </table>
            <% } %>
        </div>
    </main>
</div>
</body>
</html>
