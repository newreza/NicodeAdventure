/*
    Written by newreza🍷
    Telegram: @newreza
    ✉ Mail: newreza7@gmail.com
    Version 1.2
    Updates: added mode LINEAR;

    ***For legal use, don't remove this comment***
 */

package ir.newreza.nicodeadventure.Modules;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class MyAnimator {
    private boolean stop, animationEnded;
    private ArrayList<Boolean> comeBacks;
    private int delay;
    public final static int TRANSLATE_X = 1, TRANSLATE_Y = 2, ALPHA = 3, SCALE = 4, SCALE_X = 5, SCALE_Y = 6, ROTATION = 7, ROTATION_X = 8, ROTATION_Y = 9,
            SIZE = 10, BACKGROUND_COLOR = 11, GRADIENT_COLOR = 12, TEXT_COLOR = 13, PADDING = 14, ICON_RATIO = 15, WIDTH = 16, HEIGHT = 17;
    private ArrayList<Integer> methods, repeats;
    private ArrayList<Float> starts, offsets;
    private ArrayList<Float[]> values;
    public final static String SIGMOID = "sigmoid", SINE = "sine", LINEAR = "linear";
    private Context context;
    private OnAnimationListener onAnimationListener;
    private View mView;
    GradientDrawable gd;

    public MyAnimator() {
        setFPS(60);
        stop = true;
        methods = new ArrayList<>();
        comeBacks = new ArrayList<>();
        repeats = new ArrayList<>();
        values = new ArrayList<>();
        starts = new ArrayList<>();
        offsets = new ArrayList<>();
        onAnimationListener = new OnAnimationListener() {
            public void onAnimationStart(View view) {
            }

            public void onAnimationEnd(View view) {
            }
        };
    }

    public void setFPS(int FPS) {
        delay = (int) Math.ceil((float) 1000 / FPS);
    }

    public void startColorAnimation(final int method, int start, int end, int duration, final View view) {
        startColorAnimation(method, start, end, duration, view, null);
    }

    public void startColorAnimation(final int method, int start, int end, int duration, final View view, GradientDrawable drawable) {
        gd = drawable;
        if (gd == null) {
            gd = new GradientDrawable();
            gd.setCornerRadius(Integer.MAX_VALUE);
        }
        ValueAnimator animator = ValueAnimator.ofObject(new ArgbEvaluator(), start, end);
        animator.setDuration(duration);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animator) {
                if (method == BACKGROUND_COLOR) {
                    view.setBackgroundColor((int) animator.getAnimatedValue());
                } else if (method == GRADIENT_COLOR) {
                    gd.setColor((int) animator.getAnimatedValue());
                    view.setBackground(gd);
                } else if (method == TEXT_COLOR) {
                    ((TextView) view).setTextColor((int) animator.getAnimatedValue());
                }
            }
        });
        animator.start();
    }

    public void setAnimation(int method, float start, float end, String mode, int duration) {
        setAnimation(method, start, end, mode, duration, false, 1, 0);
    }

    public void setAnimation(int method, float start, float end, String mode, int duration, boolean comeBack, int repeat) {
        setAnimation(method, start, end, mode, duration, comeBack, repeat, 0);
    }

    public void setAnimation(int method, float start, float end, String mode, int duration, boolean comeBack, int repeat, float offset) {
        methods.add(method);
        comeBacks.add(comeBack);
        repeats.add(repeat);
        starts.add(start);
        offsets.add(offset);
        int frames = (int) Math.ceil((float) duration / delay);
        float d = end - start;
        int offsetI = 0;
        if (mode.equals(SINE)) {
            offsetI = (int) (Math.asin(offset) / (Math.PI / 2) * frames);
        } else if (mode.equals(LINEAR)) {
            offsetI = (int) (offset * frames);
        }
        int i = 0, length = frames - offsetI;
        Float[] values = new Float[length + 1];
        switch (mode) {
            case SIGMOID:
                for (i = 0; i < length; i++) {
                    values[i] = (float) (start + (float) 1 / (1 + Math.pow(Math.E, (float) (i + offsetI) / frames * -12 + 6)) * d);
                }
                break;
            case SINE:
                for (i = 0; i < length; i++) {
                    values[i] = start + (float) Math.sqrt(1 - Math.pow((float) (frames - (i + offsetI)) / frames, 2)) * d;
                }
                break;
            case LINEAR:
                for (i = 0; i < length; i++) {
                    values[i] = start + (float) (i + offsetI) / frames * d;
                }
                break;
        }
        values[i] = end;
        this.values.add(values);
    }

    public void start(View view) {
        mView = view;
        context = mView.getContext();
        stop = animationEnded = false;
        int size = methods.size();
        onAnimationListener.onAnimationStart(mView);
        for (int i = 0; i < size; i++) {
            final int finalI = i;
            new Thread() {
                public void run() {
                    final Float[] values = MyAnimator.this.values.get(finalI);
                    boolean comBack = comeBacks.get(finalI);
                    int length = comBack ? 2 * values.length - 2 : values.length - 1;
                    int repeat = repeats.get(finalI) == 0 ? Integer.MAX_VALUE : repeats.get(finalI);
                    final int method = methods.get(finalI);
                    for (int j = 0; j < repeat; j++) {
                        for (int k = 1; k <= length; k++) {
                            if (stop && !animationEnded) {
                                animationEnded = true;
                                ((Activity) context).runOnUiThread(new Runnable() {
                                    public void run() {
                                        onAnimationListener.onAnimationEnd(mView);
                                    }
                                });
                                return;
                            }
                            final int finalK = k < values.length ? k : length - k;
                            ((Activity) context).runOnUiThread(new Runnable() {
                                public void run() {
                                    if (method == TRANSLATE_X) {
                                        mView.setTranslationX(values[finalK]);
                                        return;
                                    }
                                    if (method == TRANSLATE_Y) {
                                        mView.setTranslationY(values[finalK]);
                                        return;
                                    }
                                    if (method == ALPHA) {
                                        mView.setAlpha(values[finalK]);
                                        return;
                                    }
                                    if (method == SCALE) {
                                        mView.setScaleX(values[finalK]);
                                        mView.setScaleY(values[finalK]);
                                        return;
                                    }
                                    if (method == SCALE_X) {
                                        mView.setScaleX(values[finalK]);
                                        return;
                                    }
                                    if (method == SCALE_Y) {
                                        mView.setScaleY(values[finalK]);
                                        return;
                                    }
                                    if (method == ROTATION) {
                                        mView.setRotation(values[finalK]);
                                        return;
                                    }
                                    if (method == ROTATION_X) {
                                        mView.setRotationX(values[finalK]);
                                        return;
                                    }
                                    if (method == ROTATION_Y) {
                                        mView.setRotationY(values[finalK]);
                                    }
                                    if (method == WIDTH) {
                                        ViewGroup.MarginLayoutParams mViewP = (ViewGroup.MarginLayoutParams) mView.getLayoutParams();
                                        mViewP.width = (int) (values[finalK].floatValue());
                                        mView.setLayoutParams(mViewP);
                                        return;
                                    }
                                    if (method == HEIGHT) {
                                        ViewGroup.MarginLayoutParams mViewP = (ViewGroup.MarginLayoutParams) mView.getLayoutParams();
                                        mViewP.height = (int) (values[finalK].floatValue());
                                        mView.setLayoutParams(mViewP);
                                        return;
                                    }
                                    if (method == SIZE) {
                                        ViewGroup.MarginLayoutParams mViewP = (ViewGroup.MarginLayoutParams) mView.getLayoutParams();
                                        mViewP.width = (int) (values[finalK].floatValue());
                                        mViewP.height = (int) (values[finalK].floatValue());
                                        mView.setLayoutParams(mViewP);
                                        return;
                                    }
                                    if (method == PADDING) {
                                        mView.setPadding((int) values[finalK].floatValue(), (int) values[finalK].floatValue(), (int) values[finalK].floatValue(), (int) values[finalK].floatValue());
                                        return;
                                    }
                                }
                            });
                            try {
                                sleep(delay);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    if (!animationEnded) {
                        animationEnded = true;
                        ((Activity) context).runOnUiThread(new Runnable() {
                            public void run() {
                                stop = true;
                                onAnimationListener.onAnimationEnd(mView);
                            }
                        });
                    }
                }
            }.start();
        }
    }

    public boolean isAnimating() {
        return !stop;
    }

    public void stop() {
        stop = true;
    }

    public void setOnAnimationListener(OnAnimationListener onAnimationListener) {
        this.onAnimationListener = onAnimationListener;
    }

    public interface OnAnimationListener {
        void onAnimationStart(View view);

        void onAnimationEnd(View view);
    }

}
