<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<aside class="sidebar">

    <!-- SIDEBAR HEADER -->
    <div class="sidebar-header">
        <h2>Categories</h2>
    </div>

    <div class="sidebar-categories">

        <!-- ALL PRODUCTS -->
        <a href="${pageContext.request.contextPath}/products"
           class="sidebar-category-item 
           ${empty param.categoryId ? 'active' : ''}">

            All Products

        </a>

        <!-- DYNAMIC CATEGORY LIST -->
        <c:forEach var="category" items="${categories}">

            <a href="${pageContext.request.contextPath}/products?categoryId=${category.categoryId}"
               class="sidebar-category-item 
               ${param.categoryId == category.categoryId ? 'active' : ''}">

                ${category.categoryName}

            </a>

        </c:forEach>

    </div>

</aside>