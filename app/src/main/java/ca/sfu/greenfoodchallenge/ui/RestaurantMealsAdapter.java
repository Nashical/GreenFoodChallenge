package ca.sfu.greenfoodchallenge.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;

import java.util.List;

import ca.sfu.greenfoodchallenge.meal.Meal;

/**RestaurantMealsAdapter loads clickable images with their names from a list of meals.
 */
class RestaurantMealsAdapter extends RecyclerView.Adapter<RestaurantMealsAdapter.RestaurantViewHolder>{

    private List<Meal> mealList;
    private Context context;

    public static class RestaurantViewHolder extends RecyclerView.ViewHolder {

        ImageView mealImage;
        Button mealName;

        public RestaurantViewHolder(View view) {
            super(view);
            mealImage = (ImageView) view.findViewById(R.id.restaurant_meal_image);
            mealName = (Button) view.findViewById(R.id.restaurant_meal_name);
        }
    }

    public RestaurantMealsAdapter(Context context, List<Meal> meals) {
        mealList = meals;
        this.context = context;
    }

    @NonNull
    @Override
    public RestaurantMealsAdapter.RestaurantViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.restaurant_meal_view, viewGroup, false);

        RestaurantViewHolder viewHolder = new RestaurantViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RestaurantViewHolder restaurantViewHolder, int i) {

        Picasso.with(context)
                .load(mealList.get(i).getImageUrl())
                .placeholder(R.drawable.wallpaper)
                .fit()
                .centerCrop()
                .error(R.drawable.ic_error_red_24dp)
                .into(restaurantViewHolder.mealImage);

        restaurantViewHolder.mealImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();

                //serialize to load into bundle
                Gson gson = new GsonBuilder().setDateFormat("EEE MMM dd HH:mm:ss z yyyy").create();
                String json = gson.toJson(mealList.get(i)); //Convert object to string

                bundle.putBoolean(MealViewer.NEW_MEAL,false);
                bundle.putString(MealViewer.FOREIGN_MEAL, json);
                MealViewer mv = new MealViewer();
                Intent intent = new Intent(context, MealViewer.class);
                intent.putExtras(bundle);
                context.startActivity(intent);

            }
        });

        restaurantViewHolder.mealName.setText(mealList.get(i).getMealName());

    }

    @Override
    public int getItemCount() {
        return mealList.size();
    }

    public void setMealList(List<Meal> mealList) {
        this.mealList = mealList;
    }
}
