<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.Locale" %>
<%@ page import="edu.deakin.sit738.finsight.entity.User" %>
<%@ page import="org.springframework.web.util.HtmlUtils" %>
<%
    Map<String, Object> insights =
            (Map<String, Object>) request.getAttribute("insights");
    User client = (User) request.getAttribute("client");

    double totalExpenses = (Double) insights.get("totalExpenses");
    double housingExpenses = (Double) insights.get("housingExpenses");
    double foodExpenses = (Double) insights.get("foodExpenses");
    double transportExpenses = (Double) insights.get("transportExpenses");
    double loanExpenses = (Double) insights.get("loanExpenses");
    double otherExpenses = (Double) insights.get("otherExpenses");
    String inferredInsight = (String) insights.get("inferredInsight");
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Client Insights | FinSight</title>
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
        .grid { display: grid; grid-template-columns: repeat(3, 1fr); gap: 14px; margin: 22px 0; }
        .summary { background: #f8fafc; border-radius: 10px; padding: 16px; }
        .summary span { color: #64748b; font-size: 12px; }
        .summary strong { display: block; font-size: 20px; margin-top: 7px; }
        .insight { background: #eff6ff; border-radius: 10px; padding: 16px; }
        .back { display: inline-block; margin-top: 18px; color: #2563eb; text-decoration: none; }
    </style>
</head>
<body>
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
            <h1>Client financial insights</h1>
            <p>
                Client:
                <% if (client != null) { %>
                    <%= HtmlUtils.htmlEscape(client.getFullName()) %>
                    (<%= HtmlUtils.htmlEscape(client.getEmail()) %>)
                <% } %>
            </p>

            <div class="grid">
                <div class="summary"><span>Total</span><strong>$<%= String.format(Locale.US, "%.2f", totalExpenses) %></strong></div>
                <div class="summary"><span>Housing</span><strong>$<%= String.format(Locale.US, "%.2f", housingExpenses) %></strong></div>
                <div class="summary"><span>Food</span><strong>$<%= String.format(Locale.US, "%.2f", foodExpenses) %></strong></div>
                <div class="summary"><span>Transport</span><strong>$<%= String.format(Locale.US, "%.2f", transportExpenses) %></strong></div>
                <div class="summary"><span>Loans</span><strong>$<%= String.format(Locale.US, "%.2f", loanExpenses) %></strong></div>
                <div class="summary"><span>Other</span><strong>$<%= String.format(Locale.US, "%.2f", otherExpenses) %></strong></div>
            </div>

            <div class="insight">
                <%= HtmlUtils.htmlEscape(inferredInsight == null ? "" : inferredInsight) %>
            </div>

            <a class="back" href="${pageContext.request.contextPath}/advisor/clients">← Back to clients</a>
        </div>
    </main>
</div>
</body>
</html>
