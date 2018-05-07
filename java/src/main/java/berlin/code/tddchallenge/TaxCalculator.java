package berlin.code.tddchallenge;

import berlin.code.tddchallenge.data.Product;
import berlin.code.tddchallenge.data.ShoppingCart;
import berlin.code.tddchallenge.data.ShoppingCartItem;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;

public class TaxCalculator {
    private Map<String,Product> products;
    private List<ShoppingCartItem> shoppingCartItems;

    public TaxCalculator(Database db, ShoppingCart cart) {
        products = db.getProducts();
        shoppingCartItems = cart.getContents();
    }

    public double getGrandtotalTaxAmount() {
        int grandTotal = 0;
        for(ShoppingCartItem shoppingCartItem : shoppingCartItems) {
            grandTotal += getTaxAmount(shoppingCartItem.getProductId(),shoppingCartItem.getQuantity());
        }
        return grandTotal;
    }

    public double getGrandtotalTax() {
        int grandTotalTax = 0;
        for(ShoppingCartItem shoppingCartItem : shoppingCartItems) {
            grandTotalTax += products.get(shoppingCartItem.getProductId()).getPrice();
        }
        return grandTotalTax;
    }

    public int getSubTotalCount(double taxRate) {
        return 0;
    }



    public int getGrantTotalCount() {
        int grandTotalCount = 0;
        for(ShoppingCartItem shoppingCartItem : shoppingCartItems) {
            grandTotalCount += shoppingCartItem.getQuantity();
        }
        return grandTotalCount;
    }

    public double getSubtotal(double taxRate) {
        return 0; //
    }

    public double getSubtotalTax(double taxRate) {
        return 0;
    }

    public double getPriceTotal(String productId,int quantity) {
        return products.get(productId).getPrice() * quantity;
    }

    public double getTaxAmount(String productId, int quantity) {
        Product product = products.get(productId);
        double productTaxRate = product.getTaxRate();
        double netPrice = getNetPrice(productTaxRate,product.getPrice());
        double rawTax = product.getPrice() - netPrice;
        return rawTax * quantity;
    }

    private double getNetPrice(double productTaxRate,double productPrice) {
        return  productPrice / (1 + productTaxRate);
    }



}
