package ca.sfu.greenfoodchallenge.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import ca.sfu.greenfoodchallenge.meal.Recommendation;

public class RecomendationPlanChooserPopUp extends DialogFragment {


    Fragment selectedFragment = null;
    Button Meat, Balance, Veggies;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_rec_select, container, false);


        GreenFoodChallengeApplication app = (GreenFoodChallengeApplication)
                getActivity().getApplication();




        Meat = view.findViewById(R.id.MeatEater);
        Balance = view.findViewById(R.id.Balanced);
        Veggies = view.findViewById(R.id.Vegetarian);

        Meat.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                RecommendationFragment.plan = Recommendation.MEAT_EATER_PLAN;
                updateRecomendationFragment();
                getDialog().dismiss();
            }
        });

        Balance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecommendationFragment.plan = Recommendation.BALANCED_PLAN;
                updateRecomendationFragment();
                getDialog().dismiss();
            }

        });

        Veggies.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                RecommendationFragment.plan = Recommendation.VEGETARIAN_PLAN;
                updateRecomendationFragment();
                getDialog().dismiss();
            }
        });

        return view;
    }


    private void updateRecomendationFragment(){
        RecommendationFragment recommendationFragment
                = (RecommendationFragment)getFragmentManager().findFragmentByTag(RecommendationFragment.TAG);

        //Redraw the fragment
        getFragmentManager().beginTransaction()
                .detach(recommendationFragment)
                .attach(recommendationFragment)
                .commit();
    }

}