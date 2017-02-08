package fi.jasoft.dragdroplayouts.client.ui.interfaces;

import fi.jasoft.dragdroplayouts.client.VGrabFilter;

public interface VHasDragGrabFilter {
    VGrabFilter getGrabFilter();
    void setGrabFilter(VGrabFilter grabFilter);
}
