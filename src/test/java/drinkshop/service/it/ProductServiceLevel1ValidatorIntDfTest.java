package drinkshop.service.it;


import drinkshop.domain.CategorieBautura;
import drinkshop.domain.Product;
import drinkshop.domain.TipBautura;
import drinkshop.repository.Repository;
import drinkshop.service.ProductService;
import drinkshop.service.validator.ProductValidator;
import drinkshop.service.validator.ValidationException;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.*;

// configuratia aleasa: (2) S ---> V ---> E si ulterior integrare top down, depth first
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ProductServiceLevel1ValidatorIntDfTest {
    private Product product;
    private ProductValidator validator; // real
    private Repository<Integer, Product> repository;
    private ProductService service;

    @BeforeEach
    void setUp() {
        product = mock(Product.class);
        validator = new ProductValidator(); // integram primul branch, primul nivel (top down depth first)
        repository = mock(Repository.class);

        service = new ProductService(repository, validator);
    }

    @Test
    @Order(1)
    void testAddValid_withRealValidator() {
        // simulare adaugare produs valid cu Product p = new Product(1, "pepsi", 5.0, CategorieBautura.JUICE, TipBautura.BASIC);
        when(product.getId()).thenReturn(1);
        when(product.getNume()).thenReturn("pepsi");
        when(product.getPret()).thenReturn(5.0);
        when(product.getCategorie()).thenReturn(CategorieBautura.JUICE);
        when(product.getTip()).thenReturn(TipBautura.BASIC);

        when(repository.save(product)).thenReturn(product);

        try {
            service.addProduct(product);
        } catch (Exception e) {
            fail("Invalid add operation");
        }

        // verificam interactiunea obiectului testat doar cu obiectele mock ramase
        verify(repository, times(1)).save(product);
        verify(product, times(2)).getNume();
        verify(product, times(1)).getPret();
    }

    @Test
    @Order(2)
    void testAddInvalid_withRealValidator() {
        when(product.getId()).thenReturn(-1);
        Assertions.assertThrows(ValidationException.class, () -> {
            service.addProduct(product);
        });

        // verific interactiunea obiectului testat cu obiectele mock ramase
        verify(repository, never()).save(any());
        verify(product, times(1)).getId();
    }
}
