package cn.faury.android.library.popup;

import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.annotation.StringRes;

/**
 * 菜单项
 */
public class MenuItem {
    /**
     * 菜单ID
     */
    private int id;
    /**
     * 菜单图片
     */
    private int icon;

    /**
     * 菜单内容
     */
    private int text;

    /**
     * 构造函数
     * @param text 菜单内容ID
     */
    public MenuItem(@StringRes int text) {
        this.text = text;
    }

    /**
     * 构造函数
     * @param iconId 图标ID
     * @param text 菜单内容ID
     */
    public MenuItem(@DrawableRes int iconId, @StringRes int text) {
        this.icon = iconId;
        this.text = text;
    }

    /**
     * 构造函数
     *
     * @param id 菜单ID
     * @param iconId 图标ID
     * @param text 菜单内容ID
     */
    public MenuItem(@IdRes int id, @DrawableRes int iconId, @StringRes int text) {
        this.id = id;
        this.icon = iconId;
        this.text = text;
    }

    @IdRes
    public int getId() {
        return id;
    }

    public void setId(@IdRes int id) {
        this.id = id;
    }

    @DrawableRes
    public int getIcon() {
        return icon;

    }

    public void setIcon(@DrawableRes int iconId) {
        this.icon = iconId;
    }

    @StringRes
    public int getText() {
        return text;
    }

    public void setText(@StringRes int text) {
        this.text = text;
    }
}
