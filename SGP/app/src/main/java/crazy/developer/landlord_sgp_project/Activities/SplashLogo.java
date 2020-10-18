package crazy.developer.landlord_sgp_project.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;

import crazy.developer.landlord_sgp_project.R;

public class SplashLogo extends AppCompatActivity {

    private static int SPLASH_TIME_OUT = 2500;

    //Hooks

    LottieAnimationView animationView;
    //Animations
    Animation fade_in;

    public static String TAG = "SplashLogo";

//    public static JSONObject consentObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_logo);

        // Hooks
        animationView=findViewById(R.id.animationView);




//        consentObject = new JSONObject();
//        try {
//            // Provide correct consent value to sdk which is obtained by User
//            consentObject.put(InMobiSdk.IM_GDPR_CONSENT_AVAILABLE, true);
//            // Provide 0 if GDPR is not applicable and 1 if applicable
//            consentObject.put("gdpr", "0");
//            // Provide user consent in IAB format
//            consentObject.put(InMobiSdk.IM_GDPR_CONSENT_IAB, false);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        InMobiSdk.init(this, "a05360fad3384d5bbb3badf15d2832b1", consentObject, new SdkInitializationListener() {
//            @Override
//            public void onInitializationComplete(@Nullable Error error) {
//                if (null != error) {
//                    Log.e(TAG, "InMobi Init failed -" + error.getMessage());
//                } else {
//                    Log.d(TAG, "InMobi Init Successful");
//                }
//            }
//        });
//        InMobiSdk.updateGDPRConsent(consentObject);








        //Animation Calls
        fade_in = AnimationUtils.loadAnimation(this, R.anim.fade_in);

        //-----------Setting Animations to the elements of SplashScreen--------
        animationView.setAnimation(fade_in);


        //Splash Screen Code to call new Activity after some time
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {


                Intent intent = new Intent(SplashLogo.this, MainActivity.class);
                startActivity(intent);
                finish();

            }
        }, SPLASH_TIME_OUT);

    }

}
