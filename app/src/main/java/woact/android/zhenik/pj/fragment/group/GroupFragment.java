package woact.android.zhenik.pj.fragment.group;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import woact.android.zhenik.pj.R;


public class GroupFragment extends Fragment {
    public static final String TAG = "###GroupFragment:> ";

    private View view;

    public GroupFragment() {
        Log.i(TAG, "constructor");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        btn = (Button) getActivity().findViewById(R.id.button2);
        Log.i(TAG, "onCreateView");
        return this.view=inflater.inflate(R.layout.group_fragment, null);
    }
}
