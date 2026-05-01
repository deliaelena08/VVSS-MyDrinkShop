package drinkshop.service.it;

import drinkshop.domain.CategorieBautura;
import drinkshop.domain.Product;
import drinkshop.domain.TipBautura;
import drinkshop.repository.Repository;
import drinkshop.service.ProductService;
import drinkshop.service.validator.ProductValidator;
import drinkshop.service.validator.ValidationException;
import org.junit.jupiter.api.*;

import static org.mockito.Mockito.*;

// Pasul 3: S + V (reale) + E (real). R ramane Mock.
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ProductServiceLevel2ProductIntTest {
    private ProductValidator validator; // Real
    private Repository<Integer, Product> repository; // Mock
    private ProductService service;

    @BeforeEach
    void setUp() {
        validator = new ProductValidator();
        repository = mock(Repository.class); // ramane mock pentru a izola integrarea E
        service = new ProductService(repository, validator);
    }

    @Test
    @Order(1)
    void testAddProduct_RealEntity_Success() {
        Product realProduct = new Product(1, "Pepsi", 5.0, CategorieBautura.JUICE, TipBautura.BASIC);
        when(repository.save(realProduct)).thenReturn(realProduct);

        Assertions.assertDoesNotThrow(() -> service.addProduct(realProduct));
        verify(repository, times(1)).save(realProduct);
    }

    @Test
    @Order(2)
    void testAddProduct_RealEntity_InvalidData() {
        Product invalidProduct = new Product(-5, "", -1.0, CategorieBautura.JUICE, TipBautura.BASIC);

        Assertions.assertThrows(ValidationException.class, () -> {
            service.addProduct(invalidProduct);
        });

        verify(repository, never()).save(any());
    }
}