package fi.jasoft.dragdroplayouts.client;

import com.google.gwt.user.client.ui.Widget;
import com.vaadin.client.ComponentConnector;
import com.vaadin.client.Util;
import com.vaadin.client.VCaption;
import com.vaadin.client.ui.VAccordion;
import fi.jasoft.dragdroplayouts.client.ui.interfaces.DDLayoutState;

public class VGrabFilter {
    private final DDLayoutState state;

    public VGrabFilter(DDLayoutState state) {
        this.state = state;
    }

    public boolean isDraggable(Widget widget) {
        ComponentConnector component = findConnectorFor(widget);
        if (state.draggable != null) {
            return state.draggable.contains(component);
        }
        return false;
    }

    private ComponentConnector findConnectorFor(Widget widget) {
        if (!isCaptionForAccordion(widget)) {
            return Util.findConnectorFor(widget);
        } else {
            return findConnectorForAccordionCaption(widget);
        }
    }

    private ComponentConnector findConnectorForAccordionCaption(Widget widget) {
        VAccordion.StackItem parent = (VAccordion.StackItem) widget.getParent();
        return Util.findConnectorFor(parent.getChildWidget());
    }

    private boolean isCaptionForAccordion(Widget widget) {
        if (widget == null) {
            return false;
        }
        if (!(widget instanceof VCaption)) {
            return false;
        }
        Widget parent = widget.getParent();
        return parent instanceof VAccordion.StackItem;
    }
}
