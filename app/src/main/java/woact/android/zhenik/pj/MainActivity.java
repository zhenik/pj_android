package woact.android.zhenik.pj;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.FrameLayout;
import woact.android.zhenik.pj.fragment.group.GroupListFragment;
import woact.android.zhenik.pj.fragment.profile.ProfileFragment;
import woact.android.zhenik.pj.fragment.shop.ShopFragment;

public class MainActivity extends AppCompatActivity {

    private FrameLayout mContent;
    private FragmentManager fm;


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    initProfileFragment();
                    return true;
                case R.id.navigation_dashboard:
                    initGroupFragment();
                    return true;
                case R.id.navigation_notifications:
                    initShopFragment();
                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        mContent = (FrameLayout) findViewById(R.id.content);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        navigation.setBackground(new ColorDrawable(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimaryDark)));
        navigation.setItemIconTintList(getMyColorStateList());
        navigation.setItemTextColor(getMyColorStateList());
        fm=getSupportFragmentManager();
        initProfileFragment();
    }

    private ColorStateList getMyColorStateList(){
        int[][] states = new int[][] {
//                new int[] { android.R.attr.state_enabled}, // enabled
//                new int[] {-android.R.attr.state_enabled}, // disabled
                new int[] {android.R.attr.state_checked},   // checked
                new int[] {-android.R.attr.state_checked},  // unchecked
//                new int[] { android.R.attr.state_pressed}   // pressed
        };

        int[] colors = new int[] {
//                Color.GREEN,
                Color.WHITE,
                Color.GRAY
//                Color.BLACK,
//                Color.RED


        };

        ColorStateList myList = new ColorStateList(states, colors);
        return myList;
    }

    private void initProfileFragment(){
        FragmentTransaction transaction = fm.beginTransaction();
        if (fm.findFragmentByTag(ProfileFragment.TAG)==null)
            transaction.replace(R.id.content, new ProfileFragment(), ProfileFragment.TAG);
        transaction.commit();
    }

    private void initGroupFragment(){
        FragmentTransaction transaction = fm.beginTransaction();
//        if (fm.findFragmentByTag(GroupListFragment.TAG)==null)
            transaction.replace(R.id.content, new GroupListFragment(), GroupListFragment.TAG);
        transaction.commit();
    }

    private void initShopFragment(){
        FragmentTransaction transaction = fm.beginTransaction();
        if (fm.findFragmentByTag(ShopFragment.TAG)==null)
            transaction.replace(R.id.content, new ShopFragment(), ShopFragment.TAG);
        transaction.commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
