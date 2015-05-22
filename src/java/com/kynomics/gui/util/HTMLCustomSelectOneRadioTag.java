/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kynomics.gui.util;

import javax.faces.FactoryFinder;
import javax.faces.application.Application;
import javax.faces.application.ApplicationFactory;
import javax.faces.component.UIComponent;
import javax.faces.el.ValueBinding;
import javax.faces.webapp.UIComponentELTag;
import javax.faces.webapp.UIComponentTag;
import static javax.faces.webapp.UIComponentTag.isValueReference;

/**
 *
 * @author teilnehmer
 */
public class HTMLCustomSelectOneRadioTag extends UIComponentTag {

    @Override
    public String getComponentType() {
        return "component.CustomSelectOneRadio";
    }

    @Override
    public String getRendererType() {
        return "renderer.CustomSelectOneRadio";
    }

    private String name = null;
    private String value = null;
    private String styleClass = null;
    private String style = null;
    private String disabled = null;
    private String itemLabel = null;
    private String itemValue = null;
    private String onClick = null;
    private String onMouseOver = null;
    private String onMouseOut = null;
    private String onFocus = null;
    private String onBlur = null;
    private String overrideName = null;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getStyleClass() {
        return styleClass;
    }

    public void setStyleClass(String styleClass) {
        this.styleClass = styleClass;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public String getDisabled() {
        return disabled;
    }

    public void setDisabled(String disabled) {
        this.disabled = disabled;
    }

    public String getItemLabel() {
        return itemLabel;
    }

    public void setItemLabel(String itemLabel) {
        this.itemLabel = itemLabel;
    }

    public String getItemValue() {
        return itemValue;
    }

    public void setItemValue(String itemValue) {
        this.itemValue = itemValue;
    }

    public String getOnClick() {
        return onClick;
    }

    public void setOnClick(String onClick) {
        this.onClick = onClick;
    }

    public String getOnMouseOver() {
        return onMouseOver;
    }

    public void setOnMouseOver(String onMouseOver) {
        this.onMouseOver = onMouseOver;
    }

    public String getOnMouseOut() {
        return onMouseOut;
    }

    public void setOnMouseOut(String onMouseOut) {
        this.onMouseOut = onMouseOut;
    }

    public String getOnFocus() {
        return onFocus;
    }

    public void setOnFocus(String onFocus) {
        this.onFocus = onFocus;
    }

    public String getOnBlur() {
        return onBlur;
    }

    public void setOnBlur(String onBlur) {
        this.onBlur = onBlur;
    }

    public String getOverrideName() {
        return overrideName;
    }

    public void setOverrideName(String overrideName) {
        this.overrideName = overrideName;
    }

    @Override
    protected void setProperties(UIComponent component) {
        super.setProperties(component);
        UICustomSelectOneRadio aUICustomSelectOneRadio = (UICustomSelectOneRadio) component;
        if (name != null) {
            if (isValueReference(name)) {
                aUICustomSelectOneRadio.setValueBinding("name", getValueBinding(name));
            } else {
                aUICustomSelectOneRadio.getAttributes().put(name, name);
            }
        }
        if (value != null) {
            if (isValueReference(value)) {
                aUICustomSelectOneRadio.setValueBinding("value", getValueBinding(value));
            } else {
                aUICustomSelectOneRadio.getAttributes()
                        .put("value", value);
            }
        }
        if (styleClass != null) {
            if (isValueReference(styleClass)) {
                aUICustomSelectOneRadio.setValueBinding("styleClass", getValueBinding(styleClass));
            } else {
                aUICustomSelectOneRadio.getAttributes()
                        .put("styleClass", styleClass);
            }
        }
        if (style != null) {
            if (isValueReference(style)) {
                aUICustomSelectOneRadio.setValueBinding("style", getValueBinding(style));
            } else {
                aUICustomSelectOneRadio.getAttributes()
                        .put("style", style);
            }
        }
        if (disabled != null) {
            if (isValueReference(disabled)) {
                aUICustomSelectOneRadio.setValueBinding("disabled", getValueBinding(disabled));
            } else {
                aUICustomSelectOneRadio.getAttributes()
                        .put("disabled", disabled);
            }
        }
        if (itemLabel != null) {
            if (isValueReference(itemLabel)) {
                aUICustomSelectOneRadio.setValueBinding("itemLabel", getValueBinding(itemLabel));
            } else {
                System.out.println("itemLabel=" + itemLabel);
                aUICustomSelectOneRadio.getAttributes()
                        .put("itemLabel", itemLabel);
            }
        }
        if (itemValue != null) {
            if (isValueReference(itemValue)) {
                aUICustomSelectOneRadio.setValueBinding("itemValue", getValueBinding(itemValue));
            } else {
                aUICustomSelectOneRadio.getAttributes()
                        .put("itemValue", itemValue);
            }
        }
        if (onClick != null) {
            if (isValueReference(onClick)) {
                aUICustomSelectOneRadio.setValueBinding("onClick", getValueBinding(onClick));
            } else {
                aUICustomSelectOneRadio.getAttributes()
                        .put("onClick", onClick);
            }
        }
        if (onMouseOver != null) {
            if (isValueReference(onMouseOver)) {
                aUICustomSelectOneRadio.setValueBinding("onMouseOver", getValueBinding(onMouseOver));
            } else {
                aUICustomSelectOneRadio.getAttributes()
                        .put("onMouseOver", onMouseOver);
            }
        }
        if (onMouseOut != null) {
            if (isValueReference(onMouseOut)) {
                aUICustomSelectOneRadio.setValueBinding("onMouseOut", getValueBinding(onMouseOut));
            } else {
                aUICustomSelectOneRadio.getAttributes()
                        .put("onMouseOut", onMouseOut);
            }
        }
        if (onFocus != null) {
            if (isValueReference(onFocus)) {
                aUICustomSelectOneRadio.setValueBinding("onFocus", getValueBinding(onFocus));
            } else {
                aUICustomSelectOneRadio.getAttributes()
                        .put("onFocus", onFocus);
            }
        }
        if (onBlur != null) {
            if (isValueReference(onBlur)) {
                aUICustomSelectOneRadio.setValueBinding("onBlur", getValueBinding(onBlur));
            } else {
                aUICustomSelectOneRadio.getAttributes()
                        .put("onBlur", onBlur);
            }
        }

        if (overrideName != null) {
            if (isValueReference(overrideName)) {
                aUICustomSelectOneRadio.setValueBinding("overrideName", getValueBinding(overrideName));
            } else {
                aUICustomSelectOneRadio.getAttributes()
                        .put("overrideName", overrideName);
            }
        }
    }

    public ValueBinding getValueBinding(String valueRef) {
        ApplicationFactory af
                = (ApplicationFactory) FactoryFinder.getFactory(FactoryFinder.APPLICATION_FACTORY);
        Application a = af.getApplication();

        return (a.createValueBinding(valueRef));
    }
} // end class
