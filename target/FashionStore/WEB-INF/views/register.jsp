<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Create account — FashionStore</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/layout.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/auth.css">
</head>
<body>

<%@ include file="partials/navbar.jsp" %>

<div class="auth-page">
    <div class="auth-card">
        <h1>Create account</h1>
        <p class="auth-lead">Register to save your delivery details and track orders.</p>

        <c:if test="${not empty flashMessage}">
            <div class="flash-banner flash-banner-auth" role="alert"><c:out value="${flashMessage}"/></div>
        </c:if>

        <form method="post" action="${pageContext.request.contextPath}/register">
            <div class="auth-field">
                <label for="fullName">Full name</label>
                <input id="fullName" name="fullName" required value="<c:out value='${param.fullName}'/>">
            </div>
            <div class="auth-field">
                <label for="email">Email</label>
                <input id="email" type="email" name="email" required autocomplete="email"
                       value="<c:out value='${param.email}'/>">
            </div>
            <div class="auth-field">
                <label for="phone">Phone</label>
                <input id="phone" name="phone" value="<c:out value='${param.phone}'/>">
            </div>
            <div class="auth-field">
                <label for="gender">Gender</label>
                <select id="gender" name="gender">
                    <option value="">Prefer not to say</option>
                    <option value="Female">Female</option>
                    <option value="Male">Male</option>
                    <option value="Other">Other</option>
                </select>
            </div>
            <div class="auth-field">
                <label for="address">Address</label>
                <textarea id="address" name="address"><c:out value="${param.address}"/></textarea>
            </div>
            <div class="auth-field">
                <label for="password">Password</label>
                <input id="password" type="password" name="password" required autocomplete="new-password">
            </div>
            <div class="auth-field">
                <label for="confirmPassword">Confirm password</label>
                <input id="confirmPassword" type="password" name="confirmPassword" required autocomplete="new-password">
            </div>
            <button type="submit" class="auth-submit">Register</button>
        </form>

        <p class="auth-alt">Already have an account? <a href="${pageContext.request.contextPath}/login">Sign in</a></p>
    </div>
</div>

<%@ include file="partials/footer.jsp" %>

</body>
</html>
