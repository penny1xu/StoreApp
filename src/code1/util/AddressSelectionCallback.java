package code1.util;

import code1.model.ShippingAddress;

public interface AddressSelectionCallback {
    void onAddressSelected(ShippingAddress selectedAddress);
}
