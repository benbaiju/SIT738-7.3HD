<%@ page import="java.util.List" %>
<%@ page import="edu.deakin.sit738.finsight.entity.Expense" %>
<%@ page import="org.springframework.validation.ObjectError" %>
<%@ page import="org.springframework.web.util.HtmlUtils" %>

<%
    List<Expense> expenses = (List<Expense>) request.getAttribute("expenses");
    List<ObjectError> validationErrors =
            (List<ObjectError>) request.getAttribute("validationErrors");

    double totalExpenses = 0;

    if (expenses != null) {
        for (Expense expenseRecord : expenses) {
            totalExpenses += expenseRecord.getAmount();
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

        input,
        select {
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

        .error {
            background: #fee2e2;
            color: #991b1b;
            border-radius: 10px;
            padding: 14px;
            margin-bottom: 18px;
            font-size: 14px;
        }

        .error ul {
            margin: 0;
            padding-left: 18px;
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

            <a class="active"
               href="${pageContext.request.contextPath}/expenses?userId=${userId}">
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

            <a class="logout"
               href="${pageContext.request.contextPath}/logout">
                Logout
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

                <% if (validationErrors != null && !validationErrors.isEmpty()) { %>
                    <div class="error">
                        <ul>
                            <% for (ObjectError error : validationErrors) { %>
                                <li><%= HtmlUtils.htmlEscape(error.getDefaultMessage()) %></li>
                            <% } %>
                        </ul>
                    </div>
                <% } %>

                <form id="addExpenseForm"
                      action="${pageContext.request.contextPath}/expenses/add"
                      method="post"
                      onsubmit="return validateExpenseForm();">

                    <input type="hidden"
                           name="csrfToken"
                           value="${sessionScope.csrfToken}">

                    <input type="hidden"
                           name="userId"
                           value="${userId}">

                    <label class="label" for="description">Description</label>
                    <input type="text"
                           id="description"
                           name="description"
                           maxlength="100"
                           pattern="[A-Za-z0-9\s.,'\-]{1,100}"
                           title="Letters, numbers, spaces, and . , ' - only"
                           required>

                    <label class="label" for="category">Category</label>
                    <select id="category" name="category" required>
                        <option value="">Select category</option>
                        <option value="Housing">Housing</option>
                        <option value="Food">Food</option>
                        <option value="Transport">Transport</option>
                        <option value="Loans">Loans</option>
                        <option value="Other">Other</option>
                    </select>

                    <label class="label" for="amount">Amount</label>
                    <input type="number"
                           id="amount"
                           name="amount"
                           step="0.01"
                           min="0.01"
                           required>

                    <button type="submit">Add Expense</button>

                </form>

            </div>

            <div class="card">

                <h2>Expense Records</h2>

                <form class="search-form"
                      action="${pageContext.request.contextPath}/expenses/search"
                      method="post">

                    <input type="hidden"
                           name="userId"
                           value="${userId}">

                    <label class="label">Search by description</label>
                    <input type="text" name="description" required>

                    <button type="submit">Search</button>

                </form>

                <div class="summary">

                    <span>Total expenses</span>

                    <strong>
                        $<%= String.format("%.2f", totalExpenses) %>
                    </strong>

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

                            for (Expense expenseRecord : expenses) {

                                String safeDescription = expenseRecord.getDescription() == null
                                        ? ""
                                        : HtmlUtils.htmlEscape(expenseRecord.getDescription());

                                String safeCategory = expenseRecord.getCategory() == null
                                        ? ""
                                        : HtmlUtils.htmlEscape(expenseRecord.getCategory());
                        %>

                            <tr>

                                <td><%= safeDescription %></td>

                                <td><%= safeCategory %></td>

                                <td>
                                    $<%= String.format("%.2f", expenseRecord.getAmount()) %>
                                </td>

                                <td>

                                    <form action="${pageContext.request.contextPath}/expenses/delete"
                                          method="post">

                                        <input type="hidden"
                                               name="csrfToken"
                                               value="${sessionScope.csrfToken}">

                                        <input type="hidden"
                                               name="id"
                                               value="<%= expenseRecord.getId() %>">

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

<script>
    function validateExpenseForm() {
        var description = document.getElementById("description").value.trim();
        var category = document.getElementById("category").value;
        var amount = parseFloat(document.getElementById("amount").value);
        var descriptionPattern = /^[A-Za-z0-9\s.,'\-]{1,100}$/;
        var allowedCategories = ["Housing", "Food", "Transport", "Loans", "Other"];

        if (!descriptionPattern.test(description)) {
            alert("Invalid description. Use letters, numbers, spaces, and . , ' - only (max 100 characters).");
            return false;
        }

        if (allowedCategories.indexOf(category) === -1) {
            alert("Please select a valid category.");
            return false;
        }

        if (isNaN(amount) || amount < 0.01) {
            alert("Amount must be greater than 0.");
            return false;
        }

        return true;
    }
</script>

</body>
</html>