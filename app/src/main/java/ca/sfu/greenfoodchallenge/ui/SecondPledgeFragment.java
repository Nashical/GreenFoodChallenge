package ca.sfu.greenfoodchallenge.ui;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import java.util.List;

import ca.sfu.greenfoodchallenge.database.GreenFoodChallengeDatabase;
import ca.sfu.greenfoodchallenge.database.User;

/**SecondPledgeFragment displays all users with pledges for a subset of municipalities.
 */
public class SecondPledgeFragment extends Fragment {

    private ListView pledges;
    private Spinner municipality;
    private String chosen;
    private PledgeAdapter pledgeAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_second_pledge, container, false);

        municipality = (Spinner) view.findViewById(R.id.municipality_filter);
        populateDropdown();
        pledges = (ListView) view.findViewById(R.id.list_view_pledges);

        municipality.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0) {
                    loadDatabase(GreenFoodChallengeDatabase.getAllUsersWithPledge());

                } else {
                    chosen = parent.getItemAtPosition(position).toString();
                    loadDatabase(GreenFoodChallengeDatabase.getUsersWithPledgeByMunicipality(chosen));                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                loadDatabase(GreenFoodChallengeDatabase.getAllUsersWithPledge());
            }
        });


        return view;
    }

    private void populateDropdown() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.
                createFromResource(getActivity(), R.array.municipalities_array, R.layout.support_simple_spinner_dropdown_item);

        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        municipality.setAdapter(adapter);
    }

    private void loadDatabase(List<User> userList) {
        pledgeAdapter = new PledgeAdapter(userList);
        pledges.setAdapter(pledgeAdapter);
    }

}
