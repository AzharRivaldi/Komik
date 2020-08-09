package com.azhar.komik.activities;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;

import com.akshay.library.CurveBottomBar;
import com.azhar.komik.R;
import com.azhar.komik.fragment.GenreFragment;
import com.azhar.komik.fragment.HomeFragment;
import com.azhar.komik.utils.BottomBarBehavior;

public class MainActivity extends AppCompatActivity {

    CurveBottomBar navigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        navigation = findViewById(R.id.curveBottomBar);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) navigation.getLayoutParams();
        layoutParams.setBehavior(new BottomBarBehavior());

        if (savedInstanceState == null){
            navigation.setSelectedItemId(R.id.navigation_home);
        }

    }

    private final CurveBottomBar.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new CurveBottomBar.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment;

            switch (item.getItemId()) {
                case R.id.navigation_home:
                    fragment = new HomeFragment();
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.frameContainer, fragment, fragment.getClass().getSimpleName())
                            .commit();
                    return true;
                case R.id.navigation_list:
                    fragment = new GenreFragment();
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.frameContainer, fragment, fragment.getClass().getSimpleName())
                            .commit();
                    return true;
            }
            return false;
        }
    };

}
