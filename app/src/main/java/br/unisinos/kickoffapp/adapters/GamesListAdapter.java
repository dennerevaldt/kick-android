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
import br.unisinos.kickoffapp.models.Game;

/**
 * Created by dennerevaldtmachado on 28/07/16.
 */
public class GamesListAdapter extends BaseAdapter {
    private List<Game> gameList;
    private Context context;

    public GamesListAdapter(Context context, List<Game> gameList) {
        this.context = context;
        this.gameList = gameList;
    }

    @Override
    public int getCount() {
        return gameList.size();
    }

    @Override
    public Object getItem(int position) {
        return gameList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.list_item_games, null);

        if (convertView != null){
            TextView name = (TextView) convertView.findViewById(R.id.tvName);
            TextView date = (TextView) convertView.findViewById(R.id.tvDate);
            TextView horary = (TextView) convertView.findViewById(R.id.tvHorary);
            ImageView icon = (ImageView) convertView.findViewById(R.id.ivIconPic);

            Game game = gameList.get(position);
            name.setText(game.getName());
            date.setText(game.getSchedule().getDateFormat());
            horary.setText(game.getSchedule().getHoraryFormat());

            if (game.getCourt() != null) {
                if (game.getCourt().getCategory().equals("Futebol society (7)")) {
                    icon.setImageResource(R.drawable.icon_society);
                } else if (game.getCourt().getCategory().equals("Futebol de salão (Futsal)")) {
                    icon.setImageResource(R.drawable.icon_futsal);
                }
            }
        }

        return convertView;
    }
}
