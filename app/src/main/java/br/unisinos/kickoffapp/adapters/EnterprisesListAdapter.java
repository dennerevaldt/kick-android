package br.unisinos.kickoffapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import br.unisinos.kickoffapp.R;
import br.unisinos.kickoffapp.models.Enterprise;

/**
 * Created by dennerevaldtmachado on 30/07/16.
 */
public class EnterprisesListAdapter extends BaseAdapter {
    private Context context;
    private List<Enterprise> enterpriseList;

    public EnterprisesListAdapter(Context context, List<Enterprise> enterpriseList) {
        this.context = context;
        this.enterpriseList = enterpriseList;
    }

    @Override
    public int getCount() {
        return enterpriseList.size();
    }

    @Override
    public Object getItem(int position) {
        return enterpriseList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.list_item_enterprises, null);

        if (convertView != null){
            TextView name = (TextView) convertView.findViewById(R.id.name);
            TextView address = (TextView) convertView.findViewById(R.id.address);

            Enterprise enterprise = enterpriseList.get(position);
            name.setText(enterprise.getFullName());
            address.setText(enterprise.getDistrict());
        }

        return convertView;
    }
}
