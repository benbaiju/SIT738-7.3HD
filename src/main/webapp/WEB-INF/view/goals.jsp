<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="edu.deakin.sit738.finsight.entity.FinancialGoal" %>

<%
    List<FinancialGoal> goals =
        (List<FinancialGoal>) request.getAttribute("goals");

    double totalTarget = 0;
    double totalCurrent = 0;

    if (goals != null) {
        for (FinancialGoal goal : goals) {
            totalTarget += goal.getTargetAmount();
            totalCurrent += goal.getCurrentAmount();
        }
    }

    double totalProgress =
        totalTarget > 0 ? (totalCurrent / totalTarget) * 100 : 0;

    if (totalProgress > 100) {
        totalProgress = 100;
    }
%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Financial Goals | FinSight</title>

    <style>
        * {
            box-sizing: border-box;
        }

        body {
            margin: 0;
            font-family: Arial, sans-serif;
            background: #f5f7fb;
            color: #172033;
        }

        .layout {
            display: flex;
            min-height: 100vh;
        }

        .sidebar {
            width: 250px;
            background: #172033;
            color: white;
            padding: 28px 20px;
        }

        .brand {
            font-size: 24px;
            font-weight: bold;
            margin-bottom: 38px;
        }

        .brand span {
            color: #6ee7b7;
        }

        .nav-title {
            color: #94a3b8;
            font-size: 12px;
            text-transform: uppercase;
            margin: 25px 12px 12px;
        }

        .nav a {
            display: block;
            color: #cbd5e1;
            text-decoration: none;
            padding: 13px 12px;
            border-radius: 8px;
            margin-bottom: 5px;
            font-size: 14px;
        }

        .nav a:hover,
        .nav a.active {
            background: #26344d;
            color: white;
        }

        .nav form.logout-form {
            margin-top: 25px;
        }

        .nav form.logout-form button.logout {
            display: block;
            width: 100%;
            text-align: left;
            border: none;
            border-radius: 8px;
            padding: 13px 12px;
            margin-bottom: 5px;
            font-size: 14px;
            font-weight: normal;
            cursor: pointer;
            background: #7f1d1d;
            color: white;
        }

        .nav form.logout-form button.logout:hover {
            background: #991b1b;
        }

        .main {
            flex: 1;
            padding: 34px;
        }

        .topbar {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 32px;
        }

        .topbar h1 {
            margin: 0;
            font-size: 28px;
        }

        .topbar p {
            color: #64748b;
            margin: 8px 0 0;
        }

        .user {
            background: white;
            border: 1px solid #e2e8f0;
            border-radius: 10px;
            padding: 10px 16px;
            font-size: 14px;
            color: #475569;
        }

        .grid {
            display: grid;
            grid-template-columns: 1fr 1.8fr;
            gap: 24px;
        }

        .card {
            background: white;
            border: 1px solid #e2e8f0;
            border-radius: 16px;
            padding: 26px;
            box-shadow: 0 4px 18px rgba(15, 23, 42, .04);
        }

        .card h2 {
            margin: 0 0 22px;
            font-size: 20px;
        }

        .label {
            display: block;
            font-size: 13px;
            font-weight: bold;
            color: #475569;
            margin-bottom: 7px;
        }

        input,
        textarea {
            width: 100%;
            padding: 12px;
            border: 1px solid #cbd5e1;
            border-radius: 8px;
            margin-bottom: 16px;
            font-size: 14px;
            font-family: Arial, sans-serif;
        }

        textarea {
            min-height: 80px;
            resize: vertical;
        }

        button {
            border: none;
            border-radius: 8px;
            background: #2563eb;
            color: white;
            padding: 12px 20px;
            font-weight: bold;
            cursor: pointer;
        }

        button:hover {
            background: #1d4ed8;
        }

        .summary-grid {
            display: grid;
            grid-template-columns: repeat(3, 1fr);
            gap: 12px;
            margin-bottom: 22px;
        }

        .summary {
            background: #f8fafc;
            border-radius: 10px;
            padding: 16px;
        }

        .summary span {
            color: #64748b;
            font-size: 12px;
        }

        .summary strong {
            display: block;
            font-size: 20px;
            margin-top: 7px;
        }

        .progress {
            height: 9px;
            background: #e2e8f0;
            border-radius: 20px;
            overflow: hidden;
            margin-top: 8px;
        }

        .progress-bar {
            height: 100%;
            background: #10b981;
            border-radius: 20px;
        }

        .table-wrap {
            overflow-x: auto;
        }

        table {
            width: 100%;
            border-collapse: collapse;
            min-width: 700px;
        }

        th,
        td {
            text-align: left;
            padding: 14px 10px;
            border-bottom: 1px solid #e2e8f0;
            font-size: 14px;
        }

        th {
            color: #64748b;
            font-size: 12px;
            text-transform: uppercase;
        }

        .delete {
            background: #fee2e2;
            color: #b91c1c;
            padding: 8px 12px;
        }

        .empty {
            text-align: center;
            color: #64748b;
            padding: 30px;
        }

        @media (max-width: 1000px) {
            .grid {
                grid-template-columns: 1fr;
            }
        }

        @media (max-width: 600px) {
            .layout {
                display: block;
            }

            .sidebar {
                width: 100%;
            }

            .nav {
                display: flex;
                gap: 6px;
                overflow-x: auto;
            }

            .nav a {
                white-space: nowrap;
            }

            .main {
                padding: 20px;
            }

            .topbar {
                display: block;
            }

            .user {
                display: inline-block;
                margin-top: 16px;
            }

            .summary-grid {
                grid-template-columns: 1fr;
            }
        }
    </style>
