package deepesh.info.np.karodpati;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.app.Fragment;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import deepesh.info.np.karodpati.Utils.DatabaseHandler;
import deepesh.info.np.karodpati.Utils.Questions;
import deepesh.info.np.karodpati.Utils.RestInterface;
import deepesh.info.np.karodpati.Utils.RestModel;
import deepesh.info.np.karodpati.Utils.TinyDB;


import static deepesh.info.np.karodpati.ResultActivity.Amounts;

/**
 * Created by Dpshkhnl on 2017-10-25.
 */

public class GameActivity extends FragmentActivity {

  Button audiencePollButton, fifty50Button, doubleDipButton, changeQuestionButton;
  TextView questionView, timerView, optionA, optionB, optionC, optionD;
  boolean audiencePoll, fifty50, doubleDip, changeQuestion;
  boolean cont;
  private CountDownTimer mTimer;
  private int totAnswered;
  ProgressBar timerProgress;
  TinyDB tinyDB;
  private String BASE_URL="http://www.karodpati.dx.am/android_connect/";
  private Handler mHandler;
  CountDownTimer waitTimer;
  Questions questions;
   DatabaseHandler db;
  List<Integer>arr;
  boolean activeDD = false;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.activity_game);
    arr = new ArrayList<>();
    tinyDB = new TinyDB(getApplicationContext());
    db = new DatabaseHandler(this);

    audiencePollButton = (Button) findViewById(R.id.audiencePollButton);
    fifty50Button = (Button) findViewById(R.id.fifty50Button);
    doubleDipButton = (Button) findViewById(R.id.doubleDipButton);
    changeQuestionButton = (Button) findViewById(R.id.changeQuestionButton);
    questionView = (TextView) findViewById(R.id.questionView);
    timerView = (TextView) findViewById(R.id.timerView);
    timerProgress = (ProgressBar) findViewById(R.id.timerProgress);
    optionA = (TextView) findViewById(R.id.optionA);
    optionB = (TextView) findViewById(R.id.optionB);
    optionC = (TextView) findViewById(R.id.optionC);
    optionD = (TextView) findViewById(R.id.optionD);
    totAnswered = 0;
    Fragment myFrag = new MoneyFragment();
    FrameLayout moneyTeeContainer = (FrameLayout) findViewById(R.id.moneyTeeContainer);
    getFragmentManager().beginTransaction().replace(moneyTeeContainer.getId(), myFrag).commit();
    setQuestion();

    audiencePollButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        new MaterialDialog.Builder(GameActivity.this)
          .title("Use Audience Poll ?")
          .content("So you are sure you want to use Audience Poll?")
          .positiveText("Agree")
          .negativeText("Cancel")
          .onPositive(new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
              // TODO

              boolean wrapInScrollView = true;
              MaterialDialog dialog1 = new MaterialDialog.Builder(GameActivity.this)
                .title("Audience")
                .customView(R.layout.audience_poll, wrapInScrollView)
                .positiveText("Okay")
                .show();
               //... initialization via the builder ...
                View view = dialog1.getCustomView();
              TextView[] textViews = new TextView[4];
              ProgressBar[] progressBars = new ProgressBar[4];

              textViews[0] = (TextView) view.findViewById(R.id.optionACent);
              textViews[1] = (TextView) view.findViewById(R.id.optionBCent);
              textViews[2] = (TextView) view.findViewById(R.id.optionCCent);
              textViews[3] = (TextView) view.findViewById(R.id.optionDCent);

              progressBars[0] = (ProgressBar) view.findViewById(R.id.optionABar);
              progressBars[1] = (ProgressBar) view.findViewById(R.id.optionBBar);
              progressBars[2] = (ProgressBar) view.findViewById(R.id.optionCBar);
              progressBars[3] = (ProgressBar) view.findViewById(R.id.optionDBar);
              if(questions!=null)
              {
                int correctOption = Integer.valueOf(questions.getAns())-1;
                if (correctOption != 0)
                {
                  TextView tTextView = textViews[correctOption];
                 ProgressBar tProgressBar = progressBars[correctOption];

                  textViews[correctOption] = textViews[0];
                  progressBars[correctOption] = progressBars[0];

                  textViews[0] = tTextView;
                  progressBars[0] = tProgressBar;
                }
                int centLeft = 100, option;
                Random randomGenerator= new Random();

                option =randomGenerator.nextInt((70 - 50) + 1) + 50;
                textViews[0].setText(option + "%");
                progressBars[0].setProgress(option);
                centLeft -= option;

                option = randomGenerator.nextInt(centLeft);
                textViews[1].setText(option + "%");
                progressBars[1].setProgress(option);
                centLeft -= option;

                option = randomGenerator.nextInt(centLeft);
                textViews[2].setText(option + "%");
                progressBars[2].setProgress(option);
                centLeft -= option;

                textViews[3].setText(centLeft + "%");
                progressBars[3].setProgress(centLeft);


              }
              audiencePoll = true;
              audiencePollButton.setBackgroundResource(R.drawable.audience_used);
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

    fifty50Button.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        {
          new MaterialDialog.Builder(GameActivity.this)
            .title("Use Fifty 50 ?")
            .content("So you are sure you want to use Fifty Fifty?")
            .positiveText("Agree")
            .negativeText("Cancel")
            .onPositive(new MaterialDialog.SingleButtonCallback() {
              @Override
              public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                // TODO
                fifty50 = true;
                fifty50Button.setBackgroundResource(R.drawable.fifty50_used);
                if(questions!=null)
                {
                  int ans =Integer.valueOf(questions.getAns());
                  int nextOpt = totAnswered%4;
                  switch (ans)
                  {
                    case 1:
                      optionA.setText(questions.getOptA());
                      optionB.setText("");
                      optionC.setText(questions.getOptC());
                      optionD.setText("");
                      break;

                    case 2:
                      optionA.setText(questions.getOptA());
                      optionB.setText(questions.getOptB());
                      optionC.setText("");
                      optionD.setText("");
                      break;
                    case 3:
                      optionA.setText("");
                      optionB.setText(questions.getOptB());
                      optionC.setText(questions.getOptC());
                      optionD.setText("");
                      break;
                    case 4:
                      optionA.setText(questions.getOptA());
                      optionB.setText("");
                      optionC.setText("");
                      optionD.setText(questions.getOptD());
                      break;
                    default:
                      optionA.setText(questions.getOptA());
                      optionB.setText(questions.getOptB());
                      optionC.setText(questions.getOptC());
                      optionD.setText(questions.getOptD());

                  }
                  }
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
    });

    doubleDipButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        {
          new MaterialDialog.Builder(GameActivity.this)
            .title("Use Double Dip ?")
            .content("So you are sure you want to use Double Dip?")
            .positiveText("Agree")
            .negativeText("Cancel")
            .onPositive(new MaterialDialog.SingleButtonCallback() {
              @Override
              public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                // TODO
                doubleDip = true;
                activeDD = true;
                doubleDipButton.setBackgroundResource(R.drawable.doubledip_used);
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
    });
    changeQuestionButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        {
          new MaterialDialog.Builder(GameActivity.this)
            .title("Use Skip Question ?")
            .content("So you are sure you want to use Change Question?")
            .positiveText("Agree")
            .negativeText("Cancel")
            .onPositive(new MaterialDialog.SingleButtonCallback() {
              @Override
              public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                // TODO
                changeQuestion = true;
                changeQuestionButton.setBackgroundResource(R.drawable.change_used);
                setQuestion();

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
    });


  }

  public void checkAnswer(final int answered)
  {
    if (Integer.valueOf(questions.getAns())==answered) {
      totAnswered++;
      new MaterialDialog.Builder(GameActivity.this)
        .title("Congrats")
        .content("You have won Rs."+Amounts[totAnswered]+" Continue")
        .positiveText("Sure")
        .negativeText("Cancel")
        .onPositive(new MaterialDialog.SingleButtonCallback() {
          @Override
          public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
            // TODO
            activeDD = false;
            setQuestion();
          }
        })
        .onNegative(new MaterialDialog.SingleButtonCallback() {
          @Override
          public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
            dialog.dismiss();
          }
        })
        .show();


    } else {
      new MaterialDialog.Builder(GameActivity.this)
        .title("Oops")
        .content("Wrong Answer!!")
        .positiveText("result")
        .onPositive(new MaterialDialog.SingleButtonCallback() {
          @Override
          public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
            // TODO
            if(activeDD)
            {

              activeDD = false;

              switch (answered)
              {
                case 1:
                  optionA.setText("");
                  optionB.setText(questions.getOptB());
                  optionC.setText(questions.getOptC());
                  optionD.setText(questions.getOptD());
                  break;

                case 2:
                  optionA.setText(questions.getOptA());
                  optionB.setText("");
                  optionC.setText(questions.getOptC());
                  optionD.setText(questions.getOptD());
                  break;
                case 3:
                  optionA.setText(questions.getOptA());
                  optionB.setText(questions.getOptB());
                  optionC.setText("");
                  optionD.setText(questions.getOptD());
                  break;
                case 4:
                  optionA.setText(questions.getOptA());
                  optionB.setText(questions.getOptB());
                  optionC.setText(questions.getOptC());
                  optionD.setText("");
                  break;
                default:
                  optionA.setText(questions.getOptA());
                  optionB.setText(questions.getOptB());
                  optionC.setText(questions.getOptC());
                  optionD.setText(questions.getOptD());
                break;
              }
            }else
            showResult();
          }
        })
        .show();
    }
  }

  public void setQuestion() {
    if(waitTimer != null) {
      waitTimer.cancel();
      waitTimer = null;
    }
      questions = loadQuestion(1);
    if (totAnswered < 4) {
      init(30000);
    } else if (totAnswered < 6) {
      init(60000);
    } else {
      changeQuestionButton.setVisibility(View.VISIBLE);
      timerView.setVisibility(View.INVISIBLE);
      timerProgress.setVisibility(View.INVISIBLE);

    }

    questionView.setText(questions.getQuestion());
    optionA.setText(questions.getOptA());
    optionB.setText(questions.getOptB());
    optionC.setText(questions.getOptC());
    optionD.setText(questions.getOptD());

    optionA.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {

        checkAnswer(1);
      }
    });

    optionB.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        checkAnswer(2);
      }
    });
    optionC.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        checkAnswer(3);
      }
    });
    optionD.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
      checkAnswer(4);
      }
    });

  }


  public Questions loadQuestion(int lang) {
    if (audiencePoll) {
      audiencePollButton.setBackgroundResource(R.drawable.audience_used);
      audiencePollButton.setEnabled(false);
    }
    if (fifty50) {
      fifty50Button.setBackgroundResource(R.drawable.fifty50_used);
      fifty50Button.setEnabled(false);
    }
    if (doubleDip) {
      doubleDipButton.setBackgroundResource(R.drawable.doubledip_used);
      doubleDipButton.setEnabled(false);
    }
    if (changeQuestion) {
      changeQuestionButton.setBackgroundResource(R.drawable.change_used);
      changeQuestionButton.setEnabled(false);
    }
    List<Questions> lstQues =  new ArrayList<>();
    Questions questions;
    if (totAnswered< 4) {
      lstQues = db.getAllQuestions(1, 1);
    }
     else if (totAnswered >= 4 &&  totAnswered <7)
      {
        lstQues = db.getAllQuestions(1, 2);
      }
      else
    {
      lstQues = db.getAllQuestions(1, 3);
    }
    int totQuestion = lstQues.size();


    questions = pickRandomQuestion(lstQues);
    arr.add(questions.getSno());
    Log.d("Question asked",String.valueOf(questions.getSno()));
    db.updateAskedCount(1,questions.getSno());
    return questions;
  }

  private  Questions pickRandomQuestion(List<Questions> lstQues)
  {

    Random randomGenerator= new Random();;
    int index = randomGenerator.nextInt(lstQues.size());
    Log.d("generated",String.valueOf(index));
   Questions questions = lstQues.get(index);
    Log.d("Already asked",arr.toString());

    for(int sno: arr)
    {
      if(sno==questions.getSno())
      {
        Log.d("Used Question",String.valueOf(sno));
        lstQues.remove(questions);
        pickRandomQuestion(lstQues);
        break;
      }

    }
    return questions;
  }

  private void init(int time) {
    waitTimer = new CountDownTimer(time, 1000) {

      public void onTick(long millisUntilFinished) {
        timerView.setText("" + millisUntilFinished / 1000);
        //here you can have your logic to set text to edittext
      }

      public void onFinish() {
        showResult();
      }

    }.start();
  }

  public void showResult() {
    int amt = 0;
    if (totAnswered < 3) {
      amt = 0;
    } else if (totAnswered < 7) {
      amt = 4;
    } else if (totAnswered < 15) {
      amt = 7;
    } else
      amt = 15;

    if(waitTimer != null) {
      waitTimer.cancel();
      waitTimer = null;
    }
    tinyDB.putInt("totAnswered", amt);
    Intent intent = new Intent(this, ResultActivity.class);
    startActivity(intent);
  }

/*  public void  showMessageDialog()
  {
    new MaterialDialog.Builder(GameActivity.this)
      .title("Sure?")
      .content("So you are sure you want to go with this option?")
      .positiveText("Sure")
      .negativeText("Cancel")
      .onPositive(new MaterialDialog.SingleButtonCallback() {
        @Override
        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
          // TODO
          dialog.dismiss();

        }
      })
      .onNegative(new MaterialDialog.SingleButtonCallback() {
        @Override
        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
          dialog.dismiss();
        }
      })
      .show();

  }*/

  @Override
  public void onBackPressed() {
    new MaterialDialog.Builder(GameActivity.this)
      .title("Really Quit?")
      .content("Are you sure you want to Quit?")
      .positiveText("Yes please Quit")
      .negativeText("Cancel")
      .onPositive(new MaterialDialog.SingleButtonCallback() {
        @Override
        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
          tinyDB.putInt("totAnswered", totAnswered);
          if(waitTimer != null) {
            waitTimer.cancel();
            waitTimer = null;
          }
          Intent intent = new Intent(GameActivity.this, ResultActivity.class);
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
