package br.unisinos.kickoffapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import br.unisinos.kickoffapp.R;
import br.unisinos.kickoffapp.models.Court;

/**
 * Created by dennerevaldtmachado on 21/07/16.
 */
public class CourtsListSpinnerAdapter extends BaseAdapter {
    private Context context;
    private List<Court> courtList;

    public CourtsListSpinnerAdapter(Context context, List<Court> courtList) {
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
        convertView = inflater.inflate(R.layout.spinner_list_court, null);

        if (convertView != null){
            TextView name = (TextView) convertView.findViewById(R.id.name);
            TextView category = (TextView) convertView.findViewById(R.id.category);

            Court court = courtList.get(position);
            name.setText(court.getName());
            category.setText(court.getCategory());
        }

        return convertView;
    }
}
