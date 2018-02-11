package cn.faury.android.library.popup;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * 弹出列表式菜单适配器
 */
public class PopupListMenuAdapter extends RecyclerView.Adapter<PopupListMenuAdapter.ViewHolder> {
    private Context context;
    private List<MenuItem> menuItemList;
    private boolean showIcon;
    private PopupMenu popupMenu;
    private PopupMenu.OnMenuItemClickListener onMenuItemClickListener;

    public PopupListMenuAdapter(@NonNull Context context, @NonNull PopupMenu popupMenu, @NonNull List<MenuItem> menuItemList, boolean show) {
        this.context = context;
        this.popupMenu = popupMenu;
        this.menuItemList = menuItemList;
        this.showIcon = show;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.f_library_popup_menu_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final MenuItem menuItem = menuItemList.get(position);
        int resId = menuItem.getIcon();
        if (showIcon && resId > 0) {
            holder.icon.setVisibility(View.VISIBLE);
            holder.icon.setImageResource(resId);
        } else {
            holder.icon.setVisibility(View.GONE);
        }
        holder.text.setText(menuItem.getText());

        if (position == 0) {
            holder.viewGroup.setBackgroundDrawable(addStateDrawable(context, -1, R.drawable.f_library_popup_top_pressed));
        } else if (position == menuItemList.size() - 1) {
            holder.viewGroup.setBackgroundDrawable(addStateDrawable(context, -1, R.drawable.f_library_popup_bottom_pressed));
        } else {
            holder.viewGroup.setBackgroundDrawable(addStateDrawable(context, -1, R.drawable.f_library_popup_middle_pressed));
        }
        final int pos = holder.getAdapterPosition();
        holder.viewGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupMenu.dismiss();
                if (onMenuItemClickListener != null) {
                    onMenuItemClickListener.onMenuItemClick(pos, menuItem);
                }
            }
        });
    }

    private StateListDrawable addStateDrawable(Context context, int normalId, int pressedId) {
        StateListDrawable sd = new StateListDrawable();
        Drawable normal = normalId == -1 ? null : context.getResources().getDrawable(normalId);
        Drawable pressed = pressedId == -1 ? null : context.getResources().getDrawable(pressedId);
        sd.addState(new int[]{android.R.attr.state_pressed}, pressed);
        sd.addState(new int[]{}, normal);
        return sd;
    }

    @Override
    public int getItemCount() {
        return menuItemList == null ? 0 : menuItemList.size();
    }

    public void setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener listener) {
        this.onMenuItemClickListener = listener;
    }

    /**
     * 列表界面容器
     */
    class ViewHolder extends RecyclerView.ViewHolder {
        ViewGroup viewGroup;
        ImageView icon;
        TextView text;

        ViewHolder(View itemView) {
            super(itemView);
            viewGroup = (ViewGroup) itemView;
            icon = itemView.findViewById(R.id.menu_item_icon);
            text = itemView.findViewById(R.id.menu_item_text);
        }
    }

}
