package yuan.depends.tablayout;


import com.flyco.tablayout.SlidingTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;

import java.util.ArrayList;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager.widget.ViewPager;
import yuan.core.tool.Kits;
import yuan.depends.R;

/**
 * 描述：
 * <p>
 * github  {@url https://github.com/H07000223/FlycoTabLayout/blob/master/README_CN.md}
 *
 * @author yuanye
 * @date 2019/5/20 9:33
 */
public class TabLayoutUtil {

    /**
     * 设置SlidingTableLayout默认主题并设置数据和绑定ViewPager，显示Fragment
     * 绑定Fragment时，必须保证titles与fragments一一对应
     *
     * @param tabLayout
     */
    public static void setThemeViewPager(SlidingTabLayout tabLayout, ViewPager viewPager, FragmentActivity fragmentActivity,
                                         String[] titles, ArrayList<Fragment> fragments) {
        //设置SlidingTabLayout主题属性
        setTheme(tabLayout);

        //绑定ViewPager
        ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();
        for (int i = 0; i < titles.length; i++) {
            mTabEntities.add(new TabEntity(titles[i], 0, 0));
        }
        tabLayout.setViewPager(viewPager, titles, fragmentActivity, fragments);
    }

    /**
     * 设置SlidingTableLayout默认主题
     * <p>
     * 如果单纯调用此方法设置主题，ViewPager适配器需要自己实现
     *
     * @param tabLayout
     */
    public static void setTheme(SlidingTabLayout tabLayout) {
        //设置SlidingTabLayout主题属性
        tabLayout.setIndicatorColor(ContextCompat.getColor(tabLayout.getContext(), R.color.colorPrimary));
        tabLayout.setTextSelectColor(ContextCompat.getColor(tabLayout.getContext(), R.color.colorPrimary));
        tabLayout.setTextUnselectColor(ContextCompat.getColor(tabLayout.getContext(), R.color.black));
        tabLayout.setTabSpaceEqual(true);//设置等分
        tabLayout.setUnderlineHeight(Kits.Dimens.dpToPxInt(tabLayout.getContext(), 1));
        tabLayout.setUnderlineColor(ContextCompat.getColor(tabLayout.getContext(), R.color.background));
        tabLayout.setBackgroundColor(ContextCompat.getColor(tabLayout.getContext(), R.color.white));
        tabLayout.getLayoutParams().height = Kits.Dimens.dpToPxInt(tabLayout.getContext(), 38);
    }

}
