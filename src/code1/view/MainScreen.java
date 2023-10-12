package code1.view;

import code1.Application;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainScreen extends JFrame {

    private JButton btnBuy = new JButton("Order View");
    private JButton btnSell = new JButton("Product View");

    private JPanel userInfo = new JPanel();

    public MainScreen() {
        this.setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(400, 300);

        btnSell.setPreferredSize(new Dimension(120, 50));
        btnBuy.setPreferredSize(new Dimension(120, 50));


        JLabel title = new JLabel("Store Management System");
        title.setFont(new Font("Sans Serif", Font.BOLD, 24));
        JPanel panelTitle = new JPanel();
        panelTitle.add(title);
        this.getContentPane().add(panelTitle);

        JPanel panelButton = new JPanel();
        panelButton.add(btnBuy);
        panelButton.add(btnSell);


        this.getContentPane().add(panelButton);

        btnBuy.addActionListener(new ActionListener() { // when controller is simple, we can declare it on the fly
            public void actionPerformed(ActionEvent e) {
                Application.getInstance().getOrderView().setVisible(true);            }
        });


        btnSell.addActionListener(new ActionListener() { // when controller is simple, we can declare it on the fly
            public void actionPerformed(ActionEvent e) {
                Application.getInstance().getProductView().setVisible(true);
            }
        });
    }

    public void loadUserInfo(){
        System.out.println("Loading user info");
        String userId = "User ID: "+ Application.getInstance().getCurrentUser().getUserID();
        String userName = "Username: "+ Application.getInstance().getCurrentUser().getUsername();
        String userFullName = "Full Name: "+ Application.getInstance().getCurrentUser().getFullName();
        System.out.println(userId);
        System.out.println(userName);
        System.out.println(userFullName);
        JLabel idLabel= new JLabel(userId);
        JLabel nameLabel = new JLabel(userName);
        JLabel fullNameLabel = new JLabel(userFullName);
        this.userInfo.add(idLabel);
        this.userInfo.add(nameLabel);
        this.userInfo.add(fullNameLabel);
        this.getContentPane().add(this.userInfo);
    }


}
