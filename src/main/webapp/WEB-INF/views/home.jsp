<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="en">

<head>

    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <title>FashionStore</title>

    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/assets/css/layout.css">

</head>

<body>

    <!-- NAVBAR -->
    <%@ include file="partials/navbar.jsp" %>

    <div class="app-container">

        <div class="home-main-layout">

            <!-- SIDEBAR -->
            <%@ include file="partials/sidebar.jsp" %>

            <!-- MAIN CONTENT -->
            <main class="home-main-content">

                <!-- HERO -->
                <section class="home-hero-section">

                    <div class="home-hero-text">

                        <span class="home-hero-tag">
                            New Collection 2026
                        </span>

                        <h1>Discover Modern Fashion</h1>

                        <p>
                            Explore trending outfits, premium styles,
                            and curated collections built for everyday fashion.
                        </p>

                        <a href="${pageContext.request.contextPath}/products"
                           class="primary-btn">
                            Shop Now
                        </a>

                    </div>

                </section>

                <!-- PRODUCTS -->
                <section class="home-products-section">

                    <div class="home-section-header">
                        <h2>Trending Products</h2>
                    </div>

                    <c:choose>

                        <c:when test="${empty products}">
                            <p style="text-align:center;color:#777;padding:20px;">
                                No trending products available.
                            </p>
                        </c:when>

                        <c:otherwise>

                            <div class="home-product-grid">

                                <c:forEach var="product" items="${products}">

                                    <div class="product-card">

                                        <!-- IMAGE -->
                                        <div class="product-card-image">

                                            <img src="${pageContext.request.contextPath}/${product.imageUrl}"
                                                 alt="${product.productName}">

                                        </div>

                                        <!-- INFO -->
                                        <div class="product-card-info">

                                            <span class="product-card-category">
                                                Fashion
                                            </span>

                                            <h3 class="product-card-name">
                                                ${product.productName}
                                            </h3>

                                            <!-- FINAL PRICE -->
                                            <p class="product-card-price">
                                                ₹ ${product.finalPrice}
                                            </p>

                                            <!-- MRP + DISCOUNT -->
                                            <p style="font-size:12px;color:#777;margin:0;">
                                                MRP:
                                                <span style="text-decoration:line-through;">
                                                    ₹ ${product.price}
                                                </span>
                                                • ${product.discountPercent}% OFF
                                            </p>

                                            <!-- ACTION -->
                                            <a href="${pageContext.request.contextPath}/product?id=${product.productId}"
                                               class="primary-btn product-card-btn">
                                                View Details
                                            </a>

                                        </div>

                                    </div>

                                </c:forEach>

                            </div>

                        </c:otherwise>

                    </c:choose>

                </section>

            </main>

        </div>

    </div>

    <!-- FOOTER -->
    <%@ include file="partials/footer.jsp" %>

</body>

</html>