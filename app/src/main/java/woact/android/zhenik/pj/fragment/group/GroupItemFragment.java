package woact.android.zhenik.pj.fragment.group;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import es.dmoral.toasty.Toasty;
import woact.android.zhenik.pj.MainActivity;
import woact.android.zhenik.pj.R;
import woact.android.zhenik.pj.db.UserDao;
import woact.android.zhenik.pj.db.UserGroupDao;
import woact.android.zhenik.pj.model.Group;
import woact.android.zhenik.pj.utils.ApplicationInfo;


public class GroupItemFragment extends Fragment {
    public static final String TAG = "###GroupListFragment:> ";

    private View view;
    private UserGroupDao userGroupDao;
    private Group group;

    public static GroupItemFragment newInstance(Group group) {
        GroupItemFragment groupItemFragment = new GroupItemFragment();
        groupItemFragment.setGroup(group);
        return groupItemFragment;
    }

    public void setGroup(Group group) {this.group = group;}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView");
        setHasOptionsMenu(true);
        this.view=inflater.inflate(R.layout.group_item_fragment, null);
        userGroupDao=new UserGroupDao();
        setHasOptionsMenu(true);

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.back_menu, menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==R.id.action_back){
            FragmentManager fm = getFragmentManager();
            FragmentTransaction transaction = fm.beginTransaction();
            transaction.replace(R.id.content, new GroupListFragment(), GroupListFragment.TAG);
            transaction.commit();
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onResume() {
        ((MainActivity) getActivity()).getSupportActionBar().setTitle(group==null?"Unknown group":group.getGroupName());
//        Toasty.success(getContext(), group.toString(), Toast.LENGTH_SHORT).show();
        super.onResume();
    }
}
