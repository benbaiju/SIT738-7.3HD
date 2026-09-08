<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Error | FinSight</title>
    <style>
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
            max-width: 480px;
            background: white;
            border: 1px solid #e2e8f0;
            border-radius: 16px;
            padding: 28px;
            box-shadow: 0 4px 18px rgba(15, 23, 42, .04);
        }

        h1 {
            margin: 0 0 12px;
            font-size: 22px;
        }

        p {
            margin: 0 0 22px;
            color: #475569;
            line-height: 1.5;
        }

        a {
            display: inline-block;
            background: #2563eb;
            color: white;
            text-decoration: none;
            padding: 12px 18px;
            border-radius: 8px;
            font-weight: bold;
        }

        a:hover {
            background: #1d4ed8;
        }
    </style>
</head>
<body>
<div class="page">
    <div class="card">
        <h1>Something went wrong</h1>
        <p>
            <%
                Integer statusCode =
                        (Integer) request.getAttribute("javax.servlet.error.status_code");
                if (statusCode != null && statusCode.intValue() == 403) {
                    out.print("You are not allowed to perform this action.");
                } else if (statusCode != null && statusCode.intValue() == 404) {
                    out.print("The requested page could not be found.");
                } else {
                    out.print("An unexpected error occurred. Please try again later.");
                }
            %>
        </p>
        <a href="<%= request.getContextPath() %>/login">Return to login</a>
    </div>
</div>
</body>
</html>
