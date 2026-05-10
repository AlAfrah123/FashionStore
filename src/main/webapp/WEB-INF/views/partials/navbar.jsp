<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<nav class="navbar">

    <!-- LEFT: BRAND -->
    <div class="nav-left">
        <a href="home" class="logo">
            FashionStore
        </a>
    </div>

    <!-- CENTER: NAV LINKS -->
    <div class="nav-center">
        <a href="home" class="nav-link active">Home</a>
        <a href="products" class="nav-link">Products</a>
        <a href="categories" class="nav-link">Categories</a>
    </div>

    <!-- RIGHT: ACTIONS -->
    <div class="nav-right">

        <form action="products" method="GET" class="search-form">
            <input 
                type="text" 
                name="query"
                placeholder="Search products..."
                class="search-input"
            />
        </form>

        <a href="cart" class="icon-btn">
            🛒
        </a>

        <a href="login" class="btn-primary">
            Login
        </a>

    </div>

</nav>