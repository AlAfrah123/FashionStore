<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<c:choose>
    <c:when test="${not empty param.categoryId}">
        <c:set var="effectiveCat" value="${param.categoryId}"/>
    </c:when>
    <c:when test="${sidebarCategoryId ne null}">
        <c:set var="effectiveCat" value="${sidebarCategoryId}"/>
    </c:when>
    <c:otherwise>
        <c:set var="effectiveCat" value=""/>
    </c:otherwise>
</c:choose>

<c:url value="/products" var="sidebarAllProductsHref">
    <c:if test="${not empty param.sort}"><c:param name="sort" value="${param.sort}"/></c:if>
    <c:if test="${not empty param.query}"><c:param name="query" value="${param.query}"/></c:if>
</c:url>

<aside class="sidebar" aria-label="Product categories">

    <div class="sidebar-header">
        <h2>Categories</h2>
    </div>

    <div class="sidebar-categories">

        <a href="${sidebarAllProductsHref}"
           class="sidebar-category-item ${empty effectiveCat ? 'active' : ''}">
            All products
        </a>

        <c:forEach var="category" items="${categories}">
            <c:url value="/products" var="catHref">
                <c:param name="categoryId" value="${category.categoryId}"/>
                <c:if test="${not empty param.sort}"><c:param name="sort" value="${param.sort}"/></c:if>
                <c:if test="${not empty param.query}"><c:param name="query" value="${param.query}"/></c:if>
            </c:url>
            <a href="${catHref}"
               class="sidebar-category-item ${effectiveCat == category.categoryId ? 'active' : ''}">
                <c:out value="${category.categoryName}"/>
            </a>
        </c:forEach>

    </div>

</aside>
