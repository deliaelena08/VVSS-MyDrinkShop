package drinkshop.service.ut;

import drinkshop.domain.Product;
import drinkshop.repository.Repository;
import drinkshop.service.ProductService;
import drinkshop.service.validator.ProductValidator;
import drinkshop.service.validator.ValidationException;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ProductServiceMockitoTest {
    private Product product;
    private ProductValidator validator;
    private Repository<Integer, Product> repository;
    private ProductService service;

    @BeforeEach
    public void setUp() {
        // creez obiectele mock
        product = mock(Product.class);
        validator = mock(ProductValidator.class);
        repository = mock(Repository.class);

        // obiectul testat
        service = new ProductService(repository, validator);
    }

    @BeforeEach
    public void tearDown() {
        product = null;
        validator = null;
        repository = null;
        service = null;
    }

    @Test
    @Order(1)
    public void testAddValidProduct() {
        Product p1 = mock(Product.class);
        doNothing().when(validator).validate(product);
        when(repository.save(product)).thenReturn(product);

        try {
            service.addProduct(product);
        } catch (Exception e) {
            fail("Invalid add operation");
        }

        assert 0 == service.getAllProducts().size();

        // verificari ale interactiunii obiectului testat cu obiectele mock
        verify(validator, times(1)).validate(product);
        verify(repository, times(1)).save(product);

        verify(validator, times(0)).validate(p1);
        verify(repository, never()).save(p1);
    }

    @Test
    @Order(2)
    public void testAddInvalidProduct() {
        when(product.getId()).thenReturn(-1);
        doThrow(new ValidationException("ID invalid!\n")).when(validator).validate(product);
        when(repository.save(product)).thenReturn(product);

        try {
            service.addProduct(product);
        } catch (Exception e) {
            assert e.getClass().equals(ValidationException.class);
        }

        verify(product, never()).getId();
        verify(validator, times(1)).validate(product);
        verify(repository, never()).save(any());
    }
}
