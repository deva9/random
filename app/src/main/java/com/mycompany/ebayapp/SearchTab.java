package com.mycompany.ebayapp;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class SearchTab extends Fragment implements View.OnClickListener {

    String url_1;
    Spinner spinner;
    AutoCompleteTextView zip;
    CheckBox newcheck, usedcheck, unspcheck, localcheck, freecheck, enable;
    EditText key, dist;
    TextView keyerr, ziperr;
    Button submit, clr;
    RadioButton rb1, rb2;
    LinearLayout idk;
    String distance = "10";
    boolean newc = false, usedc = false, unspec = false;
    boolean local = false, free = false, okay = true;
    String category[] = {"0","550","2984","267","11450","58058","26395","11233","1249"};
    List<String> categories = new ArrayList<>(Arrays.asList("All","Art","Baby","Books","Clothing,Shoes & Accessories",
            "Computers,Tablets & Networking","Health & Beauty","Music","Video games & Consoles"));
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_search_tab, container, false);
        init(view);
        rb1.setOnClickListener(this);
        rb2.setOnClickListener(this);
        submit.setOnClickListener(this);
        enable.setOnClickListener(this);
        clr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view1) {
                init(view);
            }
        });
        return view;
    }

    public void onClick(View v){
        switch (v.getId()){
            case R.id.radio1:
                rb2.setChecked(false);
                zip.setEnabled(false);
                break;
            case R.id.radio2:
                rb1.setChecked(false);
                zip.setEnabled(true);
                break;
            case R.id.enabler:
                idk.setVisibility(enable.isChecked()?v.VISIBLE:v.GONE);
                break;
            case R.id.searchbtn:
                okay = true;
                String text = spinner.getSelectedItem().toString();
                String val = category[categories.indexOf(text)];
                newc = newcheck.isChecked();
                usedc = usedcheck.isChecked();
                unspec = unspcheck.isChecked();
                local = localcheck.isChecked();
                free = freecheck.isChecked();
                String keyword = key.getText().toString().trim();
                String zipcode = "90007";
                if(keyword == null || keyword.length()==0){
                    okay = false;
                    keyerr.setVisibility(View.VISIBLE);
                }
                if(zip.isEnabled()){
                    zipcode = zip.getText().toString().trim();
                    if(zipcode==null || zipcode.length()!=5){
                        okay = false;
                        ziperr.setVisibility(View.GONE);
                    }
                }
                if(!okay)
                    Toast.makeText(this.getContext(), "Please fix all fields with errors", Toast.LENGTH_SHORT).show();
                else{
                    urlbana(keyword, val, zipcode);
//                    Toast.makeText(this.getContext(), url_1, Toast.LENGTH_SHORT).show();
                    System.out.println(url_1);
                }
                break;
        }
    }

    public void urlbana(String keyword, String val, String zipcode){
        int k = 0;
        url_1 += keyword;
        if(val!="0")
            url_1 += "&categoryId="+val;
        url_1 += "&buyerPostalCode="+zipcode;
        if(dist.getText().toString()!="")
            distance = dist.getText().toString();
        url_1 += "&itemFilter("+k+").name=MaxDistance&itemFilter("+(k++)+").value="+distance;
        if(free)
            url_1 += "&itemFilter("+k+").name=FreeShippingOnly&itemFilter("+(k++)+").value="+free;
        if(local)
            url_1 += "&itemFilter("+k+").name=LocalPickupOnly&itemFilter("+(k++)+").value="+local;
        url_1 += "&itemFilter("+k+").name=HideDuplicateItems&itemFilter("+(k++)+").value=true";
        int j = 0;
        if(newc){
            if(j==0)
                url_1 += "&itemFilter("+k+").name=Condition";
            url_1 += "&itemFilter("+k+").value("+(j++)+")=New";
        }
        if(usedc){
            if(j==0)
                url_1 += "&itemFilter("+k+").name=Condition";
            url_1 += "&itemFilter("+k+").value("+(j++)+")=Used";
        }
        if(unspec){
            if(j==0)
                url_1 += "&itemFilter("+k+").name=Condition";
            url_1 += "&itemFilter("+k+").value("+(j++)+")=Unspecified";
        }
    }

    public void init(View view){
        spinner = view.findViewById (R.id.myspinner);
        newcheck = view.findViewById(R.id.cond_new);
        usedcheck = view.findViewById(R.id.cond_used);
        unspcheck = view.findViewById(R.id.cond_unsp);
        localcheck = view.findViewById(R.id.ship_local);
        freecheck = view.findViewById(R.id.ship_free);
        enable = view.findViewById(R.id.enabler);
        idk = view.findViewById(R.id.depends);
        idk.setVisibility(view.GONE);
        newcheck.setChecked(false);usedcheck.setChecked(false);unspcheck.setChecked(false);
        freecheck.setChecked(false);localcheck.setChecked(false);enable.setChecked(false);

        key = view.findViewById(R.id.keyword);
        key.setText(null);
        dist = view.findViewById(R.id.distance);
        zip = view.findViewById(R.id.zipauto);
        rb1 = view.findViewById(R.id.radio1);
        rb2 = view.findViewById(R.id.radio2);
        zip.setEnabled(false);
        rb1.setChecked(true);
        rb2.setChecked(false);
        keyerr = view.findViewById(R.id.keyerror);
        keyerr.setVisibility(view.GONE);
        ziperr = view.findViewById(R.id.ziperror);
        ziperr.setVisibility(view.GONE);
        submit = view.findViewById(R.id.searchbtn);
        clr = view.findViewById(R.id.clearbtn);

        spinner.setSelection(0);
        ArrayAdapter<String> categoryAd = new ArrayAdapter<>(this.getContext(), android.R.layout.simple_spinner_item, categories);
        categoryAd.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(categoryAd);

        url_1 = "http://svcs.ebay.com/services/search/FindingService/v1?OPERATION-NAME=findItemsAdvanced&SERVICE-VERSION=1.0.0&SECURITY-APPNAME=DevanshG-PHPassig-PRD-116e2f5cf-92aba82e&RESPONSE-DATA-FORMAT=JSON&REST-PAYLOAD&paginationInput.entriesPerPage=50&keywords=";
    }
}
