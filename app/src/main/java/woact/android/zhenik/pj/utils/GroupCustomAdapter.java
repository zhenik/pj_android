package woact.android.zhenik.pj.utils;

import android.content.ClipData;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import woact.android.zhenik.pj.R;
import woact.android.zhenik.pj.model.Group;


public class GroupCustomAdapter extends ArrayAdapter<Group> {

    private List<Group> groups;

    public GroupCustomAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    public GroupCustomAdapter(Context context, int resource, List<Group> items) {
        super(context, resource, items);
        groups=items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.item, null);
        }
        Group group = getGroup(position);
        if (group != null) {
            TextView tt1 = (TextView) v.findViewById(R.id.item_group_id);
            TextView tt2 = (TextView) v.findViewById(R.id.item_group_name);

            if (tt1 != null) {tt1.setText(String.valueOf(group.getId())); }
            if (tt2 != null) {tt2.setText(group.getGroupName());}
        }
        return v;
    }

//     элемент по позиции

    public Group getGroup(int position) {
        return ((Group)getItem(position));
    }

    @Override
    public int getCount() {
        return groups.size();
    }

    // id по позиции
    @Override
    public long getItemId(int position) {
        return position;
    }
}
