package woact.android.zhenik.pj.fragment.profile;

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
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import es.dmoral.toasty.Toasty;
import woact.android.zhenik.pj.MainActivity;
import woact.android.zhenik.pj.R;
import woact.android.zhenik.pj.db.InvitationDao;
import woact.android.zhenik.pj.db.UserGroupDao;
import woact.android.zhenik.pj.fragment.group.GroupListFragment;
import woact.android.zhenik.pj.utils.ApplicationInfo;
import woact.android.zhenik.pj.utils.GroupCustomAdapter;


public class InvitationListFragment extends Fragment {
    public static final String TAG = "I.ListFragment:> ";

    private ListView invitationList;
    private View view;
    private InvitationDao invitationDao;
    private UserGroupDao userGroupDao;

//    private GroupCustomAdapter groupCustomAdapter;


    public InvitationListFragment() {}
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView");
        setHasOptionsMenu(true);
        this.view=inflater.inflate(R.layout.group_list_fragment, null);
        userGroupDao=new UserGroupDao();
        invitationDao=new InvitationDao();
        invitationList = (ListView)view.findViewById(R.id.invitation_list);
//        getSupportActionBar().setTitle("your title");
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.invitation_list_menu, menu);
    }


    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==R.id.action_list_back){
            FragmentManager fm = getFragmentManager();
            FragmentTransaction transaction = fm.beginTransaction();
            transaction.replace(R.id.content, new ProfileFragment(), ProfileFragment.TAG);
            transaction.commit();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        ((MainActivity) getActivity()).getSupportActionBar().setTitle("Invitations");
//        initAdapters();
        super.onResume();
    }
}
