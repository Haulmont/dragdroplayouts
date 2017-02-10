package fi.jasoft.dragdroplayouts;

import com.vaadin.server.Resource;

public class DragCaption {
    protected String caption;
    protected Resource icon;

    public DragCaption(String caption, Resource icon) {
        this.caption = caption;
        this.icon = icon;
    }
}
