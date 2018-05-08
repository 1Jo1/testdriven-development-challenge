package berlin.code.tddchallenge.test;

import berlin.code.tddchallenge.Database;
import berlin.code.tddchallenge.TaxCalculator;
import berlin.code.tddchallenge.data.Product;
import berlin.code.tddchallenge.data.ShoppingCart;
import berlin.code.tddchallenge.data.ShoppingCartItem;
import org.junit.jupiter.api.*;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class TaxCalculatorTest {
    private ShoppingCart cart;
    private Database db;

    @BeforeEach
    public void setup() throws IOException {
        db = new Database();
        cart = new ShoppingCart(
                new ShoppingCartItem("apple", 5),
                new ShoppingCartItem("orange", 4),
                new ShoppingCartItem("milk", 2),
                new ShoppingCartItem("red wine", 3),
                new ShoppingCartItem("stamps", 4)
        );
    }

    @Test
    public void productsAvailableTest() {
        for (ShoppingCartItem item : cart.getContents()) {
            assertNotNull(db.getProduct(item.getProductId()), "product lookup failed for " + item);
        }
    }


    @Test
    public void calculationTest() { //Integration Test
        TaxCalculator calc = new TaxCalculator(db, cart);

        assertEquals(2.60,calc.getSubtotal(0),0.005,"unexpected subtotal for 0%");
        assertEquals(9.98,calc.getSubtotal(0.07),0.005,"unexpected subtotal for 7%");
        assertEquals(20.97,calc.getSubtotal(0.19),0.005,"unexpected subtotal for 19%");

        assertEquals(3.35,calc.getSubtotalTax(0.07),0.005,"unexpected subtotalTax for 0%");
        assertEquals(0.65, calc.getSubtotalTax(0.07), 0.005, "unexpected subtotalTax for 7%");
        assertEquals(3.35,calc.getSubtotalTax(0.19),0.005,"unexpected subtotalTax for 19%");

        assertEquals(12.30,calc.getGrandtotalTax(),0.005,"unexpected getGrandtotalTax");
        assertEquals(4.00,calc.getGrandtotalTaxAmount(),0.005,"unexpected getGrandtotalTaxAmount");
    }

    @Test
    public void subTotalCounterTest() {
        TaxCalculator taxCalculator = new TaxCalculator(db,cart);
        int subTotalCounterZero = taxCalculator.getSubTotalCount(0);
        int  subTotalCounterSeven = taxCalculator.getSubTotalCount(0.07);
        int subTotalCounterNineTeen = taxCalculator.getSubTotalCount(0.19);

        assertEquals(4,subTotalCounterZero);
        assertEquals(11,subTotalCounterSeven);
        assertEquals(3,subTotalCounterNineTeen);

    }

    @Test
    public void subTotalGrandTest() {
        TaxCalculator taxCalculator = new TaxCalculator(db,cart);

        int grandTotalCount = taxCalculator.getGrantTotalCount();
        assertEquals(18,grandTotalCount);
    }


    @Test
    public void priceTotalTest() {
        TaxCalculator taxCalculator = new TaxCalculator(db,cart);

        double appleTaxAmount = taxCalculator.getTaxAmount("apple",5);
        double orangeTaxAmount = taxCalculator.getTaxAmount("orange",4);
        double milkTaxAmount = taxCalculator.getTaxAmount("milk",2);
        double redWineTaxAmount = taxCalculator.getTaxAmount("red wine",3);
        double stampsTaxAmount = taxCalculator.getTaxAmount("stamps",4);

        assertEquals(0.26,appleTaxAmount,0.005, "unexptected getTaxAmountTotal apple");
        assertEquals(0.24,orangeTaxAmount,0.005, "unexptected getTaxAmountTotal orange");
        assertEquals(0.16,milkTaxAmount,0.005, "unexptected getTaxAmountTotal milk");
        assertEquals(3.35,redWineTaxAmount,0.005, "unexptected getTaxAmountTotal red wine");
        assertEquals(0.00,stampsTaxAmount,0.005, "unexptected getTaxAmountTotal stamps");
    }

    @Test
    public void priceEachTest() {
        TaxCalculator taxCalculator = new TaxCalculator(db,cart);

        double appleGetPriceTotal = taxCalculator.getPriceTotal("apple",5);
        double orangeTaxAmountPriceTotal = taxCalculator.getPriceTotal("orange",4);

        assertEquals(4.00,appleGetPriceTotal);
        assertEquals(3.60,orangeTaxAmountPriceTotal);
    }
}
