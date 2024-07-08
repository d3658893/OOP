import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MSMStockItem {
    private final int departmentId;
    private final String code;
    private final String nameAndDescription;
    private final int unitPrice;
    private int quantityInStock;

    public MSMStockItem(int departmentId, String code, String titleAndDescription, int unitPrice, int quantityInStock) {
        this.departmentId = departmentId;
        this.code = code;
        this.nameAndDescription = titleAndDescription;
        this.unitPrice = unitPrice;
        this.quantityInStock = quantityInStock;
    }

    public int getDepartmentId() {
        return departmentId;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return nameAndDescription.substring(0, Math.min(59, nameAndDescription.length())).replaceAll("\u00a0", "").stripTrailing();
    }

    public String getDescription() {
        return nameAndDescription.substring(60).trim();
    }

    public int getUnitPrice() {
        return unitPrice;
    }

    public int getQuantityInStock() {
        return quantityInStock;
    }

    public String getHumanFriendlyUnitPrice() {
        final int pounds = getUnitPrice() / 100;
        final int pence = getUnitPrice() % 100;
        return String.format("%d.%02d", pounds, pence);
    }

    public void setQuanity(int newQuantity) {
        if (newQuantity >= 0) {
            quantityInStock = newQuantity;
        }
    }

    @Override
    public String toString() {
        return String.format("%d-%s - %s - %s - UNIT PRICE: £%s - QTY: %d",
                getDepartmentId(),
                getCode(),
                getName(),
                getDescription(),
                getHumanFriendlyUnitPrice(),
                getQuantityInStock());
    }

    public static void importMSMDataFromCSV(String filePath) throws FileNotFoundException, IOException {
    try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
        String line;
        while ((line = br.readLine()) != null) {
            // Skip BOM character if present
            if (line.startsWith("\uFEFF")) {
                line = line.substring(1);
            }

            String[] data = line.split(",");
            String department = null;
            int departmentCode = Integer.parseInt(data[0]);

            switch (departmentCode) {
                case 1 -> department = "RUN";
                case 2 -> department = "SWM";
                case 3 -> department = "CYC";
                default -> {
                    // Handle the default case if needed
                }
            }

            // Trim leading/trailing whitespaces from each value
            for (int i = 1; i < data.length; i++) {
                data[i] = data[i].trim();
            }

            // Assuming the CSV format is as follows:
            // Department, ProductCode, ProductTitle, ProductDescription, UnitPricePence, QuantityInStock
            String productCode = String.format("%s-%s-%s", department, data[1], "MSM");
            String productTitle = data[2].substring(0, Math.min(data[2].length(), 60)).trim();
            String productDescription = data[2].substring(60).trim();
            int unitPricePence = Integer.parseInt(data[3]);
            int pounds = unitPricePence / 100;
            int pence = unitPricePence % 100;
            int quantityInStock = Integer.parseInt(data[4]);

            MSMStockItem newStockItem = new MSMStockItem(
                    departmentCode, productCode, productTitle,
                    unitPricePence, quantityInStock
            );

            // Assuming StockItemDAO.insertStockItem() supports both ASCStockItem and MSMStockItem
            StockItemDAO.insertMSMStockItem(newStockItem);
        }
    } catch (NumberFormatException e) {
        e.printStackTrace();
        // Handle the exception as needed, e.g., log the error or show an error message
    }
}

}
