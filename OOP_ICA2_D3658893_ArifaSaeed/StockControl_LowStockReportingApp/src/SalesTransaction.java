/**
 *
 * @author MyPC
 */
import java.util.Date;

public class SalesTransaction {
    private Date transactionDateTime;
    private String productCode;
    private int quantitySold;
    private int unitPricePounds;
    private int unitPricePence;

    // Constructors, getters, setters, and other methods

    // Example constructor
    public SalesTransaction(Date transactionDateTime, String productCode, int quantitySold, int unitPricePounds, int unitPricePence) {
        this.transactionDateTime = transactionDateTime;
        this.productCode = productCode;
        this.quantitySold = quantitySold;
        this.unitPricePounds = unitPricePounds;
        this.unitPricePence = unitPricePence;
    }

    // Getters
    public Date getTransactionDateTime() {
        return transactionDateTime;
    }

    public String getProductCode() {
        return productCode;
    }

    public int getQuantitySold() {
        return quantitySold;
    }

    public int getUnitPricePounds() {
        return unitPricePounds;
    }

    public int getUnitPricePence() {
        return unitPricePence;
    }

    // Add getters for other attributes as needed
}
