package com.fashionstore.model;

/** URL param sort=… values consumed by ProductServlet + ProductDAO. */
public enum ProductSortOption {

    POPULARITY,
    PRICE_ASC,
    PRICE_DESC,
    NEWEST;

    public static ProductSortOption fromRequestParam(String raw) {

        if (raw == null || raw.isBlank()) {
            return POPULARITY;
        }

        switch (raw.trim()) {
            case "price_asc":
                return PRICE_ASC;
            case "price_desc":
                return PRICE_DESC;
            case "newest":
                return NEWEST;
            case "popularity":
            default:
                return POPULARITY;
        }
    }
}
