<%@ page import="java.util.List" %>
<%@ page import="edu.deakin.sit738.finsight.entity.Expense" %>
<%@ page import="edu.deakin.sit738.finsight.entity.Investment" %>
<%@ page import="edu.deakin.sit738.finsight.entity.Loan" %>
<%@ page import="edu.deakin.sit738.finsight.entity.FinancialGoal" %>

<%
    List<Expense> expenses =
            (List<Expense>) request.getAttribute("expenses");

    List<Investment> investments =
            (List<Investment>) request.getAttribute("investments");

    List<Loan> loans =
            (List<Loan>) request.getAttribute("loans");

    List<FinancialGoal> goals =
            (List<FinancialGoal>) request.getAttribute("goals");

    double totalExpenses = 0;
    double totalInvestments = 0;
    double totalLoans = 0;
    double totalGoalTarget = 0;
    double totalGoalCurrent = 0;

    if (expenses != null) {
        for (Expense expense : expenses) {
            totalExpenses += expense.getAmount();
        }
    }

    if (investments != null) {
        for (Investment investment : investments) {
            totalInvestments += investment.getCurrentValue();
        }
    }

    if (loans != null) {
        for (Loan loan : loans) {
            totalLoans += loan.getOutstandingBalance();
        }
    }

    if (goals != null) {
        for (FinancialGoal goal : goals) {
            totalGoalTarget += goal.getTargetAmount();
            totalGoalCurrent += goal.getCurrentAmount();
        }
    }

    double goalProgress = totalGoalTarget > 0
            ? (totalGoalCurrent / totalGoalTarget) * 100 : 0;

    if (goalProgress > 100) {
        goalProgress = 100;
    }
