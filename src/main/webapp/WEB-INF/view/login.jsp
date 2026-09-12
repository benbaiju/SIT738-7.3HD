<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Login | FinSight</title>

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

        .page {
            min-height: 100vh;
            display: flex;
            align-items: center;
            justify-content: center;
            padding: 34px 20px;
        }

        .card {
            width: 100%;
            max-width: 420px;
            background: white;
            border: 1px solid #e2e8f0;
            border-radius: 16px;
            overflow: hidden;
            box-shadow: 0 4px 18px rgba(15, 23, 42, .04);
        }

        .brand {
            background: #172033;
            color: white;
            font-size: 24px;
            font-weight: bold;
            padding: 20px 26px;
        }

        .brand span {
            color: #6ee7b7;
        }

        .form-area {
            padding: 26px;
        }

        h1 {
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
            width: 100%;
        }

        button:hover {
            background: #1d4ed8;
        }

        .error {
            background: #fee2e2;
            color: #991b1b;
            border-radius: 10px;
            padding: 14px;
            margin-bottom: 22px;
            font-size: 14px;
        }

        .success {
            background: #dcfce7;
            color: #166534;
            border-radius: 10px;
            padding: 14px;
            margin-bottom: 22px;
            font-size: 14px;
        }

        .link {
            text-align: center;
            margin-top: 18px;
            color: #64748b;
            font-size: 14px;
        }

        .link a {
            color: #2563eb;
            font-weight: bold;
            text-decoration: none;
        }

        .link a:hover {
            text-decoration: underline;
        }
    </style>
</head>

<body>

<div class="page">

    <div class="card">

        <div class="brand">Fin<span>Sight</span></div>

        <div class="form-area">

        <h1>Login</h1>

        <% if (request.getAttribute("error") != null) { %>
            <div class="error">
                <%= request.getAttribute("error") %>
            </div>
        <% } %>

        <% if (request.getAttribute("message") != null) { %>
            <div class="success">
                <%= request.getAttribute("message") %>
            </div>
        <% } %>

        <form action="${pageContext.request.contextPath}/login" method="post">
            <%@ include file="includes/spring-csrf.jsp" %>
            <label class="label" for="email">Email</label>
            <input type="email" id="email" name="email" required>

            <label class="label" for="password">Password</label>
            <input type="password" id="password" name="password" required>

            <button type="submit">Login</button>

        </form>

        <div class="link">
            Don't have an account?
            <a href="${pageContext.request.contextPath}/register">Register</a>
        </div>

        </div>

    </div>

</div>

</body>
</html>
