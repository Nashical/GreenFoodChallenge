package ca.sfu.greenfoodchallenge.ui;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.List;

import ca.sfu.greenfoodchallenge.database.User;

public class PledgeAdapter extends BaseAdapter{

    List<User> users;


    public PledgeAdapter(List<User> input) {
        users = input;
    }

    @Override
    public int getCount() {
        return users.size();
    }

    @Override
    public Object getItem(int position) {
        return users.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int i, View v, ViewGroup parent) {

        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_pledge, null);
        ImageView userIcon = (ImageView) v.findViewById(R.id.user_icon_pledge);
        TextView userName = (TextView) v.findViewById(R.id.user_name);
        TextView municipalityUser = (TextView) v.findViewById(R.id.municipality_pledge);
        TextView pledgeAmount = (TextView) v.findViewById(R.id.user_pledge);

        setIcon(users.get(i).getPhotoUrl(), userIcon);
        userName.setText(users.get(i).getFirstName() + " " + users.get(i).getLastNameInitial());
        municipalityUser.setText(users.get(i).getMunicipality());
        pledgeAmount.setText(users.get(i).getCo2PledgeAmount() + " kg CO2e");

        return v;
    }

    public void setIcon(String iconURL, ImageView userIcon) {
        Uri uri;
        try {
            uri = Uri.parse(iconURL);
            userIcon.setImageURI(uri);
        } catch (Exception e){
            e.getMessage();
            userIcon.setImageResource(R.mipmap.ic_app_round);
        }

    }
}
