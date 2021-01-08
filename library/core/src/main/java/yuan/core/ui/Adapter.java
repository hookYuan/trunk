package yuan.core.ui;


import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import yuan.core.list.RecyclerAdapter;

/**
 * @author yuanye
 * @date 2019/8/15
 */
@Target({ElementType.TYPE})  //作用域 类
@Retention(RetentionPolicy.RUNTIME)  //运行时有效
@Documented
public @interface Adapter {
    /**
     * 适配器
     *
     * @return
     */
    Class<? extends RecyclerAdapter> adapter();

    /**
     * Item布局
     *
     * @return
     */
    int layoutId() default -1;
}
