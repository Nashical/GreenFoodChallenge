package ca.sfu.greenfoodchallenge.ui;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;

import android.util.Log;
import android.view.KeyEvent;

import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import ca.sfu.greenfoodchallenge.database.GreenFoodChallengeDatabase;
import ca.sfu.greenfoodchallenge.database.Upload;
import ca.sfu.greenfoodchallenge.meal.Day;
import ca.sfu.greenfoodchallenge.meal.DefaultProteins;
import ca.sfu.greenfoodchallenge.meal.Meal;
import ca.sfu.greenfoodchallenge.meal.MealCalendar;
import ca.sfu.greenfoodchallenge.meal.MealComponent;

/*This class allows users to edit and view meals and their components.
 */
public class MealViewer extends AppCompatActivity {

    //----------------------------------------------------------------------------------------------
    // Variable declaration
    //----------------------------------------------------------------------------------------------



    //Constants

    //Intent Parameter
    final static String FOREIGN_MEAL = "FOREIGN_MEAL";
    final static String NEW_MEAL = "NEW_MEAL";
    final static String SELECTED_DATE = "SELECTED_DAY";
    final static String SELECTED_MEAL = "SELECTED_MEAL";


    //User values
    final static String UNIT_NAME = "g";
    final static String NON_EMPTY_SPACE = "";
    final static String EMPTY_SPACE = " ";
    final static int NUMBER_SCALES_HUNDRED = 100;
    final static int NUMBER_SCALES_TEN = 10;


    private int munipality = 0;

    private Button Save;
    private Button Back;
    private Button Delete;
    private CheckBox shareable;
    private EditText restaurant;
    private EditText itemDesc;

    private Spinner municipalitySpinner;
    private ArrayAdapter<CharSequence> municipalityAdapter;

    //Add variables for image loading
    private ImageButton Change;
    private ProgressBar mProgressBar;
    private ImageView mImageView;
    private Uri mImageUri;
    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef;
    private UploadTask mUploadTask;

    private String selectedDate;
    private Meal selectedMeal;
    private String oldMealName = null;
    private boolean frezeScrollBars;

    private EditText mealNameTextbox;


    private boolean loadedFromMeal;


    private List<TextView> textViews = new ArrayList<>();
    //TextView BeefView, PorkView, ChickenView, FishView, EggView, BeansView, VeggiesView;
    private List<SeekBar> seekBars = new ArrayList<>();
    //SeekBar beef, pork, chicken, fish, egg, beans, veggies;

    // Due to a mismatch across the app in Fragment versions we can't use the fragment manager
    // between CalendareFragment and here
    static CalendarFragment calendarFragment;

    //----------------------------------------------------------------------------------------------
    // Oncreate function
    //----------------------------------------------------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.meal_view);


        // Initialize the UI
        Save = findViewById(R.id.save_meal);
        Back = findViewById(R.id.back);
        Delete = findViewById(R.id.delete);
        shareable = findViewById(R.id.Share);
        mealNameTextbox = findViewById(R.id.MealInput);
        restaurant = findViewById(R.id.ResturantName);
        itemDesc = findViewById(R.id.MealDesc);
        municipalitySpinner = (Spinner) findViewById(R.id.mun);

        Change = findViewById(R.id.change_pic);
        mProgressBar = findViewById(R.id.progress_bar);
        mImageView = findViewById(R.id.image_view_upload2);

        mStorageRef = FirebaseStorage.getInstance().getReference("uploads");
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("uploads");

        //IMPORTANT: thses have to follow the order in the deafult protein list
        seekBars.add((SeekBar)findViewById(R.id.Beef));
        seekBars.add((SeekBar)findViewById(R.id.Pork));
        seekBars.add((SeekBar)findViewById(R.id.Chicken));
        seekBars.add((SeekBar)findViewById(R.id.Fish));
        seekBars.add((SeekBar)findViewById(R.id.Egg));
        seekBars.add((SeekBar)findViewById(R.id.Beans));
        seekBars.add((SeekBar)findViewById(R.id.Veggies));

        textViews.add((TextView)findViewById(R.id.BeefDisplay));
        textViews.add((TextView)findViewById(R.id.PorkDisplay));
        textViews.add((TextView)findViewById(R.id.ChickenDisplay));
        textViews.add((TextView)findViewById(R.id.FishDisplay));
        textViews.add((TextView)findViewById(R.id.EggDisplay));
        textViews.add((TextView)findViewById(R.id.BeanDisplay));
        textViews.add((TextView)findViewById(R.id.VeggieDisplay));



        Change = findViewById(R.id.change_pic);
        mProgressBar = findViewById(R.id.progress_bar);
        mImageView = findViewById(R.id.image_view_upload2);

        frezeScrollBars = false;



