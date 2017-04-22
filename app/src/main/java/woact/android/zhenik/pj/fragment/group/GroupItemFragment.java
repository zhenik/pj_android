package woact.android.zhenik.pj.fragment.group;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
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

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;
import woact.android.zhenik.pj.MainActivity;
import woact.android.zhenik.pj.R;
import woact.android.zhenik.pj.db.InvitationDao;
import woact.android.zhenik.pj.db.UserGroupDao;
import woact.android.zhenik.pj.model.Group;
import woact.android.zhenik.pj.model.Invitation;
import woact.android.zhenik.pj.model.User;
import woact.android.zhenik.pj.utils.ApplicationInfo;


public class GroupItemFragment extends Fragment {
    public static final String TAG = "###GroupListFragment:> ";

    private View view;
    private UserGroupDao userGroupDao;
    private InvitationDao invitationDao;
//    private long groupId;
    private Group group;
    private User user;
    private TextView investment;
    private TextView loan;
    private TextView urMoney;
    // list of users
    private ArrayAdapter<String> adapter;
    private ListView listOfUsers;
    // Charts
    private PieChart chartInvestLoan;
    private PieChart chartTotalAvailable;
    // Features
    private String m_Text;
    private Double dept;

    public static GroupItemFragment newInstance(Group group) {
        GroupItemFragment groupItemFragment = new GroupItemFragment();
        groupItemFragment.setGroup(group);
        return groupItemFragment;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView");
        setHasOptionsMenu(true);
        this.view=inflater.inflate(R.layout.group_item_fragment, null);
        userGroupDao=new UserGroupDao();
        invitationDao=new InvitationDao();
        return view;
    }

