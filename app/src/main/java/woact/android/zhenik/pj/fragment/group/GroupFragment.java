package woact.android.zhenik.pj.fragment.group;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
//import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;
import woact.android.zhenik.pj.R;
import woact.android.zhenik.pj.db.UserGroupDao;
import woact.android.zhenik.pj.model.Group;

import woact.android.zhenik.pj.utils.ApplicationInfo;
import woact.android.zhenik.pj.utils.GroupCustomAdapter;


public class GroupFragment extends Fragment {
    public static final String TAG = "###GroupFragment:> ";

    private View view;
    private UserGroupDao userGroupDao;
    private GroupCustomAdapter groupCustomAdapter;
    private ListView groupsListView;
    private String m_Text;
    private Long groupId;

    public GroupFragment() {
        Log.i(TAG, "constructor");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView");
        setHasOptionsMenu(true);
        this.view=inflater.inflate(R.layout.group_fragment, null);
        userGroupDao=new UserGroupDao();
        groupsListView = (ListView)view.findViewById(R.id.groups_group_list);
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.group_menu, menu);
    }

    //    @Override
//    public boolean onMenuItemSelected(int featureId, MenuItem item){
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==R.id.action_add){
            Log.d("SPECIAL", "I AM HERE");
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("Group name");
            View viewInflated = LayoutInflater.from(getContext())
                    .inflate(R.layout.popup_input_group_name, (ViewGroup) getView(), false);
            final EditText input = (EditText) viewInflated.findViewById(R.id.input1);
            builder.setView(viewInflated);
            builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    m_Text = input.getText().toString();

                    if (!TextUtils.isEmpty(m_Text)) {
                        groupId = userGroupDao.getGroupDao().createGroup(m_Text);
                        if (groupId == -1)
                            Toasty.error(getContext(), "Cant create group with given name", Toast.LENGTH_SHORT).show();
                        else
                            userGroupDao.registerUserInGroup(ApplicationInfo.USER_IN_SYSTEM_ID, groupId);
                    }
                    else
                        Toasty.error(getContext(), "Group name is empty", Toast.LENGTH_SHORT).show();
                    m_Text="";
                    onResume();
                    dialog.dismiss();
                }
            });
            builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });

            builder.show();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();
        initAdapters();
    }
    private void initAdapters(){
        List<Group> dummyGroupsName = userGroupDao.getGroupsOfUser(ApplicationInfo.USER_IN_SYSTEM_ID);
        groupCustomAdapter = new GroupCustomAdapter(getContext(), R.layout.item, dummyGroupsName);
        groupsListView.setAdapter(groupCustomAdapter);
        initListViewListener();
    }

    private void initListViewListener(){
        groupsListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Group group = ((Group)groupsListView.getItemAtPosition(position));
                Toast.makeText(
                        getContext(),
                        group.getGroupName(),
                        Toast.LENGTH_SHORT
                ).show();
//                FragmentTransaction ft = fm.beginTransaction();
//                invisibleAdapter();
//                String potName = pots.getItemAtPosition(position).toString();
//                PotFragment potFragment = PotFragment.newInstance(potName);
//                if (fm.findFragmentByTag(PotFragment.TAG)==null)
//                    ft.replace(R.viewListId.group_fragment, new PotFragment(), PotFragment.TAG);
//                ft.replace(R.id.group_container, potFragment, PotFragment.TAG);
//                Log.i(TAG, potName);
//                ft.addToBackStack(PotFragment.TAG);
//                ft.commit();
//                Toast.makeText(
//                        getContext(),
//                        pots.getItemAtPosition(position).toString(),
//                        Toast.LENGTH_SHORT
//                ).show();
            }
        });
    }
}
