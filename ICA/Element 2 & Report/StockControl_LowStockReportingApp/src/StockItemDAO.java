/**
 *
 * @author MyPC
 */
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

public class StockItemDAO {

    private static final String DATABASE_URL = "jdbc:sqlite:stock.db";

    // ... (other methods)
    private static Connection getConnection() throws SQLException {
        String url = "jdbc:sqlite:stock.db";
        return DriverManager.getConnection(url);
    }

    public static void deleteAllStockItems() {
        String sql = "DELETE FROM stock";

        try (Connection connection = DriverManager.getConnection(DATABASE_URL); PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle the exception as needed, e.g., log the error or show an error message
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void createTable() {
        try (Connection connection = DriverManager.getConnection(DATABASE_URL); Statement statement = connection.createStatement()) {
            // Check if the table exists
            ResultSet resultSet = connection.getMetaData().getTables(null, null, "stock", null);

            if (!resultSet.next()) {
                // If the table doesn't exist, create it
                String createTableSQL = "CREATE TABLE stock ("
                        + "product_code TEXT PRIMARY KEY,"
                        + "product_title TEXT,"
                        + "product_description TEXT,"
                        + "unit_price_pounds INTEGER,"
                        + "unit_price_pence INTEGER,"
                        + "quantity_in_stock INTEGER,"
                        + "department TEXT)";
                statement.executeUpdate(createTableSQL);
                System.out.println("table created");
            } else {
                // If the table exists, check if the "department" column is present
                resultSet = connection.getMetaData().getColumns(null, null, "stock", "department");
                if (!resultSet.next()) {
                    // If the "department" column is missing, add it
                    String addColumnSQL = "ALTER TABLE stock ADD COLUMN department TEXT";
                    statement.executeUpdate(addColumnSQL);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        try (Connection connection = DriverManager.getConnection(DATABASE_URL); Statement statement = connection.createStatement()) {
            String salesTableSQL = "CREATE TABLE IF NOT EXISTS sales ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "date_time TEXT,"
                    + "product_code TEXT,"
                    + "quantity_sold INTEGER,"
                    + "unit_price_pounds INTEGER,"
                    + "unit_price_pence INTEGER)";
            statement.executeUpdate(salesTableSQL);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<SalesTransaction> getAllSalesTransactions() {
        List<SalesTransaction> transactions = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(DATABASE_URL); Statement statement = connection.createStatement(); ResultSet resultSet = statement.executeQuery("SELECT * FROM sales")) {

            while (resultSet.next()) {
                Date transactionDateTime = resultSet.getDate("date_time");
                String productCode = resultSet.getString("product_code");
                int quantitySold = resultSet.getInt("quantity_sold");
                int unitPricePounds = resultSet.getInt("unit_price_pounds");
                int unitPricePence = resultSet.getInt("unit_price_pence");

                // Assuming SalesTransaction constructor takes relevant parameters
                SalesTransaction transaction = new SalesTransaction(
                        transactionDateTime, productCode, quantitySold, unitPricePounds, unitPricePence
                // Add other fields as needed
                );

                transactions.add(transaction);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle the exception as needed
        }

        return transactions;
    }

    public static void insertMSMStockItem(MSMStockItem msmStockItem) {
        try (Connection connection = DriverManager.getConnection(DATABASE_URL); PreparedStatement checkStatement = connection.prepareStatement("SELECT 1 FROM stock WHERE product_code = ?"); PreparedStatement insertStatement = connection.prepareStatement(
                "INSERT INTO stock (product_code, product_title, product_description, "
                + "unit_price_pounds, unit_price_pence, quantity_in_stock, department) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)")) {

            // Check if the product_code already exists
            checkStatement.setString(1, msmStockItem.getCode());
            ResultSet resultSet = checkStatement.executeQuery();

            if (resultSet.next()) {
                // Product code already exists, handle accordingly (show error, update, etc.)
                JOptionPane.showMessageDialog(null, "Product code already exists. Choose a different code.");
            } else {
                // Product code does not exist, proceed with the insertion
                insertStatement.setString(1, msmStockItem.getCode());
                insertStatement.setString(2, msmStockItem.getName());
                insertStatement.setString(3, msmStockItem.getDescription());
                insertStatement.setInt(4, msmStockItem.getUnitPrice() / 100); // Assuming unitPrice is in pence
                insertStatement.setInt(5, msmStockItem.getUnitPrice() % 100); // Assuming unitPrice is in pence
                insertStatement.setInt(6, msmStockItem.getQuantityInStock());
                insertStatement.setInt(7, msmStockItem.getDepartmentId());

                int rowsAffected = insertStatement.executeUpdate();

                if (rowsAffected > 0) {
                    // Show a popup indicating successful insertion
                    JOptionPane.showMessageDialog(null, "Record inserted successfully!");

                } else {
                    // Handle the case where no rows were affected (insertion failed)
                    JOptionPane.showMessageDialog(null, "Failed to insert record.");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            // Handle the exception as needed, e.g., log the error or show an error message
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void insertSalesTransaction(String dateTime, String productCode, int quantitySold,
            int unitPricePounds, int unitPricePence) {
        try (Connection connection = DriverManager.getConnection(DATABASE_URL); PreparedStatement preparedStatement = connection.prepareStatement(
                "INSERT INTO sales (date_time, product_code, quantity_sold, unit_price_pounds, unit_price_pence) "
                + "VALUES (?, ?, ?, ?, ?)")) {

            preparedStatement.setString(1, dateTime);
            preparedStatement.setString(2, productCode);
            preparedStatement.setInt(3, quantitySold);
            preparedStatement.setInt(4, unitPricePounds);
            preparedStatement.setInt(5, unitPricePence);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle the exception as needed
        }
    }

    public static void insertStockItem(ASCStockItem stockItem) {
        try (Connection connection = DriverManager.getConnection(DATABASE_URL); PreparedStatement checkStatement = connection.prepareStatement("SELECT 1 FROM stock WHERE product_code = ?"); PreparedStatement insertStatement = connection.prepareStatement(
                "INSERT INTO stock (product_code, product_title, product_description, "
                + "unit_price_pounds, unit_price_pence, quantity_in_stock, department) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)")) {

            // Check if the product_code already exists
            checkStatement.setString(1, stockItem.getProductCode());
            ResultSet resultSet = checkStatement.executeQuery();

            if (resultSet.next()) {
                // Product code already exists, handle accordingly (show error, update, etc.)
                JOptionPane.showMessageDialog(null, "Product code already exists. Choose a different code.");
            } else {
                // Product code does not exist, proceed with the insertion
                insertStatement.setString(1, stockItem.getProductCode());
                insertStatement.setString(2, stockItem.getProductTitle());
                insertStatement.setString(3, stockItem.getProductDescription());
                insertStatement.setInt(4, stockItem.getUnitPricePounds());
                insertStatement.setInt(5, stockItem.getUnitPricePence());
                insertStatement.setInt(6, stockItem.getQuantityInStock());
                insertStatement.setInt(7, stockItem.getDepartmentId());

                int rowsAffected = insertStatement.executeUpdate();

                if (rowsAffected > 0) {
                    // Show a popup indicating successful insertion
                    JOptionPane.showMessageDialog(null, "Record inserted successfully!");

                } else {
                    // Handle the case where no rows were affected (insertion failed)
                    JOptionPane.showMessageDialog(null, "Failed to insert record.");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            // Handle the exception as needed, e.g., log the error or show an error message
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static int getQuantityInStock(String productCode) {
        int quantityInStock = 0;

        try (Connection connection = DriverManager.getConnection(DATABASE_URL); PreparedStatement preparedStatement = connection.prepareStatement("SELECT quantity_in_stock FROM stock WHERE product_code = ?")) {

            preparedStatement.setString(1, productCode);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    quantityInStock = resultSet.getInt("quantity_in_stock");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

        return quantityInStock;
    }

    public static void updateStockQuantity(String productCode, int quantitySold) {
        try (Connection connection = DriverManager.getConnection(DATABASE_URL); PreparedStatement preparedStatement = connection.prepareStatement(
                "UPDATE stock SET quantity_in_stock = quantity_in_stock - ? WHERE product_code = ?")) {

            preparedStatement.setInt(1, quantitySold);
            preparedStatement.setString(2, productCode);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle the exception as needed, e.g., log the error or show an error message
            JOptionPane.showMessageDialog(null, "Error updating stock quantity: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static List<ASCStockItem> getAllStockItems() {
        List<ASCStockItem> stockItems = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(DATABASE_URL); Statement statement = connection.createStatement(); ResultSet resultSet = statement.executeQuery("SELECT * FROM stock")) {

            while (resultSet.next()) {
                ASCStockItem stockItem = new ASCStockItem(
                        resultSet.getString("product_code"),
                        resultSet.getString("product_title"),
                        resultSet.getString("product_description"),
                        resultSet.getInt("unit_price_pounds"),
                        resultSet.getInt("unit_price_pence"),
                        resultSet.getInt("quantity_in_stock"),
                        resultSet.getInt("department")
                // Add other fields as needed
                );
                stockItems.add(stockItem);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

        return stockItems;
    }

    public static void updateStockItem(ASCStockItem stockItem) {
        try (Connection connection = DriverManager.getConnection(DATABASE_URL); PreparedStatement preparedStatement = connection.prepareStatement(
                "UPDATE stock SET product_title=?, product_description=?, "
                + "unit_price_pounds=?, unit_price_pence=?, quantity_in_stock=?, department=? "
                + "WHERE product_code=?")) {

            preparedStatement.setString(1, stockItem.getProductTitle());
            preparedStatement.setString(2, stockItem.getProductDescription());
            preparedStatement.setInt(3, stockItem.getUnitPricePounds());
            preparedStatement.setInt(4, stockItem.getUnitPricePence());
            preparedStatement.setInt(5, stockItem.getQuantityInStock());
            preparedStatement.setInt(6, stockItem.getDepartmentId());
            preparedStatement.setString(7, stockItem.getProductCode());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static int getNextSequentialNumber(String department) {
        String query = "SELECT count(product_code) FROM stock WHERE department = ?";

        try (Connection connection = getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, department);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    // Increment the maximum sequential number by 1
                    return resultSet.getInt(1) + 1;
                } else {
                    // If no records are found, start from 1
                    return 1;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle the exception appropriately
            return -1; // Error value
        }
    }

    public static int getUnitPricePounds(String productCode) {
        int unitPricePounds = 0; // Default value, modify based on your requirements

        try (Connection connection = DriverManager.getConnection(DATABASE_URL); PreparedStatement preparedStatement = connection.prepareStatement("SELECT unit_price_pounds FROM stock WHERE product_code = ?")) {

            preparedStatement.setString(1, productCode);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    unitPricePounds = resultSet.getInt("unit_price_pounds");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle the exception as needed
        }

        return unitPricePounds;
    }

    public static int getUnitPricePence(String productCode) {
        int unitPricePence = 0; // Default value, modify based on your requirements

        try (Connection connection = DriverManager.getConnection(DATABASE_URL); PreparedStatement preparedStatement = connection.prepareStatement("SELECT unit_price_pence FROM stock WHERE product_code = ?")) {

            preparedStatement.setString(1, productCode);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    unitPricePence = resultSet.getInt("unit_price_pence");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle the exception as needed
        }

        return unitPricePence;
    }

}
