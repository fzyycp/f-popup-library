package cn.faury.android.library.popup;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.annotation.StyleRes;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;

import java.util.ArrayList;
import java.util.List;

/**
 * 弹出菜单
 */
public class PopupMenu {
    /**
     * 所在页面Activity
     */
    private Activity activity;

    /**
     * 弹出框对象
     */
    private PopupWindow popupWindow;

    /**
     * 菜单项点击事件
     */
    private OnMenuItemClickListener onMenuItemClickListener;

    /**
     * 菜单项显示前事件
     */
    private OnBeforeShowListener onBeforeShowListener;

    /**
     * 菜单项关闭后事件
     */
    private PopupWindow.OnDismissListener onDismissListener;

    /**
     * 弹窗列表适配器
     */
    private PopupListMenuAdapter popupListMenuAdapter;

    /**
     * 弹窗列表数组
     */
    private List<MenuItem> menuItems = new ArrayList<>();

    /**
     * 弹出框高度
     */
    private int popHeight = RecyclerView.LayoutParams.WRAP_CONTENT;

    /**
     * 弹出框宽度
     */
    private int popWidth = RecyclerView.LayoutParams.WRAP_CONTENT;

    /**
     * 是否显示按钮图标
     */
    private boolean showIcon = true;

    /**
     * 是否显示背景置灰
     */
    private boolean enableBackgroundDark = true;

    /**
     * 是否设置动画
     */
    private boolean showAnimation = true;

    /**
     * 动画样式
     */
    @StyleRes
    private int animationStyle = R.style.f_library_popup_menu_anim_style;

    /**
     * 背景alpha值
     */
    private float alpha = 0.75f;

    /**
     * 动画时间
     */
    private int duration = 240;

    /**
     * 初始化弹出菜单
     *
     * @param context 上下文
     */
    public PopupMenu(Activity context) {
        this.activity = context;
    }

    /**
     * 设置高度(默认WRAP_CONTENT)
     *
     * @param height 目标高度
     * @return 当前对象
     */
    public PopupMenu height(int height) {
        if (height <= 0 && height != RecyclerView.LayoutParams.MATCH_PARENT) {
            this.popHeight = RecyclerView.LayoutParams.WRAP_CONTENT;
        } else {
            this.popHeight = height;
        }
        return this;
    }

    /**
     * 设置高度(默认WRAP_CONTENT)
     *
     * @param width 目标宽度
     * @return 当前对象
     */
    public PopupMenu width(int width) {
        if (width <= 0 && width != RecyclerView.LayoutParams.MATCH_PARENT) {
            this.popWidth = RecyclerView.LayoutParams.WRAP_CONTENT;
        } else {
            this.popWidth = width;
        }
        return this;
    }

    /**
     * 是否显示菜单图标
     *
     * @param isShow 是否显示
     * @return 当前对象
     */
    public PopupMenu showIcon(boolean isShow) {
        this.showIcon = isShow;
        return this;
    }

    /**
     * 添加单个菜单
     *
     * @param item 菜单项
     * @return 当前对象
     */
    public PopupMenu addMenuItem(MenuItem item) {
        menuItems.add(item);
        return this;
    }

    /**
     * 添加多个菜单
     *
     * @param items 菜单列表
     * @return 当前对象
     */
    public PopupMenu addMenuItem(List<MenuItem> items) {
        menuItems.addAll(items);
        return this;
    }

    /**
     * 是否让背景变暗
     *
     * @param enable 是否变暗
     * @return 当前对象
     */
    public PopupMenu enableBackgroundDark(boolean enable) {
        this.enableBackgroundDark = enable;
        return this;
    }

    /**
     * 设置透明度
     *
     * @param alpha 透明度（0~1）
     * @return 当前对象
     */
    public PopupMenu alpha(float alpha) {
        if (alpha >= 0 && alpha <= 1) {
            this.alpha = alpha;
        }
        return this;
    }

    /**
     * 设置动画时间
     *
     * @param duration 动画时间
     * @return 当前对象
     */
    public PopupMenu duration(int duration) {
        if (duration > 0) {
            this.duration = duration;
        }
        return this;
    }

    /**
     * 否是需要动画
     *
     * @param showAnimation 是否需要动画
     * @return 当前对象
     */
    public PopupMenu showAnimation(boolean showAnimation) {
        this.showAnimation = showAnimation;
        return this;
    }

    /**
     * 设置动画
     *
     * @param style 设置动画
     * @return 当前对象
     */
    public PopupMenu animationStyle(@StyleRes int style) {
        if (style > 0) {
            this.animationStyle = style;
        }
        return this;
    }

    /**
     * 设置菜单点击事件
     *
     * @param listener 监听器
     * @return 当前对象
     */
    public PopupMenu onMenuItemClickListener(OnMenuItemClickListener listener) {
        this.onMenuItemClickListener = listener;
        return this;
    }

    /**
     * 设置菜单显示前事件
     *
     * @param listener 监听器
     * @return 当前对象
     */
    public PopupMenu onBeforeShowListener(OnBeforeShowListener listener) {
        this.onBeforeShowListener = listener;
        return this;
    }

