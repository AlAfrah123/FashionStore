<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html>
<head>
    <title>Product Details</title>
    <link rel="stylesheet" href="assets/css/product-details.css">
</head>

<body>

<div class="product-wrapper">

    <c:if test="${not empty product}">

        <div class="product-card">

            <!-- IMAGE SECTION -->
            <div class="product-image">
                <img src="${product.imageUrl}" alt="${product.productName}">
            </div>

            <!-- INFO SECTION -->
            <div class="product-info">

                <h1>${product.productName}</h1>

                <p class="description">
                    ${product.description}
                </p>

                <div class="price-section">

                    <c:choose>
                        <c:when test="${product.discountPercent > 0}">
                            <span class="original-price">
                                ₹${product.price}
                            </span>

                            <span class="discounted-price">
                                ₹${product.price - (product.price * product.discountPercent / 100)}
                            </span>
                        </c:when>

                        <c:otherwise>
                            <span class="price">₹${product.price}</span>
                        </c:otherwise>
                    </c:choose>

                </div>

                <button class="btn-cart">Add to Cart</button>

            </div>

        </div>

    </c:if>

</div>

</body>
</html>