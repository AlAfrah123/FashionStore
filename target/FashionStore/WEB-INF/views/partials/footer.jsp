<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<footer class="footer">

    <div class="footer-container">

        <div class="footer-section">
            <h2 class="footer-logo">FashionStore</h2>

            <p class="footer-text">
                Modern fashion marketplace with a clean layout inspired by marketplace-grade catalog UX.
            </p>
        </div>

        <div class="footer-section">
            <h3 class="footer-title">Quick links</h3>

            <a href="${ctx}/home" class="footer-link">Home</a>
            <a href="${ctx}/products" class="footer-link">Products</a>
            <a href="${ctx}/cart" class="footer-link">Cart</a>
        </div>

        <div class="footer-section">
            <h3 class="footer-title">Support</h3>

            <a href="#" class="footer-link">Help center</a>
            <a href="#" class="footer-link">Privacy policy</a>
            <a href="#" class="footer-link">Terms &amp; conditions</a>
        </div>

    </div>

    <div class="footer-bottom">
        &copy; 2026 FashionStore. All rights reserved.
    </div>

</footer>
