<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.Locale" %>

<%
    Map<String, Object> insights =
            (Map<String, Object>) request.getAttribute("insights");

    double totalExpenses =
            (Double) insights.get("totalExpenses");

    double housingExpenses =
            (Double) insights.get("housingExpenses");

    double foodExpenses =
            (Double) insights.get("foodExpenses");

    double transportExpenses =
            (Double) insights.get("transportExpenses");

    double loanExpenses =
            (Double) insights.get("loanExpenses");

    double otherExpenses =
            (Double) insights.get("otherExpenses");

    String inferredInsight =
            (String) insights.get("inferredInsight");

    double housingPercent = 0;
    double foodPercent = 0;
    double transportPercent = 0;
    double loanPercent = 0;
    double otherPercent = 0;

    if (totalExpenses > 0) {
        housingPercent = (housingExpenses / totalExpenses) * 100;
        foodPercent = (foodExpenses / totalExpenses) * 100;
        transportPercent = (transportExpenses / totalExpenses) * 100;
        loanPercent = (loanExpenses / totalExpenses) * 100;
        otherPercent = (otherExpenses / totalExpenses) * 100;
    }
%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <title>Financial Insights | FinSight</title>

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
            width: 230px;
            background: #172033;
            color: white;
            padding: 28px 20px;
        }

        .brand {
            font-size: 24px;
            font-weight: bold;
            margin-bottom: 35px;
        }

        .brand span {
            color: #6ee7b7;
        }

        .nav a {
            display: block;
            color: #cbd5e1;
            text-decoration: none;
            padding: 12px;
            border-radius: 8px;
            margin-bottom: 6px;
            font-size: 14px;
        }

        .nav a:hover,
        .nav a.active {
            background: #26344d;
            color: white;
        }

        .nav a.logout {
            margin-top: 25px;
            background: #7f1d1d;
            color: white;
        }

        .nav a.logout:hover {
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
            margin-bottom: 30px;
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
            grid-template-columns: repeat(3, 1fr);
            gap: 18px;
            margin-bottom: 24px;
        }

        .card {
            background: white;
            border: 1px solid #e2e8f0;
            border-radius: 14px;
            padding: 24px;
            box-shadow: 0 4px 18px rgba(15, 23, 42, .04);
        }

        .label {
            color: #64748b;
            font-size: 13px;
        }

        .amount {
            display: block;
            margin-top: 10px;
            font-size: 26px;
            font-weight: bold;
            color: #1d4ed8;
        }

        .section-title {
            margin: 0 0 20px;
            font-size: 19px;
        }

        .category {
            display: flex;
            justify-content: space-between;
            padding: 14px 0;
            border-bottom: 1px solid #e2e8f0;
            font-size: 14px;
        }

        .category:last-child {
            border-bottom: none;
        }

        .insight {
            background: #eff6ff;
            border: 1px solid #bfdbfe;
            border-radius: 12px;
            padding: 20px;
            margin-top: 24px;
            color: #1e40af;
            line-height: 1.6;
        }

        .insight p {
            margin-bottom: 0;
        }

        .warning {
            background: #fff7ed;
            border: 1px solid #fed7aa;
            border-radius: 12px;
            padding: 20px;
            margin-top: 24px;
            color: #9a3412;
            line-height: 1.6;
        }

        .button {
            display: inline-block;
            margin-top: 24px;
            padding: 12px 18px;
            border-radius: 8px;
            background: #2563eb;
            color: white;
            text-decoration: none;
            font-size: 14px;
        }

        .button:hover {
            background: #1d4ed8;
        }

        @media (max-width: 900px) {
            .cards {
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
        }
    </style>
</head>

<body>

<div class="layout">

    <aside class="sidebar">

        <div class="brand">
            Fin<span>Sight</span>
        </div>

        <nav class="nav">

            <a href="${pageContext.request.contextPath}/dashboard?userId=${userId}">
                Dashboard
            </a>

            <a href="${pageContext.request.contextPath}/expenses?userId=${userId}">
                Expenses
            </a>

            <a class="active"
               href="${pageContext.request.contextPath}/financial-insights?userId=${userId}">
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

            <a class="logout"
               href="${pageContext.request.contextPath}/logout">
                Logout
            </a>

        </nav>

    </aside>

    <main class="main">

        <div class="topbar">

            <div>
                <h1>Financial Insights</h1>
                <p>Summary of your recorded financial information.</p>
            </div>

            <div class="user">
                User ID: ${userId}
            </div>

        </div>

        <div class="cards">

            <div class="card">
                <span class="label">Total expenses</span>
                <span class="amount">
                    $<%= String.format(Locale.US, "%.2f", totalExpenses) %>
                </span>
            </div>

            <div class="card">
                <span class="label">Housing expenses</span>
                <span class="amount">
                    $<%= String.format(Locale.US, "%.2f", housingExpenses) %>
                </span>
            </div>

            <div class="card">
                <span class="label">Loan expenses</span>
                <span class="amount">
                    $<%= String.format(Locale.US, "%.2f", loanExpenses) %>
                </span>
            </div>

        </div>

        <div class="card">

            <h2 class="section-title">Spending breakdown</h2>

            <div class="category">
                <span>Housing</span>
                <strong>
                    $<%= String.format(Locale.US, "%.2f", housingExpenses) %>
                    (<%= String.format(Locale.US, "%.2f", housingPercent) %>%)
                </strong>
            </div>

            <div class="category">
                <span>Food</span>
                <strong>
                    $<%= String.format(Locale.US, "%.2f", foodExpenses) %>
                    (<%= String.format(Locale.US, "%.2f", foodPercent) %>%)
                </strong>
            </div>

            <div class="category">
                <span>Transport</span>
                <strong>
                    $<%= String.format(Locale.US, "%.2f", transportExpenses) %>
                    (<%= String.format(Locale.US, "%.2f", transportPercent) %>%)
                </strong>
            </div>

            <div class="category">
                <span>Loans</span>
                <strong>
                    $<%= String.format(Locale.US, "%.2f", loanExpenses) %>
                    (<%= String.format(Locale.US, "%.2f", loanPercent) %>%)
                </strong>
            </div>

            <div class="category">
                <span>Other</span>
                <strong>
                    $<%= String.format(Locale.US, "%.2f", otherExpenses) %>
                    (<%= String.format(Locale.US, "%.2f", otherPercent) %>%)
                </strong>
            </div>

        </div>

        <div class="insight">
            <p><%= inferredInsight %></p>
        </div>

        <a class="button"
           href="${pageContext.request.contextPath}/expenses?userId=${userId}">
            Back to Expenses
        </a>

    </main>

</div>

</body>
</html>