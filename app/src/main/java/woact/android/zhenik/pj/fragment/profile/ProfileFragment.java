package woact.android.zhenik.pj.fragment.profile;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mikhaellopez.circularimageview.CircularImageView;

import woact.android.zhenik.pj.MainActivity;
import woact.android.zhenik.pj.R;
import woact.android.zhenik.pj.db.UserDao;
import woact.android.zhenik.pj.db.UserGroupDao;
import woact.android.zhenik.pj.model.User;
import woact.android.zhenik.pj.utils.ApplicationInfo;


public class ProfileFragment extends Fragment {
    public static final String TAG = "###ProfileFragment:> ";

    private TextView mFullname;
    private TextView mMoney;
    private TextView mGroups;
    private TextView mScore;
    private CircularImageView mImg;
    private View view;
    private User user;

    private UserDao userDao;
    private UserGroupDao userGroupDao;

    public ProfileFragment() {
        Log.i(TAG, "constructor");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView");
        this.view=inflater.inflate(R.layout.profile_fragment, null);
        initInfoElements();
        initDaos();
        return view;
    }

    private void initDaos(){
        userDao=new UserDao();
        userGroupDao=new UserGroupDao();
    }
    private void initInfoElements(){
        mFullname=(TextView)view.findViewById(R.id.profile_fullname_txt);
        mMoney=(TextView)view.findViewById(R.id.profile_money_txt);
        mGroups=(TextView)view.findViewById(R.id.profile_groups_txt);
        mScore=(TextView)view.findViewById(R.id.profile_score_txt);
        mImg=(CircularImageView)view.findViewById(R.id.profile_img_circularImageView);
    }

    private void updateInfo(){
        user = userDao.getUserById(ApplicationInfo.USER_IN_SYSTEM_ID);
        mFullname.setText(user.getFullName());
        mMoney.setText(String.valueOf(user.getMoney()));
        mScore.setText(String.valueOf(user.getScore()));
        int amountGroups = userGroupDao.getGroupsOfUser(ApplicationInfo.USER_IN_SYSTEM_ID).size();
        mGroups.setText(String.valueOf(amountGroups));

        // set default pic TODO: development mode (hardcoded)
        if ("a".equals(user.getUserName())) mImg.setImageResource(R.drawable.changiz);
        if ("b".equals(user.getUserName())) mImg.setImageResource(R.drawable.oda);
    }

    @Override
    public void onResume() {
        ((MainActivity) getActivity()).getSupportActionBar().setTitle("Profile");
        updateInfo();
        super.onResume();

    }
}
