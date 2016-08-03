package br.unisinos.kickoffapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import br.unisinos.kickoffapp.R;
import br.unisinos.kickoffapp.models.Court;
import br.unisinos.kickoffapp.models.Schedule;

/**
 * Created by dennerevaldtmachado on 21/07/16.
 */
public class CourtsListAdapter extends BaseAdapter {
    private Context context;
    private List<Court> courtList;

    public CourtsListAdapter(Context context, List<Court> courtList) {
        this.context = context;
        this.courtList = courtList;
    }

    @Override
    public int getCount() {
        return courtList.size();
    }

    @Override
    public Object getItem(int position) {
        return courtList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.list_item_courts, null);

        if (convertView != null){
            TextView name = (TextView) convertView.findViewById(R.id.name);
            ImageView icon = (ImageView) convertView.findViewById(R.id.ivIconPic);

            Court court = courtList.get(position);
            name.setText(court.getName());

            if (court.getCategory().equals("Futebol society (7)")) {
                icon.setImageResource(R.drawable.icon_society);
            } else {
                icon.setImageResource(R.drawable.icon_futsal);
            }
        }

        return convertView;
    }
}
