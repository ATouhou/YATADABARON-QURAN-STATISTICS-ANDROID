package abdlrhmanshehata.yatadabaron.DataAdapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import abdlrhmanshehata.yatadabaron.Fragments.LettersFragment;
import abdlrhmanshehata.yatadabaron.Fragments.ReadFragment;
import abdlrhmanshehata.yatadabaron.Fragments.SearchFragment;

public class FragmentsAdapter extends FragmentPagerAdapter {
    private Context mContext;
    private boolean ShowReadFragment;
    public FragmentsAdapter(Context context, FragmentManager fm,boolean readFragment) {
        super(fm);
        ShowReadFragment = readFragment;
        mContext = context;
    }
    @Override
    public Fragment getItem(int position) {
        if(ShowReadFragment){
            if(position==0){
                return new LettersFragment();
            }
            if(position==1){
                return new SearchFragment();
            }
            if(position==2){
                return new ReadFragment();
            }
        }else{
            if(position==0){
                return new LettersFragment();
            }
            if(position==1){
                return new SearchFragment();
            }
        }
        return null;
    }

    @Override
    public int getCount() {
        if (ShowReadFragment){
            return 3;
        }else{
            return  2;
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        if(ShowReadFragment){
            if(position==0){
                return "الحروف";
            }
            if(position==1){
                return "بحث";
            }
            if(position==2){
                return "قراءة";
            }
        }else{
            if(position==0){
                return "الحروف";
            }
            if(position==1){
                return "بحث";
            }
        }
        return null;
    }
}
