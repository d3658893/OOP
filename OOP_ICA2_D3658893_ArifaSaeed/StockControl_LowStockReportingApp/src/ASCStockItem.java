/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 *
 * @author MyPC
 */
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Objects;

public class ASCStockItem {

    private String productCode;
    private String productTitle;
    private String productDescription;
    private int unitPricePounds;
    private int unitPricePence;
    private int quantityInStock;
    private String department;
    private int departmentId;

    public ASCStockItem(String productCode, String productTitle, String productDescription,
            int unitPricePounds, int unitPricePence, int quantityInStock, int departmentId) {
        this.productCode = productCode;
        this.productTitle = productTitle;
        this.productDescription = productDescription;
        this.unitPricePounds = unitPricePounds;
        this.unitPricePence = unitPricePence;
        this.quantityInStock = quantityInStock;
//        this.department = department;
        this.departmentId = departmentId;
    }

    public static boolean isASCFormat(String productCode) {
        return productCode.startsWith("RUN");
    }
//    public ASCStockItem(String productCode, String productTitle, int quantityInStock) {
//        this.productCode = productCode;
//        this.productTitle = productTitle;
//        this.quantityInStock = quantityInStock;
//        // Initialize other fields if needed
//    }

    public String getProductCode() {
        return productCode;
    }

    public String getProductTitle() {
        return productTitle;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public int getUnitPricePounds() {
        return unitPricePounds;
    }

    public int getUnitPricePence() {
        return unitPricePence;
    }

    public int getQuantityInStock() {
        return quantityInStock;
    }

    public int getDepartmentId() {
        return departmentId;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public void setProductTitle(String productTitle) {
        this.productTitle = productTitle;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public void setUnitPricePounds(int unitPricePounds) {
        this.unitPricePounds = unitPricePounds;
    }

    public void setUnitPricePence(int unitPricePence) {
        this.unitPricePence = unitPricePence;
    }

    public void setQuantityInStock(int quantityInStock) {
        this.quantityInStock = quantityInStock;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        ASCStockItem that = (ASCStockItem) obj;
        return Objects.equals(productCode, that.productCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productCode);
    }

    // Method to import data for ASCStockItem from CSV file
    public static void importASCDataFromCSV(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                // Skip BOM character if present
                if (line.startsWith("\uFEFF")) {
                    line = line.substring(1);
                }
                String[] data = line.split(",");

                // Trim leading/trailing whitespaces from each value
                for (int i = 0; i < data.length; i++) {
                    data[i] = data[i].trim();
                }

                // Assuming the CSV format is as follows:
                // ProductCode, ProductTitle, ProductDescription, QuantityInStock, UnitPricePence, Department
                String productCode = data[0];
                String productTitle = data[1];
                String productDescription = data[2];
                int unitPricePound = Integer.parseInt(data[3]);
                int unitPricePence = Integer.parseInt(data[4]);
                int quantityInStock = Integer.parseInt(data[5]);
                String department = getDepartmentFromProductCode(productCode);
                int departmentId = 0;
                switch (department) {
                case "RUN" -> departmentId = 1;
                case "SWM" -> departmentId = 2;
                case "CYC" -> departmentId = 3;
                default -> {
                    // Handle the default case if needed
                }
            }
                
                // Create ASCStockItem object and insert into the database
                ASCStockItem ascStockItem = new ASCStockItem(productCode, productTitle, productDescription, unitPricePound, unitPricePence, quantityInStock, departmentId);
                StockItemDAO.insertStockItem(ascStockItem);
            }
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
            // Handle the exception as needed, e.g., log the error or show an error message
        }
    }

// Method to import data for MSMStockItem from CSV file
// Helper method to get department from product code
    private static String getDepartmentFromProductCode(String productCode) {
        // Extract department from the product code (assuming the format "RUN1234567")
        return productCode.substring(0, 3);
    }

}

