package br.unisinos.kickoffapp.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import br.unisinos.kickoffapp.fragments.PlayerGameFragment;
import br.unisinos.kickoffapp.fragments.PlayerGameNearFragment;
import br.unisinos.kickoffapp.fragments.PlayerNotificationFragment;

/**
 * Created by dennerevaldtmachado on 20/07/16.
 */
public class TabPlayerAdapter extends FragmentPagerAdapter {
    public static int intItems = 3 ;

    public TabPlayerAdapter(FragmentManager fm) {
        super(fm);
    }

    /**
     * Return fragment with respect to Position .
     */

    @Override
    public Fragment getItem(int position)
    {
        switch (position){
            case 0 : return new PlayerGameFragment();
            case 1 : return new PlayerGameNearFragment();
            case 2 : return new PlayerNotificationFragment();
        }
        return null;
    }

    @Override
    public int getCount() {
        return intItems;
    }

    /**
     * This method returns the title of the tab according to the position.
     */

    @Override
    public CharSequence getPageTitle(int position) {

        switch (position){
            case 0 :
                return "";
            case 1 :
                return "";
            case 2 :
                return "";
        }
        return null;
    }
}
