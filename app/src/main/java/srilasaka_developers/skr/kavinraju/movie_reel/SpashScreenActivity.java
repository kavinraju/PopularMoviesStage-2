package srilasaka_developers.skr.kavinraju.movie_reel;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import srilasaka_developers.skr.kavinraju.movie_reel.R;

public class SpashScreenActivity extends AppCompatActivity {


    int SPLASH_TIME_OUT = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spash_screen);
        Typeface typeface = Typeface.createFromAsset(getAssets(),"font/Knewave-Regular.ttf");

        LottieAnimationView lottieAnimationView = findViewById(R.id.movie_reel_lottie);
        TextView textView_app_name = findViewById(R.id.textView_app_name);
        View view = findViewById(R.id.view_splash_screen);
        textView_app_name.setTypeface(typeface);
        //Build.VERSION.SDK_INT >= 21
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_FULLSCREEN);

        Animation animation = AnimationUtils.loadAnimation(this, R.anim.splash_screen_logo);
        lottieAnimationView.startAnimation(animation);
        textView_app_name.startAnimation(animation);
        view.startAnimation(animation);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {

            @Override
            public void run() {

                Intent i = new Intent(SpashScreenActivity.this, HomeActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.slide_from_right , R.anim.slide_to_left);
                finish();
            }
        }, SPLASH_TIME_OUT);

    }

}
