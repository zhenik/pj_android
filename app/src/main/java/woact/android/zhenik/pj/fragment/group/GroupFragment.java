package woact.android.zhenik.pj.fragment.group;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.lucasurbas.listitemview.ListItemView;

import java.util.ArrayList;
import java.util.List;

import woact.android.zhenik.pj.R;
import woact.android.zhenik.pj.db.UserDao;
import woact.android.zhenik.pj.db.UserGroupDao;
import woact.android.zhenik.pj.model.Group;
import woact.android.zhenik.pj.utils.ApplicationInfo;


public class GroupFragment extends Fragment {
    public static final String TAG = "###GroupFragment:> ";

    private View view;
    private ListItemView listItemView;
    private UserGroupDao userGroupDao;
    private ArrayAdapter<String> adapter;
    private ListView groupsListView;

    public GroupFragment() {
        Log.i(TAG, "constructor");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView");
        setHasOptionsMenu(true);
        this.view=inflater.inflate(R.layout.group_fragment, null);
//        listItemView=(ListItemView)view.findViewById(R.id.group_list);
        groupsListView = (ListView)view.findViewById(R.id.groups_group_list);
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.group_menu, menu);
    }

    @Override
    public void onResume() {
        super.onResume();
        initAdapters();
    }

    private void initAdapters(){
        List<String> dummyGroupsName = new ArrayList<>();
        dummyGroupsName.add("Friends");
        dummyGroupsName.add("Parents");
        dummyGroupsName.add("Charity");
        adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, dummyGroupsName);
        groupsListView.setAdapter(adapter);
    }

//    private void initAdapters(){
//        List<Group> groups = userGroupDao.getGroupsOfUser(ApplicationInfo.USER_IN_SYSTEM_ID);
//        adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, potsName1);
////        pots = (ListView) view.findViewById(R.viewListId.pots_list_fragment_listView);
//        pots = new ListView(getContext());
//        pots.setAdapter(adapter);
//        viewListId = View.generateViewId();
//        pots.setId(viewListId);
//        ((LinearLayout)view.findViewById(R.id.group_container)).addView(pots);
//    }
}
