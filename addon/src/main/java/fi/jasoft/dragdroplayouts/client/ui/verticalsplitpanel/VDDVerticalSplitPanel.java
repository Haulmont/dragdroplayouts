/*
 * Copyright 2015 John Ahlroos
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package fi.jasoft.dragdroplayouts.client.ui.verticalsplitpanel;

import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.Widget;
import com.vaadin.client.ComponentConnector;
import com.vaadin.client.MouseEventDetailsBuilder;
import com.vaadin.client.Util;
import com.vaadin.client.ui.VSplitPanelVertical;
import com.vaadin.client.ui.dd.VDragEvent;
import com.vaadin.shared.MouseEventDetails;
import com.vaadin.shared.ui.dd.VerticalDropLocation;
import fi.jasoft.dragdroplayouts.DDVerticalSplitPanel;
import fi.jasoft.dragdroplayouts.client.VDragFilter;
import fi.jasoft.dragdroplayouts.client.VGrabFilter;
import fi.jasoft.dragdroplayouts.client.ui.*;
import fi.jasoft.dragdroplayouts.client.ui.VLayoutDragDropMouseHandler.DragStartListener;
import fi.jasoft.dragdroplayouts.client.ui.interfaces.*;
import fi.jasoft.dragdroplayouts.client.ui.util.IframeCoverUtility;

/**
 * Client side implementation for {@link DDVerticalSplitPanel}
 * 
 * @author John Ahlroos / www.jasoft.fi
 * @since 0.4.0
 */
