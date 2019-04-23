package com.yuan.simple.one.multi;

import com.yuan.kernel.tools.adapter.expandable.ExpandableItem;
import com.yuan.kernel.tools.adapter.expandable.ExpandableSection;

/**
 * @author yuanye
 * @date 2018/12/19
 */
public class SectionBean extends ExpandableSection {

    public String name;

    public SectionBean(String name) {
        this.name = name;
    }
}
