package code1.view;

import code1.Application;
import code1.dao.DataAdapter;
import code1.model.CreditCard;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class EditCreditCardDialog extends JDialog {
    private JTextField cardNumberField = new JTextField(20);
    private JTextField cardHolderNameField = new JTextField(20);
    private JTextField expirationDateField = new JTextField(20);

    private JButton saveButton = new JButton("Save");

    private CreditCard card;
    private boolean isEditMode;

    private DataAdapter dataAdapter = Application.getInstance().getDataAdapter();

    public EditCreditCardDialog(JFrame parent, CreditCard card, boolean isEditMode) {
        super(parent, isEditMode ? "Edit Credit Card" : "Create Credit Card", true);
        this.card = card;
        this.isEditMode = isEditMode;
        JPanel panel = new JPanel(new GridLayout(4, 2));

        panel.add(new JLabel("Credit Card Number:"));
        panel.add(cardNumberField);

        panel.add(new JLabel("Card Holder Name:"));
        panel.add(cardHolderNameField);

        panel.add(new JLabel("Expiration Date:"));
        panel.add(expirationDateField);

        panel.add(new JLabel(""));
        panel.add(saveButton);

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        if(isEditMode) {
            cardNumberField.setText(card.getCardNumber());
            cardHolderNameField.setText(card.getCardHolderName());
            expirationDateField.setText(card.getExpirationDate());
        }

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String cardNumber = cardNumberField.getText();
                String cardHolderName = cardHolderNameField.getText();
                String expirationDate = expirationDateField.getText();
                if( cardNumber.isEmpty() || cardHolderName.isEmpty() || expirationDate.isEmpty()) {
                    JOptionPane.showMessageDialog(EditCreditCardDialog.this, "Please fill in all required fields.");
                }else if (!isValidCardExpiryDate(expirationDate)) {
                    JOptionPane.showMessageDialog(EditCreditCardDialog.this, "Invalid Expiration Date!");
                }else if (!isValidCreditCardNumber(cardNumber)) {
                    JOptionPane.showMessageDialog(EditCreditCardDialog.this, "Invalid Card Number!");
                }else {
                    card.setCardNumber(cardNumber);
                    card.setCardHolderName(cardHolderName);
                    card.setExpirationDate(expirationDate);
                    card.setUserId(Application.getInstance().getCurrentUser().getUserID());
                    dataAdapter.saveCreditCard(card);
                    dispose();
                }
            }
        });

        add(panel);
        pack();
        setLocationRelativeTo(parent);
    }

    public CreditCard getCard() {
        return card;
    }
    private boolean isValidCardExpiryDate(String expiryDate) {
        return expiryDate.matches("(?:0[1-9]|1[0-2])/[0-9]{2}");
    }

    public static boolean isValidCreditCardNumber(String creditCardNumber) {
        String cleanedNumber = creditCardNumber.replaceAll("\\s", "");

        if (!cleanedNumber.matches("\\d+")) {
            return false;
        }

        int length = cleanedNumber.length();
        return (length >= 13 && length <= 19);
    }
}
