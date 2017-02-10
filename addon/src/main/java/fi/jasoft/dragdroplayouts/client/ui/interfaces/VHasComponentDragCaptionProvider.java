package fi.jasoft.dragdroplayouts.client.ui.interfaces;

import fi.jasoft.dragdroplayouts.client.ui.VComponentDragCaptionProvider;

public interface VHasComponentDragCaptionProvider {
    void setComponentDragCaption(VComponentDragCaptionProvider dragCaption);
    VComponentDragCaptionProvider getComponentDragCaption();
}
