package code1.view;

import code1.Application;
import code1.dao.DataAdapter;
import code1.model.*;
import code1.model.receipt.OrderContent;
import code1.model.receipt.OrderContentBuilder;
import code1.model.receipt.ReceiptContent;
import code1.util.AddressSelectionCallback;
import code1.util.CreditCardSelectionCallback;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class SummaryView extends JFrame{

    private ShippingAddress selectedAddress;
    private CreditCard selectedCreditCard;
    private Order order;

    private JLabel addressLabel = new JLabel("Selected Address:");
    private JLabel creditCardLabel = new JLabel("Selected Credit Card:");
    private JLabel totalAmountLabel = new JLabel();

    public SummaryView(Order order){
        this.order = order;
        totalAmountLabel.setText("Total Amount: $" + order.getTotalCost());
        setTitle("Order Summary");
        setSize(600, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new GridLayout(3, 1));
        infoPanel.add(totalAmountLabel);
        infoPanel.add(addressLabel);
        infoPanel.add(creditCardLabel);

        panel.add(infoPanel, BorderLayout.NORTH);

        JButton selectAddressButton = new JButton("Select Address");
        selectAddressButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ShippingAddressView addressView = new ShippingAddressView(new AddressSelectionCallback() {
                    public void onAddressSelected(ShippingAddress selectedAddress) {
                        // Handle the selected address from AddressBookApp here
                        SummaryView.this.selectedAddress = selectedAddress;
                        if (selectedAddress != null) {
                            addressLabel.setText("Selected Address: " + selectedAddress.toString());
                        }
                        // Update UI or perform other actions with the selected address
                        // For example, you can display it on the order summary page
                    }
            });
                addressView.setVisible(true);
        }});

        JButton selectCreditCardButton = new JButton("Select Credit Card");
        selectCreditCardButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                CreditCardView creditCardView = new CreditCardView(new CreditCardSelectionCallback() {
                    @Override
                    public void onCardSelected(CreditCard card) {
                        SummaryView.this.selectedCreditCard = card;
                        if (card!=null) {
                            creditCardLabel.setText("Selected Card: " + card.toString());
                        }
                    }
                });
                creditCardView.setVisible(true);
            }
        });

        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (selectedAddress == null) {
                    JOptionPane.showMessageDialog(SummaryView.this, "Please select an address.");
                } else if (selectedCreditCard == null) {
                    JOptionPane.showMessageDialog(SummaryView.this, "Please select a credit card.");
                } else {
                    // Implement logic to submit the order
                    // You can use selectedAddress, selectedCreditCard, and totalAmount in this logic
                    if (saveOrder()) {
                        ReceiptContent receiptContent = generateAndSaveReceipt();
                        JOptionPane.showMessageDialog(SummaryView.this, "Order Submitted!");
                        System.out.println(receiptContent);
                        setVisible(false);
                    }else {
                        JOptionPane.showMessageDialog(SummaryView.this, "Order Submitting Failed!");
                    }

                }
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());
        buttonPanel.add(selectAddressButton);
        buttonPanel.add(selectCreditCardButton);
        buttonPanel.add(submitButton);

        panel.add(buttonPanel, BorderLayout.CENTER);

        add(panel);
    }

    private boolean saveOrder() {
        DataAdapter dataAdapter = Application.getInstance().getDataAdapter();
        order.setDate(ZonedDateTime.now().format(DateTimeFormatter.RFC_1123_DATE_TIME));
        order.setAddressId(selectedAddress.getAddressId());
        order.setCardId(selectedCreditCard.getCardId());
        for (OrderLine orderLine : order.getLines()) {
            Product product = dataAdapter.loadProduct(orderLine.getProductID());
            product.setQuantity(product.getQuantity() - orderLine.getQuantity());
            dataAdapter.saveProduct(product);
        }
        return dataAdapter.saveOrder(order);
    }

    private ReceiptContent generateAndSaveReceipt() {
        //generate
        ReceiptContent receipt = new ReceiptContent();
        User user = Application.getInstance().getCurrentUser();
        receipt.setUserId(user.getUserID());
        receipt.setFullName(user.getFullName());
        receipt.setOrderId(order.getOrderID());
        receipt.setTotalCost(order.getTotalCost());
        receipt.setOrderDate(order.getDate());
        receipt.setShippingAddress(selectedAddress.toString());
        receipt.setPaymentCard(selectedCreditCard.toString());
        receipt.setOrderContents(getOrderContents());
        //save
        DataAdapter dataAdapter = Application.getInstance().getDataAdapter();
        dataAdapter.saveReceipt(receipt);
        return receipt;
    }

    private List<OrderContent> getOrderContents(){
        DataAdapter dataAdapter = Application.getInstance().getDataAdapter();
        List<OrderContent> results = new ArrayList<>();
        for(OrderLine orderLine: order.getLines()) {
            Product product = dataAdapter.loadProduct(orderLine.getProductID());
            results.add(new OrderContentBuilder()
                    .setProductName(product.getName())
                    .setUnitPrice(product.getPrice())
                    .setQuantity(orderLine.getQuantity())
                    .setTotalCost(orderLine.getCost())
                    .createOrderContent());
        }
        return results;
    }
}
