package ca.sfu.greenfoodchallenge.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import ca.sfu.greenfoodchallenge.database.GreenFoodChallengeDatabase;
import ca.sfu.greenfoodchallenge.database.MealsSearch;
import ca.sfu.greenfoodchallenge.meal.Meal;
import java.util.List;

/*The RestaurantMealsFragment provides meal images from all users which can be filtered.
 */
public class RestaurantMealsFragment extends Fragment {

    private final String TAG = "RestaurantMealsFragment";
    private TextInputEditText searchText;
    private Spinner municipalitySpinner;
    private ArrayAdapter<CharSequence> municipalityAdapter;
    private Spinner proteinSpinner;
    private ArrayAdapter<CharSequence> proteinAdapter;
    private RecyclerView restaurantMeals;
    private RecyclerView.LayoutManager mealsLayout;
    private RecyclerView.Adapter restaurantMealsAdapter;
    private String protein = null;
    private String munipality = null;
    private String searchTerm = null;
    private MealsSearch mealsSearch;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_restaurant_meals, container, false);

        searchText = (TextInputEditText) view.findViewById(R.id.search_text);

        proteinSpinner = (Spinner) view.findViewById(R.id.protein_spinner);
        municipalitySpinner = (Spinner) view.findViewById(R.id.municipality_restaurant_spinner);

        loadSpinnerAdapters();

        restaurantMeals = (RecyclerView) view.findViewById(R.id.restaurant_recycler_view);

        mealsLayout = new GridLayoutManager(this.getContext(), 1);
        restaurantMeals.setLayoutManager(mealsLayout);
        setRestaurantMealsAdapter();

        searchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                searchTerm = searchText.getText().toString().trim();

                if(searchTerm != "" && searchTerm != null) {
                    proteinSpinner.setSelection(0);
                    municipalitySpinner.setSelection(0);

                    setRestaurantMealsAdapter(mealsSearch.fullText(searchTerm));
                }

                return false;
            }
        });

        proteinSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0) {
                    protein = null;
                    Log.d(TAG,"No protein selected");
                    setRestaurantMealsAdapter();
                    //No protein selected -- update query for no protein
                } else {
                    Log.d(TAG,protein + " selected");
                    municipalitySpinner.setSelection(0);
                    protein = parent.getItemAtPosition(position).toString();
                    setRestaurantMealsAdapter(mealsSearch.byMainProtein(protein));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                protein = null;
                setRestaurantMealsAdapter();
            }
        });

        municipalitySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0) {
                    Log.d(TAG,"No municipality selected.");
                    munipality = null;
                    setRestaurantMealsAdapter();
                    //No municipality selected
                } else {
                    proteinSpinner.setSelection(0);
                    munipality = parent.getItemAtPosition(position).toString();
                    Log.d(TAG,munipality + "selected");
                    setRestaurantMealsAdapter(mealsSearch.byMunicipality(position));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                munipality = null;
                setRestaurantMealsAdapter();
            }
        });

        return view;
    }

    public void loadSpinnerAdapters() {
        municipalityAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.municipalities_array,
                R.layout.support_simple_spinner_dropdown_item);
        municipalityAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        municipalitySpinner.setAdapter(municipalityAdapter);
        proteinAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.proteins_array, R.layout.support_simple_spinner_dropdown_item);
        proteinAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        proteinSpinner.setAdapter(proteinAdapter);
    }

    public void setRestaurantMealsAdapter() {
        mealsSearch = new MealsSearch(GreenFoodChallengeDatabase.getAllUsers());
        restaurantMealsAdapter = new RestaurantMealsAdapter(getActivity(),mealsSearch.byNoFilter());
        restaurantMeals.setAdapter(restaurantMealsAdapter);
    }

    public void setRestaurantMealsAdapter(List<Meal> mealList) {
        restaurantMealsAdapter = new RestaurantMealsAdapter(getContext(), mealList);
        restaurantMeals.setAdapter(restaurantMealsAdapter);
    }
}