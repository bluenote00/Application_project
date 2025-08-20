<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 20px;
        }
        .container {
            width: 400px;
            margin: 50px auto;
            padding: 20px;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
            border-radius: 5px;
        }
        h2 {
            text-align: center;
            color: #333;
            margin-bottom: 30px;
        }
        .form-group {
            margin-bottom: 15px;
            display: flex;
            align-items: center;
        }
        .form-group label {
            width: 100px;
            text-align: right;
            margin-right: 15px;
            font-weight: bold;
        }
        .form-control {
            flex: 1;
            padding: 8px;
            border: 1px solid #ccc;
            border-radius: 4px;
        }
        .btn-submit {
            background-color: #007bff;
            color: white;
            padding: 10px 20px;
            border: none;
            border-radius: 4px;
            cursor: pointer;
            width: 100%;
            margin-top: 20px;
        }
        .btn-submit:hover {
            background-color: #0056b3;
        }
        .error-message {
            color: red;
            font-size: 12px;
            text-align: center;
            margin-bottom: 15px;
        }
        .links {
            margin-top: 20px;
            text-align: center;
        }
        .links a {
            color: #007bff;
            text-decoration: none;
            margin: 0 10px;
        }
        .links a:hover {
            text-decoration: underline;
        }
        .remember-me {
            display: flex;
            justify-content: center;
            align-items: center;
            margin-top: 15px;
        }
        .remember-me input {
            margin-right: 5px;
        }
    </style>
    <title>로그인</title>

</head>
<body>
    <div class="container">
        <h2>로그인</h2>

        <c:if test="${not empty errorMessage}">
            <div class="error-message">${errorMessage}</div>
        </c:if>

        <form action="${pageContext.request.contextPath}/member/login" method="post">
            <div class="form-group">
                <label for="userId">아이디</label>
                <input type="text" class="form-control" id="loginId" name="loginId" required placeholder="아이디를 입력하세요">
            </div>

            <div class="form-group">
                <label for="userPw">비밀번호</label>
                <input type="password" class="form-control" id="loginPw" name="loginPw" required placeholder="비밀번호를 입력하세요">
            </div>

            <div class="links">
                <button type="submit">Login</button>
                <a href="${pageContext.request.contextPath}/member/moveJoin">Join</a>
            </div>

            <div class="links">
                <a href="${pageContext.request.contextPath}/member/movefindID">아이디 찾기</a>
                |
                <a href="${pageContext.request.contextPath}/member/movefindPW">비밀번호 찾기</a>
            </div>
        </form>
    </div>
</body>
</html>