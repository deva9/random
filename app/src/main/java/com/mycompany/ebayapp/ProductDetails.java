package com.mycompany.ebayapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

public class ProductDetails extends AppCompatActivity {

    TabLayout tabLayout;
    FloatingActionButton fab;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);
        Intent intent = getIntent();
        String id = intent.getStringExtra("ID");
        String title = intent.getStringExtra("Title");
        System.out.println(id+" "+title);
        Toolbar toolbar = findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(title);
        tabLayout = findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("Product"));
        tabLayout.addTab(tabLayout.newTab().setText("Shipping"));
        tabLayout.addTab(tabLayout.newTab().setText("Photos"));
        tabLayout.addTab(tabLayout.newTab().setText("Similar"));
        tabLayout.getTabAt(0).setIcon(R.drawable.ic_info);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_ship);
        tabLayout.getTabAt(2).setIcon(R.drawable.ic_google);
        tabLayout.getTabAt(3).setIcon(R.drawable.ic_equal);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        final ViewPager viewPager = findViewById(R.id.view_pager);
        PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) { viewPager.setCurrentItem(tab.getPosition()); }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

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
                    ProductTab productTab = new ProductTab();
                    return productTab;
                case 1:
                    ShippingTab shippingTab = new ShippingTab();
                    return shippingTab;
                case 2:
                    PhotosTab photosTab = new PhotosTab();
                    return photosTab;
                case 3:
                    SimilarTab similarTab = new SimilarTab();
                    return similarTab;
                default:
                    return null;
            }
        }
    }
}
