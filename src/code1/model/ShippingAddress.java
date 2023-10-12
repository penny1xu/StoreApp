package code1.model;

public class ShippingAddress {

    private int addressId;

    private int userId;

    private String streetAddress;

    private String city;

    private String state;

    private String postalCode;

    private String country;

    public ShippingAddress(){}

    public ShippingAddress(int addressId, int userId, String streetAddress, String city, String state, String postalCode, String country) {
        this.addressId = addressId;
        this.userId = userId;
        this.streetAddress = streetAddress;
        this.city = city;
        this.state = state;
        this.postalCode = postalCode;
        this.country = country;
    }

    public int getAddressId() {
        return addressId;
    }

    public void setAddressId(int addressId) {
        this.addressId = addressId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getStreetAddress() {
        return streetAddress;
    }

    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(this.streetAddress);
        stringBuilder.append(", ");
        stringBuilder.append(this.city);
        stringBuilder.append(", ");
        stringBuilder.append(this.state);
        stringBuilder.append(", ");
        stringBuilder.append(this.postalCode);
        stringBuilder.append(", ");
        stringBuilder.append(this.country);
        return stringBuilder.toString();
    }
}
