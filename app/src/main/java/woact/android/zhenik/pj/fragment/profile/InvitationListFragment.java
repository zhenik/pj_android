package woact.android.zhenik.pj.fragment.profile;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.List;

import es.dmoral.toasty.Toasty;
import woact.android.zhenik.pj.MainActivity;
import woact.android.zhenik.pj.R;
import woact.android.zhenik.pj.db.InvitationDao;
import woact.android.zhenik.pj.db.UserGroupDao;
import woact.android.zhenik.pj.model.Invitation;
import woact.android.zhenik.pj.utils.ApplicationInfo;
import woact.android.zhenik.pj.utils.InvitationCustomAdapter;


public class InvitationListFragment extends Fragment {
    public static final String TAG = "I.ListFragment:> ";


    private View view;
    private InvitationDao invitationDao;
    private UserGroupDao userGroupDao;
    private InvitationCustomAdapter invitationCustomAdapter;

    private ListView listOfInvitations;


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
        List<Invitation> invitations = invitationDao.getInvitationsListOfUser(ApplicationInfo.USER_IN_SYSTEM_ID);
        TextView noInvitations = (TextView)view.findViewById(R.id.invitations_message);
        noInvitations.setText("");
        noInvitations.setVisibility(View.GONE);
        if (invitations.size()==0) {
            noInvitations.setVisibility(View.VISIBLE);
            noInvitations.setText("You have no invitations");
//            TextView noInvitations = new TextView(getContext());
//            noInvitations.setText("No invitations");
//            LinearLayout.LayoutParams params =
//                    new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
//                                                  LinearLayout.LayoutParams.WRAP_CONTENT);
//            noInvitations.setLayoutParams(params);
//            noInvitations.setGravity(Gravity.START);
//            ((LinearLayout)view.findViewById(R.id.invitation_layout))
//                    .addView(noInvitations);
        }
        Log.d("SENDING.I", " invitations size "+invitations.size());
        Log.d("CRASH1 ", "1 ");
        listOfInvitations=(ListView) view.findViewById(R.id.invitation_list_listView);
        Log.d("CRASH1 ", "2 "+ listOfInvitations.toString()+"");
        invitationCustomAdapter= new InvitationCustomAdapter(getContext(), R.layout.item_invitation, invitations);
        listOfInvitations.setAdapter(invitationCustomAdapter);
        initListViewListener();
    }

    private void initListViewListener(){
        listOfInvitations.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final Invitation invitation = ((Invitation)listOfInvitations.getItemAtPosition(position));
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.AlertDialogCustom);
                builder.setTitle("Invitation");
                builder.setMessage(
                        invitation.getSendBy().getFullName()+" invites you to TEEM "+invitation.getGroup().getGroupName());
                builder.setPositiveButton("Accept",
                                          new DialogInterface.OnClickListener()
                                          {
                                              public void onClick(DialogInterface dialog, int id)
                                              {
                                                  userGroupDao.registerUserInGroup(invitation.getReceivedById(), invitation.getGroupId());
                                                  invitationDao.deleteRaw(invitation.getId());
                                                  Toasty.success(getContext(), "Invitation was accepted",Toast.LENGTH_SHORT).show();
                                                  onResume();
                                                  dialog.cancel();
                                              }
                                          });
                builder.setNeutralButton("Decline",
                                         new DialogInterface.OnClickListener()
                                         {
                                             public void onClick(DialogInterface dialog, int id)
                                             {
                                                 invitationDao.deleteRaw(invitation.getId());
                                                 Toasty.info(getContext(), "Invitation was declined",Toast.LENGTH_SHORT).show();
                                                 onResume();
                                                 dialog.cancel();
                                             }
                                         });
                builder.setNegativeButton("Cancel",
                                          new DialogInterface.OnClickListener()
                                          {
                                              public void onClick(DialogInterface dialog, int id)
                                              {
                                                  dialog.cancel();
                                              }
                                          });
                builder.show();
            }
        });
    }
}
