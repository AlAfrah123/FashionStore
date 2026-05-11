<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Checkout — FashionStore</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/layout.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/cart-checkout.css">
</head>
<body>

<%@ include file="partials/navbar.jsp" %>

<div class="app-container">
    <main class="commerce-page-main">

        <c:if test="${not empty flashMessage}">
            <div class="flash-banner" role="alert"><c:out value="${flashMessage}"/></div>
        </c:if>

        <nav class="breadcrumb">
            <a href="${pageContext.request.contextPath}/home">Home</a>
            <span class="breadcrumb-sep">/</span>
            <a href="${pageContext.request.contextPath}/cart">Cart</a>
            <span class="breadcrumb-sep">/</span>
            <span class="breadcrumb-current">Checkout</span>
        </nav>

        <div class="checkout-grid">
            <section class="section-card">
                <h1 class="section-title">Delivery &amp; payment</h1>
                <form method="post" action="${pageContext.request.contextPath}/checkout">
                    <div style="margin-bottom:18px;">
                        <label class="label-block" for="deliveryAddress">Delivery address</label>
                        <textarea id="deliveryAddress" name="deliveryAddress"
                                  class="checkout-address"
                                  placeholder="Full address, landmark, PIN code..."
                                  required><c:choose><c:when test="${not empty param.deliveryAddress}"><c:out value="${param.deliveryAddress}"/></c:when><c:when test="${not empty prefillAddress}"><c:out value="${prefillAddress}"/></c:when></c:choose></textarea>
                    </div>
                    <div style="margin-bottom:18px;">
                        <label class="label-block" for="paymentMethod">Payment</label>
                        <select id="paymentMethod" name="paymentMethod" class="select-pay">
                            <option value="COD" selected>Cash on delivery</option>
                            <option value="UPI">UPI (demo)</option>
                            <option value="CARD">Card (demo)</option>
                        </select>
                    </div>
                    <button type="submit" class="btn-mini btn-mini-primary" style="padding:12px 22px;width:100%;">
                        Confirm order
                    </button>
                    <p class="cart-line-meta" style="margin-top:14px;">
                        Each checkout line saves the selected <strong>product size id</strong> on the order item and reduces live stock atomically before the header order is persisted.
                    </p>
                </form>
            </section>

            <section class="section-card">
                <h2 class="section-title" style="font-size:18px;">Order summary</h2>
                <ul class="mini-list">
                    <c:forEach var="line" items="${cartLines}">
                        <li>
                            <span>
                                <strong><c:out value="${line.productName}"/></strong>
                                (<c:out value="${line.sizeLabel}"/>)
                                × <c:out value="${line.quantity}"/>
                            </span>
                            <span>₹<c:out value="${line.lineTotal}"/></span>
                        </li>
                    </c:forEach>
                </ul>
                <div class="summary-row" style="margin-top:14px;">
                    <span>Subtotal</span>
                    <span>₹<c:out value="${cartSubtotal}"/></span>
                </div>
                <div class="summary-row">
                    <span>Shipping</span>
                    <span>₹<c:out value="${shippingCharge}"/></span>
                </div>
                <div class="summary-row summary-grand">
                    <span>Total</span>
                    <span>₹<c:out value="${orderGrandTotal}"/></span>
                </div>
                <div class="cart-actions" style="margin-top:14px;">
                    <a href="${pageContext.request.contextPath}/cart" class="btn-mini btn-mini-ghost">← Back to cart</a>
                </div>
            </section>
        </div>

    </main>
</div>

<%@ include file="partials/footer.jsp" %>

</body>
</html>
