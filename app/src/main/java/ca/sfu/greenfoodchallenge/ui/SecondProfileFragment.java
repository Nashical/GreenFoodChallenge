package ca.sfu.greenfoodchallenge.ui;

import static ca.sfu.greenfoodchallenge.database.User.NONE;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import ca.sfu.greenfoodchallenge.database.GreenFoodChallengeDatabase;
import ca.sfu.greenfoodchallenge.database.User;


/*A user can set their pledge, profile and icon on this page and also delete their pledge.
 *The profile consists of :
 * Required Field(s): pledge amount of CO2e in tonnes
 * Optional Fields: first name, last initial, municipality, icon image
 */
public class SecondProfileFragment extends Fragment implements UserIcons.EditIconListener {

    private String iconURL = getURL(R.mipmap.ic_app_round);
    int USER_ICON_VIEW_ID = R.mipmap.ic_app_round;
    private Button saveProfile;
    private Button deleteProfile;
    private ImageView userIcon;
    private TextInputEditText firstName, lastInitial, pledge;
    private Spinner municipality;
    private String firstNameText, lastInitialText, municipalityText;
    private double pledgeAmount;
    private User user;
    private String PREFIX_PATH = "android.resource://ca.sfu.greenfoodchallenge.ui/";


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_second_profile, container, false);

        userIcon = (ImageView) view.findViewById(R.id.user_icon);
        firstName = (TextInputEditText) view.findViewById(R.id.first_name);
        lastInitial = (TextInputEditText) view.findViewById(R.id.lastInitial);
        municipality = (Spinner) view.findViewById(R.id.municipality);
        populateDropdown();

        pledge = (TextInputEditText) view.findViewById(R.id.pledge_amount);
        saveProfile = (Button) view.findViewById(R.id.save_profile);
        deleteProfile = (Button) view.findViewById(R.id.delete_profile);

        user = GreenFoodChallengeDatabase.getCurrentUser();
        loadUser();

        userIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showIconDialog();
            }
        });

        municipality.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0) {
                    municipalityText = user.getMunicipality(); //no municipality selected
                }
                else {
                    municipalityText = parent.getItemAtPosition(position).toString();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                municipalityText = user.getMunicipality();
            }
        });

        saveProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Save the profile
                getText();
                if(validatePledge()) {
                    user.setFirstName(firstNameText);
                    user.setLastNameInitial(lastInitialText);
                    user.setMunicipality(municipalityText);
                    user.setPhotoUrl(iconURL);
                    user.setCo2PledgeAmount(pledgeAmount);

                    GreenFoodChallengeDatabase.updateCurrentUser();
                    Toast.makeText(getActivity(), getText(R.string.saved), Toast.LENGTH_SHORT).show();
                    //changeFragment();
                }

            }
        });

        deleteProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //remove the user's pledge
                user.setCo2PledgeAmount(0);

                GreenFoodChallengeDatabase.updateCurrentUser(); //update database
                Toast.makeText(getActivity(), getText(R.string.deleted), Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    //Populate municipality dropdown menu
    private void populateDropdown() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.
                createFromResource(getActivity(), R.array.municipalities_array,
                        R.layout.support_simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        municipality.setAdapter(adapter);
    }

    private void showIconDialog() {
        FragmentManager fm = getFragmentManager();
        UserIcons dialog = new UserIcons();
        dialog.setStyle(R.style.ThemeOverlay_AppCompat, R.style.AppTheme);
        dialog.setTargetFragment(SecondProfileFragment.this, 0);
        dialog.show(getFragmentManager(),"Display user icons");
    }

    @Override
    public void onFinishDialog(int id) {
        userIcon.setImageResource(id);
        USER_ICON_VIEW_ID = id;
        iconURL =getURL(USER_ICON_VIEW_ID);
    }

    private String getURL(int id) {
        return PREFIX_PATH + id;
    }

    public void loadUser() {

        if(!user.getPhotoUrl().equals(NONE)) {
            iconURL = user.getPhotoUrl();
            Uri uri = Uri.parse(iconURL);
            userIcon.setImageURI(uri);
        }

        firstNameText = user.getFirstName().equals("none") ? "" : user.getFirstName();
        firstName.setText(firstNameText);

        lastInitialText = user.getLastNameInitial().equals("none")
                || user.getLastNameInitial().equals("n") ? "" : user.getLastNameInitial();
        lastInitial.setText(lastInitialText);

        municipalityText = user.getMunicipality();

        pledgeAmount = user.getCo2PledgeAmount();
        pledge.setText("" + pledgeAmount);
    }

    public void getText() {

        firstNameText = firstName.getText().toString().trim();
        lastInitialText = lastInitial.getText().toString();

        if(!pledge.getText().toString().isEmpty()){
            pledgeAmount = Double.parseDouble(pledge.getText().toString());
        } else {
            pledge.setText(null);
        }
    }

    private boolean validatePledge() {
        if(pledgeAmount <= 0) {
            pledge.setError(getString(R.string.non_zero_number));
            return false;
        } else {
            pledge.setError(null);
            return true;
        }
    }

}
