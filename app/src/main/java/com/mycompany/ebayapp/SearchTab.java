package com.mycompany.ebayapp;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
    AutosuggestAdapter autosuggestAdapter;
    LinearLayout idk;
    Handler handler;
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

        zip.setThreshold(1);
        zip.setAdapter(autosuggestAdapter);
        autosuggestAdapter = new AutosuggestAdapter(this.getContext(), android.R.layout.simple_dropdown_item_1line);
        zip.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                autosuggestAdapter.getObject(position);
            }
        });
        zip.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                handler.removeMessages(100);
                handler.sendEmptyMessageDelayed(100, 200);
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });

        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                if (msg.what == 100) {
                    if (!TextUtils.isEmpty(zip.getText())) {
                        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
                        String zip_url = "http://api.geonames.org/postalCodeSearchJSON?postalcode_startsWith="
                                + zip.getText().toString() + "&username=devanshgada&country=US&maxRows=5";
                        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                                (Request.Method.GET, zip_url, null, new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                List<String> stringList = new ArrayList<>();
                                try{
                                    if(response.has("postalCodes")){
                                        JSONArray suggestions = response.getJSONArray("postalCodes");
                                        for(int i = 0; i < suggestions.length(); i++)
                                            stringList.add(suggestions.getJSONObject(i).getString("postalCode"));
                                        System.out.println(stringList);
                                    }
                                }
                                catch (JSONException e){
                                    e.printStackTrace();
                                }
                                autosuggestAdapter.setData(stringList);
                                autosuggestAdapter.notifyDataSetChanged();
                            }
                        },new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) { }
                                });
                        requestQueue.add(jsonObjectRequest);
                    }
                }
                return false;
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
                    urlBana(keyword, val, zipcode);
                    System.out.println(url_1);
                    Intent intent = new Intent(this.getContext(),ResultsActivity.class);
                    intent.putExtra("URL",url_1);
                    intent.putExtra("KEYWORD",keyword);
                    startActivity(intent);
                }
                break;
        }
    }

    public void urlBana(String keyword, String val, String zipcode){
        url_1 = "http://svcs.ebay.com/services/search/FindingService/v1?OPERATION-NAME=findItemsAdvanced&SERVICE-VERSION=1.0.0&SECURITY-APPNAME=DevanshG-PHPassig-PRD-116e2f5cf-92aba82e&RESPONSE-DATA-FORMAT=JSON&REST-PAYLOAD&paginationInput.entriesPerPage=50&keywords=";
        int k = 0;
        url_1 += keyword;
        if(val!="0")
            url_1 += "&categoryId="+val;
        url_1 += "&buyerPostalCode="+zipcode;
        if(!dist.getText().toString().equals("") && dist.getText().toString().trim().length()>0)
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
    }
}
