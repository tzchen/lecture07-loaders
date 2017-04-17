package edu.uw.loaderdemo;


import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.UserDictionary;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;

/**
 * A simple Fragment to display a list of words.
 */
public class WordListFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String TAG = "WordList";

    private SimpleCursorAdapter adapter;

    public WordListFragment() {
        // Required empty public constructor
    }

    //A factory method to create a new fragment with some arguments
    public static WordListFragment newInstance() {
        WordListFragment fragment = new WordListFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_word_list, container, false);

        //controller
        AdapterView listView = (AdapterView)rootView.findViewById(R.id.wordListView);

        adapter = new SimpleCursorAdapter(
                getActivity(),
                R.layout.list_item_layout,
                null,
                new String[] {UserDictionary.Words.WORD},
                new int[] {R.id.txtListItem},
                0);
        listView.setAdapter(adapter);

        //load the data
        getLoaderManager().initLoader(0, null, this);

        //handle button input
        final TextView inputText = (TextView)rootView.findViewById(R.id.txtAddWord);
        Button addButton = (Button)rootView.findViewById(R.id.btnAddWord);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentValues mNewValues = new ContentValues();
                mNewValues.put(UserDictionary.Words.APP_ID, "edu.uw.decorations");
                mNewValues.put(UserDictionary.Words.LOCALE, "en_US");
                mNewValues.put(UserDictionary.Words.WORD, inputText.getText().toString());
                mNewValues.put(UserDictionary.Words.FREQUENCY, "100");

                Uri mNewUri = getActivity().getContentResolver().insert(
                        UserDictionary.Words.CONTENT_URI,   // the user dictionary content URI
                        mNewValues                          // the values to insert
                );
                Log.v(TAG, "New word at: "+mNewUri);
            }
        });


        return rootView;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {UserDictionary.Words._ID, UserDictionary.Words.WORD};
        //create the CursorLoader
        CursorLoader loader = new CursorLoader(
                getActivity(),
                UserDictionary.Words.CONTENT_URI,
                projection,
                null,
                null,
                null
        );

        return loader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        //replace the data
        adapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        //empty the data
        adapter.swapCursor(null);
    }
}
