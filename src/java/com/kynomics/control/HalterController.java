/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kynomics.control;

import com.kynomics.daten.Halter;
import com.kynomics.daten.Haltertyp;
import com.kynomics.daten.Patient;
import com.kynomics.daten.Rasse;
import com.kynomics.daten.RassePK;
import com.kynomics.daten.Spezies;
import com.kynomics.lib.TransmitterSessionBeanRemote;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.html.HtmlInputText;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import javax.inject.Named;
import org.apache.commons.lang3.StringUtils;

/**
 * <h1>Controls the Halter und Haltertyp Model</h1>
 * The HalterController class implements the controller of the Halter and
 * Haltertyp model. It contains some methods used to fetch the entries of the
 * Halter and Haltertyp table from the database as well as other values
 *
 * @author Detlef Boehm
 */
@Named(value = "halterController")
@SessionScoped
public class HalterController implements Serializable {

    private static final long serialVersionUID = 1L;
    @EJB
    private TransmitterSessionBeanRemote transmitterSessionBeanRemote;

    private Halter halter;
    private Patient patient;
    private Spezies spezies;
    private Rasse rasse;
    private RassePK rassePK;
    private Haltertyp haltertyp;

    /**
     * the default constructor
     */
    public HalterController() {
        halter = new Halter();
        patient = new Patient();
        haltertyp = new Haltertyp();
        spezies = new Spezies();
        rassePK = new RassePK();
    }

    /* 
     the HalterTypen - Map for the  <h:selectOneMenu ... 
     */
    private final Map<String, Integer> alleHalterTypenMap = new HashMap();

    /* 
     the SpeziesTypen - Map for the  <h:selectOneMenu ... 
     */
    private final Map<String, Integer> alleSpeziesTypenMap = new HashMap();

    /* 
     the RasseTypen - Map for the  <h:selectOneMenu ... 
     */
    private final Map<String, Integer> alleRasseTypenMap = new HashMap();

    /*
    Getters and setters
     */
    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public Spezies getSpezies() {
        return spezies;
    }

    public void setSpezies(Spezies spezies) {
        this.spezies = spezies;
    }

    public Rasse getRasse() {
        return rasse;
    }

    public void setRasse(Rasse rasse) {
        this.rasse = rasse;
    }

    public Halter getHalter() {
        return halter;
    }

    public void setHalter(Halter halter) {
        this.halter = halter;
    }

    public RassePK getRassePK() {
        return rassePK;
    }

    public void setRassePK(RassePK rassePK) {
        this.rassePK = rassePK;
    }

    public Haltertyp getHaltertyp() {
        return haltertyp;
    }

    public void setHaltertyp(Haltertyp haltertyp) {
        this.haltertyp = haltertyp;
    }
    
    
    
    /*
    Own Logic
     */

    public String saveHalter() {
        System.out.println("**********************************");
        System.out.println("HalterName: " + halter.getHalterName());
        System.out.println("Halterbemwerkung: " + halter.getHalterBemerkung());
        System.out.println("Haltertyp selected: " + haltertyp.getHaltertypId());
        System.out.println("HaltertypName selected: " + haltertyp.getHaltertypName());
        System.out.println("Rufname Patient: " + patient.getPatientRuf());
        System.out.println("SpeziesId: " + spezies.getSpeziesId());
        System.out.println("SpeziesName: " + spezies.getSpeziesName());
        System.out.println("RasseId: " + rassePK.getRasseId());
        System.out.println("SpeziesId from RassePK: " + rassePK.getSpeziesId());
        System.out.println("**********************************");
        return "index";
    }

    public String sucheHalter() {
        List<Halter> allHalter = transmitterSessionBeanRemote.halterGet();
        List<Patient> allPatient = transmitterSessionBeanRemote.patientGet();
        
        return "index";
    }
    /**
     * create and returns a Map with String of all Halter -Objects to show in
     * the <h:selectOneMenu ...>
     * and the HaltertypId as key
     *
     * @return Map<String, Integer>
     */
    public Map<String, Integer> getAlleHalterTypenMap() {
        List<Haltertyp> tempList = transmitterSessionBeanRemote.initializeHalterTypen();
        for (Haltertyp next : tempList) {
            this.alleHalterTypenMap.put(next.getHaltertypName(), next.getHaltertypId());
        }
        return alleHalterTypenMap;
    }

    public Map<String, Integer> getAlleRasseTypenMap() {
        List<Rasse> list = transmitterSessionBeanRemote.initializeRasseTypen();
        /*
        filter the list by selected speziesId
        1. empty the Map
        2. avoid NullPointerException when SpeziesId is null, -> then add all Rassen
        3. Filter the Rasse List by Spezies.Id
         */
        alleRasseTypenMap.clear();
        for (Rasse r : list) {
            if (spezies.getSpeziesId() != null) {
                if (r.getRassePK().getSpeziesId() == spezies.getSpeziesId()) {
                    System.out.println("" + r.getRasseName() + "-" + r.getRassePK().getSpeziesId());
                    alleRasseTypenMap.put(r.getRasseName(), r.getRassePK().getRasseId());
                }
            } else { // sonst alle
                alleRasseTypenMap.put(r.getRasseName(), r.getRassePK().getRasseId());
            }
        }
        return alleRasseTypenMap;
    }

    public Map<String, Integer> getAlleSpeziesTypenMap() {
        List<Spezies> list = transmitterSessionBeanRemote.initializeSpeziesTypen();
        /*
        filter the list by selected rasseId
        1. empty the Map
        2. avoid NullPointerException when rasseId is null, -> then add all Spezies
        3. Filter the Spezies List by RasseId
        4. If we do that, we need a system to RESET selection !!!! this is a "goldener Henkel" 
        => concentrate on business logic implementation 
         */
        alleSpeziesTypenMap.clear();
        for (Spezies s : list) {
//            if (s.getSpeziesId() == rassePK.getRasseId()) {
//                System.out.println("" + s.getSpeziesName() + "for Rasse " + rassePK.getRasseId());
            alleSpeziesTypenMap.put(s.getSpeziesName(), s.getSpeziesId());

//            }
        }
        return alleSpeziesTypenMap;
    }

    /*
    Own validators
    */
    public void validateAlpha(FacesContext context, UIComponent uiComponent, Object value) throws ValidatorException {
        if (!StringUtils.isAlphaSpace((String) value)) {
            HtmlInputText htmlInputText = (HtmlInputText) uiComponent;
            FacesMessage facesMessage = new FacesMessage(htmlInputText.getLabel() + ": nur Buchstaben erlaubt");
            throw new ValidatorException(facesMessage);
        }
    }

    public void validateAlphaNumeric(FacesContext context, UIComponent uiComponent, Object value) throws ValidatorException {
        if (!StringUtils.isAlphanumericSpace((String) value)) {
            HtmlInputText htmlInputText = (HtmlInputText) uiComponent;
            FacesMessage facesMessage = new FacesMessage(htmlInputText.getLabel() + ": nur Buchstaben und Zahlen erlaubt");
            throw new ValidatorException(facesMessage);
        }
    }

    public void validateNumeric(FacesContext context, UIComponent uiComponent, Object value) throws ValidatorException {
        if (!StringUtils.isNumeric((String) value)) {
            HtmlInputText htmlInputText = (HtmlInputText) uiComponent;
            FacesMessage facesMessage = new FacesMessage(htmlInputText.getLabel() + ": nur Zahlen erlaubt");
            throw new ValidatorException(facesMessage);
        }
    }
}
