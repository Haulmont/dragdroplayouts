package fi.jasoft.dragdroplayouts.client.ui.interfaces;

import fi.jasoft.dragdroplayouts.client.ui.VDragCaptionProvider;

public interface VHasDragCaptionProvider {
    void setComponentDragCaption(VDragCaptionProvider dragCaption);
    VDragCaptionProvider getComponentDragCaption();
}
