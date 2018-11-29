package ca.sfu.greenfoodchallenge.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

public class Splash extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        ImageView iv= (ImageView) findViewById(R.id.Splash);
        iv.setImageResource(R.drawable.wallpaper);
        iv.setScaleType(ImageView.ScaleType.CENTER_CROP);

        Thread myThread = new Thread(){
            @Override
            public void run(){
                try {
                    sleep(500);
                    Intent in = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(in);
                    finish();

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }

        };

        myThread.start();

    }

}
