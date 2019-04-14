package com.mycompany.ebayapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
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

public class ResultsActivity extends AppCompatActivity {

    ProgressBar progressBar;
    TextView textView,nores;
    String url_1;
    String node_url = "https://homework8-1554020046944.appspot.com/ebay-api1?";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);
        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Search Results");
        progressBar = findViewById(R.id.wait);
        textView = findViewById(R.id.nikal);
        nores = findViewById(R.id.noresults);
        nores.setVisibility(View.GONE);
        Intent intent = getIntent();
        progressBar.setVisibility(View.VISIBLE);
        url_1 = intent.getStringExtra("URL");
        System.out.println("Other: "+url_1);
        node_url += url_1;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(View.GONE);
                textView.setVisibility(View.GONE);
                getData();
            }
        },2000);
    }

    public void getData(){
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        System.out.println(node_url);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,node_url,null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try{
                            //System.out.println(response);
                            JSONArray a = response.getJSONArray("findItemsAdvancedResponse");
                            String success = a.getJSONObject(0).getJSONArray("ack").getString(0);
                            JSONObject temp = a.getJSONObject(0).getJSONArray("searchResult").getJSONObject(0);
                            String count= temp.getString("@count");
                            if(!success.equals("Success") || Integer.parseInt(count)<1)
                                nores.setVisibility(View.VISIBLE);
                            else {
                                JSONArray items = temp.getJSONArray("item");
                                System.out.println(items.get(0));
                                for(int i = 0;i < items.length(); i++){
                                    JSONObject current_item = (JSONObject) items.get(i);
                                    String id = current_item.getString("itemId");
                                    String image = current_item.getString("galleryURL");
                                    String title = current_item.getString("title");
                                    String postal_code = current_item.getString("postalCode");
                                    JSONArray ship = current_item.getJSONArray("shippingInfo");
                                    JSONArray seller = current_item.getJSONArray("sellingStatus");
                                    String returns = current_item.getString("returnsAccepted");
                                    //System.out.println(id + title + image + postal_code +"\n"+ ship + seller + returns);
                                    // Do something here

                                }
                            }
                        }catch (JSONException e){
                            System.out.println("----------------------------");
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error){
                        Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
                        System.out.println("Error");
                        Log.e("ERROR","error occured ",error);
                    }
                });
        requestQueue.add(jsonObjectRequest);
        System.out.println("Hey");
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
