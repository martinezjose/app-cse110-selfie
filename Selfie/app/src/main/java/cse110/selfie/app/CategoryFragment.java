package cse110.selfie.app;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * Created by JuanJ on 5/1/2014.
 */
public class CategoryFragment extends ListFragment{

    final static int ARG_POSITION = -1;

    onItemSelectedListener mCallback;
    public interface onItemSelectedListener {
        public void onItemSelected(int itemId);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        /*INCLUDES DATABASE
        super.onCreate(savedInstanceState);
        String[] category = database.getCategory();
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1, category);
        setListAdapter(arrayAdapter);
         */
        super.onCreate(savedInstanceState);

        String [] category = getResources().getStringArray(R.array.cat_list);

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallback = (onItemSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() +" must implement");
        }
        mCallback = (onItemSelectedListener) activity;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        mCallback.onItemSelected(position);
        getListView().setItemChecked(position, true);
    }
}
