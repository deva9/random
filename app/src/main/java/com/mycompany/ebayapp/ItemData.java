package com.mycompany.ebayapp;

import org.json.JSONArray;

public class ItemData {
    public String id, title, zipcode, shippingType, conditionType;
    public JSONArray sellerInfo, shippingInfo;

    public ItemData(String id, String title, String zipcode, String shippingType, String conditionType,
                    JSONArray sellerInfo, JSONArray shippingInfo){
        this.id = id;
        this.title = title;
        this.zipcode = zipcode;
        this.shippingType = shippingType;
        this.conditionType = conditionType;
        this.sellerInfo = sellerInfo;
        this.shippingInfo = shippingInfo;
    }
}
