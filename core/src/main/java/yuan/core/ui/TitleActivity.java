package yuan.core.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;

import yuan.core.R;
import yuan.core.mvp.BaseActivity;
import yuan.core.mvp.Presenter;
import yuan.core.title.ActionBarUtil;
import yuan.core.title.TitleBar;

/**
 * 描述：Title标题Activity
 * 采用默认标题ActionBar显示
 *
 * @author yuanye
 * @date 2019/8/28 11:50
 */
public abstract class TitleActivity<presenter extends Presenter> extends BaseActivity<presenter> {

    /**
     * title
     */
    protected TitleBar titleBar;

    @Override
    public void parseBundle(@Nullable Bundle bundle) {
        Title annotation = this.getClass().getAnnotation(Title.class);
        if (annotation != null) {
            String titleContent = "";
            //设置标题
            if (annotation.title() != -1) {
                titleContent = getString(annotation.title());
            } else {
                titleContent = annotation.titleStr();
            }
            titleBar = ActionBarUtil.create(this);
            titleBar.setTitleText(titleContent);
            //设置返回
            if (annotation.finish()) {
                titleBar.setLeftIcon(R.drawable.ic_base_back_white)
                        .setLeftClickFinish();
            }
        }
    }
}
