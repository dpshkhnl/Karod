package deepesh.info.np.karodpati.Utils;

/**
 * Created by Dpshkhnl on 2017-10-25.
 */

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHandler extends SQLiteOpenHelper {

  // All Static variables
  // Database Version
  private static final int DATABASE_VERSION = 1;

  // Database Name
  private static final String DATABASE_NAME = "karodpati";

  // Contacts table name
  private static final String TABLE_QUESTION_EN = "question_en";
  private static final String TABLE_QUESTION_NP = "question_np";

  // Contacts Table Columns names
  private static final String KEY_ID = "id";
  private static final String KEY_QUESTION= "question";
  private static final String KEY_OPTA = "optA";
  private static final String KEY_OPTB = "optB";
  private static final String KEY_OPTC = "optC";
  private static final String KEY_OPTD = "optD";
  private static final String KEY_ANS = "ans";
  private static final String KEY_COMPLEXITY = "complexity";
  private static final String KEY_ASKED = "tot_asked";

  public DatabaseHandler(Context context) {
    super(context, DATABASE_NAME, null, DATABASE_VERSION);
  }

  // Creating Tables
  @Override
  public void onCreate(SQLiteDatabase db) {
    String CREATE_QUESTION_TABLE_EN = "CREATE TABLE IF NOT EXISTS " + TABLE_QUESTION_EN + "("
      + KEY_ID + " INTEGER PRIMARY KEY," + KEY_QUESTION + " TEXT,"
      + KEY_OPTA + " TEXT ," + KEY_OPTB + " TEXT ,"+ KEY_OPTC + " TEXT ,"
      + KEY_OPTD + " TEXT ," + KEY_ANS + " TEXT ,"+ KEY_COMPLEXITY + " INTEGER ,"
      + KEY_ASKED + " INTEGER " +  ")";
    db.execSQL(CREATE_QUESTION_TABLE_EN);

    String CREATE_QUESTION_TABLE_NP = "CREATE TABLE IF NOT EXISTS " + TABLE_QUESTION_EN + "("
      + KEY_ID + " INTEGER PRIMARY KEY," + KEY_QUESTION + " TEXT,"
      + KEY_OPTA + " TEXT ," + KEY_OPTB + " TEXT ,"+ KEY_OPTC + " TEXT ,"
      + KEY_OPTD + " TEXT ," + KEY_ANS + " TEXT ,"+ KEY_COMPLEXITY + " INTEGER ,"
      + KEY_ASKED + " INTEGER " +  ")";
    db.execSQL(CREATE_QUESTION_TABLE_NP);

  }

  // Upgrading database
  @Override
  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    // Drop older table if existed
    db.execSQL("DROP TABLE IF EXISTS " + TABLE_QUESTION_EN);
    db.execSQL("DROP TABLE IF EXISTS " + TABLE_QUESTION_NP);
    // Create tables again
    onCreate(db);
  }

  /**
   * All CRUD(Create, Read, Update, Delete) Operations
   */

  // Adding new Question lang 1 for english and 2 for Nepali
 public void addQuestion(Questions ques,int lang) {
    SQLiteDatabase db = this.getWritableDatabase();

    ContentValues values = new ContentValues();
    values.put(KEY_QUESTION, ques.getQuestion());
    values.put(KEY_OPTA, ques.getOptA());
    values.put(KEY_OPTB, ques.getOptB());
    values.put(KEY_OPTC, ques.getOptC());
    values.put(KEY_OPTD, ques.getOptD());
    values.put(KEY_ANS, ques.getAns());
    values.put(KEY_COMPLEXITY,ques.getComplexity());
    values.put(KEY_ASKED,0);

    // Inserting Row
    if(lang==1)
    db.insert(TABLE_QUESTION_EN, null, values);
    else
      db.insert(TABLE_QUESTION_NP, null, values);

    db.close(); // Closing database connection
  }

  // Getting single Question
  Questions getQuestion(int id,int lang) {
    SQLiteDatabase db = this.getReadableDatabase();
    Cursor cursor= null;
    if(lang == 1) {
      cursor = db.query(TABLE_QUESTION_EN, new String[]{KEY_ID,
          KEY_QUESTION, KEY_OPTA, KEY_OPTB, KEY_OPTC, KEY_OPTD, KEY_ANS, KEY_COMPLEXITY, KEY_ASKED}, KEY_ID + "=?",
        new String[]{String.valueOf(id)}, null, null, null, null);
    }else if(lang == 2)
    {
      cursor = db.query(TABLE_QUESTION_NP, new String[]{KEY_ID,
          KEY_QUESTION, KEY_OPTA, KEY_OPTB, KEY_OPTC, KEY_OPTD, KEY_ANS, KEY_COMPLEXITY, KEY_ASKED}, KEY_ID + "=?",
        new String[]{String.valueOf(id)}, null, null, null, null);
    }
    if (cursor != null)
      cursor.moveToFirst();

    Questions questions = new Questions(Integer.parseInt(cursor.getString(0)),
      cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4),
      cursor.getString(5),cursor.getString(6),Integer.parseInt(cursor.getString(7)),Integer.parseInt(cursor.getString(8)));
    // return contact
    return questions;
  }

  // Getting All Question
  public List<Questions> getAllQuestions(int lang) {
    List<Questions> contactList = new ArrayList<Questions>();
    // Select All Query
    String selectQuery ="";
    if(lang == 1) {
       selectQuery = "SELECT  * FROM " + TABLE_QUESTION_EN;
    }else{
      selectQuery = "SELECT  * FROM " + TABLE_QUESTION_NP;
    }
    SQLiteDatabase db = this.getWritableDatabase();
    Cursor cursor = db.rawQuery(selectQuery, null);

    // looping through all rows and adding to list
    if (cursor.moveToFirst()) {
      do {
        Questions question = new Questions();
        question.setSno(Integer.parseInt(cursor.getString(0)));
        question.setQuestion(cursor.getString(1));
        question.setOptA(cursor.getString(2));
        question.setOptB(cursor.getString(3));
        question.setOptC(cursor.getString(4));
        question.setOptD(cursor.getString(5));
        question.setAns(cursor.getString(6));
        question.setComplexity(Integer.parseInt(cursor.getString(7)));
        question.setTotAsked(Integer.parseInt(cursor.getString(8)));
        // Adding contact to list
        contactList.add(question);
      } while (cursor.moveToNext());
    }

    // return contact list
    return contactList;
  }

  public List<Questions> getAllQuestions(int lang,int complexity) {
    List<Questions> contactList = new ArrayList<Questions>();
    // Select All Query
    String selectQuery ="";
    if(lang == 1) {
      selectQuery = "SELECT  * FROM " + TABLE_QUESTION_EN+" where "+KEY_COMPLEXITY+" = "+complexity+" ORDER BY "+KEY_ASKED+" ASC";
    }else{
      selectQuery = "SELECT  * FROM " + TABLE_QUESTION_NP+" where "+KEY_COMPLEXITY+" = "+complexity+" ORDER BY "+KEY_ASKED+" ASC";
    }
    SQLiteDatabase db = this.getWritableDatabase();
    Cursor cursor = db.rawQuery(selectQuery, null);

    // looping through all rows and adding to list
    if (cursor.moveToFirst()) {
      do {
        Questions question = new Questions();
        question.setSno(Integer.parseInt(cursor.getString(0)));
        question.setQuestion(cursor.getString(1));
        question.setOptA(cursor.getString(2));
        question.setOptB(cursor.getString(3));
        question.setOptC(cursor.getString(4));
        question.setOptD(cursor.getString(5));
        question.setAns(cursor.getString(6));
        question.setComplexity(Integer.parseInt(cursor.getString(7)));
        question.setTotAsked(Integer.parseInt(cursor.getString(8)));
        // Adding contact to list
        contactList.add(question);
      } while (cursor.moveToNext());
    }

    // return contact list
    return contactList;
  }

  // Updating single contact
  public int updateQuestion(Questions ques,int lang) {
    SQLiteDatabase db = this.getWritableDatabase();

    ContentValues values = new ContentValues();
    values.put(KEY_QUESTION, ques.getQuestion());
    values.put(KEY_OPTA, ques.getOptA());
    values.put(KEY_OPTB, ques.getOptB());
    values.put(KEY_OPTC, ques.getOptC());
    values.put(KEY_OPTD, ques.getOptD());
    values.put(KEY_ANS, ques.getAns());
    values.put(KEY_COMPLEXITY,ques.getComplexity());
    values.put(KEY_ASKED,ques.getTotAsked());

    // updating row
    if(lang == 1){
      return db.update(TABLE_QUESTION_EN, values, KEY_ID + " = ?",
        new String[] { String.valueOf(ques.getSno()) });
    }else
    {
      return db.update(TABLE_QUESTION_NP, values, KEY_ID + " = ?",
        new String[] { String.valueOf(ques.getSno()) });
    }

  }

  // Deleting single contact
  public void deleteQuestion(Questions questions,int lang) {
    SQLiteDatabase db = this.getWritableDatabase();
    if(lang ==1)
    {
      db.delete(TABLE_QUESTION_EN, KEY_ID + " = ?",
        new String[] { String.valueOf(questions.getSno()) });
    }else
    {
      db.delete(TABLE_QUESTION_NP, KEY_ID + " = ?",
        new String[] { String.valueOf(questions.getSno()) });
    }

    db.close();
  }

  public void deleteAllQuestion(int lang) {
    SQLiteDatabase db = this.getWritableDatabase();
    if(lang ==1)
    {
      db.delete(TABLE_QUESTION_EN,null,null);
    }else
    {
      db.delete(TABLE_QUESTION_NP,null,null);
    }

    db.close();
  }


  // Getting contacts Count
  public int getQuestionCount(int lang) {
    String countQuery="";
    if(lang ==1)
    {
      countQuery = "SELECT  * FROM " + TABLE_QUESTION_EN;
    }else
    {
      countQuery = "SELECT  * FROM " + TABLE_QUESTION_NP;
    }

    SQLiteDatabase db = this.getReadableDatabase();
    Cursor cursor = db.rawQuery(countQuery, null);
    int count = cursor.getCount();
    cursor.close();

    // return count
    return count;
  }
  public void updateAskedCount(int lang,int sno)
  {
    Questions questions = getQuestion(sno,lang);
    questions.setTotAsked(questions.getTotAsked()+1);
    updateQuestion(questions,lang);
  }
}
