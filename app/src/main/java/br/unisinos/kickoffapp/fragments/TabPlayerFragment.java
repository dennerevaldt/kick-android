package br.unisinos.kickoffapp.fragments;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import br.unisinos.kickoffapp.R;
import br.unisinos.kickoffapp.adapters.TabPlayerAdapter;

public class TabPlayerFragment extends Fragment {

    public static TabLayout tabLayout;
    public static ViewPager viewPager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        /**
         *Inflate tab_layout and setup Views.
         */
        View x =  inflater.inflate(R.layout.fragment_tab_player,null);
        tabLayout = (TabLayout) x.findViewById(R.id.tabs);
        viewPager = (ViewPager) x.findViewById(R.id.viewpager);

        /**
         *Set an Apater for the View Pager
         */
        viewPager.setAdapter(new TabPlayerAdapter(getChildFragmentManager()));

        /**
         * Now , this is a workaround ,
         * The setupWithViewPager dose't works without the runnable .
         * Maybe a Support Library Bug .
         */

        tabLayout.post(new Runnable() {
            @Override
            public void run() {
                tabLayout.setupWithViewPager(viewPager);
                tabLayout.getTabAt(0).setIcon(R.mipmap.ic_directions_run_white_24dp);
                tabLayout.getTabAt(1).setIcon(R.mipmap.ic_location_on_white_24dp);
                tabLayout.getTabAt(2).setIcon(R.mipmap.ic_notifications_white_24dp);
                /**
                 * Set title initial
                 */
                getActivity().setTitle("Meus jogos");

                tabLayout.getTabAt(0).getIcon().setColorFilter(ContextCompat.getColor(getContext(), R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
                tabLayout.getTabAt(1).getIcon().setColorFilter(ContextCompat.getColor(getContext(), R.color.iron), PorterDuff.Mode.SRC_IN);
                tabLayout.getTabAt(2).getIcon().setColorFilter(ContextCompat.getColor(getContext(), R.color.iron), PorterDuff.Mode.SRC_IN);

                tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                    @Override
                    public void onTabSelected(TabLayout.Tab tab) {
                        viewPager.setCurrentItem(tab.getPosition());
                        tab.getIcon().setColorFilter(ContextCompat.getColor(getContext(), R.color.colorPrimary), PorterDuff.Mode.SRC_IN);

                        switch (tab.getPosition()){
                            case 0:
                                getActivity().setTitle("Meus jogos");
                                break;
                            case 1:
                                getActivity().setTitle("Jogos próximos");
                                break;
                            case 2:
                                getActivity().setTitle("Notificações");
                                break;
                        }
                    }

                    @Override
                    public void onTabUnselected(TabLayout.Tab tab) {
                        tab.getIcon().setColorFilter(ContextCompat.getColor(getContext(), R.color.iron), PorterDuff.Mode.SRC_IN);
                    }

                    @Override
                    public void onTabReselected(TabLayout.Tab tab) {

                    }
                });

            }
        });

        return x;
    }

}
