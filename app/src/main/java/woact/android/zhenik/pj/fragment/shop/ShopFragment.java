package woact.android.zhenik.pj.fragment.shop;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import woact.android.zhenik.pj.MainActivity;
import woact.android.zhenik.pj.R;
import woact.android.zhenik.pj.model.User;

public class ShopFragment extends Fragment {
    public static final String TAG = "###ShopFragment:> ";


    private View view;
    private User user;


    public ShopFragment() {
        Log.i(TAG, "constructor");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        btn = (Button) getActivity().findViewById(R.id.button2);
        Log.i(TAG, "onCreateView");
        this.view=inflater.inflate(R.layout.shop_fragment, null);
        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public void onResume() {
        ((MainActivity) getActivity()).getSupportActionBar().setTitle("Shop");
        super.onResume();
    }
}
