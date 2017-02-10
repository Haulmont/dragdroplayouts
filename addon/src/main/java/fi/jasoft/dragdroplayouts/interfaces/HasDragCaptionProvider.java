package fi.jasoft.dragdroplayouts.interfaces;

import com.vaadin.server.Resource;

/**
 * Created by petunin on 09.02.2017.
 */
public interface HasDragCaptionProvider {

    void setDragCaptionProvider(DragCaptionProvider provider);
    DragCaptionProvider getDragCaptionProvider();
    void setComponentResource(String id, Resource resource);
}