%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Dashboard | FinSight</title>

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

        .cards {
            display: grid;
            grid-template-columns: repeat(4, 1fr);
            gap: 18px;
            margin-bottom: 24px;
        }

        .card {
            background: white;
            border: 1px solid #e2e8f0;
            border-radius: 16px;
            padding: 24px;
            box-shadow: 0 4px 18px rgba(15, 23, 42, .04);
        }

        .card span {
            color: #64748b;
            font-size: 13px;
        }

        .card strong {
            display: block;
            font-size: 25px;
            margin-top: 9px;
        }

        .content {
            display: grid;
            grid-template-columns: 1.5fr 1fr;
            gap: 24px;
        }

        .panel {
            background: white;
            border: 1px solid #e2e8f0;
            border-radius: 16px;
            padding: 26px;
        }

        .panel h2 {
            margin: 0 0 20px;
            font-size: 20px;
        }

        .goal {
            margin-bottom: 20px;
        }

        .goal-header {
            display: flex;
            justify-content: space-between;
            gap: 12px;
            margin-bottom: 8px;
        }

        .goal-name {
            font-weight: bold;
        }

        .goal-amount {
            color: #64748b;
            font-size: 13px;
        }

        .progress {
            height: 9px;
            background: #e2e8f0;
            border-radius: 20px;
            overflow: hidden;
        }

        .progress-bar {
            height: 100%;
            background: #10b981;
            border-radius: 20px;
        }

        .links {
            display: grid;
            grid-template-columns: repeat(2, 1fr);
            gap: 12px;
        }

        .quick-link {
            display: block;
            text-decoration: none;
            background: #f8fafc;
            border-radius: 10px;
            padding: 18px;
            color: #172033;
            font-weight: bold;
            font-size: 14px;
        }

        .quick-link:hover {
            background: #eff6ff;
            color: #2563eb;
        }

        .empty {
            color: #64748b;
            padding: 20px 0;
        }

        @media (max-width: 1100px) {
            .cards {
                grid-template-columns: repeat(2, 1fr);
            }

            .content {
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

            .cards {
                grid-template-columns: 1fr;
            }

            .links {
                grid-template-columns: 1fr;
            }
        }
    </style>
</head>

<body>

<div class="layout">

    <aside class="sidebar">

        <div class="brand">
            Fin<span>Sight</span>
        </div>

        <div class="nav-title">Main menu</div>

        <nav class="nav">

            <a class="active"
               href="${pageContext.request.contextPath}/dashboard?userId=${userId}">
                Dashboard
            </a>

            <a href="${pageContext.request.contextPath}/expenses?userId=${userId}">
                Expenses
            </a>

            <a href="${pageContext.request.contextPath}/financial-insights?userId=${userId}">
                Financial Insights
            </a>

            <a href="${pageContext.request.contextPath}/investments?userId=${userId}">
                Investments
            </a>

            <a href="${pageContext.request.contextPath}/loans?userId=${userId}">
                Loans
            </a>

            <a href="${pageContext.request.contextPath}/goals?userId=${userId}">
                Financial Goals
            </a>

            <a href="${pageContext.request.contextPath}/transactions?userId=${userId}">
                Transactions
            </a>

            <a href="${pageContext.request.contextPath}/upload?userId=${userId}">
                Upload Statement
            </a>

            <a href="${pageContext.request.contextPath}/my-advisor">
                My Advisor
            </a>

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
                <h1>Financial Dashboard</h1>
                <p>Welcome back. Here is your financial overview.</p>
            </div>

            <div class="user">
                User ID: ${userId}
            </div>
        </div>

        <div class="cards">

            <div class="card">
                <span>Total expenses</span>
                <strong>
                    $<%= String.format("%.2f", totalExpenses) %>
                </strong>
            </div>

            <div class="card">
                <span>Investment Value</span>
                <strong>
                    $<%= String.format("%.2f", totalInvestments) %>
                </strong>
            </div>

            <div class="card">
                <span>Outstanding Loans</span>
                <strong>
                    $<%= String.format("%.2f", totalLoans) %>
                </strong>
            </div>

            <div class="card">
                <span>Financial Goals</span>
                <strong>
                    <%= goals != null ? goals.size() : 0 %>
                </strong>
            </div>

        </div>

        <div class="content">

            <div class="panel">

                <h2>Financial Goal Progress</h2>

                <% if (goals != null && !goals.isEmpty()) { %>

                    <% for (FinancialGoal goal : goals) {

                        double progress = goal.getTargetAmount() > 0
                                ? (goal.getCurrentAmount() / goal.getTargetAmount()) * 100 : 0;

                        if (progress > 100) {
                            progress = 100;
                        }
                    %>

                        <div class="goal">

                            <div class="goal-header">

                                <span class="goal-name">
                                    <%= goal.getGoalName() %>
                                </span>

                                <span class="goal-amount">
                                    $<%= String.format("%.2f", goal.getCurrentAmount()) %>
                                    /
                                    $<%= String.format("%.2f", goal.getTargetAmount()) %>
                                </span>

                            </div>

                            <div class="progress">
                                <div class="progress-bar"
                                     style="width:<%= progress %>%">
                                </div>
                            </div>

                        </div>

                    <% } %>

                <% } else { %>

                    <div class="empty">
                        No financial goals recorded yet.
                    </div>

                <% } %>

            </div>

            <div class="panel">

                <h2>Quick Access</h2>

                <div class="links">

                    <a class="quick-link"
                       href="${pageContext.request.contextPath}/expenses?userId=${userId}">
                        Manage Expenses
                    </a>

                    <a class="quick-link"
                       href="${pageContext.request.contextPath}/investments?userId=${userId}">
                        View Investments
                    </a>

                    <a class="quick-link"
                       href="${pageContext.request.contextPath}/loans?userId=${userId}">
                        Manage Loans
                    </a>

                    <a class="quick-link"
                       href="${pageContext.request.contextPath}/goals?userId=${userId}">
                        Track Goals
                    </a>

                    <a class="quick-link"
                       href="${pageContext.request.contextPath}/transactions?userId=${userId}">
                        View Transactions
                    </a>

                    <a class="quick-link"
                       href="${pageContext.request.contextPath}/upload?userId=${userId}">
                        Upload Statement
                    </a>

                </div>

            </div>

        </div>

    </main>

</div>

</body>
</html>