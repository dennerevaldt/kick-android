package br.unisinos.kickoffapp.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;

import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import br.unisinos.kickoffapp.R;

/**
 * Created by dennerevaldtmachado on 20/07/16.
 */
public class EnterpriseNotificationFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_enterprise_notification, null);
        return view;
    }
}
