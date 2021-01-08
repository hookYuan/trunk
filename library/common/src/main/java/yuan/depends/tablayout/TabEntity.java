package yuan.depends.tablayout;

import com.flyco.tablayout.listener.CustomTabEntity;

import java.io.Serializable;

/**
 * Created by coofeel on 2019/1/20.
 */

public class TabEntity implements CustomTabEntity, Serializable {
    public String title;
    public int selectedIcon;
    public int unSelectedIcon;

    public TabEntity(String title, int selectedIcon, int unSelectedIcon) {
        this.title = title;
        this.selectedIcon = selectedIcon;
        this.unSelectedIcon = unSelectedIcon;
    }

    @Override
    public String getTabTitle() {
        return title;
    }

    @Override
    public int getTabSelectedIcon() {
        return selectedIcon;
    }

    @Override
    public int getTabUnselectedIcon() {
        return unSelectedIcon;
    }
}