    // Menu
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {inflater.inflate(R.menu.back_menu, menu);}

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
//            case R.id.action_back:
//                goToGroupList();
//                break;
            case R.id.action_invest:
                investMoney(ApplicationInfo.USER_IN_SYSTEM_ID, group.getId());
                break;
            case R.id.action_loan:
                loanMoney(ApplicationInfo.USER_IN_SYSTEM_ID, group.getId());
                break;
            case R.id.action_pay_back:
                payBack(ApplicationInfo.USER_IN_SYSTEM_ID, group.getId());
                break;
            case R.id.action_withdraw:
                withdraw(user.getId(),group.getId());
//                goToGroupList();
                break;
            case R.id.action_invite:
                invite(user.getId(), group.getId());
                break;
        }
        return super.onOptionsItemSelected(item);
    }



    private void goToGroupList(){
        FragmentManager fm = getFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.content, new GroupListFragment(), GroupListFragment.TAG);
        transaction.commit();
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
    public void updateAll(){

        group = userGroupDao.getGroupDao().getGroup(group.getId());
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.content, GroupItemFragment.newInstance(group), GroupItemFragment.TAG);
        ft.addToBackStack(GroupItemFragment.TAG);
        ft.commit();
    }



    private void fetch_update_UserGroupInvestmentLoanInfo(){
        group=userGroupDao.getGroupDao().getGroup(group.getId());
        user=userGroupDao.getUserDao().getUserById(ApplicationInfo.USER_IN_SYSTEM_ID);


        ((MainActivity) getActivity())
                .getSupportActionBar()
                .setTitle(group==null?"Unknown group":group.getGroupName());
        investment.setText(String.valueOf(userGroupDao.getUserInvestment(user.getId(),group.getId())));
        loan.setText(String.valueOf(userGroupDao.getUserLoan(user.getId(), group.getId())));
        urMoney.setText(String.valueOf(userGroupDao.getUserDao().getMoney(user.getId())));

    }

    private void initTextViews(){
        investment=(TextView)view.findViewById(R.id.group_item_investments);
        loan=(TextView)view.findViewById(R.id.group_item_loan);
        urMoney=(TextView)view.findViewById(R.id.group_item_urmoney);
    }

    // use in initTabs()
    private void initAdapter() {
        List<String> userNamesInGroup = new ArrayList<>();
        List<User> users = userGroupDao.getUsersFromGroup(group.getId());
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


        // set text-color of tabs to white
//        for(int i=0;i<tabHost.getTabWidget().getChildCount();i++) {
//            TextView tv = (TextView) tabHost.getTabWidget().getChildAt(i).findViewById(android.R.id.title);
//            tv.setTextColor(Color.parseColor("#ffffff"));
//        }

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
//        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
//            public void onTabChanged(String tabId) {
//                Toast.makeText(getContext(), "tabId = " + tabId, Toast.LENGTH_SHORT).show();
//            }
//        });

    }


    private void setPieInvestmentLoan(PieChart chart){
        // set pot
//        chart.clearValues();
        chart.setDrawHoleEnabled(false);
//        chart.setUsePercentValues(false);
        chart.getDescription().setEnabled(false);
        chart.setExtraOffsets(5, 10, 5, 5);
//        chart.setCenterText("Pot: available");
        chart.setEntryLabelColor(Color.WHITE);

        List<PieEntry> entries = new ArrayList<PieEntry>();
        float totalMoney = userGroupDao.getGroupDao().getTotalMoney(group.getId()).floatValue();
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

        PieDataSet dataSet = new PieDataSet(entries, " | Total money = "+userGroupDao.getGroupDao().getTotalMoney(group.getId())); // add entries to dataset
        chart.getLegend().setTextColor(Color.WHITE);

//        dataSet.setColor(Color.WHITE);
        // set colors #1
        ArrayList<Integer> colors = new ArrayList<Integer>();
        colors.add(0xFE27AE60);
        colors.add(0xFE52BE80);
//        colors.add(0xFFff9800);
        colors.add(0xFFFFC300);
//        colors.add(0xFF262626);
//        colors.add(ColorTemplate.getHoloBlue());
        dataSet.setColors(colors);

        PieData pieData = new PieData(dataSet);
//        pieData.setValueFormatter(new PercentFormatter());
        pieData.setValueTextSize(11f);
        pieData.setValueTextColor(Color.WHITE);
        chart.setData(pieData);
        chart.invalidate(); // refresh
        chart.spin(1500, 0, -360f, Easing.EasingOption.EaseInOutCubic);
    }

    private void setPieTotalAvailable(PieChart chart){
        // set pot
//        chart.clearValues();
        chart.setDrawHoleEnabled(false);
//        chart.setUsePercentValues(false);
        chart.getDescription().setEnabled(false);
        chart.setExtraOffsets(5, 10, 5, 5);
//        chart.setCenterText("Pot: available");
        chart.setEntryLabelColor(Color.WHITE);

        List<PieEntry> entries = new ArrayList<PieEntry>();
        // worst crutch EVER !!!
        float totalMoney = (float)group.getTotalMoney();
        float availableMoney = (float)group.getAvailableMoney();

        Log.d("MATH: ", "totalMoney:"+totalMoney);
        Log.d("MATH: ", "availableMoney:"+availableMoney);

        entries.add(new PieEntry(totalMoney-availableMoney, "Loaned"));
        entries.add(new PieEntry(availableMoney, "Available"));

        PieDataSet dataSet = new PieDataSet(entries, " | Total money = "+userGroupDao.getGroupDao().getTotalMoney(group.getId())); // add entries to dataset
        chart.getLegend().setTextColor(Color.WHITE);
        dataSet.setColor(Color.WHITE);
        // set colors #1
        ArrayList<Integer> colors = new ArrayList<Integer>();
        colors.add(0xFEC0392B);
        colors.add(0xFE1E8449);
        colors.add(ColorTemplate.getHoloBlue());
        dataSet.setColors(colors);

        PieData pieData = new PieData(dataSet);
//        pieData.setValueFormatter(new PercentFormatter());
        pieData.setValueTextSize(11f);
        pieData.setValueTextColor(Color.WHITE);
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

    private void investMoney(final long userId, final long groupId){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("How much money do you want invest?");
        View viewInflated = LayoutInflater.from(getContext())
                .inflate(R.layout.popup_input_investment, (ViewGroup) getView(), false);
        final EditText input = (EditText) viewInflated.findViewById(R.id.input1);
        builder.setView(viewInflated);
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.d("INVEST: ", "input: "+input.getText().toString());
//                m_Text = input.getText().toString();
                m_Text = input.getText().toString();
                if (!TextUtils.isEmpty(m_Text)) {
                    Double investment=null;
                    try{
                        investment=Double.parseDouble(m_Text);
                        Log.d("INVEST: ", "investment: "+investment);
                    }
                    catch (Exception e){}

                    if (investment==null){
                        Toasty.error(getContext(), "Cant invest", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (userGroupDao.getUserDao().getUserById(user.getId()).getMoney()<investment){
                        Toasty.error(getContext(), "Not enought money to invest "+investment, Toast.LENGTH_SHORT).show();
                        return;
                    }

                    /**
                     * All DB INTERACTIONS
                     * */
                    double alreadyInvested = userGroupDao.getUserInvestment(userId, groupId);
                    long raws = userGroupDao.setUserInvestment(userId,groupId,investment+alreadyInvested);
                    double oldMoney=userGroupDao.getUserDao().getMoney(user.getId());
                    userGroupDao.getUserDao().setMoney(user.getId(), oldMoney-investment);

                    double oldTotalMoney = userGroupDao.getGroupDao().getTotalMoney(group.getId());
                    userGroupDao.getGroupDao().updateTotalMoney(group.getId(), oldTotalMoney+investment);

                    double oldAvailableMoney = userGroupDao.getGroupDao().getAvailableMoney(group.getId());
                    userGroupDao.getGroupDao().updateAvailableMoney(group.getId(), oldAvailableMoney+investment);

                    // score bonus
                    userGroupDao.getUserDao().scoreBonus(userId,investment);

                    Log.d("INVEST: ", "raws: "+raws);
                }
                else
                    Toasty.error(getContext(), "Investment is empty", Toast.LENGTH_SHORT).show();
                m_Text="";
                updateAll();
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

    private void loanMoney(final long userId, final long groupId){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("How much money do you want to loan?");
        View viewInflated = LayoutInflater.from(getContext())
                .inflate(R.layout.popup_input_investment, (ViewGroup) getView(), false);
        final EditText input = (EditText) viewInflated.findViewById(R.id.input1);
        builder.setView(viewInflated);
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.d("LOAN: ", "input: "+input.getText().toString());
//                m_Text = input.getText().toString();
                m_Text = input.getText().toString();
                if (!TextUtils.isEmpty(m_Text)) {
                    Double loan=null;
                    try{
                        loan=Double.parseDouble(m_Text);
                        Log.d("LOAN: ", "loan: "+loan);
                    }
                    catch (Exception e){}

                    if (loan==null){
                        Toasty.error(getContext(), "Cant loan", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    // validation group
                    if (userGroupDao.getGroupDao().getAvailableMoney(groupId)<loan){
                        Toasty.error(getContext(), "Group has not so much available money "+loan, Toast.LENGTH_SHORT).show();
                        return;
                    }

                    /**
                     * All DB INTERACTIONS
                     * 1. User money= +money
                     * 2. User loan= +( money + %*loan)
                     * 3. Group total= +(%)
                     * 4. Group available= total - loan
                     * */

                    // 1
                    Log.d("LOAN: ", "1-----------------------------");
                    double userMoneyOld = userGroupDao.getUserDao().getUserById(userId).getMoney();
                    long rowsAffected = userGroupDao.getUserDao().
                            setMoney(userId, userMoneyOld+loan);
                    double userMoneyNew = userGroupDao.getUserDao().getUserById(userId).getMoney();
                    Log.d("LOAN: ", "rowsAffected: "+rowsAffected);
                    Log.d("LOAN: ", "userMoneyNew: "+userMoneyNew);

                    // 2
                    Log.d("LOAN: ", "2-----------------------------");
                    double userLoanOld = userGroupDao.getUserLoan(userId, groupId);
                    double newLoan = userLoanOld+(loan+(loan*ApplicationInfo.LOAN_PERCENT));
                    userGroupDao
                            .setUserLoan(
                                    ApplicationInfo.USER_IN_SYSTEM_ID,
                                    group.getId(),
                                    newLoan
                                    );
                    double userLoanNew = userGroupDao.getUserLoan(userId, groupId);
                    Log.d("LOAN: ", "userLoanOld: "+userLoanOld);
                    Log.d("LOAN: ", "newLoan: "+newLoan);
                    Log.d("LOAN: ", "userLoanNew: "+userLoanNew);

                    // 3
                    Log.d("LOAN: ", "3-----------------------------");
                    double groupTotalOld = userGroupDao.getGroupDao().getTotalMoney(groupId);
                    userGroupDao.getGroupDao().updateTotalMoney(groupId,groupTotalOld + (loan*ApplicationInfo.LOAN_PERCENT));
                    double groupTotalNew = userGroupDao.getGroupDao().getTotalMoney(groupId);
                    Log.d("LOAN: ", "groupTotalOld: "+groupTotalOld);
                    Log.d("LOAN: ", "groupTotalNew: "+groupTotalNew);

                    // 4
                    Log.d("LOAN: ", "4-----------------------------");
                    double groupAvailableOld = userGroupDao.getGroupDao().getAvailableMoney(groupId);
//                    double groupTotal = userGroupDao.getGroupDao().getTotalMoney(groupId);
                    userGroupDao.getGroupDao().updateAvailableMoney(groupId, groupAvailableOld-loan);
                    double groupAvailableNew = userGroupDao.getGroupDao().getAvailableMoney(groupId);
                    Log.d("LOAN: ", "groupAvailableOld: "+groupAvailableOld);
                    Log.d("LOAN: ", "groupAvailableNew: "+groupAvailableNew);

                    // score bonus
                    userGroupDao.getUserDao().scoreBonus(userId,loan);
                }
                else
                    Toasty.error(getContext(), "Loan is empty", Toast.LENGTH_SHORT).show();
                m_Text="";
                updateAll();
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

    public void payBack(final long userId, final long groupId){
        // validation. Does user wants to pay more than loan
        dept = userGroupDao.getUserLoan(userId, groupId);
        if (dept<1.0){
            Toasty.info(getContext(), "No loans", Toast.LENGTH_SHORT).show();
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("How much money do you want to pay back?");
        View viewInflated = LayoutInflater.from(getContext())
                .inflate(R.layout.popup_input_investment, (ViewGroup) getView(), false);
        final EditText input = (EditText) viewInflated.findViewById(R.id.input1);
        builder.setView(viewInflated);

        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.d("PAYBACK: ", "input: "+input.getText().toString());
//                m_Text = input.getText().toString();
                m_Text = input.getText().toString();
                if (!TextUtils.isEmpty(m_Text)) {
                    Double payBack=null;
                    try{
                        payBack=Double.parseDouble(m_Text);
                    }
                    catch (Exception e){}
                    // validation.
                    if (payBack==null){
                        Toasty.error(getContext(), "Cant payback", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    // validation. Does user has enough money
                    double userMoney = userGroupDao.getUserDao().getMoney(userId);
                    if (userMoney<payBack){
                        Log.d("PAYBACK: ", "userMoney: "+userMoney);
                        Log.d("PAYBACK: ", "payBack: "+payBack);
                        Toasty.error(getContext(), "Not enough money "+payBack, Toast.LENGTH_SHORT).show();
                        return;
                    }
                    // validation. Check if payBack more than user has to pay
                    if (payBack>dept){
                        Toasty.error(getContext(), "It is too much money bro, we need only "+dept, Toast.LENGTH_SHORT).show();
                        return;
                    }

                    /**
                     * All DB INTERACTIONS
                     * 1. User money=       -payback
                     * 2. User loan=        loan - payback
                     * 3. Group available=  +payback
                     * */

                    // 1
                    Log.d("PAYBACK: ", "1-----------------------------");
                    double userOldMoney = userGroupDao.getUserDao().getMoney(userId);
                    userGroupDao.getUserDao().setMoney(userId, userOldMoney-payBack);
                    double userNewMoney = userGroupDao.getUserDao().getMoney(userId);
                    Log.d("PAYBACK: ", "userOldMoney: "+userOldMoney);
                    Log.d("PAYBACK: ", "userNewMoney: "+userNewMoney);

                    // 2
                    Log.d("PAYBACK: ", "2-----------------------------");
                    double userLoanOld = userGroupDao.getUserLoan(userId,groupId);
                    userGroupDao.setUserLoan(userId,groupId,userLoanOld-payBack);
                    double userLoanNew = userGroupDao.getUserLoan(userId,groupId);
                    Log.d("PAYBACK: ", "userLoanOld: "+userLoanOld);
                    Log.d("PAYBACK: ", "userLoanNew: "+userLoanNew);

                    // 3
                    Log.d("PAYBACK: ", "3-----------------------------");
                    double groupAvailableOld = userGroupDao.getGroupDao().getAvailableMoney(groupId);
                    userGroupDao.getGroupDao().updateAvailableMoney(groupId, groupAvailableOld+payBack);
                    double groupAvailableNew = userGroupDao.getGroupDao().getAvailableMoney(groupId);
                    Log.d("PAYBACK: ", "groupAvailableOld: "+groupAvailableOld);
                    Log.d("PAYBACK: ", "groupAvailableNew: "+groupAvailableNew);

                    // score bonus
                    userGroupDao.getUserDao().scoreBonus(userId,payBack);
                }
                else
                    Toasty.error(getContext(), "Pay back is empty", Toast.LENGTH_SHORT).show();
                m_Text="";
                updateAll();
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

    public void withdraw(long userId, long groupId){

        //1 validation. Does user has loan
        dept = userGroupDao.getUserLoan(userId, groupId);
        if (dept>1.0){
            Toasty.info(getContext(), "You have loan you can not leave group", Toast.LENGTH_SHORT).show();
            return;
        }
        //2 validation. Does group has enough money to withdraw
        Double totalMoney = userGroupDao.getGroupDao().getTotalMoney(groupId);
        Double investment = userGroupDao.getUserInvestment(userId,groupId);
        Double allInvestments = userGroupDao.getAllInvestmentsInGroup(groupId);
        Double income = investment*totalMoney/allInvestments-investment;
        Double userMoneyFromGroup = investment+income;
        Double groupAvailable = userGroupDao.getGroupDao().getAvailableMoney(groupId);
        Log.d("WITHDRAW>", "_________________________");
        Log.d("WITHDRAW>", "investment: "+investment);
        Log.d("WITHDRAW>", "allInvestments: "+allInvestments);
        Log.d("WITHDRAW>", "income: "+income);
        Log.d("WITHDRAW>", "userMoneyFromGroup: "+userMoneyFromGroup);
        Log.d("WITHDRAW>", "groupAvailable"+groupAvailable);
        if (userMoneyFromGroup>groupAvailable){
            Toasty.info(getContext(), "Group has no available money to withdraw "+userMoneyFromGroup, Toast.LENGTH_SHORT).show();
            return;
        }

        /**
         * All DB INTERACTIONS
         * 1. User money=               +investment +income
         * 2. Group totalMoney=         -investment -income
         * 3. Group availableMoney=     -investment -income
         * 4. UserGroup delete raw in db
         * */

        // 1
        double uMoneyOld = userGroupDao.getUserDao().getMoney(userId);
        userGroupDao.getUserDao().setMoney(userId, uMoneyOld+userMoneyFromGroup);
        double uNewMoney = userGroupDao.getUserDao().getMoney(userId);
        Log.d("WITHDRAW>", "1_________________________");
        Log.d("WITHDRAW>", "uMoneyOld: "+uMoneyOld);
        Log.d("WITHDRAW>", "userMoneyFromGroup: "+userMoneyFromGroup);
        Log.d("WITHDRAW>", "uNewMoney: "+uNewMoney);

        // 2
        double gTotalMoneyOld = userGroupDao.getGroupDao().getTotalMoney(groupId);
        userGroupDao.getGroupDao().updateTotalMoney(groupId, gTotalMoneyOld-userMoneyFromGroup);
        double gTotalMoneyNew = userGroupDao.getGroupDao().getTotalMoney(groupId);
        Log.d("WITHDRAW>", "2_________________________");
        Log.d("WITHDRAW>", "gTotalMoneyOld: "+gTotalMoneyOld);
        Log.d("WITHDRAW>", "gTotalMoneyNew: "+gTotalMoneyNew);

        // 3
        double gAvailableMoneyOld = userGroupDao.getGroupDao().getAvailableMoney(groupId);
        userGroupDao.getGroupDao().updateAvailableMoney(groupId, gAvailableMoneyOld-userMoneyFromGroup);
        double gAvailableMoneyNew = userGroupDao.getGroupDao().getAvailableMoney(groupId);
        Log.d("WITHDRAW>", "3_________________________");
        Log.d("WITHDRAW>", "gAvailableMoneyOld: "+gAvailableMoneyOld);
        Log.d("WITHDRAW>", "gAvailableMoneyNew: "+gAvailableMoneyNew);

        // 4
        int rawDeleted = userGroupDao.deleteRaw(userId,groupId);
        Log.d("WITHDRAW>", "4_________________________");
        Log.d("WITHDRAW>", "rawDeleted: "+rawDeleted);

        Toasty.success(getContext(), "Withdraw all your money bro", Toast.LENGTH_SHORT).show();
        goToGroupList();
    }

    private void invite(final long senderId, final long groupId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Enter username of person to invite in current group");
        View viewInflated = LayoutInflater.from(getContext())
                .inflate(R.layout.popup_input_group_name, (ViewGroup) getView(), false);
        final EditText input = (EditText) viewInflated.findViewById(R.id.input1);
        builder.setView(viewInflated);

        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                m_Text = input.getText().toString();
                if (!TextUtils.isEmpty(m_Text)) {
                    User user = userGroupDao.getUserDao().getUserByUserName(m_Text);
                    // validation if user eist
                    if (user==null){
                        Toasty.error(getContext(), "User do not exist", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // validation if user already in group
                    List<User> usersInThisGroup = userGroupDao.getUsersFromGroup(groupId);
                    for (User u:usersInThisGroup){
                        if (u.getId()==user.getId()){
                            Toasty.info(getContext(), "This user is in this group already",
                                         Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }

                    // validation if invitation has already been send
                    Invitation invitation = invitationDao
                            .getInvitation(senderId, user.getId(), groupId);
                    if (invitation!=null){
                        Toasty.info(getContext(), "You have already send invitation to user with username: "+user.getUserName(),
                                    Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // TODO: validate for is User already invited to this group | find by (receivedId, groupId)

                    // sending invitation
                    long inviteId = invitationDao.createInvitation(senderId, user.getId(), groupId);
                    Log.d("SENDING.I", "senderId: "+senderId + " user.getId(): "+user.getId()+" groupId "+groupId);

                    if (inviteId==-1){
                        Toasty.error(getContext(), "Something goes wrong",
                                    Toast.LENGTH_SHORT).show();
                        return;
                    }
                    else {
                        Toasty.success(getContext(), "Invitation has been send to Person: "+user.getFullName(),
                                    Toast.LENGTH_SHORT).show();
                    }

                }
                else
                    Toasty.error(getContext(), "The field is empty", Toast.LENGTH_SHORT).show();
                m_Text="";
//                updateAll();
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



}
