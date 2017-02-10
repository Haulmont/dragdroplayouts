package fi.jasoft.dragdroplayouts.client.ui;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.ui.Widget;
import com.vaadin.client.ComponentConnector;
import com.vaadin.client.Util;
import com.vaadin.client.ui.AbstractConnector;
import com.vaadin.client.ui.Icon;
import fi.jasoft.dragdroplayouts.client.ui.interfaces.DDLayoutState;

public class VComponentDragCaptionProvider {
    private final DDLayoutState state;

    public VComponentDragCaptionProvider(DDLayoutState state) {
        this.state = state;
    }

    public Element getDragCaptionElement(Widget w, Widget root) {
        ComponentConnector component = Util.findConnectorFor(w);
        ComponentConnector rootComponent = Util.findConnectorFor(root);
        String dragCaptionText = state.dragCaptions.get(component);

        Element dragCaptionImage = Document.get().createElement("div");
        Element dragCaption = Document.get().createElement("span");
        dragCaption.setInnerText(dragCaptionText);

        String dragIconId = state.dragIcons.get(component);
        if (dragIconId != null) {
            String resourceUrl = ((AbstractConnector) rootComponent).getResourceUrl(dragIconId);
            Icon icon = component.getConnection().getIcon(resourceUrl);
            dragCaptionImage.appendChild(icon.getElement());
        }

        dragCaptionImage.appendChild(dragCaption);

        return dragCaptionImage;
    }
}
