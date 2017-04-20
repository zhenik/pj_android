package woact.android.zhenik.pj.fragment.profile;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import woact.android.zhenik.pj.MainActivity;
import woact.android.zhenik.pj.R;
import woact.android.zhenik.pj.db.InvitationDao;
import woact.android.zhenik.pj.db.UserGroupDao;
import woact.android.zhenik.pj.model.Invitation;
import woact.android.zhenik.pj.utils.ApplicationInfo;
import woact.android.zhenik.pj.utils.InvitationCustomAdapter;


public class InvitationListFragment extends Fragment {
    public static final String TAG = "I.ListFragment:> ";

    private ListView invitationList;
    private View view;
    private InvitationDao invitationDao;
    private UserGroupDao userGroupDao;
    private InvitationCustomAdapter invitationCustomAdapter;

    private ListView listOfInvitations;
    private ArrayAdapter<String> adapter;


//    private GroupCustomAdapter groupCustomAdapter;


    public InvitationListFragment() {}
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView");
        setHasOptionsMenu(true);
        this.view=inflater.inflate(R.layout.invitation_list_fragment, null);
        userGroupDao=new UserGroupDao();
        invitationDao=new InvitationDao();

//        getSupportActionBar().setTitle("your title");
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {inflater.inflate(R.menu.invitation_list_menu, menu);}


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
        initAdapters();
        super.onResume();
    }

    private void initAdapters() {


//        List<Invitation> invitations = invitationDao.getInvitationsListOfUser(ApplicationInfo.USER_IN_SYSTEM_ID);
//        Log.d("CRASH1", "before invitationCustomAdapter");
//        List<String> names = new ArrayList<>();
//        names.add("1");
//        names.add("2");
//        names.add("3");
//        Log.d("CRASH1 ", "1");
//        listOfInvitations=(ListView) view.findViewById(R.id.invitation_list_listView);
//        adapter=new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, names);
//        listOfInvitations.setAdapter(adapter);

//        List<Invitation> invitations = new ArrayList<>();
        //        invitations.add(new Invitation(1,2,ApplicationInfo.USER_IN_SYSTEM_ID,1));
//        invitations.add(new Invitation(2,2,ApplicationInfo.USER_IN_SYSTEM_ID,2));
//        invitations.add(new Invitation(3,2,ApplicationInfo.USER_IN_SYSTEM_ID,3));
        List<Invitation> invitations = invitationDao.getInvitationsListOfUser(ApplicationInfo.USER_IN_SYSTEM_ID);
        Log.d("SENDING.I", " invitations size "+invitations.size());

        Log.d("CRASH1 ", "1 ");
        listOfInvitations=(ListView) view.findViewById(R.id.invitation_list_listView);
        Log.d("CRASH1 ", "2 "+ listOfInvitations.toString()+"");
        invitationCustomAdapter= new InvitationCustomAdapter(getContext(), R.layout.item_invitation, invitations);
        listOfInvitations.setAdapter(invitationCustomAdapter);


//        initListViewListener();

    }

//    private void initListViewListener(){
//        invitationList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Invitation invitation = ((Invitation)invitationList.getItemAtPosition(position));
//                Toast.makeText(
//                        getContext(),
//                        "invitation id: " +invitation.getId(),
//                        Toast.LENGTH_SHORT
//                ).show();
//            }
//        });
//    }

//    private void initAdapters(){
//        List<Group> dummyGroupsName = userGroupDao.getGroupsOfUser(ApplicationInfo.USER_IN_SYSTEM_ID);
//        groupCustomAdapter = new GroupCustomAdapter(getContext(), R.layout.item_friend, dummyGroupsName);
//        groupsListView.setAdapter(groupCustomAdapter);
//        initListViewListener();
//    }
//
//    private void initListViewListener(){
//        groupsListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Group group = ((Group)groupsListView.getItemAtPosition(position));
//                ApplicationInfo.GROUP_CLICKED=null;
//                ApplicationInfo.GROUP_CLICKED=group.getId();
//                Toast.makeText(
//                        getContext(),
//                        group.getGroupName() + ":"+ApplicationInfo.GROUP_CLICKED,
//                        Toast.LENGTH_SHORT
//                ).show();
//                fm = getFragmentManager();
//                FragmentTransaction ft = fm.beginTransaction();
//                ft.replace(R.id.content, GroupItemFragment.newInstance(group), GroupItemFragment.TAG);
//                ft.addToBackStack(GroupItemFragment.TAG);
//                ft.commit();
//            }
//        });
//    }
}
