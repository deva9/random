import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.mycompany.ebayapp.SearchTab;
import com.mycompany.ebayapp.WishListTab;

class PagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;
    public PagerAdapter(FragmentManager fm, int NoofTabs){
        super(fm);
        this.mNumOfTabs = NoofTabs;
    }
    @Override
    public int getCount() {
        return mNumOfTabs;
    }
    @Override
    public Fragment getItem(int position){
        switch (position){
            case 0:
                SearchTab src = new SearchTab();
                return src;
            case 1:
                WishListTab wish = new WishListTab();
                return wish;
            default:
                return null;
        }
    }
}