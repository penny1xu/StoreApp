package code1.view;

import code1.Application;
import code1.model.ShippingAddress;
import code1.model.User;
import code1.util.AddressSelectionCallback;
import code1.dao.DataAdapter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class ShippingAddressView extends JFrame {
    private List<ShippingAddress> addressList = new ArrayList<>();
    private DefaultListModel<ShippingAddress> listModel = new DefaultListModel<>();
    private JList<ShippingAddress> addressJList = new JList<>(listModel);

    private AddressSelectionCallback addressSelectionCallback;

    private JButton btnSelect = new JButton("Select");
    private JButton btnEdit = new JButton("Edit");
    private JButton btnCreate = new JButton("Create");

    private DataAdapter dataAdapter = Application.getInstance().getDataAdapter();

    public ShippingAddressView(AddressSelectionCallback addressSelectionCallback) {
        this.addressSelectionCallback = addressSelectionCallback;
        setTitle("Address Book");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());

        buttonPanel.add(btnSelect);
        buttonPanel.add(btnEdit);
        buttonPanel.add(btnCreate);

        panel.add(new JScrollPane(addressJList), BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        add(panel);

        User user = Application.getInstance().getCurrentUser();
        addressList = dataAdapter.loadShippingAddress(user.getUserID());

        for(ShippingAddress address : addressList) {
            listModel.addElement(address);
        }

        btnSelect.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ShippingAddress selectedAddress = addressJList.getSelectedValue();
                if (selectedAddress != null) {
                    addressSelectionCallback.onAddressSelected(selectedAddress);
                    JOptionPane.showMessageDialog(ShippingAddressView.this, "Selected Address:\n" + selectedAddress);
                    setVisible(false);
                }
            }
        });

        btnEdit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ShippingAddress selectedAddress = addressJList.getSelectedValue();
                if (selectedAddress != null) {
                    EditAddressDialog editDialog = new EditAddressDialog(ShippingAddressView.this, selectedAddress, true);
                    editDialog.setVisible(true);

                    // After the dialog is closed, update the list and UI with the edited address
                    listModel.setElementAt(selectedAddress, addressJList.getSelectedIndex());
                }
            }
        });

        btnCreate.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                EditAddressDialog editDialog = new EditAddressDialog(ShippingAddressView.this, new ShippingAddress(), false);
                editDialog.setVisible(true);

                ShippingAddress newAddress = editDialog.getAddress();
                if (newAddress != null && isValidNewAddress(newAddress)) {
                    addressList.add(newAddress);
                    listModel.addElement(newAddress);
                }
            }
        });
    }

    private boolean isValidNewAddress(ShippingAddress address) {
        return address.getStreetAddress() != null
                && address.getState() != null
                && address.getCity() != null
                && address.getPostalCode() != null;
    }
}
