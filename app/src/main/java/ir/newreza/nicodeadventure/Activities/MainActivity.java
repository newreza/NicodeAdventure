package ir.newreza.nicodeadventure.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import ir.newreza.nicodeadventure.Models.Question;
import ir.newreza.nicodeadventure.Modules.MyAnimator;
import ir.newreza.nicodeadventure.R;
import ir.newreza.nicodeadventure.Modules.T;

public class MainActivity extends AppCompatActivity {
    float x1, x2, xDiff, y1, y2, yDiff;
    Question currentQuestion;
    ArrayList<Question> questionsList, showedQuestionsList;
    ArrayList<Boolean> choicesList;
    Context context;
    LinearLayout llMain, llQuestion, llResult;
    LinearLayout.LayoutParams llResultP;
    TextView tvQuestion, tvYes, tvNo;
    MyAnimator questionShowAnimator, questionHideAnimator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        T t = new T(context);
        t.setLocale("en");
        T.setOrientation(context, 7);
        questionsList = new ArrayList<>();
        showedQuestionsList = new ArrayList<>();
        choicesList = new ArrayList<>();

        createQuestionsData();

        questionShowAnimator = new MyAnimator();
        questionShowAnimator.setAnimation(MyAnimator.ALPHA, 0f, 1f, MyAnimator.LINEAR, 500);
        questionHideAnimator = new MyAnimator();
        questionHideAnimator.setAnimation(MyAnimator.ALPHA, 1f, 0f, MyAnimator.LINEAR, 500);

        LinearLayout.LayoutParams llRootP = new LinearLayout.LayoutParams(-1, -1);
        LinearLayout llRoot = new LinearLayout(context);
        llRoot.setLayoutParams(llRootP);
        llRoot.setGravity(Gravity.CENTER | Gravity.TOP);
        llRoot.setOrientation(LinearLayout.VERTICAL);
        llRoot.setBackgroundColor(Color.WHITE);
        setContentView(llRoot);
        setActionbar(llRoot);

        LinearLayout.LayoutParams llMainP = new LinearLayout.LayoutParams(-1, -1);
        llMain = new LinearLayout(context);
        llMain.setLayoutParams(llMainP);
        llMain.setGravity(Gravity.CENTER);
        llMain.setOrientation(LinearLayout.VERTICAL);
        llMain.setBackgroundColor(Color.WHITE);
        llRoot.addView(llMain);

