package woact.android.zhenik.pj.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import woact.android.zhenik.pj.R;
import woact.android.zhenik.pj.model.Invitation;



public class InvitationCustomAdapter extends ArrayAdapter<Invitation> {
    private List<Invitation> invitations;

    public InvitationCustomAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    public InvitationCustomAdapter(Context context, int resource, List<Invitation> items) {
        super(context, resource, items);
        invitations=items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.item_invitation, null);
        }
        Invitation invitation = getInvitation(position);
        if (invitation != null) {
            TextView tt1 = (TextView) v.findViewById(R.id.item_invitation_from);
            TextView tt2 = (TextView) v.findViewById(R.id.item_invitation_group);

            if (tt1 != null) {tt1.setText(String.valueOf(invitation.getSendBy().getFullName()));}
            if (tt2 != null) {tt2.setText(invitation.getGroup().getGroupName());}

        }
        return v;
    }

    //     элемент по позиции
    public Invitation getInvitation(int position) {
        return ((Invitation)getItem(position));
    }

    @Override
    public int getCount() {
        return invitations.size();
    }

    // id по позиции
    @Override
    public long getItemId(int position) {
        return position;
    }
}
