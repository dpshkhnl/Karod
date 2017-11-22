package deepesh.info.np.karodpati;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;


public class SplashActivity extends Activity {

    private CountDownTimer mTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);


        ImageView imageView = (ImageView) findViewById(R.id.image);

        /*Glide.with(getApplicationContext()).load(imgUrl)
                .thumbnail(0.5f)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView);*/

        init();
    }


    private void init() {
        mTimer = new CountDownTimer(3000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                navigate();
                finish();
            }
        };
        mTimer.start();
    }

    private void navigate() {


        PackageInfo info = null;
        try {
            info = getPackageManager().getPackageInfo("webbank.com.busticket", 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

       /* int currentVersion = info.versionCode;
        SharedPreferences prefsa = PreferenceManager.getDefaultSharedPreferences(this);
        int lastVersion = prefsa.getInt("version_code", 0);
        if (currentVersion > lastVersion) {
            prefsa.edit().putInt("version_code", currentVersion).commit();
            //  do the activity that u would like to do once here.
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }else
        {*/
            Intent intent = new Intent(this, FullscreenActivity.class);
            startActivity(intent);
       // }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
    }
}