        showLogo();
    }

    public void setActionbar(LinearLayout parentView) {
        LinearLayout.LayoutParams rlActionBarP = new LinearLayout.LayoutParams(-1, T.actionHeight);
        RelativeLayout rlActionBar = new RelativeLayout(context);
        rlActionBar.setLayoutParams(rlActionBarP);
        rlActionBar.setBackgroundResource(R.color.colorPrimary);
        ViewCompat.setTranslationZ(rlActionBar, (int) (T.inch / 30));
        parentView.addView(rlActionBar);

        RelativeLayout.LayoutParams tvActionBarP = new RelativeLayout.LayoutParams(-2, -1);
        tvActionBarP.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        TextView tvActionBar = new TextView(context);
        tvActionBar.setLayoutParams(tvActionBarP);
        tvActionBar.setPadding(T.actionPadding, 0, T.actionPadding, 0);
        tvActionBar.setGravity(Gravity.CENTER);
        tvActionBar.setText(R.string.app_name);
        tvActionBar.setTypeface(T.tf02);
        tvActionBar.setTextColor(Color.WHITE);
        tvActionBar.setTextSize(16);
        rlActionBar.addView(tvActionBar);

        RelativeLayout.LayoutParams ivRefreshP = new RelativeLayout.LayoutParams(T.actionHeight, T.actionHeight);
        ivRefreshP.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        ImageView ivRefresh = new ImageView(context);
        ivRefresh.setLayoutParams(ivRefreshP);
        ivRefresh.setPadding(T.actionPadding, T.actionPadding, T.actionPadding, T.actionPadding);
        ivRefresh.setImageResource(R.drawable.refresh_white);
        T.setColor(ivRefresh, Color.argb(0, 255, 255, 255), Color.argb(50, 255, 255, 255));
        rlActionBar.addView(ivRefresh);
        ivRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = getIntent();
                finish();
                startActivity(intent);
            }
        });
    }

    public void showLogo() {
        LinearLayout.LayoutParams ivLogoP = new LinearLayout.LayoutParams(T.w * 2 / 3, T.w * 2 / 3);
        final ImageView ivLogo = new ImageView(context);
        ivLogo.setLayoutParams(ivLogoP);
        ivLogo.setImageResource(R.drawable.logo);
        ivLogo.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        llMain.addView(ivLogo);

        MyAnimator ivLogoAnimator = new MyAnimator();
        ivLogoAnimator.setAnimation(MyAnimator.ALPHA, 0f, 1f, MyAnimator.LINEAR, 1000);
        ivLogoAnimator.start(ivLogo);
        ivLogoAnimator.setOnAnimationListener(new MyAnimator.OnAnimationListener() {
            @Override
            public void onAnimationStart(View view) {

            }

            @Override
            public void onAnimationEnd(View view) {
                new Thread(){
                    public void run() {
                        try {
                            sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                MyAnimator ivLogoAnimator = new MyAnimator();
                                ivLogoAnimator.setAnimation(MyAnimator.ALPHA, 1f, 0f, MyAnimator.LINEAR, 1000);
                                ivLogoAnimator.start(ivLogo);
                                ivLogoAnimator.setOnAnimationListener(new MyAnimator.OnAnimationListener() {
                                    @Override
                                    public void onAnimationStart(View view) {

                                    }

                                    @Override
                                    public void onAnimationEnd(View view) {
                                        llMain.removeView(ivLogo);
                                        createQuestionsView();
                                        showQuestion(0);
                                    }
                                });
                            }
                        });
                    }
                }.start();
            }
        });
    }

    public void createQuestionsView() {
        LinearLayout.LayoutParams llQuestionP = new LinearLayout.LayoutParams(-1, -2);
        llQuestion = new LinearLayout(context);
        llQuestion.setLayoutParams(llQuestionP);
        llQuestion.setGravity(Gravity.CENTER);
        llQuestion.setOrientation(LinearLayout.VERTICAL);
        llMain.addView(llQuestion);

        LinearLayout.LayoutParams tvQuestionP = new LinearLayout.LayoutParams(-1, -2);
        tvQuestion = new TextView(context);
        tvQuestion.setLayoutParams(tvQuestionP);
        tvQuestion.setPadding(T.p1, 0, T.p1, 0);
        tvQuestion.setGravity(Gravity.CENTER);
        tvQuestion.setTextSize(20);
        tvQuestion.setTextColor(Color.argb(200, 0, 0, 0));
        tvQuestion.setTypeface(T.tf02);
        llQuestion.addView(tvQuestion);

        LinearLayout.LayoutParams llOptionsP = new LinearLayout.LayoutParams(-1, -2);
        llOptionsP.setMargins(0, T.p1, 0, 0);
        final LinearLayout llOptions = new LinearLayout(context);
        llOptions.setLayoutParams(llOptionsP);
        llOptions.setGravity(Gravity.CENTER);
        llOptions.setOrientation(LinearLayout.HORIZONTAL);
        llQuestion.addView(llOptions);

        GradientDrawable tvYesBg1 = new GradientDrawable();
        tvYesBg1.setCornerRadius(T.w);
        tvYesBg1.setStroke(2, T.c11);
        GradientDrawable tvYesBg2 = new GradientDrawable();
        tvYesBg2.setCornerRadius(T.w);
        tvYesBg2.setStroke(2, T.c11);
        tvYesBg2.setColor(T.c13);
        LinearLayout.LayoutParams tvYesP = new LinearLayout.LayoutParams(-2, -2);
        tvYes = new TextView(context);
        tvYes.setLayoutParams(tvYesP);
        tvYes.setGravity(Gravity.CENTER);
        tvYes.setPadding(T.p0, T.p2, T.p0, T.p2);
        tvYes.setTextSize(18);
        tvYes.setTextColor(T.c11);
        tvYes.setTypeface(T.tf01);
        T.setGradient(tvYes, tvYesBg1, tvYesBg2);
        tvYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentQuestion.getYesReferIndex() == -1 || currentQuestion.getNoReferIndex() == -1) {
                    questionHideAnimator.start(llQuestion);
                    questionHideAnimator.setOnAnimationListener(new MyAnimator.OnAnimationListener() {
                        @Override
                        public void onAnimationStart(View view) {

                        }

                        @Override
                        public void onAnimationEnd(View view) {
                            llMain.removeView(llQuestion);
                            showResult();
                        }
                    });
                } else {
                    choicesList.add(true);
                    questionHideAnimator.start(llQuestion);
                    questionHideAnimator.setOnAnimationListener(new MyAnimator.OnAnimationListener() {
                        @Override
                        public void onAnimationStart(View view) {

                        }

                        @Override
                        public void onAnimationEnd(View view) {
                            showQuestion(currentQuestion.getYesReferIndex());
                        }
                    });
                }
            }
        });
        llOptions.addView(tvYes);

        GradientDrawable tvNoBg1 = new GradientDrawable();
        tvNoBg1.setCornerRadius(T.w);
        tvNoBg1.setStroke(2, T.c21);
        GradientDrawable tvNoBg2 = new GradientDrawable();
        tvNoBg2.setCornerRadius(T.w);
        tvNoBg2.setStroke(2, T.c21);
        tvNoBg2.setColor(T.c13);
        LinearLayout.LayoutParams tvNoP = new LinearLayout.LayoutParams(-2, -2);
        tvNoP.setMargins(T.w / 4, 0, 0, 0);
        tvNo = new TextView(context);
        tvNo.setLayoutParams(tvNoP);
        tvNo.setGravity(Gravity.CENTER);
        tvNo.setPadding(T.p0, T.p2, T.p0, T.p2);
        tvNo.setTextSize(18);
        tvNo.setTextColor(T.c21);
        tvNo.setTypeface(T.tf01);
        T.setGradient(tvNo, tvNoBg1, tvNoBg2);
        tvNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                choicesList.add(false);
                questionHideAnimator.start(llQuestion);
                questionHideAnimator.setOnAnimationListener(new MyAnimator.OnAnimationListener() {
                    @Override
                    public void onAnimationStart(View view) {

                    }

                    @Override
                    public void onAnimationEnd(View view) {
                        showQuestion(currentQuestion.getNoReferIndex());
                    }
                });
            }
        });
        llOptions.addView(tvNo);
    }

    public void createQuestionsData() {
        addQuestion("Do I want a doughnut?", 1, 2);
        addQuestion("Do I deserve it?", 3, 4);
        addQuestion("Maybe you want an apple?", 5, 6);
        addQuestion("Are you sure?", 7, 8);
        addQuestion("Is it a good doughnut?", 9, 10);
        addQuestion("Are you sure?", 11, 12);
        addQuestion("Is it a juicy apple?", 13, 14);
        addQuestion("Get it.", -1, -1);
        addQuestion("Do jumping jacks first.", -1, -1);
        addQuestion("What are you waiting for? Grab it now.", -1, -1);
        addQuestion("Wait 'til you find a sinful, unforgettable doughnut.", -1, -1);
        addQuestion("Get it.", -1, -1);
        addQuestion("Watch TV first.", -1, -1);
        addQuestion("What are you waiting for? Grab it now.", -1, -1);
        addQuestion("Wait 'til you find a juicy, unforgettable apple.", -1, -1);
    }

    public void addQuestion(String questionText, int yesReferIndex, int noReferIndex) {
        Question question = new Question();
        question.setIndex(questionsList.size());
        question.setQuestionText(questionText);
        question.setYesReferIndex(yesReferIndex);
        question.setNoReferIndex(noReferIndex);
        questionsList.add(question);
    }

    @SuppressLint("SetTextI18n")
    public void showQuestion(int questionIndex) {
        currentQuestion = questionsList.get(questionIndex);
        showedQuestionsList.add(currentQuestion);
        tvQuestion.setText(currentQuestion.getQuestionText());
        if (currentQuestion.getYesReferIndex() == -1 || currentQuestion.getNoReferIndex() == -1) {
            tvYes.setText("OK");
            tvNo.setVisibility(View.GONE);

        } else {
            tvYes.setText("YES");
            tvNo.setText("NO");
            tvNo.setVisibility(View.VISIBLE);
        }
        questionShowAnimator.start(llQuestion);
    }

    @SuppressLint("ClickableViewAccessibility")
    public void showResult() {
        int layersCount = (int) (Math.log(questionsList.size() + 1) / Math.log(2));
        int lastLayerChildsCount = (questionsList.size() + 1) / 2;
        int questionWidth = T.w * 4 / 10;

        llResultP = new LinearLayout.LayoutParams(Math.max((questionWidth * lastLayerChildsCount) + ((lastLayerChildsCount + 1) * T.p1), T.w), T.h * 2);
        llResultP.setMargins(T.p0, 0, T.p0, 0);
        llResult = new LinearLayout(context);
        llResult.setLayoutParams(llResultP);
        llResult.setGravity(Gravity.CENTER);
        llResult.setOrientation(LinearLayout.VERTICAL);
        llResult.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    x1 = motionEvent.getX();
                    y1 = motionEvent.getY();
                } else if (motionEvent.getAction() == MotionEvent.ACTION_MOVE || motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    x2 = motionEvent.getX();
                    y2 = motionEvent.getY();
                    xDiff = x2 - x1;
                    yDiff = y2 - y1;
                    if (view.getX() + xDiff >= 0) {
                        view.setX(0);
                    } else if (view.getX() + xDiff <= T.w - view.getWidth()) {
                        view.setX(T.w - view.getWidth());
                    } else {
                        view.setX(view.getX() + xDiff);
                    }
                    if (view.getY() + yDiff >= 0) {
                        view.setY(0);
                    } else if (view.getY() + yDiff <= T.h - view.getHeight()) {
                        view.setY(T.h - view.getHeight());
                    } else {
                        view.setY(view.getY() + yDiff);
                    }
                }
                return true;
            }
        });
        llMain.addView(llResult);

        for (int i = 0; i < layersCount; i++) {
            Paint paint = new Paint();
            paint.setColor(Color.argb(150, 0, 0, 0));
            paint.setAntiAlias(true);
            paint.setStrokeWidth(5);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeJoin(Paint.Join.ROUND);

            LinearLayout.LayoutParams llQuestionLayerP = new LinearLayout.LayoutParams(-1, -2);
            LinearLayout llQuestionLayer = new LinearLayout(context);
            llQuestionLayer.setLayoutParams(llQuestionLayerP);
            llQuestionLayer.setGravity(Gravity.CENTER);
            llQuestionLayer.setOrientation(LinearLayout.HORIZONTAL);
            llResult.addView(llQuestionLayer, 0);

            for (int j = 0; j < Math.pow(2, layersCount - i - 1); j++) {
                GradientDrawable tvQuestionBg = new GradientDrawable();
                tvQuestionBg.setCornerRadius((int) (T.inch / 10));
                tvQuestionBg.setStroke(1, Color.argb(200, 0, 0, 0));
                tvQuestionBg.setColor(showedQuestionsList.contains(questionsList.get((int) Math.pow(2, layersCount - i) - j - 2)) ? Color.argb(50, 0, 255, 0) : 0);
                LinearLayout.LayoutParams tvQuestionP = new LinearLayout.LayoutParams(questionWidth, -2);
                tvQuestionP.setMargins(0, 0, j == 0 ? 0 : (int) ((Math.pow(2, i) - 1) * questionWidth + ((Math.pow(2, i)) * T.p1)), 0);
                TextView tvQuestion = new TextView(context);
                tvQuestion.setLayoutParams(tvQuestionP);
                tvQuestion.setPadding(T.p3, T.p3, T.p3, T.p3);
                tvQuestion.setGravity(Gravity.CENTER);
                tvQuestion.setTextSize(12);
                tvQuestion.setTextColor(Color.argb(200, 0, 0, 0));
                tvQuestion.setTypeface(T.tf02);
                tvQuestion.setText(questionsList.get((int) Math.pow(2, layersCount - i) - j - 2).getQuestionText());
                tvQuestion.setBackground(tvQuestionBg);
                llQuestionLayer.addView(tvQuestion, 0);
            }

            if (i < layersCount - 1) {
                LinearLayout.LayoutParams llChoiceLayerP = new LinearLayout.LayoutParams(-2, -2);
                LinearLayout llChoiceLayer = new LinearLayout(context);
                llChoiceLayer.setLayoutParams(llChoiceLayerP);
                llChoiceLayer.setGravity(Gravity.CENTER);
                llChoiceLayer.setOrientation(LinearLayout.HORIZONTAL);
                llResult.addView(llChoiceLayer, 0);

                for (int j = 0; j < Math.pow(2, layersCount - i - 1); j++) {
                    LinearLayout.LayoutParams tvChoiceP = new LinearLayout.LayoutParams(questionWidth, -2);
                    tvChoiceP.setMargins(0, 0, j == 0 ? 0 : (int) ((Math.pow(2, i) - 1) * questionWidth + ((Math.pow(2, i)) * T.p1)), 0);
                    TextView tvChoice = new TextView(context);
                    tvChoice.setLayoutParams(tvChoiceP);
                    tvChoice.setGravity(Gravity.CENTER);
                    tvChoice.setTextSize(12);
                    tvChoice.setTextColor(showedQuestionsList.contains(questionsList.get((int) Math.pow(2, layersCount - i) - j - 2)) ? Color.argb(255, 0, 150, 0) : Color.argb(100, 0, 0, 0));
                    tvChoice.setTypeface(T.tf02);
                    tvChoice.setText(j % 2 == 0 ? "NO" : "YES");
                    llChoiceLayer.addView(tvChoice, 0);
                }

                LinearLayout.LayoutParams llFlashLayerP = new LinearLayout.LayoutParams(-2, -2);
                llFlashLayerP.setMargins(0, T.p3, 0, 0);
                LinearLayout llFlashLayer = new LinearLayout(context);
                llFlashLayer.setLayoutParams(llFlashLayerP);
                llFlashLayer.setGravity(Gravity.CENTER);
                llFlashLayer.setOrientation(LinearLayout.HORIZONTAL);
                llResult.addView(llFlashLayer, 0);

                for (int j = 0; j < Math.pow(2, layersCount - i - 2); j++) {
                    LinearLayout.LayoutParams ivFleshP = new LinearLayout.LayoutParams((int) ((Math.pow(2, i)) * questionWidth + ((Math.pow(2, i)) * T.p1)), T.inch / 2);
                    ivFleshP.setMargins(0, 0, j == 0 ? 0 : (int) ((Math.pow(2, i)) * questionWidth + ((Math.pow(2, i)) * T.p1)), 0);
                    ImageView ivFlesh = new ImageView(context);
                    ivFlesh.setLayoutParams(ivFleshP);
                    llFlashLayer.addView(ivFlesh, 0);

                    Bitmap bitmap = Bitmap.createBitmap(ivFleshP.width, ivFleshP.height, Bitmap.Config.ARGB_8888);
                    Path path = new Path();
                    path.moveTo(0, ivFleshP.height - 3);
                    path.lineTo((int) (ivFleshP.width / 2), 3);
                    path.lineTo(ivFleshP.width, ivFleshP.height - 3);
                    Canvas canvas = new Canvas(bitmap);
                    canvas.drawPath(path, paint);
                    ivFlesh.setImageBitmap(bitmap);
                }
            }
        }
    }

    public void onBackPressed() {
        finish();
    }

}