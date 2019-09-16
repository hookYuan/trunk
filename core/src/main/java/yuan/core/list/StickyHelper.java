package yuan.core.list;


import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * RecyclerView 悬浮Decoration
 *
 * @author yuanye
 * @date 2018/12/12
 */
public class StickyHelper extends RecyclerView.ItemDecoration {
    /**
     * 缓存当前的View
     */
    private View floatingView;
    /**
     * 缓存当前悬浮View的位置
     */
    private int oldPosition = -1;
    /**
     * 悬浮布局Id
     */
    private int floatLayoutId;

    private StickyCallback stickyCallback;

    public StickyHelper(StickyCallback stickyCallback) {
        this.stickyCallback = stickyCallback;
    }

    /**
     * 获取Item偏移量，为每个Item设置偏移量，例如分割线
     *
     * @param outRect
     * @param view
     * @param parent
     * @param state
     */
    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
    }

    /**
     * 绘制背景，通过该方法，在Canvas上绘制内容，在绘制Item之前调用。
     * （如果没有通过getItemOffsets设置偏移的话，Item的内容会将其覆盖）
     *
     * @param c
     * @param parent
     * @param state
     */
    @Override
    public void onDraw(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.onDraw(c, parent, state);
    }


    /**
     * 绘制前景 onDrawOver：通过该方法，在Canvas上绘制内容,在Item之后调用。
     * (画的内容会覆盖在item的上层)
     *
     * @param c
     * @param parent
     * @param state
     */
    @Override
    public void onDrawOver(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.onDrawOver(c, parent, state);
        //确保包含有子View,才绘制悬浮View
        if (parent.getChildCount() > 0) {
            //获取第一个可见item的position
            int firstVisiblePosition = ((LinearLayoutManager) parent.getLayoutManager()).findFirstVisibleItemPosition();
            int itemHeight = parent.getLayoutManager().findViewByPosition(firstVisiblePosition).getMeasuredHeight();
            int itemWith = parent.getMeasuredWidth();
            int left = parent.getPaddingLeft();
            int right = parent.getMeasuredWidth() - parent.getPaddingRight();
            //加载FloatingView、添加一层包裹View,解决悬浮调layout失效的bug
            floatingView = stickyCallback.getStickyView();
            if (floatingView == null) return;
            //指定View的宽/高
            RecyclerView.LayoutParams params = new RecyclerView.LayoutParams(itemWith, itemHeight);
            floatingView.setLayoutParams(params);
            floatingView.measure(View.MeasureSpec.makeMeasureSpec(itemWith, View.MeasureSpec.EXACTLY),
                    View.MeasureSpec.makeMeasureSpec(itemHeight, View.MeasureSpec.EXACTLY));

            //获取顶部第一个View,当两个Section交汇时，逐渐刷新FloatingView的高度
            View topView = parent.getChildAt(0);
            int floatViewHeight = floatingView.getMeasuredHeight();
            int topViewHeight = Math.abs(topView.getTop());

            if (firstVisiblePosition < state.getItemCount() - 1
                    && (itemHeight - topViewHeight) <= floatViewHeight) {
                floatingView.layout(left, floatViewHeight - (itemHeight - topViewHeight)
                        , right, floatViewHeight - (floatViewHeight - (itemHeight - topViewHeight)));
            } else {
                floatingView.layout(left, 0, right, floatViewHeight);
            }
        }
    }

    public interface StickyCallback {
        View getStickyView();
    }
}