</head>

<body>
<div class="layout">

    <aside class="sidebar">
        <div class="brand">Fin<span>Sight</span></div>

        <div class="nav-title">Main menu</div>

        <nav class="nav">
            <a href="${pageContext.request.contextPath}/dashboard?userId=${userId}">Dashboard</a>
            <a href="${pageContext.request.contextPath}/expenses?userId=${userId}">Expenses</a>
            <a href="${pageContext.request.contextPath}/financial-insights?userId=${userId}">Financial Insights</a>
            <a href="${pageContext.request.contextPath}/investments?userId=${userId}">Investments</a>
            <a href="${pageContext.request.contextPath}/loans?userId=${userId}">Loans</a>
            <a class="active" href="${pageContext.request.contextPath}/goals?userId=${userId}">Financial Goals</a>
            <a href="${pageContext.request.contextPath}/transactions?userId=${userId}">Transactions</a>
            <a href="${pageContext.request.contextPath}/upload?userId=${userId}">Upload Statement</a>
            <form class="logout-form"
                  action="${pageContext.request.contextPath}/logout"
                  method="post">
                <%@ include file="includes/spring-csrf.jsp" %>
                <input type="hidden"
                       name="csrfToken"
                       value="${sessionScope.csrfToken}">
                <button class="logout" type="submit">Logout</button>
            </form>
        </nav>
    </aside>

    <main class="main">

        <div class="topbar">
            <div>
                <h1>Financial Goals</h1>
                <p>Plan and track your financial achievements.</p>
            </div>

            <div class="user">User ID: ${userId}</div>
        </div>

        <div class="grid">

            <div class="card">
                <h2>Add Financial Goal</h2>

                <form action="${pageContext.request.contextPath}/goals/add" method="post">
                    <%@ include file="includes/spring-csrf.jsp" %>
                    <input type="hidden"
                           name="csrfToken"
                           value="${sessionScope.csrfToken}">

                    <input type="hidden" name="userId" value="${userId}">

                    <label class="label">Goal Name</label>
                    <input type="text" name="goalName" required>

                    <label class="label">Description</label>
                    <textarea name="description"></textarea>

                    <label class="label">Target Amount</label>
                    <input type="number" name="targetAmount" step="0.01" min="0" required>

                    <label class="label">Current Amount</label>
                    <input type="number" name="currentAmount" step="0.01" min="0" required>

                    <button type="submit">Add Goal</button>

                </form>
            </div>

            <div class="card">
                <h2>Your Financial Goals</h2>

                <div class="summary-grid">

                    <div class="summary">
                        <span>Total target</span>
                        <strong>$<%= String.format("%.2f", totalTarget) %></strong>
                    </div>

                    <div class="summary">
                        <span>Total saved</span>
                        <strong>$<%= String.format("%.2f", totalCurrent) %></strong>
                    </div>

                    <div class="summary">
                        <span>Overall progress</span>
                        <strong><%= String.format("%.1f", totalProgress) %>%</strong>
                    </div>

                </div>

                <div class="table-wrap">
                    <table>

                        <tr>
                            <th>Goal</th>
                            <th>Description</th>
                            <th>Target</th>
                            <th>Current</th>
                            <th>Progress</th>
                            <th>Action</th>
                        </tr>

                        <% if (goals != null && !goals.isEmpty()) {

                            for (FinancialGoal goal : goals) {

                                double progress = goal.getTargetAmount() > 0
                                    ? (goal.getCurrentAmount() / goal.getTargetAmount()) * 100
                                    : 0;

                                if (progress > 100) {
                                    progress = 100;
                                }
                        %>

                        <tr>

                            <td><%= goal.getGoalName() %></td>

                            <td><%= goal.getDescription() %></td>

                            <td>$<%= String.format("%.2f", goal.getTargetAmount()) %></td>

                            <td>$<%= String.format("%.2f", goal.getCurrentAmount()) %></td>

                            <td>
                                <%= String.format("%.1f", progress) %>%

                                <div class="progress">
                                    <div class="progress-bar"
                                         style="width: <%= progress %>%"></div>
                                </div>
                            </td>

                            <td>
                                <form action="${pageContext.request.contextPath}/goals/delete" method="post">
                                    <%@ include file="includes/spring-csrf.jsp" %>
                                    <input type="hidden"
                                           name="csrfToken"
                                           value="${sessionScope.csrfToken}">

                                    <input type="hidden"
                                           name="id"
                                           value="<%= goal.getId() %>">

                                    <input type="hidden"
                                           name="userId"
                                           value="${userId}">

                                    <button class="delete" type="submit">Delete</button>

                                </form>
                            </td>

                        </tr>

                        <% } } else { %>

                        <tr>
                            <td class="empty" colspan="6">
                                No financial goals recorded yet.
                            </td>
                        </tr>

                        <% } %>

                    </table>
                </div>

            </div>

        </div>

    </main>

</div>
</body>
</html>