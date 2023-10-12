package code1.model;

import java.util.Date;

public class CreditCard {

    private int cardId;

    private int userId;

    private String cardNumber;

    private String cardHolderName;

    private String expirationDate;

    public CreditCard(){}
    public CreditCard(int cardId, int userId, String cardNumber, String cardHolderName, String expirationDate) {
        this.cardId = cardId;
        this.userId = userId;
        this.cardNumber = cardNumber;
        this.cardHolderName = cardHolderName;
        this.expirationDate = expirationDate;
    }

    public int getCardId() {
        return cardId;
    }

    public void setCardId(int cardId) {
        this.cardId = cardId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getCardHolderName() {
        return cardHolderName;
    }

    public void setCardHolderName(String cardHolderName) {
        this.cardHolderName = cardHolderName;
    }

    public String getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
    }

    public String toString() {
        return cardNumber.substring(cardNumber.length()-4);
    }
}
