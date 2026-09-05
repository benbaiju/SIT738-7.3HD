<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>FinSight - Create Account</title>

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

        .feature {
            margin-top: 35px;
            display: flex;
            align-items: center;
            gap: 12px;
            color: #d1e5dc;
            font-size: 14px;
        }

        .feature-icon {
            width: 34px;
            height: 34px;
            border-radius: 10px;
            background: #24584d;
            display: flex;
            align-items: center;
            justify-content: center;
            color: #79d6a3;
            font-size: 18px;
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

        .success {
            background: #edf8f1;
            color: #287c5b;
            border: 1px solid #b9dfc8;
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

        .register-button {
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

        .register-button:hover {
            background: #206b4d;
        }

        .login-link {
            text-align: center;
            margin-top: 25px;
            color: #71807c;
            font-size: 14px;
        }

        .login-link a {
            color: #287c5b;
            font-weight: bold;
            text-decoration: none;
        }

        .login-link a:hover {
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

            .feature {
                margin-top: 20px;
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

            <h1>Start managing<br>your future.</h1>

            <p>
                Create your FinSight account and bring your financial
                information together in one place.
            </p>

            <div class="feature">
                <div class="feature-icon">✓</div>
                <div>Track expenses and investments</div>
            </div>

            <div class="feature">
                <div class="feature-icon">✓</div>
                <div>Set goals and monitor progress</div>
            </div>

        </div>

        <div class="form-section">

            <h2>Create your account</h2>

            <p class="subtitle">
                Enter your details to get started with FinSight.
            </p>

            <% if (request.getAttribute("err") != null) { %>
                <div class="message">
                    <%= request.getAttribute("err") %>
                </div>
            <% } %>

            <% if (request.getAttribute("message") != null) { %>
                <div class="message success">
                    <%= request.getAttribute("message") %>
                </div>
            <% } %>

            <form action="${pageContext.request.contextPath}/register" method="post">

                <div class="form-group">
                    <label for="fullName">Full name</label>
                    <input
                        type="text"
                        id="fullName"
                        name="fullName"
                        placeholder="Enter your full name"
                        required>
                </div>

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
                        placeholder="Create a password"
                        required>
                </div>

                <button type="submit" class="register-button">
                    Create account
                </button>

            </form>

            <div class="login-link">
                Already have an account?
                <a href="${pageContext.request.contextPath}/login">
                    Sign in
                </a>
            </div>

        </div>

    </div>

</div>

</body>
</html>