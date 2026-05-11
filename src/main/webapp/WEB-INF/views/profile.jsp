<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>My profile — FashionStore</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/layout.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/auth.css">
</head>
<body>

<%@ include file="partials/navbar.jsp" %>

<div class="auth-page">
    <div class="auth-card" style="max-width:520px;">
        <h1>My profile</h1>
        <p class="auth-lead">Update your contact details. Leave password fields empty to keep your current password.</p>

        <c:if test="${not empty flashMessage}">
            <div class="flash-banner flash-banner-auth" role="status"><c:out value="${flashMessage}"/></div>
        </c:if>

        <c:if test="${not empty profileUser}">
            <form method="post" action="${pageContext.request.contextPath}/profile">
                <div class="auth-field">
                    <label for="fullName">Full name</label>
                    <input id="fullName" name="fullName" required
                           value="<c:out value='${profileUser.fullName}'/>">
                </div>
                <div class="auth-field">
                    <label>Email</label>
                    <input type="text" value="<c:out value='${profileUser.email}'/>" disabled style="background:#f5f5f5;">
                </div>
                <div class="auth-field">
                    <label for="phone">Phone</label>
                    <input id="phone" name="phone" value="<c:out value='${profileUser.phone}'/>">
                </div>
                <div class="auth-field">
                    <label for="gender">Gender</label>
                    <select id="gender" name="gender">
                        <option value="" <c:if test="${empty profileUser.gender}">selected</c:if>>—</option>
                        <option value="Female" <c:if test="${profileUser.gender eq 'Female'}">selected</c:if>>Female</option>
                        <option value="Male" <c:if test="${profileUser.gender eq 'Male'}">selected</c:if>>Male</option>
                        <option value="Other" <c:if test="${profileUser.gender eq 'Other'}">selected</c:if>>Other</option>
                    </select>
                </div>
                <div class="auth-field">
                    <label for="address">Address</label>
                    <textarea id="address" name="address"><c:out value="${profileUser.address}"/></textarea>
                </div>
                <hr style="border:none;border-top:1px solid rgba(0,0,0,0.08);margin:20px 0;">
                <div class="auth-field">
                    <label for="newPassword">New password (optional)</label>
                    <input id="newPassword" type="password" name="newPassword" autocomplete="new-password">
                </div>
                <div class="auth-field">
                    <label for="confirmPassword">Confirm new password</label>
                    <input id="confirmPassword" type="password" name="confirmPassword" autocomplete="new-password">
                </div>
                <button type="submit" class="auth-submit">Save changes</button>
            </form>
        </c:if>
    </div>
</div>

<%@ include file="partials/footer.jsp" %>

</body>
</html>
