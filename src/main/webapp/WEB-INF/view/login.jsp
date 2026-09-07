<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>FinSight - Login</title>

    <style>
        * {
            box-sizing: border-box;
        }

        body {
            margin: 0;
            font-family: Arial, sans-serif;
            background: #f4f7f6;
            color: #172b2a;
        }

        .page {
            min-height: 100vh;
            display: flex;
            align-items: center;
            justify-content: center;
            padding: 30px 20px;
        }

        .container {
            width: 100%;
            max-width: 1000px;
            display: grid;
            grid-template-columns: 1fr 1fr;
            background: white;
            border-radius: 20px;
            overflow: hidden;
            box-shadow: 0 10px 35px rgba(0, 0, 0, 0.08);
        }

        .welcome {
            background: #123b35;
            color: white;
            padding: 55px 45px;
            display: flex;
            flex-direction: column;
            justify-content: center;
        }

        .logo {
            font-size: 30px;
            font-weight: bold;
            margin-bottom: 45px;
        }

        .logo span {
            color: #79d6a3;
        }

        .welcome h1 {
            font-size: 38px;
            line-height: 1.2;
            margin: 0 0 20px;
        }

        .welcome p {
            color: #d1e5dc;
            font-size: 16px;
            line-height: 1.7;
            max-width: 360px;
        }

        .form-section {
            padding: 55px 45px;
            display: flex;
            flex-direction: column;
            justify-content: center;
        }

        .form-section h2 {
            margin: 0 0 10px;
            font-size: 28px;
            color: #172b2a;
        }

        .subtitle {
            margin: 0 0 30px;
            color: #71807c;
            font-size: 14px;
        }

        .message {
            background: #fff1f0;
            color: #c0392b;
            border: 1px solid #f5c6c2;
            padding: 12px;
            border-radius: 8px;
            margin-bottom: 20px;
            font-size: 14px;
        }

        .form-group {
            margin-bottom: 20px;
        }

        label {
            display: block;
            margin-bottom: 8px;
            font-size: 14px;
            font-weight: bold;
            color: #344542;
        }

        input {
            width: 100%;
            padding: 14px 15px;
            border: 1px solid #dce5e1;
            border-radius: 9px;
            font-size: 15px;
            outline: none;
            transition: border-color 0.2s, box-shadow 0.2s;
        }

        input:focus {
            border-color: #3c9b72;
            box-shadow: 0 0 0 3px rgba(60, 155, 114, 0.12);
        }

        .login-button {
            width: 100%;
            padding: 14px;
            border: none;
            border-radius: 9px;
            background: #287c5b;
            color: white;
            font-size: 15px;
            font-weight: bold;
            cursor: pointer;
            margin-top: 5px;
            transition: background 0.2s;
        }

        .login-button:hover {
            background: #206b4d;
        }

        .register-link {
            text-align: center;
            margin-top: 25px;
            color: #71807c;
            font-size: 14px;
        }

        .register-link a {
            color: #287c5b;
            font-weight: bold;
            text-decoration: none;
        }

        .register-link a:hover {
            text-decoration: underline;
        }

        @media (max-width: 700px) {
            .container {
                grid-template-columns: 1fr;
            }

            .welcome {
                padding: 35px 30px;
            }

            .welcome h1 {
                font-size: 30px;
            }

            .logo {
                margin-bottom: 25px;
            }

            .form-section {
                padding: 35px 30px;
            }
        }
    </style>
</head>

<body>

<div class="page">

    <div class="container">

        <div class="welcome">

            <div class="logo">
                Fin<span>Sight</span>
            </div>

            <h1>Your finances.<br>One clear view.</h1>

            <p>
                Manage your expenses, investments, loans, and financial goals
                in one place.
            </p>

        </div>

        <div class="form-section">

            <h2>Welcome back</h2>

            <p class="subtitle">
                Sign in to continue to your FinSight account.
            </p>

            <% if (request.getAttribute("err") != null) { %>
                <div class="message">
                    <%= request.getAttribute("err") %>
                </div>
            <% } %>

            <form action="${pageContext.request.contextPath}/login" method="post">

                <div class="form-group">
                    <label for="email">Email address</label>
                    <input
                        type="email"
                        id="email"
                        name="email"
                        placeholder="Enter your email"
                        required>
                </div>

                <div class="form-group">
                    <label for="password">Password</label>
                    <input
                        type="password"
                        id="password"
                        name="password"
                        placeholder="Enter your password"
                        required>
                </div>

                <button type="submit" class="login-button">
                    Sign in
                </button>

            </form>

            <div class="register-link">
                Don't have an account?
                <a href="${pageContext.request.contextPath}/register">
                    Create an account
                </a>
            </div>

        </div>

    </div>

</div>

</body>
</html>