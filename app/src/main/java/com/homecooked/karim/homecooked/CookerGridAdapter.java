package com.homecooked.karim.homecooked;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.homecooked.karim.homecooked.model.Cooker;

/**
 * Created by karim on 11/05/2017.
 */

public class CookerGridAdapter extends BaseAdapter {

    private Context mContext;
    private List<Cooker> cookerItems; // original data
    private List<Cooker> cookerItemsSearched; // filtered data in search query

    public CookerGridAdapter(Context c, List<Cooker> cookerItems) {

        mContext = b;

        this.cookerItems = cookerItems;
        cookerItemsSearched = new ArrayList<Cooker>();
        cookerItemsSearched.addAll(cookerItems);
    }

    public CookerGridAdapter() {

    }

    @Override
    public int getCount() {

        return cookerItems.size();
    }

    @Override
    public Object getItem(int position) {

        return cookerItems.get(position);
    }

    @Override
    public long getItemId(int position) {

        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View gridview;


        if (convertView == null) {

            LayoutInflater inflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            //convertView = inflater.inflate(R.layout.grid_cooker_item, null);
            gridview = inflater.inflate(com.homecooked.karim.homecooked.R.layout.grid_cooker_item, null);

        } else {

            gridview = (View) convertView;
        }
        ImageView img = (ImageView) gridview.findViewById(com.homecooked.karim.homecooked.R.id.cooker_image);
        TextView name = (TextView) gridview.findViewById(com.homecooked.karim.homecooked.R.id.cooker_name_item);
        TextView address = (TextView) gridview.findViewById(com.homecooked.karim.homecooked.R.id.cooker_address_item);

        // getting movie data for the row
        Cooker cookerInfo = cookerItems.get(position);

        Glide.with(mContext).load(cookerInfo.getUrl().toString()).into(img);
        name.setText(cookerInfo.getName());
        address.setText(cookerInfo.getAddress());
        return gridview;
    }





    public void filter(String charText) {

        charText = charText.toLowerCase(Locale.getDefault());

        cookerItems.clear();
        if (charText.length() == 0) {
            cookerItems.addAll(cookerItemsSearched);
            notifyDataSetChanged();
        } else {
            for (Cooker Detail : cookerItemsSearched) {

                if (Detail.getName().toLowerCase(Locale.getDefault()).contains(charText) || Detail.getAddress().toLowerCase(Locale.getDefault()).contains(charText) ) {
                    cookerItems.add(Detail);
                    notifyDataSetChanged();
                }

            }
            notifyDataSetChanged();
        }
    }

// this is specific for grid to can catch the changes + in the MainActivity we use Adapter.add not List.add
    public void add(Cooker p) {
        cookerItems.add(p);
        cookerItemsSearched.add(p);
        notifyDataSetChanged();
    }




}

