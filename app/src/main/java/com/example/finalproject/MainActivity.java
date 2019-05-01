package com.example.finalproject;

import android.app.AlertDialog;
import android.app.DownloadManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.content.Intent;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity {
    private Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MainActivity2.responseFromApi = new ArrayList<>();
        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView input = findViewById(R.id.typeHere);
                String inp = input.getText().toString();
                String[] inputs = inp.split(",");
                String str = "";
                for (int i = 0; i < inputs.length; i++) {
                    if (i != inputs.length - 1) {
                        String add = inputs[i] + "%2C";
                        str = str + add;
                    } else {
                        str = str + inputs[inputs.length - 1];
                    }
                }
                str = str.replaceAll("\\s", "%20");

// ...

// Request a string response from the provided URL.
                RequestQueue queue = Volley.newRequestQueue(getApplicationContext());



                String url = "https://www.food2fork.com/api/search?key=0288b79cdaed2a7d85918dbe6a376ba9&q=" + str;

                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                        (Request.Method.GET, url, (JSONObject)null, new Response.Listener<JSONObject>() {

                            @Override
                            public void onResponse(JSONObject response) {
                                String rawJson  = response.toString();
                                JsonParser parser = new JsonParser();

                                //root json
                                JsonObject root = parser.parse(rawJson).getAsJsonObject();

                                //count
                                //int count = root.get("count").getAsInt();
                                //recipes

                                JsonArray recipes = root.get("recipes").getAsJsonArray();

                                //add all titles
                                int count = 0;
                                for(JsonElement recipe : recipes){
                                    String title= recipe.getAsJsonObject().get("title").toString();
                                    String link = recipe.getAsJsonObject().get("f2f_url").getAsString();
                                    MainActivity2.responseFromApi.add(title);
                                    MainActivity2.responseFromApi.add(link);
                                    count++;
                                    if (count == 5) {
                                        break;
                                    }

                                }

                                Intent intent = new Intent(MainActivity.this, MainActivity2.class);
                                startActivity(intent);
                            }
                        }, new Response.ErrorListener() {

                            @Override
                            public void onErrorResponse(VolleyError error) {
                                // TODO: Handle error

                            }
                        });
                queue.add(jsonObjectRequest);



//                String rawJson = response.getBody().toString();
//
//                JsonParser parser = new JsonParser();
//                JsonArray result = parser.parse(rawJson).getAsJsonArray();
//                MainActivity2.responseFromApi.clear();
//                for(int i = 0; i < result.size();i++ ){
//                    JsonObject obj = result.get(i).getAsJsonObject();
//                    String title = obj.get("title").getAsString();
//                    MainActivity2.responseFromApi.add(title);
//                }
//
//                Intent intent = new Intent(MainActivity.this, MainActivity2.class);
//                startActivity(intent);
            }
        });
    }
}
