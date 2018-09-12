package com.example.prateek.recycleview;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    RecyclerView view;
    EditText txtSearch;
     ArrayList<String> arrTitles = new ArrayList();
     ArrayList<String> arrDescr = new ArrayList();
     ArrayList<String> arrImgVv = new ArrayList();
    public int responseCode = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtSearch = findViewById(R.id.editSearch);
        txtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            @SuppressWarnings("deprecation")
            public void afterTextChanged(final Editable editable) {

                String text = txtSearch.getText().toString();

                text = text.replace(" ","+");

                RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
                final StringRequest request = new StringRequest(Request.Method.POST, "https://en.wikipedia.org/w/api.php?action=query&formatversion=2&generator=prefixsearch&gpssearch="+text+"&gpslimit=10&prop=pageimages%7Cpageterms&piprop=thumbnail&pithumbsize=50&pilimit=10&redirects=&wbptterms=description&format=json", new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                                JSONObject jsn = new JSONObject(response);
                                JSONObject js1 = jsn.getJSONObject("query");
                                JSONArray jsAr1 = js1.getJSONArray("pages");

                                arrTitles.clear();
                                arrDescr.clear();
                                arrImgVv.clear();
                                for(int i = 0; i<jsAr1.length(); i++){
                                    final JSONObject jsn1 = jsAr1.getJSONObject(i);
                                    String title = jsn1.get("title").toString();
                                    arrTitles.add(title);
                                    if(jsn1.has("thumbnail")){
                                        JSONObject jsn2 = jsn1.getJSONObject("thumbnail");
                                        String src = jsn2.get("source").toString();
                                        arrImgVv.add(src);
                                        //Thread.sleep(3000);
                                    }

                                    else{
                                        arrImgVv.add(null);
                                    }

                                    if(jsn1.has("terms")){
                                        JSONObject jsn3 = jsn1.getJSONObject("terms");
                                        JSONArray jsAr2 = jsn3.getJSONArray("description");
                                        ///System.out.println("JSONLEN = " + jsAr2.length());
                                        for(int j = 0; j<jsAr2.length(); j++){
                                            String desc = jsAr2.get(j).toString();
                                            //Log.i("Desc",desc);
                                            arrDescr.add(desc);
                                            ///Log.i("DescSize",arrDescr.size() + "");
                                        }
                                    }

                                    else{
                                        arrDescr.add(null);
                                    }
                                }
                        }

                        catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        return null;
                    }
                };

                queue.add(request);

                view = findViewById(R.id.rcVw);
                LinearLayoutManager manager = new LinearLayoutManager(MainActivity.this);
                view.setLayoutManager(manager);

                DisplayList list = new DisplayList(MainActivity.this,arrTitles,arrDescr,arrImgVv);
                view.setAdapter(list);
            }
        });
    }

//    @SuppressWarnings("deprecation")
//    public ImageRequest getReq(String url){
//
//        ImageRequest imageRequest = new ImageRequest(url, new Response.Listener<Bitmap>() {
//            @Override
//            public void onResponse(Bitmap response) {
//                Log.i("Bitmap",response.toString());
//                arrImgVv.add(response);
//                Log.i("ImageSize",arrImgVv.size() + "");
//            }
//        }, 0, 0, null, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//
//            }
//        });
//
//        return imageRequest;
//    }
}
