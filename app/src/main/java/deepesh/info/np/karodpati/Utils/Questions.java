package deepesh.info.np.karodpati.Utils;

/**
 * Created by Dpshkhnl on 2017-10-25.
 */

public class Questions {
  public Questions() {
  }

  int sno;

  public Questions(int sno, String question, String optA, String optB,
                   String optC, String optD,String ans, int complexity, int totAsked) {
    this.sno = sno;
    this.question = question;
    this.optA = optA;
    this.optB = optB;
    this.optC = optC;
    this.optD = optD;
    this.complexity = complexity;
    this.totAsked = totAsked;
    this.ans = ans;
  }

  String question;
  String optA;
  String optB;
  String optC;
  String optD;
  int complexity;

  public int getComplexity() {
    return complexity;
  }

  public void setComplexity(int complexity) {
    this.complexity = complexity;
  }

  public int getTotAsked() {
    return totAsked;
  }

  public void setTotAsked(int totAsked) {
    this.totAsked = totAsked;
  }

  int totAsked;

  public int getSno() {
    return sno;
  }

  public void setSno(int sno) {
    this.sno = sno;
  }

  public String getQuestion() {
    return question;
  }

  public void setQuestion(String question) {
    this.question = question;
  }

  public String getOptA() {
    return optA;
  }

  public void setOptA(String optA) {
    this.optA = optA;
  }

  public String getOptB() {
    return optB;
  }

  public void setOptB(String optB) {
    this.optB = optB;
  }

  public String getOptC() {
    return optC;
  }

  public void setOptC(String optC) {
    this.optC = optC;
  }

  public String getOptD() {
    return optD;
  }

  public void setOptD(String optD) {
    this.optD = optD;
  }

  public String getAns() {
    return ans;
  }

  public void setAns(String ans) {
    this.ans = ans;
  }

  String ans;
}
