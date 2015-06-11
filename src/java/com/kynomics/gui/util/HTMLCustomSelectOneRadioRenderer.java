/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kynomics.gui.util;

import java.io.IOException;
import java.util.Map;
import javax.faces.component.UIComponent;
import javax.faces.component.UIData;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.FacesRenderer;
import javax.faces.render.Renderer;

/**
 *
 * @author teilnehmer
 */
//@FacesRenderer(componentFamily = "javax.faces.Input",
//        rendererType = HTMLCustomSelectOneRadioRenderer.RENDERER_TYPE)
public class HTMLCustomSelectOneRadioRenderer extends Renderer {

    public static final String RENDERER_TYPE = "com.kynomics.gui.util.HTMLCustomSelectOneRadioRenderer";

    public HTMLCustomSelectOneRadioRenderer() {
    }

    @Override
    public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
        if ((context == null) || (component == null)) {
            throw new NullPointerException();
        }

        UICustomSelectOneRadio aUICustomSelectOneRadio
                = (UICustomSelectOneRadio) component;

        if (component.isRendered()) {
            ResponseWriter writer = context.getResponseWriter();

            writer.write("<input type=\"radio\"");

            writer.write(" id=\"" + component.getClientId(context) + "\"");
            writer.write(" name=\"" + getName(aUICustomSelectOneRadio, context) + "\"");
            if (aUICustomSelectOneRadio.getStyleClass() != null
                    && aUICustomSelectOneRadio.getStyleClass().trim().length() > 0) {
                writer.write(" class=\""
                        + aUICustomSelectOneRadio.getStyleClass().trim() + "\"");
            }
            if (aUICustomSelectOneRadio.getStyle() != null
                    && aUICustomSelectOneRadio.getStyle().trim().length() > 0) {
                writer.write(" style=\""
                        + aUICustomSelectOneRadio.getStyle().trim() + "\"");
            }
            if (aUICustomSelectOneRadio.getDisabled() != null
                    && aUICustomSelectOneRadio.getDisabled().trim().length() > 0
                    && aUICustomSelectOneRadio.getDisabled().trim().equals("true")) {
                writer.write(" disabled=\"disabled\"");
            }
            if (aUICustomSelectOneRadio.getItemValue() != null) {
                writer.write(" value=\""
                        + aUICustomSelectOneRadio.getItemValue().trim() + "\"");
            }
            if (aUICustomSelectOneRadio.getOnClick() != null
                    && aUICustomSelectOneRadio.getOnClick().trim().length() > 0) {
                writer.write(" onclick=\""
                        + aUICustomSelectOneRadio.getOnClick().trim() + "\"");
            }
            if (aUICustomSelectOneRadio.getOnMouseOver() != null
                    && aUICustomSelectOneRadio.getOnMouseOver().trim().length() > 0) {
                writer.write(" onmouseover=\""
                        + aUICustomSelectOneRadio.getOnMouseOver().trim() + "\"");
            }
            if (aUICustomSelectOneRadio.getOnMouseOut() != null
                    && aUICustomSelectOneRadio.getOnMouseOut().trim().length() > 0) {
                writer.write(" onmouseout=\""
                        + aUICustomSelectOneRadio.getOnMouseOut().trim() + "\"");
            }
            if (aUICustomSelectOneRadio.getOnFocus() != null
                    && aUICustomSelectOneRadio.getOnFocus().trim().length() > 0) {
                writer.write(" onfocus=\"" + aUICustomSelectOneRadio.getOnFocus().trim() + "\"");
            }
            if (aUICustomSelectOneRadio.getOnBlur() != null
                    && aUICustomSelectOneRadio.getOnBlur().trim().length() > 0) {
                writer.write(" onblur=\"" + aUICustomSelectOneRadio.getOnBlur().trim() + "\"");
            }

            if (aUICustomSelectOneRadio.getValue() != null
                    && aUICustomSelectOneRadio.getValue().equals(
                            aUICustomSelectOneRadio.getItemValue())) {
                writer.write(" checked=\"checked\"");
            }
            writer.write(">");
            if (aUICustomSelectOneRadio.getItemLabel() != null) {
                writer.write(aUICustomSelectOneRadio.getItemLabel());
            }
            writer.write("</input>");
        }
    }

    @Override
    public void encodeChildren(FacesContext context, UIComponent component) throws IOException {
        super.encodeChildren(context, component); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
        super.encodeBegin(context, component); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void decode(FacesContext context, UIComponent component) {
        if ((context == null) || (component == null)) {
            throw new NullPointerException();
        }

        UICustomSelectOneRadio aUICustomSelectOneRadio = null;
        if (component instanceof UICustomSelectOneRadio) {
            aUICustomSelectOneRadio = (UICustomSelectOneRadio) component;
        } else {
            return;
        }
        Map map = context.getExternalContext().getRequestParameterMap();
        String name = getName(aUICustomSelectOneRadio, context);
        if (map.containsKey(name)) {
            String value = (String) map.get(name);
            if (value != null) {
                setSubmittedValue(component, value);
            }
        }
    }

    private String getName(UICustomSelectOneRadio aUICustomSelectOneRadio,
            FacesContext context) {

        UIComponent parentUIComponent
                = getParentDataTableFromHierarchy(aUICustomSelectOneRadio);
        if (parentUIComponent == null) {
            return aUICustomSelectOneRadio.getClientId(context);
        } else {
            if (aUICustomSelectOneRadio.getOverrideName() != null
                    && aUICustomSelectOneRadio.getOverrideName().equals("true")) {
                return aUICustomSelectOneRadio.getName();
            } else {
                String id = aUICustomSelectOneRadio.getClientId(context);
                int lastIndexOfColon = id.lastIndexOf(":");
                String partName = "";
                if (lastIndexOfColon != -1) {
                    partName = id.substring(0, lastIndexOfColon + 1);
                    if (aUICustomSelectOneRadio.getName() == null) {
                        partName = partName + "generatedRad";
                    } else {
                        partName = partName + aUICustomSelectOneRadio.getName();
                    }
                }
                return partName;
            }
        }
    }

    public void setSubmittedValue(UIComponent component, Object obj) {
        if (component instanceof UIInput) {
            ((UIInput) component).setSubmittedValue(obj);
        }
    }

    private UIComponent getParentDataTableFromHierarchy(UIComponent uiComponent) {
        if (uiComponent == null) {
            return null;
        }
        if (uiComponent instanceof UIData) {
            return uiComponent;
        } else {
            //try to find recursively in the Component tree hierarchy
            return getParentDataTableFromHierarchy(uiComponent.getParent());
        }
    }

}
