package br.unisinos.kickoffapp.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import br.unisinos.kickoffapp.R;
import br.unisinos.kickoffapp.activities.CreateGameActivity;
import br.unisinos.kickoffapp.adapters.GamesListAdapter;
import br.unisinos.kickoffapp.asynk.gameTask.GetListGamesTask;
import br.unisinos.kickoffapp.models.Game;

public class PlayerGameFragment extends Fragment {
    private ListView listViewGames;
    private GamesListAdapter gameListAdapter;
    private SwipeRefreshLayout swipeContainer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_player_game, container, false);

        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getDataList();
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(R.color.colorPrimary, R.color.colorPrimaryDark, R.color.colorAccent);

        final List<Game> gameList = new ArrayList<>();
        gameListAdapter = new GamesListAdapter(getContext(), gameList);
        listViewGames = (ListView) view.findViewById(R.id.listGames);
        listViewGames.setAdapter(gameListAdapter);

        listViewGames.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Intent intent = new Intent(getActivity(), EditCourtActivity.class);
//                Court court = (Court) courtListAdapter.getItem(position);
//                intent.putExtra("Court", court);
//                startActivityForResult(intent, 1);
            }
        });

        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        if (fab != null){
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getActivity(), CreateGameActivity.class);
                    startActivityForResult(intent, 1);
                }
            });
        }

        getDataList();

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){
                getDataList();
                Toast.makeText(getContext(), data.getExtras().getString("message"), Toast.LENGTH_LONG).show();
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Cancelled
            }
        }
    }

    private void getDataList() {
        GetListGamesTask getListGamesTask = new GetListGamesTask(getContext(), false);
        List<Game> gameListReturn = new ArrayList<>();
        try {
            gameListReturn = new ArrayList<>();
            gameListReturn = getListGamesTask.execute().get();
            setListCourts(gameListReturn);
        } catch (ExecutionException | InterruptedException ei) {
            setListCourts(gameListReturn);
            ei.printStackTrace();
        }
    }

    private void setListCourts(List<Game> gameListReturn) {
        gameListAdapter = new GamesListAdapter(getContext(), gameListReturn);
        listViewGames.setAdapter(gameListAdapter);
        swipeContainer.setRefreshing(false);
    }

}
