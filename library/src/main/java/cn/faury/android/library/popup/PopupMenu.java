package cn.faury.android.library.popup;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
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
     * 弹窗列表适配器
     */
    private PopupListMenuAdapter popupListMenuAdapter;

    /**
     * 弹窗列表数组
     */
    private List<MenuItem> menuItems = new ArrayList<>();

    /**
     * 默认菜单项行高
     */
    private static final int DEFAULT_ITEM_HEIGHT = 160;

    /**
     * 弹出框高度
     */
    private int popHeight = getDefaultHeight();

    /**
     * 弹出框宽度
     */
    private int popWidth = RecyclerView.LayoutParams.WRAP_CONTENT;

    /**
     * 是否显示按钮图标
     */
    private boolean isShowIcon = true;

    /**
     * 是否显示背景置灰
     */
    private boolean enableBackgroundDark = true;

    /**
     * 是否设置动画
     */
    private boolean isAnimation = true;

    /**
     * 动画样式
     */
    private int animationStyle;

    /**
     * 背景alpha值
     */
    private final float alpha = 0.75f;

    /**
     * 初始化弹出菜单
     *
     * @param context 上下文
     */
    public PopupMenu(Activity context) {
        this.activity = context;
    }

    /**
     * 设置高度
     *
     * @param height 目标高度
     * @return 当前对象
     */
    public PopupMenu setHeight(int height) {
        if (height <= 0 && height != RecyclerView.LayoutParams.MATCH_PARENT
                && height != RecyclerView.LayoutParams.WRAP_CONTENT) {
            this.popHeight = getDefaultHeight();
        } else {
            this.popHeight = height;
        }
        return this;
    }

    /**
     * 设置高度
     *
     * @param width 目标宽度
     * @return 当前对象
     */
    public PopupMenu setWidth(int width) {
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
        this.isShowIcon = isShow;
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
     * 否是需要动画
     *
     * @param isAnimation 是否需要动画
     * @return 当前对象
     */
    public PopupMenu showAnimation(boolean isAnimation) {
        this.isAnimation = isAnimation;
        return this;
    }

    /**
     * 设置动画
     *
     * @param style 设置动画
     * @return 当前对象
     */
    public PopupMenu setAnimationStyle(int style) {
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
    public PopupMenu setOnMenuItemClickListener(OnMenuItemClickListener listener) {
        this.onMenuItemClickListener = listener;
        return this;
    }

    /**
     * 显示下拉菜单
     *
     * @param anchor 目标对象
     * @return 当前对象
     */
    public PopupMenu showAsDropDown(View anchor) {
        showAsDropDown(anchor, 0, 0);
        return this;
    }

    /**
     * 显示下拉菜单
     *
     * @param anchor  目标对象
     * @param xOffset 横向偏移
     * @param yOffset 纵向偏移
     * @return 当前对象
     */
    public PopupMenu showAsDropDown(View anchor, int xOffset, int yOffset) {
        if (popupWindow == null) {
            initPopupWindow();
        }
        if (!popupWindow.isShowing()) {
            popupWindow.showAsDropDown(anchor, xOffset, yOffset);
            if (enableBackgroundDark) {
                setBackgroundAlpha(1f, alpha, 240);
            }
        }
        return this;
    }

    /**
     * 初始化弹窗
     *
     * @return 弹窗对象
     */
    private PopupWindow initPopupWindow() {

        View contentView = LayoutInflater.from(activity).inflate(R.layout.f_library_popup_menu, null);
        popupWindow = new PopupWindow(contentView);
        popupWindow.setHeight(popHeight);
        popupWindow.setWidth(popWidth);
        if (isAnimation) {
            popupWindow.setAnimationStyle(animationStyle <= 0 ? R.style.f_library_popup_menu_anim_style : animationStyle);
        }

        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new ColorDrawable());
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                if (enableBackgroundDark) {
                    setBackgroundAlpha(alpha, 1f, 300);
                }
            }
        });

        popupListMenuAdapter = new PopupListMenuAdapter(activity, this, menuItems, isShowIcon);
        popupListMenuAdapter.setData(menuItems);
        popupListMenuAdapter.setShowIcon(isShowIcon);
        if (this.onMenuItemClickListener != null) {
            popupListMenuAdapter.setOnMenuItemClickListener(this.onMenuItemClickListener);
        }
        RecyclerView recyclerView = contentView.findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(popupListMenuAdapter);
        popupWindow.update();
        return popupWindow;
    }

    /**
     * 获取默认高度
     *
     * @return 根据ITEM个数计算默认高度
     */
    private int getDefaultHeight() {
        if (this.menuItems == null || this.menuItems.size() <= 0) {
            return DEFAULT_ITEM_HEIGHT;
        }
        return this.menuItems.size() * DEFAULT_ITEM_HEIGHT;
    }

    /**
     * 设置背景色透明度
     *
     * @param from     起始透明度
     * @param to       目标透明度
     * @param duration 过渡时间
     */
    private void setBackgroundAlpha(float from, float to, int duration) {
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
     * 关闭对话框
     */
    public void dismiss() {
        if (popupWindow != null && popupWindow.isShowing()) {
            popupWindow.dismiss();
        }
    }

    /**
     * 菜单项单击事件
     */
    public interface OnMenuItemClickListener {
        void onMenuItemClick(int position, MenuItem item);
    }
}