    /**
     * 设置菜单消失后事件
     *
     * @param listener 监听器
     * @return 当前对象
     */
    public PopupMenu onDismissListener(PopupWindow.OnDismissListener listener) {
        this.onDismissListener = listener;
        return this;
    }

    /**
     * 显示下拉菜单
     *
     * @param anchor 目标对象
     * @return 弹出窗口对象
     */
    public PopupWindow showAsDropDown(View anchor) {
        this.build();
        if (!popupWindow.isShowing()) {
            if (onBeforeShowListener != null) {
                this.onBeforeShowListener.onBeforeShow(popupWindow);
            }
            backgroundDark();
            popupWindow.showAsDropDown(anchor);
        }
        return popupWindow;
    }

    /**
     * 显示下拉菜单
     *
     * @param anchor  目标对象
     * @param xOffset 横向偏移
     * @param yOffset 纵向偏移
     * @return 弹出窗口对象
     */
    public PopupWindow showAsDropDown(View anchor, int xOffset, int yOffset) {
        this.build();
        if (!popupWindow.isShowing()) {
            if (onBeforeShowListener != null) {
                this.onBeforeShowListener.onBeforeShow(popupWindow);
            }
            backgroundDark();
            popupWindow.showAsDropDown(anchor, xOffset, yOffset);
        }
        return popupWindow;
    }

    /**
     * 显示下拉菜单
     *
     * @param anchor  目标对象
     * @param xOffset 横向偏移
     * @param yOffset 纵向偏移
     * @return 弹出窗口对象
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public PopupWindow showAsDropDown(View anchor, int xOffset, int yOffset, int gravity) {
        this.build();
        if (!popupWindow.isShowing()) {
            if (onBeforeShowListener != null) {
                this.onBeforeShowListener.onBeforeShow(popupWindow);
            }
            backgroundDark();
            popupWindow.showAsDropDown(anchor, xOffset, yOffset, gravity);
        }
        return popupWindow;
    }

    /**
     * 显示下拉菜单
     *
     * @param parent  父容器
     * @param gravity 基准点
     * @param x       x轴偏移
     * @param y       y轴偏移
     * @return 弹出窗口对象
     */
    public PopupWindow showAtLocation(View parent, int gravity, int x, int y) {
        this.build();
        if (!popupWindow.isShowing()) {
            if (onBeforeShowListener != null) {
                this.onBeforeShowListener.onBeforeShow(popupWindow);
            }
            backgroundDark();
            popupWindow.showAtLocation(parent, gravity, x, y);
        }
        return popupWindow;
    }

    /**
     * 关闭对话框
     */
    public void dismiss() {
        if (popupWindow != null && popupWindow.isShowing()) {
            popupWindow.dismiss();
        }
    }

    /**
     * 获取弹出框
     *
     * @return 弹出框
     */
    public PopupWindow build() {
        if (popupWindow == null) {
            initPopupWindow();
        }
        return popupWindow;
    }

    /**
     * 初始化弹窗
     *
     * @return 弹窗对象
     */
    private PopupWindow initPopupWindow() {
        synchronized (this) {
            if (popupWindow == null) {
                View contentView = LayoutInflater.from(activity).inflate(R.layout.f_library_popup_menu, null);
                popupWindow = new PopupWindow(contentView);
                popupWindow.setHeight(popHeight);
                popupWindow.setWidth(popWidth);
                if (showAnimation) {
                    popupWindow.setAnimationStyle(animationStyle);
                }
                popupWindow.setFocusable(true);
                popupWindow.setOutsideTouchable(true);
                popupWindow.setBackgroundDrawable(new ColorDrawable());
                popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        backgroundReset();
                        if (onDismissListener != null) {
                            onDismissListener.onDismiss();
                        }
                    }
                });

                popupListMenuAdapter = new PopupListMenuAdapter(activity, this, menuItems, showIcon);
                if (this.onMenuItemClickListener != null) {
                    popupListMenuAdapter.setOnMenuItemClickListener(this.onMenuItemClickListener);
                }
                RecyclerView recyclerView = contentView.findViewById(R.id.recyclerview);
                recyclerView.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false));
                recyclerView.setAdapter(popupListMenuAdapter);
                popupWindow.update();
            }
        }
        return popupWindow;
    }

    /**
     * 背景变暗
     */
    private void backgroundDark() {
        setBackgroundAlpha(1f, alpha);
    }

    /**
     * 背景复原
     */
    private void backgroundReset() {
        if (enableBackgroundDark) {
            setBackgroundAlpha(alpha, 1f);
        }
    }


    /**
     * 设置背景色透明度
     *
     * @param from 起始透明度
     * @param to   目标透明度
     */
    private void setBackgroundAlpha(float from, float to) {
        final WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
        ValueAnimator animator = ValueAnimator.ofFloat(from, to);
        animator.setDuration(duration);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                lp.alpha = (float) animation.getAnimatedValue();
                activity.getWindow().setAttributes(lp);
            }
        });
        animator.start();
    }

    /**
     * 菜单项单击事件
     */
    public interface OnMenuItemClickListener {
        void onMenuItemClick(int position, MenuItem item);
    }

    /**
     * 菜单显示前事件
     */
    public interface OnBeforeShowListener {
        void onBeforeShow(PopupWindow popupWindow);
    }
}
