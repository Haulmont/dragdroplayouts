package fi.jasoft.dragdroplayouts;

import com.vaadin.event.dd.DropHandler;
import com.vaadin.server.KeyMapper;
import com.vaadin.server.ResourceReference;
import com.vaadin.shared.Connector;
import com.vaadin.ui.Component;
import com.vaadin.ui.HasComponents;
import fi.jasoft.dragdroplayouts.client.ui.interfaces.DDLayoutState;
import fi.jasoft.dragdroplayouts.client.ui.interfaces.DragAndDropAwareState;
import fi.jasoft.dragdroplayouts.drophandlers.AbstractDefaultLayoutDropHandler;
import fi.jasoft.dragdroplayouts.interfaces.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class DDUtil {

    public static void onBeforeClientResponse(HasComponents layout,
            DragAndDropAwareState state) {
        DDLayoutState dragAndDropState = state.getDragAndDropState();
        Iterator<Component> componentIterator = layout.iterator();
        dragAndDropState.draggable = new ArrayList<Connector>();
        dragAndDropState.referenceImageComponents = new HashMap<Connector, Connector>();
        dragAndDropState.nonGrabbable = new ArrayList<>();
        dragAndDropState.dragCaptions = new HashMap<>();
        dragAndDropState.dragIcons = new HashMap<>();
        KeyMapper keyMapper = new KeyMapper();

        while (componentIterator.hasNext()) {
            Component c = componentIterator.next();

            if (layout instanceof DragFilterSupport
                    && ((DragFilterSupport) layout).getDragFilter()
                            .isDraggable(c)) {
                dragAndDropState.draggable.add(c);
            }

            if (layout instanceof DragGrabFilterSupport) {
                DragGrabFilter dragGrabFilter = ((DragGrabFilterSupport) layout).getDragGrabFilter();
                if (dragGrabFilter != null) {
                    addNonGrabbedComponents(dragAndDropState.nonGrabbable, c, dragGrabFilter);
                }
            }

            if (layout instanceof HasDragCaptionProvider) {
                DragCaptionProvider dragCaptionProvider = ((HasDragCaptionProvider) layout)
                        .getDragCaptionProvider();

                if (dragCaptionProvider != null) {
                    if (dragCaptionProvider.getDragCaption(c).icon != null) {
                        String resourceId = keyMapper.key(dragCaptionProvider.getDragCaption(c).icon);
                        ResourceReference resourceReference = ResourceReference.create(dragCaptionProvider.getDragCaption(c).icon, layout, resourceId);
                        dragAndDropState.resources.put(resourceId, resourceReference);
                        ((DDVerticalLayout) layout).setComponentResource(resourceId,
                                dragCaptionProvider.getDragCaption(c).icon);
                        dragAndDropState.dragIcons.put(c, resourceId);
                    }
                    dragAndDropState.dragCaptions.put(c,
                                    dragCaptionProvider.getDragCaption(c).caption);
                }
            }

            if (layout instanceof DragImageReferenceSupport) {
                DragImageProvider provider = ((DragImageReferenceSupport) layout)
                        .getDragImageProvider();
                if (provider != null) {
                    Component dragImage = provider.getDragImage(c);
                    if (dragImage != null) {
                        dragAndDropState.referenceImageComponents.put(c,
                                dragImage);
                    }
                }
            }
        }
    }

    private static void addNonGrabbedComponents(List<Connector> nonGrabbable, Component component,
                                                DragGrabFilter dragGrabFilter) {
        if (!dragGrabFilter.canBeGrabbed(component)) {
            nonGrabbable.add(component);
        } else if (component instanceof HasComponents
                && !(component instanceof LayoutDragSource)) {
            for (Component child : ((HasComponents) component)) {
                addNonGrabbedComponents(nonGrabbable, child, dragGrabFilter);
            }
        }
    }

    public static void verifyHandlerType(HasComponents layout,
            DropHandler handler) {
        if (handler instanceof AbstractDefaultLayoutDropHandler) {
            AbstractDefaultLayoutDropHandler dropHandler = (AbstractDefaultLayoutDropHandler) handler;
            if (!dropHandler.getTargetLayoutType()
                    .isAssignableFrom(layout.getClass())) {
                throw new IllegalArgumentException("Cannot add a handler for "
                        + dropHandler.getTargetLayoutType() + " to a "
                        + layout.getClass());
            }
        }
    }
}