        // Initialize the selected meal
            Bundle args = getIntent().getExtras();
            loadedFromMeal = false;

            if(args.getBoolean(NEW_MEAL)){
                selectedMeal = new Meal("");
                oldMealName = null;
                mealNameTextbox.setHint("Please enter name here");
                selectedDate = (String) args.get(SELECTED_DATE);

            }
            else if(args.getString(FOREIGN_MEAL) != null) {
                loadedFromMeal = true;
                String json = (String) args.getString(FOREIGN_MEAL);
                Gson gson = new GsonBuilder().setDateFormat("EEE MMM dd HH:mm:ss z yyyy").create();
                Meal meal = gson.fromJson(json, Meal.class);
                selectedMeal = meal;
                oldMealName = selectedMeal.getMealName();
                fillFields();
                freezeFields();
                frezeScrollBars = true;
            }
            else {
                selectedDate = (String) args.get(SELECTED_DATE);
                selectedMeal = GreenFoodChallengeDatabase
                        .getCurrentUser()
                        .getMealCalendar()
                        .getDay(selectedDate)
                        .getMeal((String) args.get(SELECTED_MEAL));
                oldMealName = selectedMeal.getMealName();
                mealNameTextbox.setText(selectedMeal.getMealName(), TextView.BufferType.EDITABLE);
                fillFields();
            }


        // Initialize UI items


        municipalityAdapter = ArrayAdapter.createFromResource(this, R.array.municipalities_array,
                R.layout.support_simple_spinner_dropdown_item);
        municipalityAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        municipalitySpinner.setAdapter(municipalityAdapter);


        municipalitySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0) {
                    Log.d("TAG",getString(R.string.no_municipality_selected));
                    munipality = 0;
                } else {
                    munipality = position;
                    Log.d("TAG",munipality + " selected");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                munipality = 0;
            }
        });

        if(selectedMeal.getImageUrl()!= null) {
            Picasso.with(this).load(selectedMeal.getImageUrl()).into(mImageView);
        }

        //Manage the status bars
        for (int i = 0; i < DefaultProteins.NUMBER_OF_PROTEINS; i++) {

            final SeekBar seekBar = seekBars.get(i);
            final TextView textView = textViews.get(i);
            final String currentProteinName = DefaultProteins.NAMES[i];

            double oldGramsValue = selectedMeal.getGramsByProteinName(currentProteinName);
            if(oldGramsValue>=1000){//needed because of floating point errors
                oldGramsValue=999;
            }


            selectedMeal.addComponent(new MealComponent(DefaultProteins.array()[i],
                    selectedMeal.getKilogramsByProteinName(currentProteinName)
            ));



            final double oldSliderValue = oldGramsValue / NUMBER_SCALES_HUNDRED;


            seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                //ranges from 1 to 10 where 10 is one kilogram
                int currentValue = (int) oldSliderValue;

                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    if(!frezeScrollBars) {
                        currentValue = progress;
                    }
                    seekBar.setProgress(currentValue);
                    textView.setText(
                            NON_EMPTY_SPACE + currentValue * NUMBER_SCALES_HUNDRED + UNIT_NAME
                    );

                }


                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                    if(!frezeScrollBars){
                        MealComponent newComponent = selectedMeal.getComponentByProteinName(currentProteinName);
                        //this takes input in kilograms
                        newComponent.setServingSize(((double) currentValue) / NUMBER_SCALES_TEN);

                        Toast.makeText(
                                MealViewer.this.getBaseContext(),
                                currentProteinName + EMPTY_SPACE
                                        + String.valueOf(currentValue * NUMBER_SCALES_HUNDRED)
                                        + UNIT_NAME, Toast.LENGTH_SHORT
                        ).show();
                    }

                    seekBar.setProgress(currentValue);

                }


            });//end seekbarcurrentProteinNamecurrentProteinName

            seekBar.setProgress((int) oldSliderValue);
        }


        // Manage the buttons at the bottom
        Save.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                boolean canSave = true;
                if(!loadedFromMeal) {
                    String mealName = mealNameTextbox.getText().toString().trim();
                    if(mealName.equals("") || mealName == null) {
                        mealNameTextbox.setError(getText(R.string.meal_name_required).toString());
                        Toast.makeText(MealViewer.this, R.string.meal_name_required,Toast.LENGTH_SHORT).show();
                        canSave = false;
                    } else {
                        updateMeal();
                        if (mUploadTask != null && mUploadTask.isInProgress()) {
                            Toast.makeText(MealViewer.this, R.string.upload_in_progress,
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            uploadFile(selectedDate, selectedMeal.getMealName());
                        }

                        calendarFragment.updateSelectedDayView();
                    }
                }
                if(canSave){
                    finish();
                }
            }
        });

        Back.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                calendarFragment.updateSelectedDayView();
                finish();
            }
        });

        Delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MealCalendar mc = GreenFoodChallengeDatabase.getCurrentUser().getMealCalendar();
                Day day = mc.getDay(selectedDate);
                day.removeMeal(selectedMeal.getMealName());
                mc.updateDay(selectedDate,day);
                GreenFoodChallengeDatabase.updateCurrentUser();

                calendarFragment.updateSelectedDayView();
                finish();
            }
        });

        Change.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });



    }

    private void updateMeal() {
        MealCalendar mc = GreenFoodChallengeDatabase.getCurrentUser().getMealCalendar();

        Day selectedDay = mc.getDay(selectedDate);
        String mealName = mealNameTextbox.getText().toString().trim();

/*        if(mealName.equals("") || mealName == null) {
            selectedMeal.setMealName("Default Meal Name");
        } else {
            selectedMeal.setMealName(mealName);
        }*/

        selectedMeal.setMealName(mealName);
        //needed to change the name
        if(oldMealName!= null) {
            selectedDay.removeMeal(oldMealName);
        }

        selectedMeal.setRestaurant(restaurant.getText().toString());
        selectedMeal.setShared(shareable.isChecked());
        selectedMeal.setMunicipality(munipality);//don't change
        selectedMeal.setDescription(itemDesc.getText().toString());

        selectedDay.addMeal(selectedMeal);

        Log.d("GFC_UI",selectedMeal.getDescription());

        mc.updateDay(selectedDate,selectedDay);

        GreenFoodChallengeDatabase.updateCurrentUser();
    }


    //----------------------------------------------------------------------------------------------
    // Functions for Image uploading
    //----------------------------------------------------------------------------------------------
    private static final int PICK_IMAGE_REQUEST = 1;

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setTypeAndNormalize("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {

            mImageUri = data.getData();

            Picasso.with(this).load(mImageUri).into(mImageView);

        }
    }
    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }


    private void uploadFile(String uploadDate,String uploadMealName) {
        //Start binding the the image with the meal

        Context context = this;
        Thread imageWithMealBinder = new Thread(){
            private Context localContext = context;
            @Override
            public void run(){
                if (mImageUri != null) {
                    String address = System.currentTimeMillis() + "." + getFileExtension(mImageUri);
                    final StorageReference fileReference = mStorageRef.child(address);


                    fileReference.putFile(mImageUri).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(MealViewer.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                            mProgressBar.setProgress((int) progress);

                        }
                    }).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                            if (!task.isSuccessful()) {
                                throw task.getException();
                            }
                            return fileReference.getDownloadUrl();
                        }
                    }).addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(@NonNull Uri downloadUri) {
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    mProgressBar.setProgress(0);
                                }
                            }, 5000);

                            Toast.makeText(MealViewer.this, R.string.upload_successfull, Toast.LENGTH_SHORT).show();
                            Upload upload = new Upload(mealNameTextbox.getText().toString().trim(), downloadUri.toString());

                            // Make a unique Id - We can change this later.
                            String uploadId = mDatabaseRef.push().getKey();

                            // Use this to get data
                            mDatabaseRef.child(uploadId).setValue(upload);

                            // Get the url
                            String imageUrl = upload.getImageUrl();


                            //update the database
                            MealCalendar mc =
                                    GreenFoodChallengeDatabase.getCurrentUser().getMealCalendar();

                            synchronized (mc){
                                Day day = mc.getDay(uploadDate);
                                Meal meal = day.getMeal(uploadMealName);
                                meal.setImageUrl(imageUrl);
                                day.setMeal(meal);
                                mc.updateDay(uploadDate,day);

                                GreenFoodChallengeDatabase.updateCurrentUser();
                            }

                        }
                    });

                }
            }

        };
        imageWithMealBinder.start();
    }

    private void freezeFields() {

        mealNameTextbox.setEnabled(false);
        if (selectedMeal.getRestaurant() == null || selectedMeal.getRestaurant() == "") {
            restaurant.setHint(R.string.no_restaurant_specified);
        }
        restaurant.setEnabled(false);
        if (selectedMeal.getDescription() == null || selectedMeal.getDescription() == "") {
            restaurant.setHint(R.string.empty_description);
        }
        itemDesc.setEnabled(false);

        Change.setVisibility(View.GONE);
        shareable.setVisibility(View.GONE);

        Save.setVisibility(View.GONE);
        Delete.setVisibility(View.GONE);
        Back.setVisibility(View.GONE);

    }

    private void fillFields(){

        mealNameTextbox.setText(selectedMeal.getMealName());
        restaurant.setText(selectedMeal.getRestaurant());
        itemDesc.setText(selectedMeal.getDescription());

        municipalitySpinner.setSelection(selectedMeal.getMunicipality());
        shareable.setChecked(selectedMeal.isShared());

    }


}