public class VDDVerticalSplitPanel extends VSplitPanelVertical
        implements VHasDragMode,
        VDDHasDropHandler<VDDVerticalSplitPanelDropHandler>, DragStartListener,
        VHasDragFilter, VHasDragImageReferenceSupport, VHasIframeShims,
        VHasGrabFilter, VHasComponentDragCaptionProvider {

    public static final String OVER = "v-ddsplitpanel-over";

    public static final String OVER_SPLITTER = OVER + "-splitter";

    private VDDVerticalSplitPanelDropHandler dropHandler;

    private VGrabFilter grabFilter;

    private Element firstContainer;

    private Element secondContainer;

    private Element splitter;

    private Element currentEmphasis;

    private VDragFilter dragFilter;

    private VComponentDragCaptionProvider dragCaption;

    private final IframeCoverUtility iframeCoverUtility = new IframeCoverUtility();

    // The drag mouse handler which handles the creation of the transferable
    private final VLayoutDragDropMouseHandler ddMouseHandler = new VLayoutDragDropMouseHandler(
            this, LayoutDragMode.NONE);

    private LayoutDragMode mode = LayoutDragMode.NONE;

    private boolean iframeCovers = false;

    public VDDVerticalSplitPanel() {
        super();
    }

    @Override
    public void setComponentDragCaption(VComponentDragCaptionProvider dragCaption) {
        this.dragCaption = dragCaption;
    }

    @Override
    public VComponentDragCaptionProvider getComponentDragCaption() {
        return dragCaption;
    }

    @Override
    protected void onLoad() {
        super.onLoad();
        ddMouseHandler.addDragStartListener(this);
        setDragMode(mode);
        iframeShimsEnabled(iframeCovers);
    }

    @Override
    protected void onUnload() {
        super.onUnload();
        ddMouseHandler.removeDragStartListener(this);
        ddMouseHandler.updateDragMode(LayoutDragMode.NONE);
        iframeCoverUtility.setIframeCoversEnabled(false, getElement(),
                LayoutDragMode.NONE);
    }

    @Override
    protected void constructDom() {
        super.constructDom();

        // Save references
        Element wrapper = getElement().getChild(0).cast();
        firstContainer = wrapper.getChild(0).cast();
        splitter = wrapper.getChild(1).cast();
        secondContainer = wrapper.getChild(2).cast();
    }

    /**
     * A hook for extended components to post process the the drop before it is
     * sent to the server. Useful if you don't want to override the whole drop
     * handler.
     */
    protected boolean postDropHook(VDragEvent drag) {
        // Extended classes can add content here...
        return true;
    }

    /**
     * A hook for extended components to post process the the enter event.
     * Useful if you don't want to override the whole drophandler.
     */
    protected void postEnterHook(VDragEvent drag) {
        // Extended classes can add content here...
    }

    /**
     * A hook for extended components to post process the the leave event.
     * Useful if you don't want to override the whole drophandler.
     */
    protected void postLeaveHook(VDragEvent drag) {
        // Extended classes can add content here...
    }

    /**
     * A hook for extended components to post process the the over event. Useful
     * if you don't want to override the whole drophandler.
     */
    protected void postOverHook(VDragEvent drag) {
        // Extended classes can add content here...
    }

    /**
     * Can be used to listen to drag start events, must return true for the drag
     * to commence. Return false to interrupt the drag:
     */
    @Override
    public boolean dragStart(Widget widget, LayoutDragMode mode) {
        ComponentConnector layout = Util.findConnectorFor(this);
        return VDragDropUtil.isDraggingEnabled(layout, widget);
    }

    public void setDropHandler(VDDVerticalSplitPanelDropHandler handler) {
        dropHandler = handler;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.vaadin.terminal.gwt.client.ui.dd.VHasDropHandler#getDropHandler()
     */
    public VDDVerticalSplitPanelDropHandler getDropHandler() {
        return dropHandler;
    }

    /*
     * (non-Javadoc)
     * 
     * @see fi.jasoft.dragdroplayouts.client.ui.VHasDragMode#getDragMode()
     */
    public LayoutDragMode getDragMode() {
        return ddMouseHandler.getDragMode();
    }

    /**
     * Emphasisizes a container element
     * 
     * @param element
     */
    protected void emphasis(Element element) {
        // Remove previous emphasis
        deEmphasis();

        // validate container
        if (element == null || !getElement().isOrHasChild(element)) {
            return;
        }

        if (element == firstContainer || element == secondContainer) {
            element.addClassName(OVER);
            currentEmphasis = element;
        } else if (splitter.isOrHasChild(element)) {
            currentEmphasis = splitter.getChild(0).cast();
            currentEmphasis.addClassName(OVER_SPLITTER);
        }
    }

    /**
     * Removes any previous emphasis made by drag&amp;drag
     */
    protected void deEmphasis() {
        if (currentEmphasis != null) {
            currentEmphasis.removeClassName(OVER);
            currentEmphasis.removeClassName(OVER_SPLITTER);
            currentEmphasis = null;
        }
    }

    /**
     * Returns the container element which wraps the first (left-most) component
     * 
     * @return
     */
    protected Element getFirstContainer() {
        return firstContainer;
    }

    /**
     * Returns the container element which wraps the second (right-most)
     * component
     * 
     * @return
     */
    protected Element getSecondContainer() {
        return secondContainer;
    }

    /**
     * Returns the splitter element
     * 
     * @return
     */
    protected Element getSplitter() {
        return splitter;
    }

    /**
     * Updates the drop details while dragging. This is needed to ensure client
     * side criterias can validate the drop location.
     * 
     * @param event
     *            The drag event
     */
    protected void updateDragDetails(VDragEvent event) {
        Element over = event.getElementOver();

        // Resolve where the drop was made
        VerticalDropLocation location = null;
        Widget content = null;
        if (firstContainer.isOrHasChild(over)) {
            location = VerticalDropLocation.TOP;
            content = Util.findWidget(firstContainer, null);
        } else if (splitter.isOrHasChild(over)) {
            location = VerticalDropLocation.MIDDLE;
            content = this;
        } else if (secondContainer.isOrHasChild(over)) {
            location = VerticalDropLocation.BOTTOM;
            content = Util.findWidget(secondContainer, null);
        }

        event.getDropDetails().put(Constants.DROP_DETAIL_VERTICAL_DROP_LOCATION,
                location);

        if (content != null) {
            event.getDropDetails().put(Constants.DROP_DETAIL_OVER_CLASS,
                    content.getClass().getName());
        } else {
            event.getDropDetails().put(Constants.DROP_DETAIL_OVER_CLASS,
                    this.getClass().getName());
        }

        // Add mouse event details
        MouseEventDetails details = MouseEventDetailsBuilder
                .buildMouseEventDetails(event.getCurrentGwtEvent(),
                        getElement());
        event.getDropDetails().put(Constants.DROP_DETAIL_MOUSE_EVENT,
                details.serialize());
    }

    /*
     * (non-Javadoc)
     * 
     * @see fi.jasoft.dragdroplayouts.client.ui.interfaces.VHasDragFilter#
     * getDragFilter ()
     */
    public VDragFilter getDragFilter() {
        return dragFilter;
    }

    IframeCoverUtility getIframeCoverUtility() {
        return iframeCoverUtility;
    }

    @Override
    public void setDragFilter(VDragFilter filter) {
        this.dragFilter = filter;
    }

    @Override
    public void iframeShimsEnabled(boolean enabled) {
        iframeCovers = enabled;
        iframeCoverUtility.setIframeCoversEnabled(enabled, getElement(), mode);
    }

    @Override
    public boolean isIframeShimsEnabled() {
        return iframeCovers;
    }

    @Override
    public void setDragMode(LayoutDragMode mode) {
        this.mode = mode;
        ddMouseHandler.updateDragMode(mode);
        iframeShimsEnabled(iframeCovers);
    }

    @Override
    public void setDragImageProvider(VDragImageProvider provider) {
        ddMouseHandler.setDragImageProvider(provider);
    }

    protected final VLayoutDragDropMouseHandler getMouseHandler() {
        return ddMouseHandler;
    }

    @Override
    public VGrabFilter getGrabFilter() {
        return grabFilter;
    }

    @Override
    public void setGrabFilter(VGrabFilter grabFilter) {
        this.grabFilter = grabFilter;
    }
}
