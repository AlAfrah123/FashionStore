<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>
        <c:choose>
            <c:when test="${not empty product}"><c:out value="${product.productName}"/> — FashionStore</c:when>
            <c:otherwise>Product — FashionStore</c:otherwise>
        </c:choose>
    </title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/layout.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/product-details.css">
</head>
<body>

<%@ include file="partials/navbar.jsp" %>

<div class="app-container">
    <div class="home-main-layout">

        <%@ include file="partials/sidebar.jsp" %>

        <main class="home-main-content detail-page-main">

            <c:if test="${not empty cartFlashMessage}">
                <div class="flash-banner flash-banner-cart" role="status"><c:out value="${cartFlashMessage}"/></div>
            </c:if>

            <c:choose>
                <c:when test="${empty product}">
                    <div class="empty-state-card" role="alert">
                        <p class="empty-state-title">Product unavailable</p>
                        <p class="empty-state-text">This item may have been removed. Return to the catalog to keep shopping.</p>
                        <a class="primary-btn empty-state-btn" href="${pageContext.request.contextPath}/products">Browse products</a>
                    </div>
                </c:when>
                <c:otherwise>

                    <nav class="breadcrumb detail-breadcrumb" aria-label="Breadcrumb">
                        <a href="${pageContext.request.contextPath}/home">Home</a>
                        <span class="breadcrumb-sep" aria-hidden="true">/</span>
                        <a href="${pageContext.request.contextPath}/products">Products</a>
                        <span class="breadcrumb-sep" aria-hidden="true">/</span>
                        <span class="breadcrumb-current"><c:out value="${product.productName}"/></span>
                    </nav>

                    <article class="detail-layout">

                        <c:set var="detailImgAlt"><c:out value="${product.productName}"/></c:set>

                        <div class="detail-gallery card-surface">
                            <div class="detail-image-shell">
                                <img src="${pageContext.request.contextPath}/${product.imageUrl}"
                                     alt="${detailImgAlt}"
                                     class="detail-image">
                            </div>
                        </div>

                        <div class="detail-panel card-surface">

                            <h1 class="detail-title"><c:out value="${product.productName}"/></h1>

                            <div class="detail-price-row">
                                <span class="detail-price-sale">₹<c:out value="${product.finalPrice}"/></span>
                                <c:if test="${product.discountPercent gt 0}">
                                    <span class="detail-price-was">₹<c:out value="${product.price}"/></span>
                                    <span class="detail-save-pill"><c:out value="${product.discountPercent}"/>% off</span>
                                </c:if>
                            </div>

                            <p class="detail-description"><c:out value="${product.description}"/></p>

                            <c:choose>
                                <c:when test="${not empty variants}">
                                    <c:choose>
                                        <c:when test="${hasPurchasableVariant}">
                                            <div class="detail-section">
                                                <h2 class="detail-section-label">Pick a size &amp; quantity</h2>
                                                <form method="post" action="${pageContext.request.contextPath}/cart" class="add-to-cart-form">
                                                    <input type="hidden" name="action" value="add">
                                                    <input type="hidden" name="productId" value="${product.productId}"/>
                                                    <input type="hidden" name="returnTo" value="detail">
                                                    <input type="hidden" name="returnProductId" value="${product.productId}">
                                                    <ul class="variant-chips variant-select">
                                                        <c:forEach var="v" items="${variants}">
                                                            <li>
                                                                <c:choose>
                                                                    <c:when test="${v.stockQuantity gt 0}">
                                                                        <label class="variant-radio">
                                                                            <c:choose>
                                                                                <c:when test="${v.productSizeId == defaultProductSizeId}">
                                                                                    <input type="radio" name="productSizeId"
                                                                                           value="${v.productSizeId}" required checked>
                                                                                </c:when>
                                                                                <c:otherwise>
                                                                                    <input type="radio" name="productSizeId"
                                                                                           value="${v.productSizeId}" required>
                                                                                </c:otherwise>
                                                                            </c:choose>
                                                                            <span class="variant-chip variant-chip-available"
                                                                                  title="SKU <c:out value='${v.skuCode}'/>">
                                                                                <span class="variant-label"><c:out value="${v.sizeLabel}"/></span>
                                                                                <span class="variant-qty"><c:out value="${v.stockQuantity}"/> left</span>
                                                                            </span>
                                                                        </label>
                                                                    </c:when>
                                                                    <c:otherwise>
                                                                        <span class="variant-chip variant-chip-gone" aria-disabled="true">
                                                                            <span class="variant-label"><c:out value="${v.sizeLabel}"/></span>
                                                                            <span class="variant-qty">Out of stock</span>
                                                                        </span>
                                                                    </c:otherwise>
                                                                </c:choose>
                                                            </li>
                                                        </c:forEach>
                                                    </ul>
                                                    <div class="detail-qty-row">
                                                        <label class="qty-label" for="qty-detail">Qty</label>
                                                        <input type="number" id="qty-detail" name="qty" min="1" value="1" class="detail-qty-input">
                                                    </div>
                                                    <div class="detail-actions">
                                                        <button type="submit" class="btn-cart-primary-submit">Add to cart</button>
                                                        <span class="detail-hint">Sign in to save your profile; cart works for guests too.</span>
                                                    </div>
                                                </form>
                                            </div>
                                        </c:when>
                                        <c:otherwise>
                                            <div class="detail-section">
                                                <h2 class="detail-section-label">Sizes</h2>
                                                <ul class="variant-chips">
                                                    <c:forEach var="v" items="${variants}">
                                                        <li>
                                                            <span class="variant-chip variant-chip-gone">
                                                                <span class="variant-label"><c:out value="${v.sizeLabel}"/></span>
                                                                <span class="variant-qty">Out of stock</span>
                                                            </span>
                                                        </li>
                                                    </c:forEach>
                                                </ul>
                                                <p class="detail-hint">All sizes are currently out of stock.</p>
                                            </div>
                                        </c:otherwise>
                                    </c:choose>
                                </c:when>
                                <c:otherwise>
                                    <div class="detail-actions">
                                        <button type="button" class="btn-cart-primary btn-cart-muted" disabled>Add to cart</button>
                                        <span class="detail-hint">No size / SKU rows in the database for this product yet.</span>
                                    </div>
                                </c:otherwise>
                            </c:choose>

                        </div>

                    </article>

                </c:otherwise>
            </c:choose>

        </main>
    </div>
</div>

<%@ include file="partials/footer.jsp" %>

</body>
</html>
