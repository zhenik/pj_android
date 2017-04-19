package woact.android.zhenik.pj.fragment.group;

import android.graphics.Color;
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
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

import woact.android.zhenik.pj.MainActivity;
import woact.android.zhenik.pj.R;
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
    // Charts
    private PieChart chartInvestLoan;
    private PieChart chartTotalAvailable;

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
//        initCharts();
//        setPieInvestmentLoan(chartInvestLoan);
        updateChartData();
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
    private void initCharts(){
        chartInvestLoan=(PieChart)view.findViewById(R.id.pie_invest_loan);
        chartTotalAvailable=(PieChart)view.findViewById(R.id.pie_available_total);
        Log.d("TAG: ",chartInvestLoan.toString()+"");
        Log.d("TAG: ",chartTotalAvailable.toString()+"");
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
//        updateChartData();

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


    private void setPieInvestmentLoan(PieChart chart){
        // set pot
//        chart.clearValues();
        chart.setDrawHoleEnabled(false);
//        chart.setUsePercentValues(false);
        chart.getDescription().setEnabled(false);
        chart.setExtraOffsets(5, 10, 5, 5);
//        chart.setCenterText("Pot: available");
        chart.setEntryLabelColor(Color.BLACK);

        List<PieEntry> entries = new ArrayList<PieEntry>();
        float totalMoney = userGroupDao.getGroupDao().getTotalMoney(groupId).floatValue();
        float investment = userGroupDao.getUserInvestment(user.getId(),group.getId()).floatValue();
        float allInvestments = userGroupDao.getAllInvestmentsInGroup(group.getId()).floatValue();
        float income = investment*totalMoney/allInvestments-investment;
        float other =  totalMoney-investment-income;

//        Log.d("MATH: ", "totalMoney:"+totalMoney);
//        Log.d("MATH: ", "investment:"+investment);
//        Log.d("MATH: ", "allInvestments:"+allInvestments);
//        Log.d("MATH: ", "income:"+income);
//        Log.d("MATH: ", "other:"+other);

        entries.add(new PieEntry(investment, "Investments"));
        entries.add(new PieEntry(income, "Income"));
        entries.add(new PieEntry(other, "Other"));
//        entries.add(new PieEntry(pot.getTotalLoaned(), "Loaned"));
//        if (pot.getUserLoan(user.getName())>0)
//            entries.add(new PieEntry(pot.getUserLoan(user.getName()), "Your loan"));

        PieDataSet dataSet = new PieDataSet(entries, ""); // add entries to dataset
        dataSet.setColor(Color.BLACK);
        // set colors #1
        ArrayList<Integer> colors = new ArrayList<Integer>();
        colors.add(0xFE45BF55);
        colors.add(0xFE167F39);
        colors.add(0xFFFFF5A6);
//        colors.add(0xFF262626);
        colors.add(ColorTemplate.getHoloBlue());
        dataSet.setColors(colors);

        PieData pieData = new PieData(dataSet);
//        pieData.setValueFormatter(new PercentFormatter());
        pieData.setValueTextSize(11f);
        pieData.setValueTextColor(Color.BLACK);
        chart.setData(pieData);
        chart.invalidate(); // refresh
    }

    private void setPieTotalAvailable(PieChart chart){
        // set pot
//        chart.clearValues();
        chart.setDrawHoleEnabled(false);
//        chart.setUsePercentValues(false);
        chart.getDescription().setEnabled(false);
        chart.setExtraOffsets(5, 10, 5, 5);
//        chart.setCenterText("Pot: available");
        chart.setEntryLabelColor(Color.BLACK);

        List<PieEntry> entries = new ArrayList<PieEntry>();
        // worst crutch EVER !!!
        float totalMoney = (float)group.getTotalMoney();
        float availableMoney = (float)group.getAvailableMoney();

        Log.d("MATH: ", "totalMoney:"+totalMoney);
        Log.d("MATH: ", "availableMoney:"+availableMoney);

        entries.add(new PieEntry(totalMoney-availableMoney, "Loaned"));
        entries.add(new PieEntry(availableMoney, "Available"));

        PieDataSet dataSet = new PieDataSet(entries, ""); // add entries to dataset
        dataSet.setColor(Color.BLACK);
        // set colors #1
        ArrayList<Integer> colors = new ArrayList<Integer>();
        colors.add(0xFEE5000D);
        colors.add(0xFE00BF3E);
        colors.add(ColorTemplate.getHoloBlue());
        dataSet.setColors(colors);

        PieData pieData = new PieData(dataSet);
//        pieData.setValueFormatter(new PercentFormatter());
        pieData.setValueTextSize(11f);
        pieData.setValueTextColor(Color.BLACK);
        chart.setData(pieData);
        chart.invalidate(); // refresh
    }


    private void updateChartData(){
        initCharts();
        if (chartInvestLoan.getData()!=null)
            chartInvestLoan.clearValues();
        if (chartTotalAvailable.getData()!=null)
            chartTotalAvailable.clearValues();

        setPieInvestmentLoan(chartInvestLoan);
        setPieTotalAvailable(chartTotalAvailable);
    }

}
