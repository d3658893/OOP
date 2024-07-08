/**
 *
 * @author MyPC
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AddStockItemDialog extends JDialog {

    private JTextField productTitleField;
    private JTextField productDescriptionField;
    private JTextField unitPricePoundsField;
    private JTextField unitPricePenceField;
    private JTextField quantityInStockField;
    private JButton addButton;
    private JComboBox<String> departmentComboBox;
    String[] departments = {"RUN", "CYC", "SWM"};
    private JComboBox<String> companyComboBox;
    private String[] companies = {"ASC", "MSM"};

    public AddStockItemDialog(JFrame parent) {
        super(parent, "Add Stock Item", true);

        // Initialize components
        companyComboBox = new JComboBox<>(companies);
        departmentComboBox = new JComboBox<>(departments);
        productTitleField = new JTextField(20);
        productDescriptionField = new JTextField(20);
        unitPricePoundsField = new JTextField(20);
        unitPricePenceField = new JTextField(20);
        quantityInStockField = new JTextField(20);
        addButton = new JButton("Add Stock");

        // Set layout
        setLayout(new GridBagLayout());
        setSize(350, 500);
        setResizable(false);
        setLocationRelativeTo(parent);

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.insets = new Insets(5, 5, 5, 5);

        // Add components to the layout with center alignment
        addToLayout(createCenteredLabel("Company:"), constraints);
        addToLayout(companyComboBox, constraints);
        addToLayout(createCenteredLabel("Product Title:"), constraints);
        addToLayout(productTitleField, constraints);
        addToLayout(createCenteredLabel("Product Description:"), constraints);
        addToLayout(productDescriptionField, constraints);
        addToLayout(createCenteredLabel("Unit Price (Pounds):"), constraints);
        addToLayout(unitPricePoundsField, constraints);
        addToLayout(createCenteredLabel("Unit Price (Pence):"), constraints);
        addToLayout(unitPricePenceField, constraints);
        addToLayout(createCenteredLabel("Quantity in Stock:"), constraints);
        addToLayout(quantityInStockField, constraints);
        addToLayout(createCenteredLabel("Department:"), constraints);
        addToLayout(departmentComboBox, constraints);
        addToLayout(createCenteredButton(addButton), constraints);

        // Add action listener to the "Add Stock" button
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (validateFields()) {
                    handleAddStockItem();
                    dispose();  // Close the dialog only if validation succeeds
                } else {
                    JOptionPane.showMessageDialog(AddStockItemDialog.this,
                            "Please enter valid values for all fields.", "Validation Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    private void addToLayout(Component component, GridBagConstraints constraints) {
        add(component, constraints);
        constraints.gridy++;

        // Reset gridy to the next row if it's an even row number
        if (constraints.gridy % 2 == 0) {
            constraints.gridy++;
        }
    }

    private void handleAddStockItem() {
        String company = (String) companyComboBox.getSelectedItem();
        String productCode;
        String department = (String) departmentComboBox.getSelectedItem();
        JComboBox<Department> departmentComboBox = new JComboBox<>();

//        JComboBox<Department> department = new JComboBox<>();
        // Add departments to the ComboBox
        departmentComboBox.addItem(new Department("RUN", 1));
        departmentComboBox.addItem(new Department("SWM", 2));
        departmentComboBox.addItem(new Department("CYC", 3));

        // Access selected department and its ID
        Department selectedDepartment = (Department) departmentComboBox.getSelectedItem();
        int selectedDepartmentId = selectedDepartment.getId(); // Determine which class to use based on the selected company
        if ("ASC".equals(company)) {
            productCode = generateUniqueProductCode(department);
            ASCStockItem newStockItem = new ASCStockItem(
                    productCode, productTitleField.getText(), productDescriptionField.getText(),
                    Integer.parseInt(unitPricePoundsField.getText()), Integer.parseInt(unitPricePenceField.getText()),
                    Integer.parseInt(quantityInStockField.getText()), selectedDepartmentId
            );
            StockItemDAO.insertStockItem(newStockItem);
        } else if ("MSM".equals(company)) {
            productCode = generateUniqueMSMProductCode(department);
            ASCStockItem newStockItem = new ASCStockItem(
                    productCode, productTitleField.getText(), productDescriptionField.getText(),
                    Integer.parseInt(unitPricePoundsField.getText()), Integer.parseInt(unitPricePenceField.getText()),
                    Integer.parseInt(quantityInStockField.getText()), selectedDepartmentId
            );
            StockItemDAO.insertStockItem(newStockItem);
        }
    }

    private JLabel createCenteredLabel(String text) {
        JLabel label = new JLabel(text);
        label.setHorizontalAlignment(JLabel.CENTER);
        return label;
    }

    private JButton createCenteredButton(JButton button) {
        GridBagConstraints constraints = createConstraints();
        constraints.insets = new Insets(15, 5, 2, 5);  // Adjust the top inset value (10 in this example)
        button.setHorizontalAlignment(JButton.CENTER);
//        Color customColor = new Color(20, 181, 55);  // Custom color with RGB values
//        button.setBackground(customColor);
//        button.setForeground(Color.WHITE);
        return button;
    }

    private GridBagConstraints createConstraints() {
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.insets = new Insets(0, 5, 0, 5);  // Adjust the values as needed
        return constraints;
    }

    private boolean validateFields() {
        return isValidInteger(unitPricePoundsField.getText())
                && isValidInteger(unitPricePenceField.getText())
                && isValidInteger(quantityInStockField.getText())
                && !productTitleField.getText().trim().isEmpty()
                && !productDescriptionField.getText().trim().isEmpty();
    }

    private boolean isValidInteger(String text) {
        try {
            Integer.parseInt(text);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static String generateUniqueProductCode(String department) {
        int nextSequentialNumber = StockItemDAO.getNextSequentialNumber(department);
        return String.format("%s%06d", department, nextSequentialNumber);
    }

    public static String generateUniqueMSMProductCode(String department) {
        int nextSequentialNumber = StockItemDAO.getNextSequentialNumber(department);
        // Generate a 6-digit sequential number for MSM
        String sequentialNumber = String.format("%06d", nextSequentialNumber);

        // Generate the MSM product code in the "RUNXXXXXX-MSM" format
        return String.format("%s-%s-MSM", department, sequentialNumber);
    }

}
