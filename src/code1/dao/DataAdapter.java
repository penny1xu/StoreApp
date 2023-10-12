package code1.dao;

import code1.model.*;
import code1.model.receipt.ReceiptContent;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DataAdapter {
    private Connection connection;

    public DataAdapter(Connection connection) {
        this.connection = connection;
    }

    public Product loadProduct(int id) {
        try {
            String query = "SELECT * FROM Products WHERE ProductID = " + id;

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            if (resultSet.next()) {
                Product product = new Product();
                product.setProductID(resultSet.getInt(1));
                product.setName(resultSet.getString(2));
                product.setPrice(resultSet.getDouble(3));
                product.setQuantity(resultSet.getDouble(4));
                resultSet.close();
                statement.close();

                return product;
            }

        } catch (SQLException e) {
            System.out.println("Database access error!");
            e.printStackTrace();
        }
        return null;
    }

    public boolean saveProduct(Product product) {
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM Products WHERE ProductID = ?");
            statement.setInt(1, product.getProductID());

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) { // this product exists, update its fields
                statement = connection.prepareStatement("UPDATE Products SET Name = ?, Price = ?, Quantity = ? WHERE ProductID = ?");
                statement.setString(1, product.getName());
                statement.setDouble(2, product.getPrice());
                statement.setDouble(3, product.getQuantity());
                statement.setInt(4, product.getProductID());
            } else { // this product does not exist, use insert into
                statement = connection.prepareStatement("INSERT INTO Products VALUES (?, ?, ?, ?,?)");
                statement.setString(2, product.getName());
                statement.setDouble(3, product.getPrice());
                statement.setDouble(4, product.getQuantity());
                statement.setInt(1, product.getProductID());
            }
            statement.execute();
            resultSet.close();
            statement.close();
            return true;        // save successfully

        } catch (SQLException e) {
            System.out.println("Database access error!");
            e.printStackTrace();
            return false; // cannot save!
        }
    }

    public Order loadOrder(int id) {
        try {
            Order order = null;
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM Orders WHERE OrderID = " + id);

            if (resultSet.next()) {
                order = new Order();
                order.setOrderID(resultSet.getInt("OrderID"));
                order.setBuyerID(resultSet.getInt("CustomerID"));
                order.setTotalCost(resultSet.getDouble("TotalCost"));
                order.setDate(resultSet.getString("OrderDate"));
                resultSet.close();
                statement.close();
            }

            // loading the order lines for this order
            resultSet = statement.executeQuery("SELECT * FROM OrderLine WHERE OrderID = " + id);

            while (resultSet.next()) {
                OrderLine line = new OrderLine();
                line.setOrderID(resultSet.getInt(1));
                line.setProductID(resultSet.getInt(2));
                line.setQuantity(resultSet.getDouble(3));
                line.setCost(resultSet.getDouble(4));
                order.addLine(line);
            }

            return order;

        } catch (SQLException e) {
            System.out.println("Database access error!");
            e.printStackTrace();
            return null;
        }
    }

    public boolean saveOrder(Order order) {
        try {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO Orders VALUES (?, ?, ?, ?, ?, ?, ?)");
            statement.setInt(1, order.getOrderID());
            statement.setInt(3, order.getBuyerID());
            statement.setString(2, order.getDate());
            statement.setDouble(4, order.getTotalCost());
            statement.setDouble(5, order.getTotalTax());
            statement.setInt(6, order.getAddressId());
            statement.setInt(7, order.getCardId());

            statement.execute();    // commit to the database;
            statement.close();

            statement = connection.prepareStatement("INSERT INTO OrderLine VALUES (?, ?, ?, ?)");

            for (OrderLine line : order.getLines()) { // store for each order line!
                statement.setInt(1, line.getOrderID());
                statement.setInt(2, line.getProductID());
                statement.setDouble(3, line.getQuantity());
                statement.setDouble(4, line.getCost());

                statement.execute();    // commit to the database;
            }
            statement.close();
            return true; // save successfully!
        } catch (SQLException e) {
            System.out.println("Database access error!");
            e.printStackTrace();
            return false;
        }
    }

    public User loadUser(String username, String password) {
        try {

            PreparedStatement statement = connection.prepareStatement("SELECT * FROM Users WHERE UserName = ? AND Password = ?");
            statement.setString(1, username);
            statement.setString(2, password);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                User user = new User();
                user.setUserID(resultSet.getInt("UserID"));
                user.setUsername(resultSet.getString("UserName"));
                user.setPassword(resultSet.getString("Password"));
                user.setFullName(resultSet.getString("DisplayName"));
                resultSet.close();
                statement.close();

                return user;
            }

        } catch (SQLException e) {
            System.out.println("Database access error!");
            e.printStackTrace();
        }
        return null;
    }

    public List<ShippingAddress> loadShippingAddress(int userId) {
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM ShippingAddress WHERE UserID = ?");
            statement.setInt(1, userId);
            ResultSet resultSet = statement.executeQuery();
            List<ShippingAddress> result = new ArrayList<>();
            while (resultSet.next()) {
                ShippingAddress shippingAddress = new ShippingAddress();
                shippingAddress.setAddressId(resultSet.getInt(1));
                shippingAddress.setUserId(userId);
                shippingAddress.setStreetAddress(resultSet.getString(3));
                shippingAddress.setCity(resultSet.getString(4));
                shippingAddress.setState(resultSet.getString(5));
                shippingAddress.setPostalCode(resultSet.getString(6));
                shippingAddress.setCountry(resultSet.getString(7));
                result.add(shippingAddress);
            }
            return result;
        } catch (SQLException e) {
            System.out.println("Database access error!");
            e.printStackTrace();
            return null;
        }
    }

    public boolean saveShippingAddress(ShippingAddress shippingAddress) {
        try {
            PreparedStatement statement;
            if (shippingAddress.getAddressId() == 0) {
                statement = connection.prepareStatement("INSERT INTO ShippingAddress VALUES (?,?,?,?,?,?,?)");
                statement.setNull(1, Types.INTEGER);
                statement.setInt(2, shippingAddress.getUserId());
                statement.setString(3, shippingAddress.getStreetAddress());
                statement.setString(4, shippingAddress.getCity());
                statement.setString(5, shippingAddress.getState());
                statement.setString(6, shippingAddress.getPostalCode());
                statement.setString(7, shippingAddress.getCountry());

            } else {
                statement = connection.prepareStatement("UPDATE ShippingAddress SET StreetAddress = ?, City = ?, State = ?, PostalCode = ?, Country = ? WHERE AddressID=?");
                statement.setString(1, shippingAddress.getStreetAddress());
                statement.setString(2, shippingAddress.getCity());
                statement.setString(3, shippingAddress.getState());
                statement.setString(4, shippingAddress.getPostalCode());
                statement.setString(5, shippingAddress.getCountry());
                statement.setInt(6, shippingAddress.getAddressId());
            }
            statement.execute();
            statement.close();

            return true;
        } catch (SQLException e) {
            System.out.println("Database access error!");
            e.printStackTrace();
            return false; // cannot save!
        }

    }

    public List<CreditCard> loadCreditCards(int userId) {
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM CreditCard WHERE UserID = ?");
            statement.setInt(1, userId);
            ResultSet resultSet = statement.executeQuery();
            List<CreditCard> result = new ArrayList<>();
            while (resultSet.next()) {
                CreditCard creditCard = new CreditCard();
                creditCard.setCardId(resultSet.getInt(1));
                creditCard.setUserId(userId);
                creditCard.setCardNumber(resultSet.getString(3));
                creditCard.setCardHolderName(resultSet.getString(4));
                creditCard.setExpirationDate(resultSet.getString(5));
                result.add(creditCard);
            }
            return result;
        } catch (SQLException e) {
            System.out.println("Database access error!");
            e.printStackTrace();
            return null;
        }
    }

    public boolean saveCreditCard(CreditCard card) {
        try {
            PreparedStatement statement;
            if (card.getCardId() == 0) {
                statement = connection.prepareStatement("INSERT INTO CreditCard VALUES (?,?,?,?,?)");
                statement.setNull(1, Types.INTEGER);
                statement.setInt(2, card.getUserId());
                statement.setString(3, card.getCardNumber());
                statement.setString(4, card.getCardHolderName());
                statement.setString(5, card.getExpirationDate());

            } else {
                statement = connection.prepareStatement("UPDATE CreditCard SET CardNumber = ?, CardHolderName = ?, ExpirationDate = ? WHERE CardID=?");
                statement.setString(1, card.getCardNumber());
                statement.setString(2, card.getCardHolderName());
                statement.setString(3, card.getExpirationDate());
                statement.setInt(4, card.getCardId());
            }
            statement.execute();
            statement.close();

            return true;
        } catch (SQLException e) {
            System.out.println("Database access error!");
            e.printStackTrace();
            return false; // cannot save!
        }
    }

    public int getOrderId() {
        try{
            PreparedStatement statement = connection.prepareStatement("SELECT Max(OrderID) from Orders");
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1)+1;
            }else {
                return 1;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean saveReceipt(ReceiptContent receiptContent) {
        try {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO Receipt VALUES (?,?,?,?)");
            statement.setNull(1, Types.INTEGER);
            statement.setInt(2, receiptContent.getUserId());
            statement.setInt(3, receiptContent.getOrderId());
            statement.setString(4, receiptContent.toString());

            statement.execute();
            statement.close();

            return true;
        } catch (SQLException e) {
            System.out.println("Database access error!");
            e.printStackTrace();
            return false; // cannot save!
        }
    }
}
