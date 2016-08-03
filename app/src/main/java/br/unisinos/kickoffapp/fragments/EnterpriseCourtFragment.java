package br.unisinos.kickoffapp.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import br.unisinos.kickoffapp.activities.CreateCourtActivity;
import br.unisinos.kickoffapp.activities.EditCourtActivity;
import br.unisinos.kickoffapp.adapters.CourtsListAdapter;
import br.unisinos.kickoffapp.asynk.courtTask.GetListCourtsTask;
import br.unisinos.kickoffapp.models.Court;

/**
 * Created by dennerevaldtmachado on 20/07/16.
 */
public class EnterpriseCourtFragment extends Fragment {
    private ListView listViewCourts;
    private CourtsListAdapter courtListAdapter;
    private SwipeRefreshLayout swipeContainer;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_enterprise_court, null);

        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getDataList();
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(R.color.colorPrimary, R.color.colorPrimaryDark, R.color.colorAccent);

        final List<Court> courtList = new ArrayList<>();
        courtListAdapter = new CourtsListAdapter(getContext(), courtList);
        listViewCourts = (ListView) view.findViewById(R.id.listCourts);
        listViewCourts.setAdapter(courtListAdapter);

        listViewCourts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), EditCourtActivity.class);
                Court court = (Court) courtListAdapter.getItem(position);
                intent.putExtra("Court", court);
                startActivityForResult(intent, 1);
            }
        });

        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        if (fab != null){
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getActivity(), CreateCourtActivity.class);
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
        GetListCourtsTask getCategoriesHttp = new GetListCourtsTask(getContext(), false);
        List<Court> courtListReturn = null;
        try {
            courtListReturn = getCategoriesHttp.execute().get();
            setListCourts(courtListReturn);
        } catch (ExecutionException | InterruptedException ei) {
            setListCourts(courtListReturn);
            ei.printStackTrace();
        }
    }

    private void setListCourts(List<Court> courtListReturn) {
        courtListAdapter = new CourtsListAdapter(getContext(), courtListReturn);
        listViewCourts.setAdapter(courtListAdapter);
        swipeContainer.setRefreshing(false);
    }

}
