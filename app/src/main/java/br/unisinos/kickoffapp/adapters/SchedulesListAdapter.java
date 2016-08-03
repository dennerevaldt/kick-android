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
import br.unisinos.kickoffapp.models.Schedule;

/**
 * Created by dennerevaldtmachado on 20/07/16.
 */
public class SchedulesListAdapter extends BaseAdapter {
    private Context context;
    private List<Schedule> scheduleList;

    public SchedulesListAdapter(Context context, List<Schedule> scheduleList) {
        this.context = context;
        this.scheduleList = scheduleList;
    }

    @Override
    public int getCount() {
        return scheduleList.size();
    }

    @Override
    public Object getItem(int position) {
        return scheduleList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.list_item_schedules, null);

        if (convertView != null){
            TextView date = (TextView) convertView.findViewById(R.id.date);
            TextView horary = (TextView) convertView.findViewById(R.id.horary);
            TextView nameCourt = (TextView) convertView.findViewById(R.id.nameCourt);
            ImageView icon = (ImageView) convertView.findViewById(R.id.ivIconPic);

            Schedule schedule = scheduleList.get(position);

            if (schedule!=null){
                date.setText(schedule.getDateFormat());
                horary.setText(schedule.getHoraryFormat());
                nameCourt.setText(schedule.getCourt().getName());

                if (schedule.getCourt().getCategory().equals("Futebol society (7)")) {
                    icon.setImageResource(R.drawable.icon_society);
                } else {
                    icon.setImageResource(R.drawable.icon_futsal);
                }
            }
        }

        return convertView;
    }
}
