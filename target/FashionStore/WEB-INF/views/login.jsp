<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Sign in — FashionStore</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/layout.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/auth.css">
</head>
<body>

<%@ include file="partials/navbar.jsp" %>

<div class="auth-page">
    <div class="auth-card">
        <h1>Sign in</h1>
        <p class="auth-lead">Use your email and password. New here? Create an account.</p>

        <c:if test="${not empty flashMessage}">
            <div class="flash-banner flash-banner-auth" role="alert"><c:out value="${flashMessage}"/></div>
        </c:if>

        <form method="post" action="${pageContext.request.contextPath}/login">
            <c:if test="${not empty param.returnUrl}">
                <input type="hidden" name="returnUrl" value="<c:out value='${param.returnUrl}' escapeXml='true'/>">
            </c:if>
            <div class="auth-field">
                <label for="email">Email</label>
                <input id="email" type="email" name="email" autocomplete="username" required
                       value="<c:out value='${param.email}'/>">
            </div>
            <div class="auth-field">
                <label for="password">Password</label>
                <input id="password" type="password" name="password" autocomplete="current-password" required>
            </div>
            <button type="submit" class="auth-submit">Sign in</button>
        </form>

        <p class="auth-alt">No account? <a href="${pageContext.request.contextPath}/register">Register</a></p>
    </div>
</div>

<%@ include file="partials/footer.jsp" %>

</body>
</html>
