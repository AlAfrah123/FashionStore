-- Links each order_line to inventory variant (ProductSize row).
-- Run once against your FashionStore schema before deploying checkout.

ALTER TABLE order_items
    ADD COLUMN product_size_id INT NULL AFTER size_label;

ALTER TABLE order_items
    ADD CONSTRAINT fk_order_items_product_size
        FOREIGN KEY (product_size_id) REFERENCES product_sizes (product_size_id);

-- Older rows tolerate NULL tooling rows; tighten to NOT NULL after back-filling SKU ids if required.

-- Anonymous / classroom checkout fallback user (referenced when no login session exists)
INSERT IGNORE INTO users (user_id, full_name, email, phone, password, gender, address)
VALUES (1, 'Guest Checkout', 'guest@fashionstore.local', '0000000000', '-', '-', '-');
