package com.mycompany.ebayapp;

import org.json.JSONArray;

public class ItemData {
    public String id, title, zipcode, shippingType, conditionType, imageURL, price;
    public JSONArray sellerInfo, shippingInfo;

    public ItemData(String id, String img, String title, String p, String zipcode, String shippingType, String conditionType,
                    JSONArray sellerInfo, JSONArray shippingInfo){
        this.id = id;
        this.imageURL = img;
        this.title = title;
        this.price = p;
        this.zipcode = zipcode;
        this.shippingType = shippingType;
        this.conditionType = conditionType;
        this.sellerInfo = sellerInfo;
        this.shippingInfo = shippingInfo;
    }
}
