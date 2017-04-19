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
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;
import woact.android.zhenik.pj.MainActivity;
import woact.android.zhenik.pj.R;
import woact.android.zhenik.pj.db.UserDao;
import woact.android.zhenik.pj.db.UserGroupDao;
import woact.android.zhenik.pj.model.Group;
import woact.android.zhenik.pj.model.User;
import woact.android.zhenik.pj.utils.ApplicationInfo;


public class GroupItemFragment extends Fragment {
    public static final String TAG = "###GroupListFragment:> ";

    private View view;
    private UserGroupDao userGroupDao;
    private long groupId;
    private Group group;
    private User user;
    private TextView investment;
    private TextView loan;
    // list of users
    private ArrayAdapter<String> adapter;
    private ListView listOfUsers;

    public static GroupItemFragment newInstance(Group group) {
        GroupItemFragment groupItemFragment = new GroupItemFragment();
        groupItemFragment.setGroup(group);
        return groupItemFragment;
    }

    public void setGroup(Group group) {
        groupId = group.getId();
    }

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

    // Menu
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {inflater.inflate(R.menu.back_menu, menu);}


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
        // update user

        initTextViews();
        fetch_update_UserGroupInvestmentLoanInfo();
        initTabs(view);
        super.onResume();
    }



    private void fetch_update_UserGroupInvestmentLoanInfo(){
        group=userGroupDao.getGroupDao().getGroup(groupId);
        user=userGroupDao.getUserDao().getUserById(ApplicationInfo.USER_IN_SYSTEM_ID);
        String money = user.getMoney()+"";
        ((MainActivity) getActivity())
                .getSupportActionBar()
                .setTitle(group==null?"Unknown group":group.getGroupName() +" | "+money);
        investment.setText(String.valueOf(userGroupDao.getUserInvestment(user.getId(),group.getId())));
        loan.setText(String.valueOf(userGroupDao.getUserLoan(user.getId(), group.getId())));
    }

    private void initTextViews(){
        investment=(TextView)view.findViewById(R.id.group_item_investments);
        loan=(TextView)view.findViewById(R.id.group_item_loan);
    }

    // use in initTabs()
    private void initAdapter() {
        List<String> userNamesInGroup = new ArrayList<>();
        List<User> users = userGroupDao.getUsersFromGroup(groupId);
        for (User user:users)
            userNamesInGroup.add(user.getFullName());
        listOfUsers=(ListView) view.findViewById(R.id.group_item_collaborators);
        adapter=new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, userNamesInGroup);
        listOfUsers.setAdapter(adapter);
    }
    private void initTabs(View view){

        TabHost tabHost = (TabHost) view.findViewById(R.id.group_item_tabhost);
        // инициализация
        tabHost.setup();
        TabHost.TabSpec tabSpec;
//
        // создаем вкладку и указываем тег
        tabSpec = tabHost.newTabSpec("tag1");
        tabSpec.setIndicator("Total");
        tabSpec.setContent(R.id.tab1);
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec("tag2");
        tabSpec.setIndicator("Available");
        tabSpec.setContent(R.id.tab2);
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec("tag3");
        tabSpec.setIndicator("Members");
        tabSpec.setContent(R.id.tab3);
        tabHost.addTab(tabSpec);
        initAdapter();


//        // указываем id компонента из FrameLayout, он и станет содержимым
        //// -------!!!!!!!!!!!!
        //         добавляем в корневой элемент
        /**
        tabSpec.setContent(R.id.chart1);
        tabHost.addTab(tabSpec);
        */
//        tabSpec = tabHost.newTabSpec("tag2");
////        // указываем название и картинку
////        // в нашем случае вместо картинки идет xml-файл,
////        // который определяет картинку по состоянию вкладки
//        tabSpec.setIndicator("Available");

        /**
        tabSpec.setContent(R.id.chart_avaliable);
        tabHost.addTab(tabSpec);
        */
//        tabSpec = tabHost.newTabSpec("tag3");
//        // создаем View из layout-файла
//        tabSpec.setContent(R.id.tvTab3);
//        tabHost.addTab(tabSpec);
//
//        // вторая вкладка будет выбрана по умолчанию
//        tabHost.setCurrentTabByTag("tag2");

        // обработчик переключения вкладок
        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            public void onTabChanged(String tabId) {
                Toast.makeText(getContext(), "tabId = " + tabId, Toast.LENGTH_SHORT).show();
            }
        });
    }

}
