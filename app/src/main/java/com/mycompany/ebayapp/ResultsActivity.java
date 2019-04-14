package com.mycompany.ebayapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
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

import java.util.ArrayList;

public class ResultsActivity extends AppCompatActivity implements CardAdapter.ItemListener {

    ProgressBar progressBar;
    TextView textView,nores,number;
    GridLayoutManager gridLayoutManager;
    RecyclerView recyclerView;
    String url_1,keyword;
    String node_url = "https://homework8-1554020046944.appspot.com/ebay-api1?";
    ArrayList<ItemData> list;
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);
        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Search Results");
        context = getBaseContext();
        progressBar = findViewById(R.id.wait);
        textView = findViewById(R.id.nikal);
        nores = findViewById(R.id.noresults);
        number = findViewById(R.id.number);
        nores.setVisibility(View.GONE);
        recyclerView = findViewById(R.id.recycler);
        gridLayoutManager = new GridLayoutManager(getBaseContext(),2);
        recyclerView.setLayoutManager(gridLayoutManager);
        Intent intent = getIntent();
        progressBar.setVisibility(View.VISIBLE);
        url_1 = intent.getStringExtra("URL");
        keyword = intent.getStringExtra("KEYWORD");
        System.out.println("Other: "+url_1);
        node_url += url_1;
        getData();
    }

    public void getData(){
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,node_url,null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try{
                            progressBar.setVisibility(View.GONE);
                            textView.setVisibility(View.GONE);
                            JSONArray a = response.getJSONArray("findItemsAdvancedResponse");
                            String count = "0", success = null;
                            JSONObject temp = null;
                            if(a.getJSONObject(0).has("ack"))
                                success = a.getJSONObject(0).getJSONArray("ack").getString(0);
                            if(a.getJSONObject(0).has("searchResult"))
                                temp = a.getJSONObject(0).getJSONArray("searchResult").getJSONObject(0);
                            count= temp.getString("@count");
                            if(success == null || !success.equals("Success") || Integer.parseInt(count)<1 || temp == null)
                                nores.setVisibility(View.VISIBLE);
                            else {
                                list = new ArrayList<>();
                                String setter = "Showing  <font color='#fd572f'>"+count+"</font>  results for  <font color='#fd572f'>"+keyword+"</font>";
                                number.setVisibility(View.VISIBLE);
                                number.setText(Html.fromHtml(setter),TextView.BufferType.SPANNABLE);
                                JSONArray items = temp.getJSONArray("item");
                                System.out.println(items.get(0));
                                for(int i = 0;i < items.length(); i++){
                                    JSONObject cur = (JSONObject) items.get(i);
                                    String id = (cur.has("itemId"))?cur.getJSONArray("itemId").getString(0):null;
                                    String image = (cur.has("galleryURL"))?cur.getJSONArray("galleryURL").getString(0):null;
                                    String title = (cur.has("title"))?cur.getJSONArray("title").getString(0):null;
                                    String postal_code = (cur.has("postalCode"))?cur.getJSONArray("postalCode").getString(0):null;
                                    String condition = null, cost = null;
                                    if(cur.has("condition") && cur.getJSONArray("condition").getJSONObject(0).has("conditionDisplayName"))
                                        condition = cur.getJSONArray("condition").getJSONObject(0).getJSONArray("conditionDisplayName").getString(0);
                                    JSONArray ship = (cur.has("shippingInfo"))?cur.getJSONArray("shippingInfo"):null;
                                    if(ship!=null && ship.getJSONObject(0).has("shippingServiceCost") && ship.getJSONObject(0).getJSONArray("shippingServiceCost").getJSONObject(0).has("__value__"))
                                        cost = ship.getJSONObject(0).getJSONArray("shippingServiceCost").getJSONObject(0).get("__value__").toString();
                                    JSONArray seller = (cur.has("sellingStatus"))?cur.getJSONArray("sellingStatus"):null;
                                    String returns = (cur.has("returnsAccepted"))?cur.getString("returnsAccepted"):null;
//                                    System.out.println(id + title + image + postal_code +"\n"+ ship + seller + returns);
//                                    System.out.println(cost);
                                    // Do something here
                                    ItemData cur_item = new ItemData(id,title,postal_code,cost,condition,seller,ship);
                                    list.add(cur_item);
                                }
                            }
                        }catch (JSONException e){
                            System.out.println("----------------------------");
                            e.printStackTrace();
                        }
                        CardAdapter cardAdapter = new CardAdapter(context,list);
                        recyclerView.setAdapter(cardAdapter);
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
    }

    @Override
    public void onItemClick(ItemData item) {
        Toast.makeText(getApplicationContext(), item.title + " is clicked", Toast.LENGTH_SHORT).show();

    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
