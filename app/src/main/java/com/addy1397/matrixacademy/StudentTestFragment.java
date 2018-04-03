package com.addy1397.matrixacademy;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 */
public class StudentTestFragment extends Fragment {

    public static String batchID;

    private ViewPager viewPager;
    private SectionsPagerAdapter sectionsPagerAdapter;

    private TabLayout tabLayout;

    public StudentTestFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_student_test, container, false);

        viewPager = (ViewPager) view.findViewById(R.id.viewPager_test);

        sectionsPagerAdapter = new SectionsPagerAdapter(getActivity().getSupportFragmentManager());

        viewPager.setAdapter(sectionsPagerAdapter);

        tabLayout = (TabLayout) view.findViewById(R.id.tabLayout_test);
        tabLayout.setupWithViewPager(viewPager);

        Bundle arguments = getArguments();
        batchID = arguments.getString("batch");

        return view;
    }

    class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch(position){
                case 0:
                    TestShowFragment testShowFragment = new TestShowFragment();
                    Bundle arguments = new Bundle();
                    arguments.putString( "batch" , batchID);
                    testShowFragment.setArguments(arguments);
                    return testShowFragment;
                case 1:
                    GraphFragment graphFragment = new GraphFragment();
                    arguments = new Bundle();
                    arguments.putString( "batch" , batchID);
                    graphFragment.setArguments(arguments);
                    return graphFragment;
            }
            return null;
        }

        @Override
        public int getCount() {
            return 2;
        }

        public CharSequence getPageTitle(int position){
            switch(position){
                case 0:
                    return "Test";
                case 1:
                    return "Graph";
            }
            return null;
        }
    }

}
