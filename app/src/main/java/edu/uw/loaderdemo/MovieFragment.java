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
import com.android.volley.toolbox.Volley;

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

        //TODO: send request for data from url

        RequestQueue queue = VolleyRequestSingleton.getInstance(getActivity()).getRequestQueue();

        Request movieRequest = new JsonObjectRequest(Request.Method.GET, urlString, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.v(TAG, response.toString());
                            String posterUrl = response.getString("Poster");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        );

        queue.add(movieRequest);
    }

    public void fetchMoviePoster(String posterUrl) {
        // need queue
//        RequestQueue queue = VolleyRequestSingleton.getInstance(getActivity()).getRequestQueue();
        ImageLoader imgLoader = VolleyRequestSingleton.getInstance(getActivity()).getImageLoader();

        imgLoader.get(posterUrl, ImageLoader.getImageListener(movieImage, 0, 0));
        // need request
        // send request
    }
}
