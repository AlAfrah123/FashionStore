<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Cart — FashionStore</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/layout.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/cart-checkout.css">
</head>
<body>

<%@ include file="partials/navbar.jsp" %>

<div class="app-container">
    <div class="commerce-page-main">

        <c:if test="${not empty flashMessage}">
            <div class="flash-banner" role="status"><c:out value="${flashMessage}"/></div>
        </c:if>

        <nav class="breadcrumb">
            <a href="${pageContext.request.contextPath}/home">Home</a>
            <span class="breadcrumb-sep">/</span>
            <span class="breadcrumb-current">Shopping cart</span>
        </nav>

        <section class="section-card">
            <h1 class="section-title">Shopping cart</h1>

            <c:choose>
                <c:when test="${empty cartLines}">
                    <p class="cart-line-meta" style="margin:0;">Your cart is empty.</p>
                    <div class="cart-actions">
                        <a class="btn-mini btn-mini-primary" href="${pageContext.request.contextPath}/products">Continue shopping</a>
                    </div>
                </c:when>
                <c:otherwise>
                    <div class="cart-table-wrapper">
                        <table class="cart-table">
                            <thead>
                            <tr>
                                <th>Product</th>
                                <th>Price</th>
                                <th>Qty</th>
                                <th>Line total</th>
                                <th></th>
                            </tr>
                            </thead>
                            <tbody>
                            <c:forEach var="line" items="${cartLines}">
                                <tr>
                                    <td>
                                        <div class="cart-product-cell">
                                            <div class="cart-thumb-shell">
                                                <img src="${pageContext.request.contextPath}/${line.imageUrl}" alt="">
                                            </div>
                                            <div>
                                                <p class="cart-line-title"><c:out value="${line.productName}"/></p>
                                                <p class="cart-line-meta">
                                                    Size: <c:out value="${line.sizeLabel}"/>
                                                    · SKU line: variant #<c:out value="${line.productSizeId}"/>
                                                </p>
                                            </div>
                                        </div>
                                    </td>
                                    <td>₹<c:out value="${line.unitPrice}"/></td>
                                    <td>
                                        <form method="post" action="${pageContext.request.contextPath}/cart" class="cart-inline-form">
                                            <input type="hidden" name="action" value="update_qty">
                                            <input type="hidden" name="productSizeId" value="${line.productSizeId}">
                                            <input class="cart-qty-input" type="number" name="qty" min="1"
                                                   value="${line.quantity}">
                                            <button type="submit" class="btn-mini btn-mini-ghost">Update</button>
                                        </form>
                                    </td>
                                    <td style="font-weight:700;color:#635BFF;">₹<c:out value="${line.lineTotal}"/></td>
                                    <td>
                                        <form method="post" action="${pageContext.request.contextPath}/cart">
                                            <input type="hidden" name="action" value="remove">
                                            <input type="hidden" name="productSizeId" value="${line.productSizeId}">
                                            <button type="submit" class="btn-mini btn-mini-danger">Remove</button>
                                        </form>
                                    </td>
                                </tr>
                            </c:forEach>
                            </tbody>
                        </table>
                    </div>

                    <div class="section-card" style="margin-top:22px;margin-bottom:0;background:#faf9ff;">
                        <div class="summary-row">
                            <span>Subtotal</span>
                            <span>₹<c:out value="${cartSubtotal}"/></span>
                        </div>
                        <div class="summary-row">
                            <span>Shipping (flat)</span>
                            <span>₹<c:out value="${cartShippingPreview}"/></span>
                        </div>
                        <div class="summary-row summary-grand">
                            <span>Estimated total</span>
                            <span>₹<c:out value="${cartSubtotal + cartShippingPreview}"/></span>
                        </div>

                        <div class="cart-actions">
                            <a class="btn-mini btn-mini-primary" href="${pageContext.request.contextPath}/checkout">
                                Proceed to checkout
                            </a>
                            <form method="post" action="${pageContext.request.contextPath}/cart">
                                <input type="hidden" name="action" value="clear">
                                <button type="submit" class="btn-mini btn-mini-ghost">Clear cart</button>
                            </form>
                            <a class="btn-mini btn-mini-ghost" href="${pageContext.request.contextPath}/products">Browse more</a>
                        </div>
                    </div>
                </c:otherwise>
            </c:choose>

        </section>
    </div>
</div>

<%@ include file="partials/footer.jsp" %>

</body>
</html>
