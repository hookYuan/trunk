package com.yuan.simple.one.expandable;

import com.yuan.base.tools.adapter.expandable.ExpandableItem;
import com.yuan.base.tools.adapter.expandable.ExpandableSection;

import java.util.List;

/**
 * Created by YuanYe on 2018/9/29.
 */
public class ExpandableBean extends ExpandableSection {

    private String group;

    private List<ChildItem> list;

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public List<ChildItem> getList() {
        return list;
    }

    public void setList(List<ChildItem> list) {
        this.list = list;
    }

    public static class ChildItem extends ExpandableItem {

        private String item;

        public String getItem() {
            return item;
        }

        public void setItem(String item) {
            this.item = item;
        }
    }
}
