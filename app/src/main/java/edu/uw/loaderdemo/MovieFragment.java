package edu.uw.loaderdemo;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * A simple Fragment to show an (individual) searched for movie.
 */
public class MovieFragment extends Fragment {

    public static final String TAG = "MovieFragment";

    //Dynamic views
    private TextView movieText;
    private ImageView movieImage;

    public MovieFragment() {
        // Required empty public constructor
    }

    public static MovieFragment newInstance() {
        Bundle args = new Bundle();
        MovieFragment fragment = new MovieFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView =  inflater.inflate(R.layout.fragment_movie, container, false);

        movieText = (TextView)rootView.findViewById(R.id.txtMovie);
        movieImage = (ImageView)rootView.findViewById(R.id.imgMovie);

        final EditText searchText = (EditText)rootView.findViewById(R.id.txtMovieSearch);
        ImageButton searchButton = (ImageButton)rootView.findViewById(R.id.btnMovieSearch);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String query = searchText.getText().toString();
                fetchMovieData(query); //call helper method
            }
        });

        ImageButton clearButton = (ImageButton)rootView.findViewById(R.id.btnClearSearch);
        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //clear all the dynamic content
                searchText.setText("");
                movieText.setText("");
                movieImage.setImageDrawable(null);
            }
        });

        return rootView;
    }

    //Helper method to demonstrate Volley
    private void fetchMovieData(String query){
        Log.v(TAG, "Downloading data for "+query);

        String urlString = "";
        try {
            urlString = "http://www.omdbapi.com/?t=" + URLEncoder.encode(query, "UTF-8");

        }catch(UnsupportedEncodingException uee){
            Log.e(TAG, uee.toString());
            return; //break
        }

        //the request queue
        RequestQueue queue = VolleySingleton.getInstance(getActivity()).getRequestQueue();

        //build a requst
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, urlString, null,
            new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {
                    try {
                        String title = response.getString("Title");
                        String year = response.getString("Year");
                        movieText.setText(title+" ("+year+")");
                        fetchMoviePoster(response.getString("Poster"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                public void onErrorResponse(VolleyError error) { /* empty for now */ }
            }
        );

        //add the request to the queue to send the HTTP Request
        queue.add(jsonRequest);
    }

    //download the movie poster using an ImageLoader
    private void fetchMoviePoster(String url){
        //loader utilizes the requestQueue!
        ImageLoader loader = VolleySingleton.getInstance(getActivity()).getImageLoader();

        //get the image from the loader
        loader.get(url, ImageLoader.getImageListener(movieImage, 0, 0));
    }

}
