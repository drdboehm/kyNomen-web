/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kynomics.control;

import com.kynomics.daten.Adresstyp;
import com.kynomics.daten.Halter;
import com.kynomics.daten.wrapper.HalterAdresssenPatientWrapper;
import com.kynomics.daten.Halteradresse;
import com.kynomics.daten.Haltertyp;
import com.kynomics.daten.Patient;
import com.kynomics.daten.Rasse;
import com.kynomics.daten.RassePK;
import com.kynomics.daten.Spezies;
import com.kynomics.daten.finder.Haltertreffer;
import com.kynomics.daten.finder.Patiententreffer;
import com.kynomics.daten.finder.Suchkriterien;
import com.kynomics.lib.TransmitterSessionBeanRemote;
import java.io.Serializable;
import java.util.ArrayList;
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
    private Halteradresse halteradresse;
    private Adresstyp adressTyp;

    private List<Haltertreffer> halterTrefferList;
    private List<Halter> halterList;
    private List<Patiententreffer> patientenTrefferList;
    private List<Patient> patientList;

    /**
     * the default constructor
     */
    public HalterController() {
        halter = new Halter();
        patient = new Patient();
        haltertyp = new Haltertyp();
        spezies = new Spezies();
        rassePK = new RassePK();
        halteradresse = new Halteradresse();
        adressTyp = new Adresstyp();
    }

    /* 
     the HalterTypen - Map for the  <h:selectOneMenu ... 
     */
    private final Map<String, Integer> alleHalterTypenMap = new HashMap();

    /* 
     the HalterAdressTypen - Map for the  <h:selectOneMenu ... 
     */
    private final Map<String, Integer> alleAdressTypenMap = new HashMap();

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

    public Halteradresse getHalteradresse() {
        return halteradresse;
    }

    public void setHalteradresse(Halteradresse halteradresse) {
        this.halteradresse = halteradresse;
    }

    public Adresstyp getAdressTyp() {
        return adressTyp;
    }

    public void setAdressTyp(Adresstyp adressTyp) {
        this.adressTyp = adressTyp;
    }

    public List<Haltertreffer> getHalterTrefferList() {
        return halterTrefferList;
    }

    public void setHalterTrefferList(List<Haltertreffer> halterTrefferList) {
        this.halterTrefferList = halterTrefferList;
    }

    public List<Patiententreffer> getPatientenTrefferList() {
        return patientenTrefferList;
    }

    public void setPatientenTrefferList(List<Patiententreffer> patientenTrefferList) {
        this.patientenTrefferList = patientenTrefferList;
    }

    public List<Halter> getHalterList() {
        return halterList;
    }

    public void setHalterList(List<Halter> halterList) {
        this.halterList = halterList;
    }

    public List<Patient> getPatientList() {
        return patientList;
    }

    public void setPatientList(List<Patient> patientList) {
        this.patientList = patientList;
    }

    
    /*
     Own Logic
     */
    public String saveHalter() {
        HalterAdresssenPatientWrapper wrapper = new HalterAdresssenPatientWrapper();
        halter.setHaltertypId(haltertyp);
        wrapper.setHalter(halter);
        transmitterSessionBeanRemote.storeEjb(wrapper);
        return "index";
    }

    public String resetSpeziesRasse() {
        spezies.setSpeziesId(0);
        return "index";
    }

    public String sucheHalter() {
        /*
         check the attributes first
         */
        System.out.println("halter.toString() = " + halter.toString());
        Suchkriterien suchKr = new Suchkriterien(halter.getHalterId(), halter.getHalterName(),
                halter.getHalterBemerkung());
        if (suchKr.toString().length() == 0) {
            System.out.println("WhereClause is empty: '" + suchKr + "'");
        } else {
            System.out.println("WhereClause: '" + suchKr + "'");
        }
        halterTrefferList = transmitterSessionBeanRemote.sucheHalter(suchKr);
        halterList = new ArrayList<Halter>();
        patientList = new ArrayList<Patient>();
                
        for (Haltertreffer ht : halterTrefferList) {
            Halter h = transmitterSessionBeanRemote.findById(Halter.class, ht.halterId);
            halterList.add(h);
            System.out.println(ht.toString());
            patientList.addAll(h.getPatientCollection());
        }
        return "index.xhtml";
    }

    public Halter details(Haltertreffer halterTreffer) {
        return (Halter) transmitterSessionBeanRemote.findById(Object.class, halterTreffer.halterId);
    }

    public Patient details(Patiententreffer patientenTreffer) {
        return (Patient) transmitterSessionBeanRemote.findById(Object.class, patientenTreffer.getPatientId());
    }

    public String suchePatient() {
        HalterAdresssenPatientWrapper wrapper = new HalterAdresssenPatientWrapper();

        wrapper.setHalter(halter);
        transmitterSessionBeanRemote.storeEjb(wrapper);
        return "index";
    }

    public String savePatient() {
        System.out.println("Patient: " + patient);
        return "index.xhtml";
    }

    public void logAttributes() {
        /*
         first collect, what we have from the JSF page
         */
        System.out.println("**********  Halter related Attributes ***** ");
        System.out.println("halter.getHalterId() = " + halter.getHalterId());
        System.out.println("halter.getHalterName() = " + halter.getHalterName());
        System.out.println("halter.getHalterBemerkung() = " + halter.getHalterBemerkung());
        System.out.println("halter.getHaltertypId() = " + halter.getHaltertypId());
        System.out.println("haltertyp = " + haltertyp);
        System.out.println("haltertyp.getHaltertypId() = " + haltertyp.getHaltertypId());
        System.out.println("haltertyp.getHaltertypName() = " + haltertyp.getHaltertypName());
        System.out.println("**********  Patient related Attributes ***** ");
        System.out.println("patient.getPatientRuf() = " + patient.getPatientRuf());
        System.out.println("spezies.getSpeziesId() = " + spezies.getSpeziesId());
        System.out.println("spezies.getSpeziesName() = " + spezies.getSpeziesName());
        System.out.println("rassePK.getRasseId() = " + rassePK.getRasseId());
        System.out.println("rassePK.getSpeziesId() = " + rassePK.getSpeziesId());
        System.out.println("**********************************");
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
        }
        return alleSpeziesTypenMap;
    }

    public Map<String, Integer> getAlleAdressTypenMap() {
        List<Adresstyp> list = transmitterSessionBeanRemote.initializeAdressTypen();
        for (Adresstyp a : list) {
            alleAdressTypenMap.put(a.getAdresstypName(), a.getAdresstypId());
        }
        return alleAdressTypenMap;
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
