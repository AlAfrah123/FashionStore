package com.fashionstore.util;

import com.fashionstore.model.ShoppingCartLine;

import jakarta.servlet.http.HttpSession;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Session-backed cart persistence (guest-friendly). MVC controllers own business rules — this stores lines only.
 */
public final class SessionCart {

    public static final String SESSION_KEY = "fashionShoppingCartLines";

    private SessionCart() {}

    @SuppressWarnings("unchecked")
    public static List<ShoppingCartLine> getLines(HttpSession session) {

        Object raw = session.getAttribute(SESSION_KEY);

        if (raw instanceof List<?>) {

            List<ShoppingCartLine> copy = new ArrayList<>();

            for (Object o : (List<?>) raw) {
                if (o instanceof ShoppingCartLine) {
                    copy.add((ShoppingCartLine) o);
                }
            }

            session.setAttribute(SESSION_KEY, copy);
            return copy;
        }

        List<ShoppingCartLine> fresh = new ArrayList<>();
        session.setAttribute(SESSION_KEY, fresh);
        return fresh;
    }

    public static void replaceAll(HttpSession session, List<ShoppingCartLine> lines) {

        session.setAttribute(SESSION_KEY, new ArrayList<>(lines));
    }

    public static void clear(HttpSession session) {

        session.removeAttribute(SESSION_KEY);
    }

    public static ShoppingCartLine findLine(List<ShoppingCartLine> cart, int productSizeId) {

        for (ShoppingCartLine line : cart) {

            if (line.getProductSizeId() == productSizeId) {
                return line;
            }
        }

        return null;
    }

    public static boolean removeLineByProductSize(List<ShoppingCartLine> cart, int productSizeId) {

        Iterator<ShoppingCartLine> it = cart.iterator();

        while (it.hasNext()) {

            ShoppingCartLine line = it.next();

            if (line.getProductSizeId() == productSizeId) {

                it.remove();
                return true;
            }
        }

        return false;
    }

    public static double subtotal(List<ShoppingCartLine> cart) {

        double sum = 0;

        for (ShoppingCartLine line : cart) {
            sum += line.getLineTotal();
        }

        return round2(sum);
    }

    public static double round2(double v) {

        return Math.round(v * 100.0) / 100.0;
    }
}
