package drinkshop.receipt;

import drinkshop.domain.Order;
import drinkshop.domain.OrderItem;
import drinkshop.domain.Product;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ReceiptGeneratorTest {

    private Product createProduct(int id, String name, double price) {
        return new Product(id, name, price, null, null);
    }

    @Test
    public void testTC01_DateInvalide_Null() {
        // Path 1: o == null
        String result = ReceiptGenerator.generate(null, new ArrayList<>());
        assertEquals("Eroare: Date invalide!", result);
    }

    @Test
    public void testTC02_ComandaGoala() {
        // Path 2: order fara iteme
        Order order = new Order(1);
        List<Product> products = Arrays.asList(createProduct(1, "Apa", 5.0));

        String result = ReceiptGenerator.generate(order, products);
        String expected = "===== BON FISCAL =====\n---------------------\nTOTAL: 0.0 RON\n";

        assertEquals(expected, result);
    }

    @Test
    public void testTC03_ProdusLipsaDinLista() {
        // Path 3: produs in order, dar nu e gasit in lista products (p == null)
        Order order = new Order(2);
        Product unkownProduct = createProduct(99, "Secret", 10.0);
        order.getItems().add(new OrderItem(unkownProduct, 1));

        List<Product> products = Arrays.asList(createProduct(1, "Apa", 5.0));
        String result = ReceiptGenerator.generate(order, products);
        String expected = "===== BON FISCAL =====\n---------------------\nTOTAL: 0.0 RON\n";

        assertEquals(expected, result);
    }

    @Test
    public void testTC04_ProdusGasit_FaraReduceri() {
        // Path 4: produs gasit, qty <= 5, total <= 500
        Order order = new Order(3);
        Product p = createProduct(1, "Suc", 10.0);
        order.getItems().add(new OrderItem(p, 2));

        List<Product> products = Arrays.asList(p);

        String result = ReceiptGenerator.generate(order, products);
        String expected = "===== BON FISCAL =====\nSuc: 20.0 RON\n---------------------\nTOTAL: 20.0 RON\n";

        assertEquals(expected, result);
    }

    @Test
    public void testTC05_ProdusGasit_ReducereCantitate() {
        // Path 5: produs gasit, qty > 5 (se aplica 10% reducere la linie)
        Order order = new Order(4);
        Product p = createProduct(1, "Vin", 10.0);
        order.getItems().add(new OrderItem(p, 6));

        List<Product> products = Arrays.asList(p);

        String result = ReceiptGenerator.generate(order, products);
        String expected = "===== BON FISCAL =====\nVin: 54.0 RON\n---------------------\nTOTAL: 54.0 RON\n";

        assertEquals(expected, result);
    }

    @Test
    public void testTC06_ProdusGasit_ReducereTotalMare() {
        // Path 6: produs gasit, qty <= 5, total > 500 (se scade 50 din total)
        Order order = new Order(5);
        Product p = createProduct(1, "Sampanie", 300.0);
        order.getItems().add(new OrderItem(p, 2));

        List<Product> products = Arrays.asList(p);

        String result = ReceiptGenerator.generate(order, products);
        String expected = "===== BON FISCAL =====\nSampanie: 600.0 RON\n---------------------\nTOTAL: 550.0 RON\n";

        assertEquals(expected, result);
    }
}