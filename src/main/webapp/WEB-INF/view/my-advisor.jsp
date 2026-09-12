<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="edu.deakin.sit738.finsight.entity.User" %>
<%@ page import="org.springframework.web.util.HtmlUtils" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>My Advisor | FinSight</title>
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
            padding: 13px 12px; margin-bottom: 5px; font-size: 14px; font-weight: normal;
            cursor: pointer; background: #7f1d1d; color: white;
        }
        .main { flex: 1; padding: 34px; }
        .card { background: white; border: 1px solid #e2e8f0; border-radius: 16px; padding: 26px; max-width: 640px; }
        .card h1 { margin: 0 0 12px; font-size: 24px; }
        .card p { color: #64748b; }
        .label { display: block; font-size: 13px; font-weight: bold; color: #475569; margin: 18px 0 7px; }
        select, button { width: 100%; padding: 12px; border-radius: 8px; font-size: 14px; }
        select { border: 1px solid #cbd5e1; margin-bottom: 16px; }
        button { border: none; background: #2563eb; color: white; font-weight: bold; cursor: pointer; }
        .current { background: #f8fafc; border-radius: 10px; padding: 14px; margin: 16px 0; }
        .error { background: #fee2e2; color: #b91c1c; padding: 12px; border-radius: 8px; }
    </style>
</head>
<body>
<%
    User client = (User) request.getAttribute("client");
    User currentAdvisor = (User) request.getAttribute("currentAdvisor");
    List<User> advisors = (List<User>) request.getAttribute("advisors");
%>
<div class="layout">
    <aside class="sidebar">
        <div class="brand">Fin<span>Sight</span></div>
        <nav class="nav">
            <a href="${pageContext.request.contextPath}/dashboard">Dashboard</a>
            <a href="${pageContext.request.contextPath}/expenses">Expenses</a>
            <a href="${pageContext.request.contextPath}/financial-insights">Financial Insights</a>
            <a class="active" href="${pageContext.request.contextPath}/my-advisor">My Advisor</a>
            <form class="logout-form" action="${pageContext.request.contextPath}/logout" method="post">
                <%@ include file="includes/spring-csrf.jsp" %>
                <input type="hidden" name="csrfToken" value="${sessionScope.csrfToken}">
                <button class="logout" type="submit">Logout</button>
            </form>
        </nav>
    </aside>
    <main class="main">
        <div class="card">
            <h1>My Advisor</h1>
            <p>Select the advisor assigned to review your financial insights.</p>

            <% if (request.getAttribute("error") != null) { %>
                <div class="error"><%= HtmlUtils.htmlEscape(String.valueOf(request.getAttribute("error"))) %></div>
            <% } %>

            <div class="current">
                <strong>Current advisor:</strong>
                <% if (currentAdvisor != null) { %>
                    <%= HtmlUtils.htmlEscape(currentAdvisor.getFullName()) %>
                    (<%= HtmlUtils.htmlEscape(currentAdvisor.getEmail()) %>)
                <% } else { %>
                    None selected
                <% } %>
            </div>

            <form action="${pageContext.request.contextPath}/my-advisor/select" method="post">
                <%@ include file="includes/spring-csrf.jsp" %>
                <input type="hidden" name="csrfToken" value="${sessionScope.csrfToken}">
                <label class="label" for="advisorId">Choose advisor</label>
                <select id="advisorId" name="advisorId">
                    <option value="0">No advisor</option>
                    <% if (advisors != null) {
                        for (User advisor : advisors) {
                            boolean selected = client != null
                                    && client.getAdvisorId() != null
                                    && client.getAdvisorId().intValue() == advisor.getId();
                    %>
                        <option value="<%= advisor.getId() %>" <%= selected ? "selected" : "" %>>
                            <%= HtmlUtils.htmlEscape(advisor.getFullName()) %>
                            — <%= HtmlUtils.htmlEscape(advisor.getEmail()) %>
                        </option>
                    <% } } %>
                </select>
                <button type="submit">Save advisor</button>
            </form>
        </div>
    </main>
</div>
</body>
</html>
