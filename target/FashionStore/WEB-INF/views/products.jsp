<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Products — FashionStore</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/layout.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/products.css">
</head>
<body>

<%@ include file="partials/navbar.jsp" %>

<div class="app-container">
    <div class="home-main-layout">

        <%@ include file="partials/sidebar.jsp" %>

        <main class="home-main-content products-page">

            <header class="products-page-header">
                <nav class="breadcrumb" aria-label="Breadcrumb">
                    <a href="${pageContext.request.contextPath}/home">Home</a>
                    <span class="breadcrumb-sep" aria-hidden="true">/</span>
                    <span class="breadcrumb-current">Products</span>
                </nav>

                <div class="products-title-row">
                    <div>
                        <h1 class="products-title">All products</h1>
                        <p class="products-subtitle">
                            <c:choose>
                                <c:when test="${not empty param.query}">
                                    Results for “<strong><c:out value="${param.query}"/></strong>”
                                </c:when>
                                <c:when test="${not empty param.categoryId}">
                                    Filtered by category
                                </c:when>
                                <c:otherwise>Browse the full catalog</c:otherwise>
                            </c:choose>
                        </p>
                    </div>
                    <span class="products-count-pill">
                        <c:out value="${productCount}"/> items
                    </span>
                </div>

                <div class="sort-strip" aria-label="Sort products">
                    <span class="sort-strip-label">Sort by</span>
                    <div class="sort-pills">
                        <c:url var="lnkPopularity" value="/products">
                            <c:if test="${not empty param.categoryId}"><c:param name="categoryId" value="${param.categoryId}"/></c:if>
                            <c:if test="${not empty param.query}"><c:param name="query" value="${param.query}"/></c:if>
                            <c:param name="sort" value="popularity"/>
                        </c:url>
                        <c:url var="lnkPriceAsc" value="/products">
                            <c:if test="${not empty param.categoryId}"><c:param name="categoryId" value="${param.categoryId}"/></c:if>
                            <c:if test="${not empty param.query}"><c:param name="query" value="${param.query}"/></c:if>
                            <c:param name="sort" value="price_asc"/>
                        </c:url>
                        <c:url var="lnkPriceDesc" value="/products">
                            <c:if test="${not empty param.categoryId}"><c:param name="categoryId" value="${param.categoryId}"/></c:if>
                            <c:if test="${not empty param.query}"><c:param name="query" value="${param.query}"/></c:if>
                            <c:param name="sort" value="price_desc"/>
                        </c:url>
                        <c:url var="lnkNewest" value="/products">
                            <c:if test="${not empty param.categoryId}"><c:param name="categoryId" value="${param.categoryId}"/></c:if>
                            <c:if test="${not empty param.query}"><c:param name="query" value="${param.query}"/></c:if>
                            <c:param name="sort" value="newest"/>
                        </c:url>

                        <a href="${lnkPopularity}" class="sort-pill ${currentSort eq 'popularity' ? 'active' : ''}">Popularity</a>
                        <a href="${lnkPriceAsc}" class="sort-pill ${currentSort eq 'price_asc' ? 'active' : ''}">Price · Low→High</a>
                        <a href="${lnkPriceDesc}" class="sort-pill ${currentSort eq 'price_desc' ? 'active' : ''}">Price · High→Low</a>
                        <a href="${lnkNewest}" class="sort-pill ${currentSort eq 'newest' ? 'active' : ''}">Newest first</a>
                    </div>
                </div>
            </header>

            <c:choose>
                <c:when test="${empty products}">
                    <div class="empty-state-card" role="status">
                        <p class="empty-state-title">No products found</p>
                        <p class="empty-state-text">Try another search term or browse all categories from the sidebar.</p>
                        <a href="${pageContext.request.contextPath}/products" class="primary-btn empty-state-btn">Clear filters</a>
                    </div>
                </c:when>
                <c:otherwise>
                    <div class="product-grid" role="list">
                        <c:forEach var="product" items="${products}">
                            <article class="product-card" role="listitem">

                                <a href="${pageContext.request.contextPath}/product-details?id=${product.productId}"
                                   class="product-card-media-link"
                                   aria-label="View product detail">
                                    <div class="product-image">
                                        <img src="${pageContext.request.contextPath}/${product.imageUrl}"
                                             alt=""
                                             loading="lazy">
                                    </div>
                                </a>

                                <div class="product-info">
                                    <h2 class="product-title">
                                        <c:out value="${product.productName}"/>
                                    </h2>

                                    <p class="product-desc">
                                        <c:out value="${product.description}"/>
                                    </p>

                                    <div class="price-row">
                                        <span class="price">₹<c:out value="${product.finalPrice}"/></span>
                                        <c:if test="${product.discountPercent gt 0}">
                                            <span class="price-was">₹<c:out value="${product.price}"/></span>
                                            <span class="discount-badge"><c:out value="${product.discountPercent}"/>% off</span>
                                        </c:if>
                                    </div>

                                    <a class="btn btn-view"
                                       href="${pageContext.request.contextPath}/product-details?id=${product.productId}">
                                        View details
                                    </a>
                                </div>

                            </article>
                        </c:forEach>
                    </div>
                </c:otherwise>
            </c:choose>

        </main>
    </div>
</div>

<%@ include file="partials/footer.jsp" %>

</body>
</html>
