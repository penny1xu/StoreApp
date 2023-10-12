package code1.view;

import code1.Application;
import code1.dao.DataAdapter;
import code1.model.ShippingAddress;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class EditAddressDialog extends JDialog {

    private JTextField streetAddressField = new JTextField(20);
    private JTextField cityField = new JTextField(20);
    private JTextField stateField = new JTextField(20);
    private JTextField postalCodeField = new JTextField(20);

    private JTextField countryField = new JTextField(20);

    private JButton saveButton = new JButton("Save");

    private ShippingAddress address;
    private boolean isEditMode;

    private DataAdapter dataAdapter = Application.getInstance().getDataAdapter();

    public EditAddressDialog(JFrame parent, ShippingAddress address, boolean isEditMode) {
        super(parent, isEditMode ? "Edit Address" : "Create Address", true);
        this.address = address;
        this.isEditMode = isEditMode;

        JPanel panel = new JPanel(new GridLayout(6, 2));

        panel.add(new JLabel("Street Address:"));
        panel.add(streetAddressField);

        panel.add(new JLabel("City:"));
        panel.add(cityField);

        panel.add(new JLabel("State:"));
        panel.add(stateField);

        panel.add(new JLabel("Postal Code:"));
        panel.add(postalCodeField);

        panel.add(new JLabel("Country"));
        panel.add(countryField);

        panel.add(new JLabel(""));
        panel.add(saveButton);

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        if (isEditMode) {
            streetAddressField.setText(address.getStreetAddress());
            cityField.setText(address.getCity());
            stateField.setText(address.getState());
            postalCodeField.setText(address.getPostalCode());
            countryField.setText(address.getCountry());
        }

        saveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String streetAddress = streetAddressField.getText();
                String city = cityField.getText();
                String state = stateField.getText();
                String postalCode = postalCodeField.getText();
                String country = countryField.getText();
                if (streetAddress.isEmpty() || city.isEmpty() || state.isEmpty() || postalCode.isEmpty() || country.isEmpty()) {
                    JOptionPane.showMessageDialog(EditAddressDialog.this, "Please fill in all required fields.");
                } else if(!validatePostalCode(postalCode)) {
                    JOptionPane.showMessageDialog(EditAddressDialog.this, "Invalid postal code!");
                }else {
                    address.setStreetAddress(streetAddressField.getText());
                    address.setCity(cityField.getText());
                    address.setState(stateField.getText());
                    address.setPostalCode(postalCodeField.getText());
                    address.setCountry(countryField.getText());
                    address.setUserId(Application.getInstance().getCurrentUser().getUserID());
                    dataAdapter.saveShippingAddress(address);

                    dispose();
                }
            }
        });

        add(panel);
        pack();
        setLocationRelativeTo(parent);
    }

    public ShippingAddress getAddress() {
        return address;
    }

    public boolean isEditMode() {
        return isEditMode;
    }

    private boolean validatePostalCode(String postalCode) {
        String regex = "^[0-9]{5}(?:-[0-9]{4})?$";
        return postalCode.matches(regex);
    }
}

