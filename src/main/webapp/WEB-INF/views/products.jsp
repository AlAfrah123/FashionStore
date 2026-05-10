<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
<head>

    <title>Products</title>

    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/assets/css/products.css">

</head>

<body>

<div class="page-header">
    <h2>All Products</h2>
</div>

<c:choose>

    <c:when test="${empty products}">
        <div class="empty-state">
            No products available at the moment.
        </div>
    </c:when>

    <c:otherwise>

        <div class="product-grid">

            <c:forEach var="product" items="${products}">

                <div class="product-card">

                    <!-- IMAGE -->
                    <div class="product-image">

                        <img src="${pageContext.request.contextPath}/${product.imageUrl}"
                             alt="${product.productName}">

                    </div>

                    <!-- CONTENT -->
                    <div class="product-info">

                        <h3 class="product-title">
                            ${product.productName}
                        </h3>

                        <p class="product-desc">
                            ${product.description}
                        </p>

                        <!-- PRICE BLOCK -->
                        <div class="price-row">

                            <span class="price">
                                ₹ ${product.finalPrice}
                            </span>

                            <span style="text-decoration: line-through; color:#999; font-size:12px;">
                                ₹ ${product.price}
                            </span>

                            <span style="color: green; font-size:12px; font-weight:600;">
                                ${product.discountPercent}% OFF
                            </span>

                        </div>

                        <!-- ACTION -->
                        <a class="btn btn-view"
                           href="${pageContext.request.contextPath}/product-details?id=${product.productId}">
                            View Details
                        </a>

                    </div>

                </div>

            </c:forEach>

        </div>

    </c:otherwise>

</c:choose>

</body>
</html>