<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<nav class="navbar" aria-label="Primary">

    <div class="nav-left">
        <a href="${ctx}/home" class="logo">
            FashionStore
        </a>
    </div>

    <div class="nav-center">
        <a href="${ctx}/home"
           class="nav-link ${activeNav eq 'home' ? 'active' : ''}">Home</a>
        <a href="${ctx}/products"
           class="nav-link ${activeNav eq 'products' ? 'active' : ''}">Products</a>
        <a href="${ctx}/products" class="nav-link">Shop by category</a>
    </div>

    <div class="nav-right">

        <form action="${ctx}/products" method="GET" class="search-form">
            <c:if test="${not empty param.sort}">
                <input type="hidden" name="sort" value="<c:out value='${param.sort}' escapeXml='true'/>">
            </c:if>
            <c:if test="${not empty param.categoryId}">
                <input type="hidden" name="categoryId" value="<c:out value='${param.categoryId}' escapeXml='true'/>">
            </c:if>
            <c:choose>
                <c:when test="${not empty param.query}">
                    <input type="text"
                           name="query"
                           value="<c:out value='${param.query}' escapeXml='true'/>"
                           placeholder="Search products…"
                           class="search-input"
                           autocomplete="off"
                           aria-label="Search products"/>
                </c:when>
                <c:otherwise>
                    <input type="text"
                           name="query"
                           placeholder="Search products…"
                           class="search-input"
                           autocomplete="off"
                           aria-label="Search products"/>
                </c:otherwise>
            </c:choose>
        </form>

        <a href="${ctx}/cart"
           class="icon-btn ${activeNav eq 'cart' ? 'icon-btn-active' : ''}"
           title="Cart"
           aria-label="Cart">
            &#128722;
        </a>

        <c:choose>
            <c:when test="${not empty sessionScope.authUser}">
                <a href="${ctx}/profile"
                   class="nav-link ${activeNav eq 'profile' ? 'active' : ''}"
                   style="padding:6px 0;">Profile</a>
                <a href="${ctx}/logout" class="btn-primary" style="background:#444;">Logout</a>
            </c:when>
            <c:otherwise>
                <a href="${ctx}/login" class="nav-link ${activeNav eq 'login' ? 'active' : ''}" style="padding:6px 0;">Login</a>
                <a href="${ctx}/register" class="btn-primary">Sign up</a>
            </c:otherwise>
        </c:choose>

    </div>

</nav>
