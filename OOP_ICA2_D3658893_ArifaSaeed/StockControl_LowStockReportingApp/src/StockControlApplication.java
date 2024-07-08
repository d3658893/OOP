
/**
 *
 * @author MyPC
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

public class StockControlApplication {

    private JFrame frame;
    private JTable stockTable;
    private DefaultTableModel stockTableModel;

    private JTable transactionTable;
    private DefaultTableModel transactionTableModel;
    private final String filePathASC = "files\\AshersSportsCollective.csv";
    private final String filePathMSM = "files\\MengdasSportyMart.csv";

    public StockControlApplication() {
        initialize();
    }
//    static void CreateDatabase() throws SQLException{
//        Connection con;
//        Statement stat;
//        con = DriverManager.getConnection("jdbc:sqlite:stock.db");
//        if(con!=null){
//            System.out.println("DB Created");
//            stat=con.createStatement();
//            con.close();
//        }
//        else{
//            System.out.println("Errirrr!!!!");
//        }
//        
//    }

    private void initialize() {
        frame = new JFrame("Stock Management System");
        frame.setSize(800, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

//        // Set the content pane to use a custom ImagePanel with a background image
//        ImagePanel backgroundPanel = new ImagePanel("C:\\Users\\MyPC\\OneDrive\\Pictures\\storeGUIB.jpeg");
//        frame.setContentPane(backgroundPanel);
        JTabbedPane tabbedPane = new JTabbedPane();

        stockTableModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make all cells non-editable
            }
        };
        stockTable = new JTable(stockTableModel);
        JScrollPane stockScrollPane = new JScrollPane(stockTable);
        stockTableModel.addColumn("Product Code");
        stockTableModel.addColumn("Product Title");
        stockTableModel.addColumn("Quantity in Stock");

        // Set background color for the table
        stockTable.setBackground(Color.WHITE);
        stockTable.getTableHeader().setBackground(new Color(100, 149, 237)); // Adjust the color as needed

        // Set background color for the scroll pane
        stockScrollPane.setBackground(Color.WHITE);

        // Add button to upload ASC CSV data
        JButton uploadASCButton = new JButton("ASC CSV Uploader");
        uploadASCButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ASCStockItem.importASCDataFromCSV(filePathASC);
                loadStockData(); // Refresh the table after upload
            }
        });

        // Add button to upload MSM CSV data
        JButton uploadMSMButton = new JButton("MSM CSV Uploader");
        uploadMSMButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    MSMStockItem.importMSMDataFromCSV(filePathMSM);
                    loadStockData(); // Refresh the table after upload
                } catch (IOException ex) {
                    Logger.getLogger(StockControlApplication.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        stockScrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton addButton = new JButton("Add Stock");
        addButton.addActionListener((ActionEvent e) -> {
            AddStockItemDialog addStockItemDialog = new AddStockItemDialog(frame);
            addStockItemDialog.setVisible(true);
            loadStockData();
        });

        JButton sellButton = new JButton("Sell Stock");
        sellButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = stockTable.getSelectedRow();
                if (selectedRow != -1) {
                    // Assuming the columns are in order as mentioned in the comment
                    String productCode = (String) stockTable.getValueAt(selectedRow, 0);
                    int availableStock = StockItemDAO.getQuantityInStock(productCode);

                    // Prompt the user for the quantity to sell
                    String quantityInput = JOptionPane.showInputDialog(null, "Enter quantity to sell:", "Sell Quantity", JOptionPane.QUESTION_MESSAGE);

                    // Check if the user canceled the input
                    if (quantityInput != null) {
                        try {
                            // Parse the input as an integer
                            int quantitySold = Integer.parseInt(quantityInput);
                            if (quantitySold > availableStock) {
                                JOptionPane.showMessageDialog(null, "Not enough stock available. Available stock: " + availableStock, "Error", JOptionPane.ERROR_MESSAGE);
                                return; // Don't proceed with the transaction
                            }
                            if (quantitySold > 0) {
                                // Fetch other details like date, time, unit price from the selected product
                                String dateTime = getCurrentDateTime(); // You need to implement this method
                                int unitPricePounds = StockItemDAO.getUnitPricePounds(productCode);
                                int unitPricePence = StockItemDAO.getUnitPricePence(productCode);

                                // Record the sales transaction
                                StockItemDAO.insertSalesTransaction(dateTime, productCode, quantitySold, unitPricePounds, unitPricePence);

                                // Update the stock quantity in the stock table
                                StockItemDAO.updateStockQuantity(productCode, quantitySold);
                                int currentStockLevel = StockItemDAO.getQuantityInStock(productCode);
                                int threshold = 5;

                                if (currentStockLevel < threshold) {
                                    // Notify the purchasing department
                                    notifyPurchasingDepartment(productCode, currentStockLevel);
                                }
                                // Refresh the stock table
                                loadStockData();
                                loadTransactionData();

                            } else {
                                JOptionPane.showMessageDialog(null, "Please enter a positive quantity.", "Error", JOptionPane.ERROR_MESSAGE);
                            }
                        } catch (NumberFormatException ex) {
                            JOptionPane.showMessageDialog(null, "Please enter a valid integer quantity.", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Please select a product to sell.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addButton);
        buttonPanel.add(sellButton);
        buttonPanel.add(uploadASCButton);
        buttonPanel.add(uploadMSMButton);
        buttonPanel.setBackground(new Color(100, 149, 237)); // Background color for the button panel

        // Set background color for the frame
        frame.setBackground(Color.WHITE);

        // Center-align the title of the app and set background color for the title bar
        JLabel titleLabel = new JLabel("Stock Management System");
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        titleLabel.setOpaque(true);
        titleLabel.setBackground(new Color(70, 130, 180)); // Adjust the color as needed
        titleLabel.setForeground(Color.WHITE); // Set text color
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18)); // Set font
        frame.getContentPane().add(titleLabel, BorderLayout.NORTH);

        frame.setLayout(new BorderLayout());
        frame.add(stockScrollPane, BorderLayout.CENTER);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        transactionTableModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make all cells non-editable
            }
        };
        transactionTable = new JTable(transactionTableModel);
        JScrollPane transactionScrollPane = new JScrollPane(transactionTable);
        transactionScrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        frame.add(transactionScrollPane, BorderLayout.CENTER);
        // Set background color for the table
        transactionTable.setBackground(Color.WHITE);
        transactionTable.getTableHeader().setBackground(new Color(100, 149, 237)); // Adjust the color as needed

        tabbedPane.addTab("Stock Items", stockScrollPane);
        tabbedPane.addTab("Sales Transactions", transactionScrollPane);

        // Set background color for the tabs
        tabbedPane.setBackground(new Color(100, 149, 237)); // Adjust the color as needed
        // Set foreground color for the tabs
        tabbedPane.setForeground(Color.WHITE);

        frame.getContentPane().add(tabbedPane);
        centerAlignTableHeaders(transactionTable);
        centerAlignTableHeaders(stockTable);
        loadStockData();
        loadTransactionData();

        frame.setVisible(true);
    }

    private void loadTransactionData() {
        List<SalesTransaction> transactions = StockItemDAO.getAllSalesTransactions();
        // Clear existing data
        transactionTableModel.setColumnCount(0);
        transactionTableModel.setRowCount(0);
        // Add static columns for transaction table
        transactionTableModel.addColumn("Transaction Date Time");
        transactionTableModel.addColumn("Product Code");
        transactionTableModel.addColumn("Quantity Sold");
        transactionTableModel.addColumn("Unit Price Pounds");
        transactionTableModel.addColumn("Unit Price Pence");
        // Add transactions to the table
        for (SalesTransaction transaction : transactions) {
            transactionTableModel.addRow(new Object[]{
                transaction.getTransactionDateTime(),
                transaction.getProductCode(),
                transaction.getQuantitySold(),
                transaction.getUnitPricePounds(),
                transaction.getUnitPricePence()
            // Add other columns as needed
            });
        }
    }

    public void loadStockData() {
        // Clear existing columns
        stockTableModel.setColumnCount(0);
        stockTableModel.setRowCount(0);
        // Adding static columns to the table model
        stockTableModel.addColumn("Product Code");
        stockTableModel.addColumn("Product Title");
        stockTableModel.addColumn("Product Description");
        stockTableModel.addColumn("Unit Price (Pounds)");
        stockTableModel.addColumn("Unit Price (Pence)");
        stockTableModel.addColumn("Quantity in Stock");
//      stockTableModel.addColumn("Department");

        // Load stock data into the table
        List<ASCStockItem> stockItems = StockItemDAO.getAllStockItems();

        // Add rows to the table model
        for (ASCStockItem stockItem : stockItems) {
            // Calculate total unit price from pounds and pence
            double unitPrice = stockItem.getUnitPricePounds() + (stockItem.getUnitPricePence() / 100.0);

            Object[] rowData = {
                stockItem.getProductCode(),
                stockItem.getProductTitle(),
                stockItem.getProductDescription(),
                stockItem.getUnitPricePounds(),
                stockItem.getUnitPricePence(),
                stockItem.getQuantityInStock(),// Format unit price to display in two decimal places
            //                stockItem.getDepartmentId()
            };
            stockTableModel.addRow(rowData);
        }
    }

    private void notifyPurchasingDepartment(String productCode, int currentStockLevel) {
        String message = String.format("Stock level for product %s is below the threshold. Current stock level: %d", productCode, currentStockLevel);
        JOptionPane.showMessageDialog(frame, message, "Low Stock Alert", JOptionPane.WARNING_MESSAGE);
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {

                StockItemDAO.createTable(); // Create the database table if not exists
                new StockControlApplication();
                StockItemDAO.deleteAllStockItems();
            }
        });
    }
//    private static boolean doesTableExist(String tableName) {
//        String url = "jdbc:sqlite:stock.db"; // replace with your actual database URL
//        String sql = "SELECT name FROM sqlite_master WHERE type='table' AND name=?";
//
//        try (Connection connection = DriverManager.getConnection(url);
//             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
//            preparedStatement.setString(1, tableName);
//            try (ResultSet resultSet = preparedStatement.executeQuery()) {
//                return resultSet.next(); // true if the table exists, false otherwise
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//            return false; // handle the exception according to your application's requirements
//        }
//    }

    public static String getCurrentDateTime() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return now.format(formatter);
    }

    private void centerAlignTableHeaders(JTable table) {
        JTableHeader header = table.getTableHeader();
        DefaultTableCellRenderer renderer = (DefaultTableCellRenderer) header.getDefaultRenderer();
        renderer.setHorizontalAlignment(JLabel.CENTER);
    }
}
