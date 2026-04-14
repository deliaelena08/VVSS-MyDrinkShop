package drinkshop.receipt;

import drinkshop.domain.Order;
import drinkshop.domain.OrderItem;
import drinkshop.domain.Product;
import java.util.List;

public class ReceiptGenerator {

    private static Product findProductById(List<Product> products, int id) {
        for (Product p : products) {
            if (p.getId() == id) {
                return p;
            }
        }
        return null;
    }

    public static String generate(Order o, List<Product> products) {
        if (o == null || products == null) {
            return "Eroare: Date invalide!";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("===== BON FISCAL =====\n");
        double totalCalculator = 0.0;

        for (OrderItem i : o.getItems()) {
            Product p = findProductById(products, i.getProduct().getId());

            if (p != null) {
                double lineTotal = p.getPret() * i.getQuantity();

                if (i.getQuantity() > 5) {
                    lineTotal = lineTotal * 0.9;
                }

                sb.append(p.getNume()).append(": ").append(lineTotal).append(" RON\n");
                totalCalculator += lineTotal;
            }
        }

        if (totalCalculator > 500) {
            totalCalculator -= 50;
        }

        sb.append("---------------------\nTOTAL: ").append(totalCalculator).append(" RON\n");
        return sb.toString();
    }
}