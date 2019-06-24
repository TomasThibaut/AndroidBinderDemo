package cn.gc.module2;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * Created by duoyang on 2017/7/26.
 */

/**
 * 系统的RecyclerView并没有支持addHeader和addFooter
 * 此类添加了addHeader和addFooter
 * <p>
 * 若要使用此类，请必须实现以下方法:
 * {@link #_getItemCount()},
 * {@link #_onCreateViewHolder(ViewGroup, int)}.
 * <p>
 * 如果要实现两种以上的viewType，请实现{@link #_getItemViewType(int)}
 * <p>
 * 限制：
 * 此类对headers和footers的数量，以及viewType值进行了限制，
 * headers和footers的数量不可以大于16
 * {@link #_getItemViewType(int)}返回的viewType值不可以大于 （Integer.MAX_VALUE ^ 31）
 */
public abstract class BaseRecyclerAdapter<VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final int TYPE_NORMAL = 0;
    /*二进制1111=15*/
    public static final int TYPE_HEADER = Integer.MAX_VALUE ^ 15;
    /*二进制11111=31*/
    public static final int TYPE_FOOTER = Integer.MAX_VALUE ^ 31;

    private ArrayList<View> headers = new ArrayList<>();
    private ArrayList<View> footers = new ArrayList<>();

    public void addHeader(@NonNull View header) {
        if (headers.size() >= 16) throw new RuntimeException("header的数量不可以大于16");
        headers.add(header);
        notifyDataSetChanged();
    }

    public void removeHeader(View header) {
        headers.remove(header);
        notifyDataSetChanged();
    }

    public void removeFooter(View footer) {
        footers.remove(footer);
        notifyDataSetChanged();
    }

    public void addFooter(@NonNull View footer) {
        if (footers.size() >= 16) throw new RuntimeException("footer的数量不可以大于16");
        footers.add(footer);
        notifyDataSetChanged();
    }

    public ArrayList<View> getHeaders() {
        return headers;
    }

    public ArrayList<View> getFooters() {
        return footers;
    }

    public int getHeaderCount() {
        return headers.size();
    }

    public int getFooterCount() {
        return footers.size();
    }


    /**
     * 返回真是item数量
     * 该方法必须实现，
     * 功能等同于原来的{@link RecyclerView.Adapter#getItemCount()}
     *
     * @return
     */
    public abstract int _getItemCount();

    /**
     * 该方法返回item加上头部尾部的数量
     * 该方法已被final，请实现 {@link #_getItemCount()}
     *
     * @return
     */
    @Override
    public final int getItemCount() {
        return getHeaderCount() + getFooterCount() + _getItemCount();
    }


    /**
     * 如果要实现两种以上viewType的adapter，请实现该方法，
     * 功能等同于原来的{@link RecyclerView.Adapter#getItemViewType(int)}
     * 返回viewtype值，默认返回0，
     * 若有需要可以覆盖，但是返回的viewtype值不可以大于（Integer.MAX_VALUE << 5）
     *
     * @param position
     * @return
     */
    public int _getItemViewType(int position) {
        return TYPE_NORMAL;
    }

    /**
     * 该方法已被final，请使用 {@link #_getItemViewType(int)}
     *
     * @return
     */
    @Override
    public final int getItemViewType(int position) {
        /**
         * 头部返回的viewtype为大于（Integer.MAX_VALUE << 4）
         * 后四位代表headers中的position
         */

        if (position < getHeaderCount()) {
            Log.i("G_C", "BaseRecyclerAdapter getItemViewType: "+ (TYPE_HEADER + position));
            return TYPE_HEADER + position;
        }

        /**
         * 为了区别header，
         * 尾部返回的viewtype为大于（Integer.MAX_VALUE << 5）
         * 后四位代表headers中的position
         */
        if (position >= getHeaderCount() + _getItemCount())
            return TYPE_FOOTER + position - getHeaderCount() - _getItemCount();

        /**
         * 返回正常的viewtype
         * 检测viewtype值，若大于（Integer.MAX_VALUE << 5），抛出异常
         */
        int type = _getItemViewType(position - getHeaderCount());
        if (type > TYPE_FOOTER) throw new RuntimeException("viewtype值不可以大于" + TYPE_FOOTER);
        return type;
    }


    /**
     * 创建普通item的ViewHolder
     * 此方法必须实现，功能等同于原来的{@link RecyclerView.Adapter#onCreateViewHolder(ViewGroup, int)}
     *
     * @param parent
     * @param viewType
     * @return
     */
    public abstract RecyclerView.ViewHolder _onCreateViewHolder(ViewGroup parent, int viewType);

    /**
     * 创建头部、尾部、普通item的ViewHolder
     * 该方法已被final，请使用 {@link #_onCreateViewHolder(ViewGroup, int)}
     *
     * @return
     */
    @Override
    public final RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            Log.i("G_C", "BaseRecyclerAdapter onCreateViewHolder viewType: " + (viewType));
        if (viewType >= TYPE_HEADER) {
            View header = headers.get(viewType - TYPE_HEADER);
            return new HeaderFooterViewHolder(header);
        } else if (viewType >= TYPE_FOOTER) {
            View footer = footers.get(viewType - TYPE_FOOTER);
            return new HeaderFooterViewHolder(footer);
        }
        return _onCreateViewHolder(parent, viewType);
    }


    /**
     * 绑定item
     * itemView绑定数据，功能等同于原来的{@link RecyclerView.Adapter#onBindViewHolder(RecyclerView.ViewHolder, int)}
     *
     * @param holder
     * @param realPosition 在item中的位置
     */
    public abstract void _onBindViewHolder(VH holder, int realPosition);


    /**
     * 该方法已被final，请使用 {@link #_onBindViewHolder(RecyclerView.ViewHolder, int)}
     *
     * @param holder
     * @param position
     */
    @Override
    @SuppressWarnings("unchecked")
    public final void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        /*头部尾部不需要继续bind数据*/
        if (position < getHeaderCount()) return;
        if (position >= _getItemCount() + getHeaderCount()) return;

        /*计算真实position*/
        int realP = position - getHeaderCount();
        _onBindViewHolder((VH) holder, realP);
    }


    public static class HeaderFooterViewHolder extends RecyclerView.ViewHolder {
        public HeaderFooterViewHolder(View itemView) {
            super(itemView);
        }
    }

}
