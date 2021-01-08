package yuan.core.ui;

import android.support.annotation.StringRes;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * 描述：TitleActivity注解项
 *
 * @author yuanye
 * @date 2019/8/28 13:59
 */
@Target({ElementType.TYPE})  //作用域 类
@Retention(RetentionPolicy.RUNTIME)  //运行时有效
@Documented
public @interface Title {

    /**
     * title 资源文件
     *
     * @return
     */
    @StringRes int title() default -1;

    /**
     * title 字符串
     *
     * @return
     */
    String titleStr() default "";

    /**
     * true 显示左测返回图标，并结束当前Activity
     * false 不显示返回图标
     *
     * @return
     */
    boolean finish() default true;
}
