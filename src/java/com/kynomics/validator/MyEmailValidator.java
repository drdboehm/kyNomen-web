/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kynomics.validator;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.html.HtmlInputText;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.routines.EmailValidator;

/**
 *
 * @author dboehm
 */
@FacesValidator(value = "emailValidator")
public class MyEmailValidator implements Validator {

    @Override
    public void validate(FacesContext context, UIComponent uiComponent, Object value) throws ValidatorException {
        EmailValidator emailValidator = EmailValidator.getInstance();
        HtmlInputText htmlInputText = (HtmlInputText) uiComponent;
        String email = (String) value;
        if (StringUtils.isNotEmpty(email)) {
            if (!emailValidator.isValid(email)) {
                FacesMessage facesMessage = new FacesMessage(htmlInputText.getLabel() + ": email is not valid!");
                throw new ValidatorException(facesMessage);
            }
        }
    }

}
