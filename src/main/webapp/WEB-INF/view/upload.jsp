<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="org.springframework.web.util.HtmlUtils" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Upload Statement | FinSight</title>
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

        .card {
            max-width: 850px;
            background: white;
            border: 1px solid #e2e8f0;
            border-radius: 16px;
            padding: 30px;
            box-shadow: 0 4px 18px rgba(15, 23, 42, 0.04);
        }

        .card h2 {
            margin: 0 0 8px;
            font-size: 21px;
        }

        .card > p {
            color: #64748b;
            margin-bottom: 26px;
        }

        .option {
            border: 1px solid #e2e8f0;
            border-radius: 14px;
            padding: 24px;
            margin-bottom: 22px;
        }

        .option h3 {
            margin: 0 0 8px;
            font-size: 17px;
        }

        .option p {
            margin: 0 0 20px;
            font-size: 14px;
        }

        .upload-box {
            border: 2px dashed #cbd5e1;
            border-radius: 12px;
            padding: 30px 20px;
            text-align: center;
            background: #f8fafc;
            margin-bottom: 20px;
        }

        .upload-box h3 {
            margin: 0 0 8px;
            font-size: 16px;
        }

        .upload-box p {
            margin: 0 0 20px;
            font-size: 14px;
        }

        input[type="file"] {
            display: block;
            width: 100%;
            max-width: 420px;
            margin: 0 auto;
            padding: 12px;
            border: 1px solid #cbd5e1;
            border-radius: 8px;
            background: white;
            font-size: 14px;
        }

        .form-group {
            margin-bottom: 20px;
        }

        .form-group label {
            display: block;
            margin-bottom: 9px;
            font-size: 14px;
            font-weight: bold;
            color: #334155;
        }

        .form-group input[type="url"] {
            width: 100%;
            padding: 14px 15px;
            border: 1px solid #cbd5e1;
            border-radius: 9px;
            font-size: 14px;
            outline: none;
        }

        .form-group input[type="url"]:focus {
            border-color: #2563eb;
            box-shadow: 0 0 0 3px rgba(37, 99, 235, 0.1);
        }

        .hint {
            font-size: 13px;
            color: #64748b;
            margin-top: 9px;
        }

        .actions {
            display: flex;
            justify-content: space-between;
            align-items: center;
            gap: 16px;
            flex-wrap: wrap;
        }

        .back {
            color: #475569;
            text-decoration: none;
            font-size: 14px;
        }

        .button {
            border: none;
            border-radius: 9px;
            color: white;
            padding: 13px 24px;
            font-size: 14px;
            font-weight: bold;
            cursor: pointer;
            text-decoration: none;
            display: inline-block;
        }

        .upload-button {
            background: #2563eb;
        }

        .upload-button:hover {
            background: #1d4ed8;
        }

        .remote-button {
            background: #0f766e;
        }

        .remote-button:hover {
            background: #115e59;
        }

        .message {
            background: #dcfce7;
            color: #166534;
            border-radius: 10px;
            padding: 14px;
            margin-bottom: 22px;
            font-size: 14px;
        }

        .error {
            background: #fee2e2;
            color: #991b1b;
            border-radius: 10px;
            padding: 14px;
            margin-bottom: 22px;
            font-size: 14px;
        }

        .divider {
            display: flex;
            align-items: center;
            gap: 14px;
            margin: 24px 0;
            color: #94a3b8;
            font-size: 13px;
        }

        .divider::before,
        .divider::after {
            content: "";
            flex: 1;
            height: 1px;
            background: #e2e8f0;
        }

        .preview {
            background: #f8fafc;
            padding: 15px;
            border-radius: 8px;
            overflow: auto;
            white-space: pre-wrap;
            word-break: break-word;
            font-family: monospace;
            font-size: 13px;
            line-height: 1.6;
            margin-top: 15px;
        }

        .preview-note {
            color: #64748b;
            margin-top: 15px !important;
            margin-bottom: 0 !important;
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

            .card {
                padding: 22px;
            }

            .option {
                padding: 18px;
            }

            .actions {
                align-items: flex-start;
            }

            .button {
                width: 100%;
                text-align: center;
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

                <a href="${pageContext.request.contextPath}/dashboard?userId=${userId}">
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

                <a class="active"
                   href="${pageContext.request.contextPath}/upload?userId=${userId}">
                    Upload Statement
                </a>

                <form class="logout-form"
                      action="${pageContext.request.contextPath}/logout"
                      method="post">
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
                    <h1>Upload Statement</h1>
                    <p>Import your bank statement into FinSight.</p>
                </div>

                <div class="user">
                    User ID: ${userId}
                </div>

            </div>

            <div class="card">

                <h2>Import Bank Transactions</h2>

                <p>
                    Choose how you want to import your financial transactions.
                </p>

                <% if (request.getAttribute("message") != null) { %>

                    <div class="message">
                        <%= request.getAttribute("message") %>
                    </div>

                <% } %>

                <% if (request.getAttribute("error") != null) { %>

                    <div class="error">
                        <%= request.getAttribute("error") %>
                    </div>

                <% } %>

                <div class="option">

                    <h3>Upload a Statement</h3>

                    <p>
                        Select a CSV file from your computer (maximum 5 MB).
                    </p>

                    <form action="${pageContext.request.contextPath}/upload?userId=<%= request.getAttribute("userId") %>"
                          method="post"
                          enctype="multipart/form-data">

                        <input type="hidden"
                               name="csrfToken"
                               value="${sessionScope.csrfToken}">

                        <input type="hidden"
                               name="userId"
                               value="<%= request.getAttribute("userId") %>">

                        <div class="upload-box">

                            <h3>Select your bank statement</h3>

                            <p>
                                Only CSV files are accepted.
                            </p>

                            <input type="file"
                                   name="file"
                                   accept=".csv,text/csv"
                                   required>

                        </div>

                        <div class="actions">

                            <span></span>

                            <button class="button upload-button"
                                    type="submit">
                                Upload Statement
                            </button>

                        </div>

                    </form>

                </div>

                <div class="divider">
                    OR
                </div>

                <div class="option">

                    <h3>Import from URL</h3>

                    <p>
                        Paste the URL of a CSV file to fetch and preview transactions.
                    </p>

                    <form action="${pageContext.request.contextPath}/remote-import"
                          method="post">

                        <input type="hidden"
                               name="csrfToken"
                               value="${sessionScope.csrfToken}">

                        <input type="hidden"
                               name="userId"
                               value="<%= request.getAttribute("userId") %>">

                        <div class="form-group">

                            <label for="url">
                                CSV File URL
                            </label>

                            <input type="url"
                                   id="url"
                                   name="url"
                                   placeholder="https://example.com/transactions.csv"
                                   required>

                            <div class="hint">
                                Only HTTPS URLs from approved domains are allowed
                                (example.com, www.example.com, raw.githubusercontent.com,
                                gist.githubusercontent.com). Local and private network
                                addresses are blocked.
                            </div>

                        </div>

                        <div class="actions">

                            <span></span>

                            <button class="button remote-button"
                                    type="submit">
                                Preview Data
                            </button>

                        </div>

                    </form>

                </div>

                <% if (request.getAttribute("csvPreview") != null) { %>

                    <div class="option">

                        <h3>Extracted CSV Data</h3>

                        <p>
                            CSV detected. Review the extracted transactions before importing.
                        </p>

                        <pre class="preview"><%= HtmlUtils.htmlEscape(
                                String.valueOf(request.getAttribute("csvPreview"))) %></pre>

                        <p class="preview-note">
                            Review the data before importing it into the database.
                        </p>

                        <form action="${pageContext.request.contextPath}/remote-import/save"
                              method="post">

                            <input type="hidden"
                                   name="csrfToken"
                                   value="${sessionScope.csrfToken}">

                            <input type="hidden"
                                   name="userId"
                                   value="<%= request.getAttribute("userId") %>">

                            <textarea name="csvContent"
                                      style="display:none;"><%= HtmlUtils.htmlEscape(
                                              String.valueOf(request.getAttribute("csvPreview"))) %></textarea>

                            <div class="actions">

                                <span></span>

                                <button class="button remote-button"
                                        type="submit">
                                    Import Transactions
                                </button>

                            </div>

                        </form>

                    </div>

                <% } %>

                <% if (request.getAttribute("responsePreview") != null) { %>

                    <div class="option">

                        <h3>Response Preview</h3>

                        <p>
                            The response is not a CSV file.
                        </p>

                        <pre class="preview"><%= HtmlUtils.htmlEscape(
                                String.valueOf(request.getAttribute("responsePreview"))) %></pre>

                    </div>

                <% } %>

                <div class="actions">

                    <a class="back"
                       href="${pageContext.request.contextPath}/dashboard?userId=${userId}">
                        ← Back to Dashboard
                    </a>

                </div>

            </div>

        </main>

    </div>

</body>
</html>