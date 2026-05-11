<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Order confirmed — FashionStore</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/layout.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/cart-checkout.css">
</head>
<body>

<%@ include file="partials/navbar.jsp" %>

<div class="app-container">
    <main class="commerce-page-main">

        <c:choose>
            <c:when test="${empty order}">
                <div class="empty-state-card">
                    <p class="empty-state-title">Order not found</p>
                    <a class="primary-btn empty-state-btn" href="${pageContext.request.contextPath}/products">Shop products</a>
                </div>
            </c:when>
            <c:otherwise>

                <header class="order-success-banner">
                    <h1>Thank you!</h1>
                    <p class="cart-line-meta" style="max-width:420px;margin:0 auto;line-height:1.6;color:#444;">
                        Your payment method and delivery snapshot are recorded. Sizes are persisted with each line for traceability against inventory.
                    </p>
                    <div class="order-chip">Order ID #<c:out value="${order.orderId}"/></div>
                    <div class="order-chip">
                        Total ₹<fmt:formatNumber value="${order.totalAmount}" maxFractionDigits="2"/>
                    </div>
                    <div class="order-chip"><c:out value="${order.paymentMethod}"/></div>
                    <div class="order-chip"><c:out value="${order.orderStatus}"/></div>
                </header>

                <section class="section-card">
                    <h2 class="section-title">Ship to</h2>
                    <p style="white-space:pre-line;color:#444;margin:0;"><c:out value="${order.deliveryAddress}"/></p>
                </section>

                <section class="section-card">
                    <h2 class="section-title">Items &amp; size linkage</h2>
                    <div class="cart-table-wrapper">
                        <table class="cart-table">
                            <thead>
                            <tr>
                                <th>Product</th>
                                <th>Size</th>
                                <th>Variant id</th>
                                <th>Qty</th>
                                <th>Subtotal</th>
                            </tr>
                            </thead>
                            <tbody>
                            <c:forEach var="row" items="${orderItems}">
                                <tr>
                                    <td style="font-weight:600;"><c:out value="${row.productName}"/></td>
                                    <td><c:out value="${row.sizeLabel}"/></td>
                                    <td>#<c:out value="${row.productSizeId}"/></td>
                                    <td><c:out value="${row.quantity}"/></td>
                                    <td>₹<c:out value="${row.subtotal}"/></td>
                                </tr>
                            </c:forEach>
                            </tbody>
                        </table>
                    </div>
                </section>

                <div class="cart-actions">
                    <a class="btn-mini btn-mini-primary" href="${pageContext.request.contextPath}/products">
                        Continue shopping
                    </a>
                    <a class="btn-mini btn-mini-ghost" href="${pageContext.request.contextPath}/home">
                        Back to home
                    </a>
                </div>

            </c:otherwise>
        </c:choose>

    </main>
</div>

<%@ include file="partials/footer.jsp" %>

</body>
</html>
