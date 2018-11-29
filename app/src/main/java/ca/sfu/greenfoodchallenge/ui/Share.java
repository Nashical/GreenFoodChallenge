package ca.sfu.greenfoodchallenge.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.DefaultLogger;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterAuthToken;
import com.twitter.sdk.android.core.TwitterConfig;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterAuthClient;
import com.twitter.sdk.android.tweetcomposer.TweetComposer;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import ca.sfu.greenfoodchallenge.database.GreenFoodChallengeDatabase;
import ca.sfu.greenfoodchallenge.meal.MealCalendar;
import ca.sfu.greenfoodchallenge.meal.Period;
import ca.sfu.greenfoodchallenge.meal.Recommendation;

public class Share extends Fragment {

    TwitterAuthClient mTwitterAuthClient;

    NumberFormat value = new DecimalFormat("#0.0");


    public static final String TAG = "Share";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        getFragmentManager().beginTransaction().add(R.id.fragment_container, this, TAG);

        TwitterConfig config = new TwitterConfig.Builder(getActivity())
                .logger(new DefaultLogger(Log.DEBUG))
                .twitterAuthConfig(new TwitterAuthConfig("BtXiVYYjMorEVhMmgY8U1cHup ", "kkbh6CMnj7isVAonmsKD9LkkHT1EjjYIauPKhE7YiJeiPUAagI "))
                .debug(true)
                .build();
        Twitter.initialize(config);

        View view = inflater.inflate(R.layout.fragment_share, container, false);


        MealCalendar mc = GreenFoodChallengeDatabase.getCurrentUser().getMealCalendar();
        Period P = new Period(mc);
        Recommendation recommendation = new Recommendation(P,Recommendation.BALANCED_PLAN);

        double carbon = recommendation.currentYearlyCarbon();

        mTwitterAuthClient = new TwitterAuthClient();

        Button twitter_custom_button = view.findViewById(R.id.twitterButton);
        twitter_custom_button.setOnClickListener((View v) -> {

            mTwitterAuthClient.authorize(getActivity(), new Callback<TwitterSession>() {

                @Override
                public void success(Result<TwitterSession> result) {

                    TwitterSession session = result.data;
                    TwitterAuthToken authToken = session.getAuthToken();
                    String token = authToken.token;
                    String secret = authToken.secret;

                    /*
                    Uri imageUri = FileProvider.getUriForFile(MainActivity.this,
                    "com.package_name" + ".file_provider",
                    new File("/path/to/image"));
                    */

                    TweetComposer.Builder builder = new TweetComposer.Builder(getActivity())
                            .text("I am currently saving: " + value.format(carbon) + " kg of CO2 emissions yearly!\n\n" +
                                    "Come join me in the #greenfoodchallenge!\n");
                    //.image(imageUri);//sharing image uri
                    builder.show();
                }

                @Override
                public void failure(TwitterException e) {
                    FailedPopUp D = new FailedPopUp();
                    D.getFragmentManager();
                }
            });
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int responseCode, Intent intent) {
        mTwitterAuthClient.onActivityResult(requestCode, responseCode, intent);
    }
}
