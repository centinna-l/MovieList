package com.example.movielist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RequestQueue requestQueue;
    private List<Movie> movieList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Setting the View to gone mode
        recyclerView.setVisibility(View.GONE);

//        requestQueue = VolleySingleton.getmInstance(this).getRequestQueue();
         requestQueue = Volley.newRequestQueue(this);

        movieList = new ArrayList<>();
        fetchMovies();
    }
    private void fetchMovies() {

        Log.d("FETCHING MOVIES", "INSIDE FETCHING MOVIES");

        String url = "https://mocki.io/v1/37cac5e4-af91-4c7c-90c3-85f412339b6e";
//        String url = "https://www.json-generator.com/api/json/get/cfsXpFGwwO?indent=2";
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        for (int i = 0 ; i < response.length() ; i ++){
                            try {
                                JSONObject jsonObject = response.getJSONObject(i);
                                String title = jsonObject.getString("title");
                                String overview = jsonObject.getString("overview");
                                String poster = jsonObject.getString("poster");
                                Double rating = jsonObject.getDouble("rating");
                                Log.d("FETCHING MOVIES", "INSIDE FETCHING MOVIES");

                                Movie movie = new Movie(title , poster , overview , rating);
                                movieList.add(movie);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            Log.d("FETCHING MOVIES", "INSIDE FETCHING MOVIES");

                            MovieAdapter adapter = new MovieAdapter(MainActivity.this , movieList);
                            recyclerView.setVisibility(View.VISIBLE);

                            recyclerView.setAdapter(adapter);
                            Log.d("FETCHING MOVIES", "INSIDE FETCHING MOVIES");

                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        requestQueue.add(jsonArrayRequest);
    }
}