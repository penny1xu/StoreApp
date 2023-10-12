package code1.view;

import code1.Application;
import code1.dao.DataAdapter;
import code1.model.CreditCard;
import code1.model.User;
import code1.util.CreditCardSelectionCallback;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class CreditCardView extends JFrame{

    private List<CreditCard> cardList = new ArrayList<>();
    private DefaultListModel<CreditCard> listModel = new DefaultListModel<>();
    private JList<CreditCard> cardJList = new JList<>(listModel);

    private CreditCardSelectionCallback creditCardSelectionCallback;

    private JButton btnSelect = new JButton("Select");
    private JButton btnEdit = new JButton("Edit");
    private JButton btnCreate = new JButton("Create");

    private DataAdapter dataAdapter = Application.getInstance().getDataAdapter();

    public CreditCardView(CreditCardSelectionCallback creditCardSelectionCallback) {
        this.creditCardSelectionCallback = creditCardSelectionCallback;
        setTitle("Credit Card Book");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());

        buttonPanel.add(btnSelect);
        buttonPanel.add(btnEdit);
        buttonPanel.add(btnCreate);

        panel.add(new JScrollPane(cardJList), BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        add(panel);

        User user = Application.getInstance().getCurrentUser();
        this.cardList = dataAdapter.loadCreditCards(user.getUserID());

        for(CreditCard card: cardList) {
            listModel.addElement(card);
        }

        btnSelect.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CreditCard card = cardJList.getSelectedValue();
                if(card != null) {
                    creditCardSelectionCallback.onCardSelected(card);
                    JOptionPane.showMessageDialog(CreditCardView.this, "Selected Card: "+card);
                    setVisible(false);
                }
            }
        });

        btnCreate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                EditCreditCardDialog editCreditCardDialog = new EditCreditCardDialog(CreditCardView.this, new CreditCard(), false);
                editCreditCardDialog.setVisible(true);

                CreditCard card =editCreditCardDialog.getCard();
                if(card != null) {
                    cardList.add(card);
                    listModel.addElement(card);
                }
            }
        });

        btnEdit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CreditCard card = cardJList.getSelectedValue();
                if (card != null && isValidCard(card)) {
                    EditCreditCardDialog editCreditCardDialog = new EditCreditCardDialog(CreditCardView.this, card, true);
                    editCreditCardDialog.setVisible(true);

                    listModel.setElementAt(card, cardJList.getSelectedIndex());
                }
            }
        });
    }

    private boolean isValidCard(CreditCard card) {
        return card.getCardNumber() != null
                && card.getCardHolderName() != null
                && card.getExpirationDate() != null;
    }
}
