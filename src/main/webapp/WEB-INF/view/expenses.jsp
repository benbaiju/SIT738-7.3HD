<%@ page import="java.util.List" %>
<%@ page import="edu.deakin.sit738.finsight.entity.Expense" %>

<%
    List<Expense> expenses = (List<Expense>) request.getAttribute("expenses");

    double totalExpenses = 0;

    if (expenses != null) {
        for (Expense expense : expenses) {
            totalExpenses += expense.getAmount();
        }
    }
%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Expenses | FinSight</title>

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
            grid-template-columns: 1fr 1.5fr;
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

        input {
            width: 100%;
            padding: 12px;
            border: 1px solid #cbd5e1;
            border-radius: 8px;
            margin-bottom: 16px;
            font-size: 14px;
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

        .search-form {
            margin-bottom: 24px;
            padding-bottom: 24px;
            border-bottom: 1px solid #e2e8f0;
        }

        .summary {
            background: #eff6ff;
            border-radius: 10px;
            padding: 18px;
            margin-bottom: 22px;
        }

        .summary span {
            color: #64748b;
            font-size: 13px;
        }

        .summary strong {
            display: block;
            font-size: 25px;
            margin-top: 6px;
            color: #1d4ed8;
        }

        .table-wrap {
            overflow-x: auto;
        }

        table {
            width: 100%;
            border-collapse: collapse;
            min-width: 480px;
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

        @media (max-width: 900px) {
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
        }
    </style>
</head>

<body>

<div class="layout">

    <aside class="sidebar">

        <div class="brand">Fin<span>Sight</span></div>

        <div class="nav-title">Main menu</div>

        <nav class="nav">
            <a href="${pageContext.request.contextPath}/dashboard?userId=${userId}">
                Dashboard
            </a>

            <a class="active" href="${pageContext.request.contextPath}/expenses?userId=${userId}">
                Expenses
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
        </nav>

    </aside>

    <main class="main">

        <div class="topbar">

            <div>
                <h1>Expenses</h1>
                <p>Track and manage your spending.</p>
            </div>

            <div class="user">
                User ID: ${userId}
            </div>

        </div>

        <div class="grid">

            <div class="card">

                <h2>Add Expense</h2>

                <form action="${pageContext.request.contextPath}/expenses/add" method="post">

                    <input type="hidden" name="userId" value="${userId}">

                    <label class="label">Description</label>
                    <input type="text" name="description" required>

                    <label class="label">Category</label>
                    <input type="text" name="category" required>

                    <label class="label">Amount</label>
                    <input type="number" name="amount" step="0.01" min="0" required>

                    <button type="submit">Add Expense</button>

                </form>

            </div>

            <div class="card">

                <h2>Expense Records</h2>

                <form class="search-form"
                      action="${pageContext.request.contextPath}/expenses/search"
                      method="post">

                    <input type="hidden" name="userId" value="${userId}">

                    <label class="label">Search by description</label>

                    <input type="text" name="description" required>

                    <button type="submit">Search</button>

                </form>

                <div class="summary">
                    <span>Total expenses</span>
                    <strong>$<%= String.format("%.2f", totalExpenses) %></strong>
                </div>

                <div class="table-wrap">

                    <table>

                        <tr>
                            <th>Description</th>
                            <th>Category</th>
                            <th>Amount</th>
                            <th>Action</th>
                        </tr>

                        <% if (expenses != null && !expenses.isEmpty()) {

                            for (Expense expense : expenses) { %>

                            <tr>

                                <td><%= expense.getDescription() %></td>

                                <td><%= expense.getCategory() %></td>

                                <td>$<%= String.format("%.2f", expense.getAmount()) %></td>

                                <td>

                                    <form action="${pageContext.request.contextPath}/expenses/delete"
                                          method="post">

                                        <input type="hidden"
                                               name="id"
                                               value="<%= expense.getId() %>">

                                        <input type="hidden"
                                               name="userId"
                                               value="${userId}">

                                        <button class="delete" type="submit">
                                            Delete
                                        </button>

                                    </form>

                                </td>

                            </tr>

                        <% }

                        } else { %>

                            <tr>
                                <td class="empty" colspan="4">
                                    No expenses recorded yet.
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