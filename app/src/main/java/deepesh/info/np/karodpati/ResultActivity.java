package deepesh.info.np.karodpati;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import deepesh.info.np.karodpati.R;
import deepesh.info.np.karodpati.Utils.TinyDB;

/**
 * Created by Dpshkhnl on 2017-10-25.
 */

public class ResultActivity extends Activity {
  TinyDB tinyDB;
  Button btnPlayAgain;
  public static String[] Amounts  =
    {
     "0", "5,000", "10,000", "20,000", "40,000", "80,000", "1,60,000", "3,20,000", "6,40,000", "12,50,000", "25,00,000", "50,00,000", "1 Crore", "3 Crore", "5 Crore", "7 Crore"
    };

  public static int[] SafeLevels = { 4, 7, 15 };
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  tinyDB = new TinyDB(getApplicationContext());
    int totAnswered = tinyDB.getInt("totAnswered");
    setContentView(R.layout.actvity_result);
    TextView resultCashView = (TextView) findViewById(R.id.resultCashView);
    btnPlayAgain = (Button) findViewById(R.id.playAgainButton);
    if(totAnswered>3)
    {
      resultCashView.setText("Rs.  "+Amounts[totAnswered]);
    }

    btnPlayAgain.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        new MaterialDialog.Builder(ResultActivity.this)
          .title("Play Again ?")
          .content("So you are sure you want to play again?")
          .positiveText("Agree")
          .negativeText("Cancel")
          .onPositive(new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
              // TODO

              Intent intent = new Intent(ResultActivity.this, FullscreenActivity.class);
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
    });
  }
  @Override
  public void onBackPressed() {
   new MaterialDialog.Builder(ResultActivity.this)
    .title("Really Exit?")
      .content("Are you sure you want to Exit?")
      .positiveText("Yes")
      .negativeText("Cancel")
      .onPositive(new MaterialDialog.SingleButtonCallback() {
    @Override
    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
      Intent intent = new Intent(ResultActivity.this, FullscreenActivity.class);
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
}
