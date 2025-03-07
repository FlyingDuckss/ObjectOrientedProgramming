import javax.swing.*;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

public class SwinburneCarParkGUI {

    private JFrame frame;
    private JLabel label;
    private JTextField textField;
    private JButton nextButton;
    private int inputCount = 0;
    private int[] inputs = new int[2];
    private static ParkingSystem parkingSystem = new ParkingSystem();
    private JPanel rightPanel;
    private JPanel staffPanel;
    private JPanel visitorPanel;
    private Map<String, JButton> slotButtonMap;
    private JComboBox<String> filterDropdown;

    private Color staffVacantColor = new Color(173, 216, 230);
    private Color staffOccupiedColor = new Color(70, 130, 180);
    private Color visitorVacantColor = new Color(255, 255, 224);
    private Color visitorOccupiedColor = new Color(255, 165, 0);

    public SwinburneCarParkGUI() {
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        frame = new JFrame("Simple GUI Example");
        label = new JLabel("Enter Staff Parking Slots:");
        textField = new JTextField(20);
        nextButton = new JButton("Next");
        slotButtonMap = new HashMap<>();

        ((AbstractDocument) textField.getDocument()).setDocumentFilter(new NumberDocumentFilter());

        nextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (inputCount < 2) { 
                    inputs[inputCount] = Integer.parseInt(textField.getText());
                    int currentInput = inputCount;
                    inputCount++;

                    if (inputCount == 2) {
                        displayFullScreenUI(); 
                        frame.dispose(); 
                        populateSlots();
                    } else {
                        textField.setText("");
                        if (currentInput == 0) {
                            label.setText("Enter Visitor Parking Slots:");
                        }
                    }
                }
            }
        });

        frame.setLayout(new FlowLayout());
        frame.add(label);
        frame.add(textField);
        frame.add(nextButton);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 150);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void displayFullScreenUI() {
        JFrame fullScreenFrame = new JFrame("Swinburne Parking System");
        fullScreenFrame.setExtendedState(JFrame.MAXIMIZED_BOTH); 
        fullScreenFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        fullScreenFrame.setLayout(new BorderLayout());

        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new GridLayout(8, 1));

        String[] options = {
                "1. Add Slot",
                "2. Remove Slot",
                "3. List All Slots",
                "4. Park Auto",
                "5. Find Auto",
                "6. Remove Auto",
                "7. Exit"
        };

        JButton[] optionButtons = new JButton[options.length];
        for (int i = 0; i < options.length; i++) {
            optionButtons[i] = new JButton(options[i]);
            leftPanel.add(optionButtons[i]);
        }

        optionButtons[0].addActionListener(e -> addSlot());
        optionButtons[1].addActionListener(e -> removeSlot());
        optionButtons[3].addActionListener(e -> parkAuto());
        optionButtons[4].addActionListener(e -> findAuto());
        optionButtons[5].addActionListener(e -> removeAuto());
        optionButtons[6].addActionListener(e -> System.exit(0));

        String[] filterOptions = {"All Slots", "Vacant", "Occupied", "Staff", "Visitor"};
        filterDropdown = new JComboBox<>(filterOptions);
        filterDropdown.addActionListener(e -> filterSlots((String) filterDropdown.getSelectedItem()));

        leftPanel.add(new JLabel("Filter Slots:"));
        leftPanel.add(filterDropdown);

        rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));

        staffPanel = new JPanel(new GridLayout(0, 4, 10, 10));
        visitorPanel = new JPanel(new GridLayout(0, 4, 10, 10));

        JLabel staffLabel = new JLabel("Staff Parking Slots:");
        staffLabel.setFont(new Font("Arial", Font.BOLD, 22));  // Set font, style, and size

        JLabel visitorLabel = new JLabel("Visitor Parking Slots:");
        visitorLabel.setFont(new Font("Arial", Font.BOLD, 22));  // Set font, style, and size

        rightPanel.add(staffLabel);
        rightPanel.add(staffPanel);

        rightPanel.add(visitorLabel);
        rightPanel.add(visitorPanel);

        JScrollPane scrollPane = new JScrollPane(rightPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        fullScreenFrame.add(scrollPane, BorderLayout.CENTER);
        fullScreenFrame.add(leftPanel, BorderLayout.WEST);
        fullScreenFrame.setVisible(true);
    }

    private void populateSlots() {
    staffPanel.removeAll();
    visitorPanel.removeAll();
    
    for (int i = 1; i <= inputs[0]; i++) {
        final String slotId = "S" + String.format("%02d", i);
        boolean check = parkingSystem.addSlot(slotId, true);
        if (check) {
            JButton slotButton = createSlotButton(slotId, "Staff");
            staffPanel.add(slotButton);
            slotButtonMap.put(slotId, slotButton);
            updateSlotButtonColor(slotId);
        }
    }

    for (int i = 1; i <= inputs[1]; i++) {
        final String slotId = "V" + String.format("%02d", i);
        boolean check = parkingSystem.addSlot(slotId, false);
        if (check) {
            JButton slotButton = createSlotButton(slotId, "Visitor");
            visitorPanel.add(slotButton);
            slotButtonMap.put(slotId, slotButton);
            updateSlotButtonColor(slotId);
        }
    }

    rightPanel.revalidate();
    rightPanel.repaint();
    }

    private JButton createSlotButton(String slotId, String slotType) {
        JButton slotButton = new JButton(slotType + "\n" + slotId);
        slotButton.setPreferredSize(new Dimension(100, 100));
        slotButton.setOpaque(true);
        slotButton.setBorderPainted(false);

        Slot slot = parkingSystem.getSlot(slotId);
        String tooltip = "Slot ID: " + slotId + " (" + slotType + ")";
        tooltip += slot.getParkedAuto() == null ? " - Vacant" : " - Occupied";
        slotButton.setToolTipText(tooltip);

        updateSlotButtonColor(slotId);

        slotButton.addActionListener(e -> handleSlotClick(slotId));
        return slotButton;
    }

    private void updateSlotButtonColor(String slotId) {
        JButton slotButton = slotButtonMap.get(slotId);
        if (slotButton != null) {
            Slot slot = parkingSystem.getSlot(slotId);
            String slotType = slotId.startsWith("S") ? "Staff" : "Visitor";

            if (slot.getParkedAuto() == null) {
                if (slotType.equals("Staff")) {
                    slotButton.setBackground(staffVacantColor);
                } else {
                    slotButton.setBackground(visitorVacantColor);
                }
            } else {
                if (slotType.equals("Staff")) {
                    slotButton.setBackground(staffOccupiedColor);
                } else {
                    slotButton.setBackground(visitorOccupiedColor);
                }
            }
        }
        rightPanel.revalidate();
        rightPanel.repaint();
    }

    private void handleSlotClick(String slotId) {
        Slot slot = parkingSystem.getSlot(slotId);
        if (slot != null) {
            if (slot.getParkedAuto() == null) {
                parkAutoInSlot(slot);
            } else {
                displayAutoDetails(slot);
            }
        } else {
            JOptionPane.showMessageDialog(null, "Slot not found!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void parkAutoInSlot(Slot slot) {
        JTextField licenseField = new JTextField();
        JTextField ownerField = new JTextField();
        JCheckBox isStaffCheckbox = new JCheckBox("Staff Member");

        Object[] message = {
                "Enter License Plate:", licenseField,
                "Enter Owner's Name:", ownerField,
                "Is Staff Member:", isStaffCheckbox
        };

        int option = JOptionPane.showConfirmDialog(null, message, "Park Auto", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String licensePlate = licenseField.getText();
            String ownerName = ownerField.getText();
            if (!licensePlate.isEmpty() && !ownerName.isEmpty()) {
                Auto auto = new Auto(licensePlate, ownerName, isStaffCheckbox.isSelected());
                boolean parkSuccess = slot.parkAuto(auto);
                if (parkSuccess) {
                    updateSlotButtonColor(slot.getId());
                    JOptionPane.showMessageDialog(null, "Auto parked successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "Slot is already occupied!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(null, "License plate and owner's name cannot be empty!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void displayAutoDetails(Slot slot) {
        Auto parkedAuto = slot.getParkedAuto();
        if (parkedAuto != null) {
            String durationParked = parkedAuto.computeDurationParked();
            String details = "License Plate: " + parkedAuto.getLicensePlate() + "\n" +
                    "Owner Name: " + parkedAuto.getOwnerName() + "\n" +
                    "Duration Parked: " + durationParked + "\n" +
                    "Parked in Slot: " + slot.getId();
            JOptionPane.showMessageDialog(null, details, "Auto Details", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void addSlot() {
        String type = JOptionPane.showInputDialog("Enter Slot Type (Staff/Visitor):");
        if (type != null) {
            boolean isStaff = type.equalsIgnoreCase("Staff");
            String slotId = JOptionPane.showInputDialog("Enter Slot ID:");
            if (slotId != null) {
                boolean check = parkingSystem.addSlot(slotId, isStaff);
                if (check) {
                    JButton slotButton = createSlotButton(slotId, isStaff ? "Staff" : "Visitor");
                    if (isStaff) {
                        staffPanel.add(slotButton);
                    } else {
                        visitorPanel.add(slotButton);
                    }
                    slotButtonMap.put(slotId, slotButton);
                    rightPanel.revalidate();
                    rightPanel.repaint();
                } else {
                    JOptionPane.showMessageDialog(null, "Slot already exists!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    private void removeSlot() {
        String slotId = JOptionPane.showInputDialog("Enter Slot ID to remove:");
        if (slotId != null) {
            boolean check = parkingSystem.removeSlot(slotId);
            if (check) {
                JButton slotButton = slotButtonMap.remove(slotId);
                if (slotButton != null) {
                    if (slotId.startsWith("S")) {
                        staffPanel.remove(slotButton);
                    } else {
                        visitorPanel.remove(slotButton);
                    }
                    rightPanel.revalidate();
                    rightPanel.repaint();
                }
            } else {
                JOptionPane.showMessageDialog(null, "Slot not found!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void parkAuto() {
        String slotId = JOptionPane.showInputDialog("Enter Slot ID (e.g., S01, V01):");
        if (slotId != null) {
            Slot slot = parkingSystem.getSlot(slotId);
            if (slot != null && slot.getParkedAuto() == null) { 
                parkAutoInSlot(slot);
            } else {
                JOptionPane.showMessageDialog(null, "Slot is either occupied or does not exist!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void findAuto() {
        JTextField licenseField = new JTextField();
        Object[] message = {
                "Enter Vehicle Registration Number:", licenseField
        };

        int option = JOptionPane.showConfirmDialog(null, message, "Find Auto", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String licensePlate = licenseField.getText();
            Slot foundSlot = parkingSystem.findAutoByLicensePlate(licensePlate);
            if (foundSlot != null) {
                JOptionPane.showMessageDialog(null, "Auto is parked in Slot: " + foundSlot.getId(), "Find Auto", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "Auto not found!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void removeAuto() {
        String[] options = {"By Registration Number", "By Slot Number"};
        int choice = JOptionPane.showOptionDialog(null, "Remove Auto", "Choose Option",
                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);

        if (choice == 0) {
            JTextField licenseField = new JTextField();
            Object[] message = {
                    "Enter Vehicle Registration Number:", licenseField
            };

            int option = JOptionPane.showConfirmDialog(null, message, "Remove Auto by License Plate", JOptionPane.OK_CANCEL_OPTION);
            if (option == JOptionPane.OK_OPTION) {
                String licensePlate = licenseField.getText();
                Slot foundSlot = parkingSystem.findAutoByLicensePlate(licensePlate);
                if (foundSlot != null) {
                    double amountOwed = foundSlot.getParkedAuto().calculateCost();
                    int confirmation = JOptionPane.showConfirmDialog(null,
                            "Total Amount Owed: $" + amountOwed + "\n\nDo you want to release the auto?", "Confirmation",
                            JOptionPane.YES_NO_OPTION);
                    if (confirmation == JOptionPane.YES_OPTION) {
                        foundSlot.removeAuto();
                        updateSlotButtonColor(foundSlot.getId());
                        JOptionPane.showMessageDialog(null, "Auto removed successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Auto not found!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else if (choice == 1) {
            String slotNumber = JOptionPane.showInputDialog("Enter Slot Number (e.g., S01, V01):");
            if (slotNumber != null) {
                Slot foundSlot = parkingSystem.getSlot(slotNumber);
                if (foundSlot != null && foundSlot.getParkedAuto() != null) {
                    double amountOwed = foundSlot.getParkedAuto().calculateCost();
                    int confirmation = JOptionPane.showConfirmDialog(null,
                            "Total Amount Owed: $" + amountOwed + "\n\nDo you want to release the auto?", "Confirmation",
                            JOptionPane.YES_NO_OPTION);
                    if (confirmation == JOptionPane.YES_OPTION) {
                        foundSlot.removeAuto();
                        updateSlotButtonColor(foundSlot.getId());
                        JOptionPane.showMessageDialog(null, "Auto removed successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Slot is empty or does not exist!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    private void filterSlots(String filter) {
        staffPanel.removeAll();
        visitorPanel.removeAll();
        for (String slotId : slotButtonMap.keySet()) {
            Slot slot = parkingSystem.getSlot(slotId);
            JButton slotButton = slotButtonMap.get(slotId);
            boolean shouldShow = true;

            switch (filter) {
                case "Vacant":
                    shouldShow = (slot.getParkedAuto() == null);
                    break;
                case "Occupied":
                    shouldShow = (slot.getParkedAuto() != null);
                    break;
                case "Staff":
                    shouldShow = slotId.startsWith("S");
                    break;
                case "Visitor":
                    shouldShow = slotId.startsWith("V");
                    break;
            }

            if (shouldShow) {
                if (slotId.startsWith("S")) {
                    staffPanel.add(slotButton);
                } else if (slotId.startsWith("V")) {
                    visitorPanel.add(slotButton);
                }
            }
        }
        rightPanel.revalidate();
        rightPanel.repaint();
    }

    public static void main(String[] args) {
        new SwinburneCarParkGUI();
    }
}

class NumberDocumentFilter extends DocumentFilter {
    public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
        if (string != null && string.matches("\\d*")) {
            super.insertString(fb, offset, string, attr);
        }
    }

    public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
        if (text != null && text.matches("\\d*")) {
            super.replace(fb, offset, length, text, attrs);
        }
    }

    public void remove(FilterBypass fb, int offset, int length) throws BadLocationException {
        super.remove(fb, offset, length);
    }
}

