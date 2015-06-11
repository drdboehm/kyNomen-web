/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kynomics.gui.util;

import javax.el.ValueExpression;
import javax.faces.component.FacesComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;

/**
 *
 * @author teilnehmer
 */
//@FacesComponent(createTag = true, namespace = "http://xmlns.kynomics.com/jsf/components", 
//        tagName = "selectOneRadio")
public class UICustomSelectOneRadio extends UIInput {

    private String name = null;
    private String overrideName = null;
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

    public String returnValueBindingAsString(String attr) {
//        ValueBinding valueBinding = getValueBinding(attr);
        ValueExpression valueExpression = getValueExpression(attr);
        if (valueExpression != null) {
            return (String) valueExpression.getValue(this.getFacesContext().getELContext());
        } else {
            return null;
        }
    }

    public String getName() {
        if (null != name) {
            return name;
        }
        return returnValueBindingAsString("name");
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOverrideName() {
        if (null != overrideName) {
            return overrideName;
        }
        return returnValueBindingAsString("overrideName");
    }

    public void setOverrideName(String overrideName) {
        this.overrideName = overrideName;
    }

    public String getStyleClass() {
        if (null != styleClass) {
            return styleClass;
        }
        return returnValueBindingAsString("styleClass");
    }

    public void setStyleClass(String styleClass) {
        this.styleClass = styleClass;
    }

    public String getStyle() {
        if (null != style) {
            return style;
        }
        return returnValueBindingAsString("style");
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public String getDisabled() {
        if (null != disabled) {
            return disabled;
        }
        return returnValueBindingAsString("disabled");
    }

    public void setDisabled(String disabled) {
        this.disabled = disabled;
    }

    public String getItemLabel() {
        if (null != itemLabel) {
            return itemLabel;
        }
        return returnValueBindingAsString("itemLabel");
    }

    public void setItemLabel(String itemLabel) {
        this.itemLabel = itemLabel;
    }

    public String getItemValue() {
        if (null != itemValue) {
            return itemValue;
        }
        return returnValueBindingAsString("itemValue");
    }

    public void setItemValue(String itemValue) {
        this.itemValue = itemValue;
    }

    public String getOnClick() {
        if (null != onClick) {
            return onClick;
        }
        return returnValueBindingAsString("onClick");
    }

    public void setOnClick(String onClick) {
        this.onClick = onClick;
    }

    public String getOnMouseOver() {
        if (null != onMouseOver) {
            return onMouseOver;
        }
        return returnValueBindingAsString("onMouseOver");
    }

    public void setOnMouseOver(String onMouseOver) {
        this.onMouseOver = onMouseOver;
    }

    public String getOnMouseOut() {
        if (null != onMouseOut) {
            return onMouseOut;
        }
        return returnValueBindingAsString("onMouseOut");
    }

    public void setOnMouseOut(String onMouseOut) {
        this.onMouseOut = onMouseOut;
    }

    public String getOnFocus() {
        if (null != onFocus) {
            return onFocus;
        }
        return returnValueBindingAsString("onFocus");
    }

    public void setOnFocus(String onFocus) {
        this.onFocus = onFocus;
    }

    public String getOnBlur() {
        if (null != onBlur) {
            return onBlur;
        }
        return returnValueBindingAsString("onBlur");
    }

    public void setOnBlur(String onBlur) {
        this.onBlur = onBlur;
    }

    @Override
    public void restoreState(FacesContext context, Object state) {
        Object[] values = (Object[]) state;
        super.restoreState(context, values[0]);
        styleClass = (String) values[1];
        style = (String) values[2];
        disabled = (String) values[3];
        itemLabel = (String) values[4];
        itemValue = (String) values[5];
        onClick = (String) values[6];
        onMouseOver = (String) values[7];
        onMouseOut = (String) values[8];
        onFocus = (String) values[9];
        onBlur = (String) values[10];
        name = (String) values[11];
        overrideName = (String) values[12];
    }

    @Override
    public String getFamily() {
        return ("CustomSelectOneRadio");
    }

    @Override
    public Object saveState(FacesContext context) {
        Object[] values = new Object[13];
        values[0] = super.saveState(context);
        values[1] = styleClass;
        values[2] = style;
        values[3] = disabled;
        values[4] = itemLabel;
        values[5] = itemValue;
        values[6] = onClick;
        values[7] = onMouseOver;
        values[8] = onMouseOut;
        values[9] = onFocus;
        values[11] = name;
        values[12] = overrideName;
        return (values);
    }

}
