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
import br.unisinos.kickoffapp.activities.CreateScheduleActivity;
import br.unisinos.kickoffapp.activities.EditScheduleActivity;
import br.unisinos.kickoffapp.adapters.SchedulesListAdapter;
import br.unisinos.kickoffapp.asynk.scheduleTask.GetScheduleListTask;
import br.unisinos.kickoffapp.models.Schedule;

/**
 * Created by dennerevaldtmachado on 20/07/16.
 */
public class EnterpriseScheduleFragment extends Fragment {
    private ListView listViewSchedules;
    private SchedulesListAdapter schedulesListAdapter;
    private SwipeRefreshLayout swipeContainer;
    private List<Schedule> scheduleList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_enterprise_schedule, null);

        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);
        if (swipeContainer != null){
            swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    getDataList();
                }
            });
            // Configure the refreshing colors
            swipeContainer.setColorSchemeResources(R.color.colorPrimary, R.color.colorPrimaryDark, R.color.colorAccent);
        }

        List<Schedule> scheduleList = new ArrayList<>();
        schedulesListAdapter = new SchedulesListAdapter(getContext(), scheduleList);
        listViewSchedules = (ListView) view.findViewById(R.id.listSchedules);
        listViewSchedules.setAdapter(schedulesListAdapter);

        listViewSchedules.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), EditScheduleActivity.class);
                Schedule schedule = (Schedule) schedulesListAdapter.getItem(position);
                intent.putExtra("Schedule", schedule);
                startActivityForResult(intent, 1);
            }
        });

        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        if (fab != null){
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getActivity(), CreateScheduleActivity.class);
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
        GetScheduleListTask getScheduleListTask = new GetScheduleListTask(getContext());
        scheduleList = new ArrayList<>();
        try {
            scheduleList = getScheduleListTask.execute().get();
            scheduleList = scheduleList == null ? new ArrayList<Schedule>() : scheduleList;
            setListViewSchedules(scheduleList);
            swipeContainer.setRefreshing(false);
        } catch (ExecutionException | InterruptedException ei) {
            setListViewSchedules(scheduleList);
            ei.printStackTrace();
        }
    }

    private void setListViewSchedules(List<Schedule> scheduleList) {
        schedulesListAdapter = new SchedulesListAdapter(getContext(), scheduleList);
        listViewSchedules.setAdapter(schedulesListAdapter);
    }
}
