package drinkshop.service;

import drinkshop.domain.CategorieBautura;
import drinkshop.domain.Product;
import drinkshop.domain.TipBautura;
import drinkshop.repository.file.FileProductRepository;
import drinkshop.service.validator.ProductValidator;
import drinkshop.service.validator.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.PrintWriter;

import static org.junit.jupiter.api.Assertions.*;

class ProductServiceTest {

    private ProductService service;
    private FileProductRepository testRepo;

    @BeforeEach
    void setUp() throws Exception {
        String testFilename = "data/test/products-test.txt";

        new PrintWriter(testFilename).close(); // sa fie sigur gol

        testRepo = new FileProductRepository(testFilename);
        service = new ProductService(testRepo, new ProductValidator());
    }

    @Test
    @DisplayName("ECP Valid: adaugare produs cu nume valid")
    @Tag("ECP")
    void testAddProduct_ECP() {
        Product p = new Product(1, "pepsi", 5.0, CategorieBautura.JUICE, TipBautura.BASIC);
        service.addProduct(p);

        assertEquals(1, service.getAllProducts().size());
        assertEquals("pepsi", service.findById(1).getNume());
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " "})
    @DisplayName("ECP Non-Valid: adaugare produs cu nume gol")
    void testAddProduct_ECPNonValid(String invalidName) {
        Product p = new Product(2, invalidName, 10.0, CategorieBautura.JUICE, TipBautura.BASIC);
        assertThrows(ValidationException.class, () -> service.addProduct(p));
    }

    @Test
    @DisplayName("BVA Valid: Pret minim (0.01)")
    void testAddProduct_BVA_Valid_min() {
        Product p = new Product(3, "pepsi", 0.01, CategorieBautura.JUICE, TipBautura.BASIC);
        service.addProduct(p);
        assertEquals(0.01, service.findById(3).getPret());
    }

    @Test
    @DisplayName("BVA Non-Valid: pret 0 (limita)")
    void testAddProduct_BVA_NonValid_limita() {
        Product p = new Product(4, "pepsi", 0.0, CategorieBautura.JUICE, TipBautura.BASIC);
        assertThrows(ValidationException.class, () -> service.addProduct(p));
    }

    @Test
    @DisplayName("BVA Non-Valid: pret negativ")
    void testAddProduct_BVA_NonValid_negativ() {
        Product p = new Product(5, "pepsi", -0.01, CategorieBautura.JUICE, TipBautura.BASIC);
        assertThrows(ValidationException.class, () -> service.addProduct(p));
    }
}