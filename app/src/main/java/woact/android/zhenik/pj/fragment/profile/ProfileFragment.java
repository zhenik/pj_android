package woact.android.zhenik.pj.fragment.profile;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.List;

import es.dmoral.toasty.Toasty;
import woact.android.zhenik.pj.LoginActivity;
import woact.android.zhenik.pj.MainActivity;
import woact.android.zhenik.pj.R;
import woact.android.zhenik.pj.db.UserDao;
import woact.android.zhenik.pj.db.UserGroupDao;
import woact.android.zhenik.pj.model.Invitation;
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
        setHasOptionsMenu(true);
        return view;
    }
    // Menu
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {inflater.inflate(R.menu.profile_menu, menu);}


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


    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==R.id.action_logout){
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("Do you want logout?");
            builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Log.d("USER_IN_SYSTEM: ", ApplicationInfo.USER_IN_SYSTEM_ID+" <-here");
                    ApplicationInfo.USER_IN_SYSTEM_ID= 0;
                    Log.d("USER_IN_SYSTEM: ", ApplicationInfo.USER_IN_SYSTEM_ID+" <-here");
                    startActivity(new Intent(view.getContext(), LoginActivity.class));
                    dialog.dismiss();
                }
            });
            builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
//            builder.show();
        }

        if (item.getItemId()==R.id.action_invitation_list){
            FragmentManager fm = getFragmentManager();
            FragmentTransaction transaction = fm.beginTransaction();
            transaction.replace(R.id.content, new InvitationListFragment(), InvitationListFragment.TAG);
            transaction.commit();
        }
        return super.onOptionsItemSelected(item);
    }
}
