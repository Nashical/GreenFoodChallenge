package ca.sfu.greenfoodchallenge.ui;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;

import ca.sfu.greenfoodchallenge.database.GreenFoodChallengeDatabase;
import ca.sfu.greenfoodchallenge.database.User;

/*Displays information about user's pledge as well as stats
  about Vancouver's accumulative pledge standing
 */

public class PledgeFragment extends Fragment {

    public static final int KM_EQUIV = 0;
    public static final int COAL_EQUIV = 1;
    public static final int HOME_EQUIV = 2;
    public static final int TREE_EQUIV = 3;

    String userIcon;
    String userName;
    String userPledge;
    String numPledges;
    String avgPledge;
    double totalCO2;
    String totalText;
    double[] pledgeEquivalence = new double[4];

    NumberFormat value = new DecimalFormat("#0.0");



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pledge, container, false);

        User user = GreenFoodChallengeDatabase.getCurrentUser();
        List<User> usersWithPledge = GreenFoodChallengeDatabase.getAllUsersWithPledge();


        //Create user specific strings
        userIcon = user.getPhotoUrl();
        userName = " " + user.getUserName();
        userPledge = " " + value.format(user.getCo2PledgeAmount()) + " kg";
        numPledges = " " + String.valueOf(User.totalPledges(usersWithPledge));
        avgPledge = " " + value.format(User.averagePledgeCarbon(usersWithPledge)) + " kg";
        totalCO2 = User.totalPledgeCarbon(usersWithPledge) / 1000.0; //tonnes
        totalText = " " + value.format(totalCO2) + " tonnes";
        pledgeEquivalence = User.pledgeEquivalence(totalCO2);

        String[] equivText = new String[]{
                value.format(pledgeEquivalence[KM_EQUIV]) + " ",
                value.format(pledgeEquivalence[COAL_EQUIV]) + " ",
                value.format(pledgeEquivalence[HOME_EQUIV]) + " ",
                value.format(pledgeEquivalence[TREE_EQUIV]) + " "
        };


        //Load user's selected profile icon
        ImageView userIconView = view.findViewById(R.id.user_icon);

        if(!userIcon.equals("none")) {
            Uri icon = Uri.parse(userIcon);
            userIconView.setImageURI(icon);
        }

        //Instantiate text views
        TextView usernameView = textViewFinder(view, R.id.username);
        TextView userPledgeView = textViewFinder(view, R.id.user_pledge);
        TextView numPledgesView = textViewFinder(view, R.id.total_pledges);
        TextView avgPledgeView = textViewFinder(view, R.id.avg_pledges);
        TextView totalPledgeView = textViewFinder(view, R.id.total_co2);
        TextView kmEquivView = textViewFinder(view, R.id.km_equiv);
        TextView coalEquivView = textViewFinder(view, R.id.coal_equiv);
        TextView homeEquivView = textViewFinder(view, R.id.home_equiv);
        TextView treesEquivView = textViewFinder(view, R.id.tree_equiv);


        //Edit page text
        usernameView.setText(getActivity().getString(R.string.welcome_username) + userName);
        userPledgeView.setText(getActivity().getString(R.string.you_have_pledged) + userPledge);
        numPledgesView.setText(getActivity().getString(R.string.number_of_pledges) + numPledges);
        avgPledgeView.setText(getActivity().getString(R.string.average_pledge) + avgPledge);
        totalPledgeView.setText(getActivity().getString(R.string.total_co2e_pledged) + totalText);
        kmEquivView.setText(equivText[KM_EQUIV] + getActivity().getString(R.string.x_km_driven_in_a_car));
        coalEquivView.setText(equivText[COAL_EQUIV] + getActivity().getString(R.string.x_lb_of_coal_burned));
        homeEquivView.setText(equivText[HOME_EQUIV] + getActivity().getString(R.string.x_homes_powered_per_year));
        treesEquivView.setText(equivText[TREE_EQUIV] + getActivity().getString(R.string.x_trees_planted));

        return view;
    }

    //textView helper
    private TextView textViewFinder(View view, int textId) {
        TextView text = view.findViewById(textId);
        return text;
    }
}
