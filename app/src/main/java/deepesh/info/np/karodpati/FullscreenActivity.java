package deepesh.info.np.karodpati;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import deepesh.info.np.karodpati.Utils.DatabaseHandler;
import deepesh.info.np.karodpati.Utils.Questions;
import deepesh.info.np.karodpati.Utils.RestInterface;
import deepesh.info.np.karodpati.Utils.RestModel;
import deepesh.info.np.karodpati.Utils.TinyDB;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class FullscreenActivity extends Activity {

  private String BASE_URL="http://www.karodpati.dx.am/android_connect/";
  private Handler mHandler;
  TinyDB tinyDB;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    tinyDB = new TinyDB(getApplicationContext());
    if(!tinyDB.getBoolean("usedbefore")) {
      syncQuestion();
      tinyDB.putBoolean("usedbefore",true);
    }

    setContentView(R.layout.activity_fullscreen);
    Button btnNewGame= (Button)findViewById(R.id.newGameButton);
    Button btnSync= (Button)findViewById(R.id.boreButton);
    Button btnHelp= (Button)findViewById(R.id.helpButton);
    btnNewGame.setOnClickListener(new View.OnClickListener(){
      @Override
      public void onClick(View view) {
        // click handling code
        Intent intent = new Intent(FullscreenActivity.this, GameActivity.class);
        startActivity(intent);
      }
    });
    btnSync.setOnClickListener(new View.OnClickListener(){
      @Override
      public void onClick(View view) {
        syncQuestion();
      }
    });
    btnHelp.setOnClickListener(new View.OnClickListener(){
      @Override
      public void onClick(View view) {
        // click handling code
        Intent intent = new Intent(FullscreenActivity.this, HelpActivity.class);
        startActivity(intent);
      }
    });


  }
  @Override
  public void onBackPressed() {
    new MaterialDialog.Builder(FullscreenActivity.this)
      .title("Really Exit?")
      .content("Are you sure you want to Exit?")
      .positiveText("Yes")
      .negativeText("Cancel")
      .onPositive(new MaterialDialog.SingleButtonCallback() {
        @Override
        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
          Intent intent = new Intent(Intent.ACTION_MAIN);
          intent.addCategory(Intent.CATEGORY_HOME);
          intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
          startActivity(intent);
        }
      })
      .onNegative(new MaterialDialog.SingleButtonCallback() {
        @Override
        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
          dialog.dismiss();
        }
      })
      .show();
  }

  public  void syncQuestion()
  {
    final DatabaseHandler db = new DatabaseHandler(this);
    RestInterface mApiService = this.getInterfaceService();
    mHandler = new Handler(Looper.getMainLooper());
    db.deleteAllQuestion(1);
    Call<RestModel> mService = mApiService.loadQuestions();
    mService.enqueue(new Callback<RestModel>() {
      int count =0;
      @Override
      public void onResponse(Call<RestModel> call, Response<RestModel> response) {
        if (response.body()!= null ) {

          RestModel restModel = response.body();
          for(Questions questions : restModel.getResults())
          {
            db.addQuestion(questions,1);
            count++;
          }
        }
        Toast.makeText(FullscreenActivity.this, "Prevoius question were "+db.getQuestionCount(1), Toast.LENGTH_SHORT).show();
        Toast.makeText(FullscreenActivity.this, "Sync Completed,"+count +" Question added", Toast.LENGTH_SHORT).show();

      }

      @Override
      public void onFailure(Call<RestModel> call, Throwable t) {
        call.cancel();
        Toast.makeText(FullscreenActivity.this, "Please check your network connection", Toast.LENGTH_SHORT).show();
      }
    });
  }

  private RestInterface getInterfaceService() {
    Retrofit retrofit = new Retrofit.Builder()
      .baseUrl(BASE_URL)
      .addConverterFactory(GsonConverterFactory.create())
      .build();
    final RestInterface mInterfaceService = retrofit.create(RestInterface.class);
    return mInterfaceService;
  }


}
