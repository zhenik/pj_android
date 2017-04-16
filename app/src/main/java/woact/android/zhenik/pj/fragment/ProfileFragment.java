package woact.android.zhenik.pj.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;

import woact.android.zhenik.pj.R;
import woact.android.zhenik.pj.model.User;


public class ProfileFragment extends Fragment {
    public static final String TAG = "###ProfileFragment:> ";

    private TextView name;
    private TextView userTelephone;
    private TextView userScore;
    private TextView userName;
    private TextView email;
    private Switch isDnbCustomer;
    private View view;
    private User user;


    public ProfileFragment() {
        Log.i(TAG, "constructor");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        btn = (Button) getActivity().findViewById(R.id.button2);
        Log.i(TAG, "onCreateView");
        return this.view=inflater.inflate(R.layout.profile_fragment, null);
    }

}
