/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kynomics.converter;

import java.io.Serializable;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;
import javax.faces.convert.IntegerConverter;

@FacesConverter(value = ZipCodeConverter.CONVERTER_ID)
public class ZipCodeConverter extends IntegerConverter implements Serializable {

    private static final long serialVersionUID = 7058986733877680578L;
    public static final String CONVERTER_ID = "com.kynomics.ZipCodeConverter";

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value)
            throws ConverterException {
        if (value != null && value.length() > 0) {
            int pos = value.indexOf('-');
            for (int i = 0; i < pos; i++) {
                if (!Character.isLetter(value.charAt(i))) {
                    throw new ConverterException("Zip code invalid.");
                }
            }
            if (pos > -1 && pos < value.length() - 1) {
                return super.getAsObject(context, component, value.substring(pos + 1));
            }
        }
        return super.getAsObject(context, component, value);
    }
}


