package com.xhtt.hiddendanger.View;

import android.content.Context;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorListenerAdapter;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;

import org.jetbrains.annotations.NotNull;

public class BottomHideBehavior extends CoordinatorLayout.Behavior<View> {

    public BottomHideBehavior() {
        super();
    }

    public BottomHideBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onStartNestedScroll(@NotNull CoordinatorLayout coordinatorLayout, @NotNull View child,
                                       @NotNull View directTargetChild, @NotNull View target, int nestedScrollAxes, int type) {
        return nestedScrollAxes == ViewCompat.SCROLL_AXIS_VERTICAL;
    }

    @Override
    public void onNestedScroll(@NotNull CoordinatorLayout coordinatorLayout, @NotNull View child,
                               @NotNull View target, int dxConsumed, int dyConsumed,
                               int dxUnconsumed, int dyUnconsumed, int type) {
        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, type);

        if (moveY == 0) {
            int height = child.getMeasuredHeight();
            CoordinatorLayout.LayoutParams clp = (CoordinatorLayout.LayoutParams) child.getLayoutParams();
            int margin = clp.bottomMargin;

            moveY = height + margin;
        }

        if (dyConsumed > 0 || dyUnconsumed > 0) {
            hide(child);
        } else if (dyConsumed < 0 || dyUnconsumed < 0) {
            show(child);
        }
    }

    private boolean isAnim = false;
    private boolean isShow = true;
    private int moveY = 0;

    private void hide(View view) {

        if (!isAnim && isShow) {
            isAnim = true;

            AnimatorSet set = new AnimatorSet();
            set.playTogether(
                    ObjectAnimator.ofFloat(view, "translationY", 0, moveY)
            );
            set.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    isAnim = false;
                    isShow = false;
                }
            });
            set.setDuration(300).start();
        }
    }

    private void show(View view) {

        if (!isAnim && !isShow) {
            isAnim = true;

            AnimatorSet set = new AnimatorSet();
            set.playTogether(
                    ObjectAnimator.ofFloat(view, "translationY", moveY, 0)
            );
            set.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    isAnim = false;
                    isShow = true;
                }
            });
            set.setDuration(300).start();
        }
    }

}
