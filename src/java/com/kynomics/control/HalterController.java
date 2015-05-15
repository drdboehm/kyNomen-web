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
import com.kynomics.daten.Spezies;
import com.kynomics.daten.finder.HalteradresseTreffer;
import com.kynomics.daten.finder.Haltertreffer;
import com.kynomics.daten.finder.Patiententreffer;
import com.kynomics.daten.finder.SuchkriterienHalter;
import com.kynomics.daten.finder.SuchkriterienHalteradresse;
import com.kynomics.daten.finder.SuchkriterienPatient;
import com.kynomics.lib.TransmitterSessionBeanRemote;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
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
    private Haltertyp haltertyp;
    private Halteradresse halteradresse;
    private Adresstyp adressTyp;

    private List<Haltertreffer> halterTrefferList;
    private List<Halter> halterList;
    private List<Patiententreffer> patientenTrefferList;
    private List<Patient> patientList;
    private List<HalteradresseTreffer> halteradresseTrefferList;
    private List<Halteradresse> halteradresseList;

    /**
     * the default constructor
     */
    public HalterController() {
        halter = new Halter();
        patient = new Patient();
        haltertyp = new Haltertyp();
        spezies = new Spezies();
        halteradresse = new Halteradresse();
        adressTyp = new Adresstyp();
        rasse = new Rasse();
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

    public List<HalteradresseTreffer> getHalteradresseTrefferList() {
        return halteradresseTrefferList;
    }

    public void setHalteradresseTrefferList(List<HalteradresseTreffer> halteradresseTrefferList) {
        this.halteradresseTrefferList = halteradresseTrefferList;
    }

    public List<Halteradresse> getHalteradresseList() {
        return halteradresseList;
    }

    public void setHalteradresseList(List<Halteradresse> halteradresseList) {
        this.halteradresseList = halteradresseList;
    }

    /*
     Own Logic
     */
    public String saveHalter() {
        halter.setHaltertypId(haltertyp);
        HalterAdresssenPatientWrapper wrapper = new HalterAdresssenPatientWrapper(halter, null, null);
        transmitterSessionBeanRemote.storeEjb(wrapper);
        return "index";
    }

    public String savePatient() {
        Halter h = transmitterSessionBeanRemote.findById(Halter.class, halter.getHalterId());
        Rasse r = transmitterSessionBeanRemote.findById(Rasse.class, rasse.getRasseId());
        patient.setRasseRasseId(r);
        patient.setHalterHalterId(h);
        HalterAdresssenPatientWrapper wrapper = new HalterAdresssenPatientWrapper(null, null, patient);
        transmitterSessionBeanRemote.storeEjb(wrapper);
        System.out.println("Patient: " + patient);
        logAttributes();
        return "index.xhtml";
    }

    public String saveAdresse() {
        halteradresse.setAdresstypId(adressTyp);
        halteradresse.setHalterId(halter);
        HalterAdresssenPatientWrapper wrapper = new HalterAdresssenPatientWrapper(null, halteradresse, null);
        transmitterSessionBeanRemote.storeEjb(wrapper);
        return "index.xhtml";
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
        SuchkriterienHalter suchKr = new SuchkriterienHalter(halter.getHalterId(), halter.getHalterName(),
                halter.getHalterBemerkung());
        if (suchKr.toString().length() == 0) {
            System.out.println("WhereClause is empty: '" + suchKr + "'");
        } else {
            System.out.println("WhereClause: '" + suchKr + "'");
        }
        halterTrefferList = transmitterSessionBeanRemote.sucheHalter(suchKr);
        halterList = new ArrayList<>();
        for (Haltertreffer ht : halterTrefferList) {
            Halter h = this.details(ht);
            halterList.add(h);
            /*
            Call to .size() here should resolve the lazy loaded relationship, thus avoiding the NullpointerException
             */
            if (h.getPatientCollection() != null) {
                System.out.println(ht.toString() + " - " + h.getPatientCollection());
//                patientList.addAll(h.getPatientCollection());
            }
        }
        return "index.xhtml";
    }

    public Halter details(Haltertreffer halterTreffer) {
        return transmitterSessionBeanRemote.findById(Halter.class, halterTreffer.halterId);
    }

    public Patient details(Patiententreffer patientenTreffer) {
        return transmitterSessionBeanRemote.findById(Patient.class, patientenTreffer.getPatientId());
    }

    public Halteradresse details(HalteradresseTreffer halteradresseTreffer) {
        return transmitterSessionBeanRemote.findById(Halteradresse.class, halteradresseTreffer.getHalteradresseId());
    }

    public String suchePatient() {
        // check the attributes first
        System.out.println("patient.toString() = " + patient.toString());
        SuchkriterienPatient suchKr = new SuchkriterienPatient();
        suchKr.setPatientId(patient.getPatientId());
        suchKr.setPatientRuf(patient.getPatientRuf());
        suchKr.setPatientName(patient.getPatientName());
        suchKr.setPatientChip(patient.getPatientChip());
        suchKr.setPatientTatoonr(patient.getPatientTatoonr());
        suchKr.setPatientZuchtbuchnr(patient.getPatientZuchtbuchnr());
        if (suchKr.toString().length() == 0) {
            System.out.println("WhereClause is empty: '" + suchKr + "'");
        } else {
            System.out.println("WhereClause: '" + suchKr + "'");
        }
        patientenTrefferList = transmitterSessionBeanRemote.suchePatient(suchKr);
        patientList = new ArrayList<>();
        for (Patiententreffer pt : patientenTrefferList) {
            patientList.add(this.details(pt));
        }
        return "index";
    }

    public String sucheAdresse() {
        // check the attributes first
        System.out.println("halteradresse.toString() = " + halteradresse.toString());

        SuchkriterienHalteradresse suchKr = new SuchkriterienHalteradresse();
        suchKr.setHalteradresseId(halteradresse.getHalteradresseId());
        suchKr.setHalterStrasse(halteradresse.getHalterStrasse());
        suchKr.setHalterPlz(halteradresse.getHalterPlz());
        suchKr.setHalterOrt(halteradresse.getHalterOrt());
        suchKr.setHalterTel(halteradresse.getHalterTel());
        suchKr.setEmail(halteradresse.getEmail());
        if (suchKr.toString().length() == 0) {
            System.out.println("WhereClause is empty: '" + suchKr + "'");
        } else {
            System.out.println("WhereClause: '" + suchKr + "'");
        }
        halteradresseTrefferList = transmitterSessionBeanRemote.sucheHalterAdresse(suchKr);
        halteradresseList = new ArrayList<>();
        for (HalteradresseTreffer hat : halteradresseTrefferList) {
            halteradresseList.add(this.details(hat));
        }
        return "index";
    }

    public void logAttributes() {
        /*
         first collect, what we have from the JSF page
         */
        System.out.println("**********  Halter related Attributes ***** ");
        System.out.println("halter = " + halter);
        System.out.println("halter.getHalterId() = " + halter.getHalterId());
        System.out.println("halter.getHalterName() = " + halter.getHalterName());
        System.out.println("halter.getHalterBemerkung() = " + halter.getHalterBemerkung());
        System.out.println("halter.getHaltertypId() = " + halter.getHaltertypId());
        System.out.println("haltertyp = " + haltertyp);
        System.out.println("haltertyp.getHaltertypId() = " + haltertyp.getHaltertypId());
        System.out.println("haltertyp.getHaltertypName() = " + haltertyp.getHaltertypName());
        System.out.println("**********  Patient related Attributes ***** ");
        System.out.println("patient = " + patient);
        System.out.println("patient.getPatientId() = " + patient.getPatientId());
        System.out.println("patient.getPatientRuf() = " + patient.getPatientRuf());
        System.out.println("patient.getPatientName() = " + patient.getPatientName());
        System.out.println("patient.getPatientChip() = " + patient.getPatientChip());
        System.out.println("patient.getPatientTatoonr() = " + patient.getPatientTatoonr());
        System.out.println("patient.getPatientZuchtbuchnr() = " + patient.getPatientZuchtbuchnr());
        System.out.println("patient.getPatientGeb() = " + patient.getPatientGeb());
        System.out.println("patient.getRasseRasseId() = " + patient.getRasseRasseId());
        System.out.println("patient.getHalterHalterId() = " + patient.getHalterHalterId());
        System.out.println("patient.getAuftragpositionCollection() = " + patient.getAuftragpositionCollection());
        System.out.println("**********  Spezies related Attributes ***** ");
        System.out.println("spezies = " + spezies);
        System.out.println("spezies.getSpeziesId() = " + spezies.getSpeziesId());
        System.out.println("spezies.getSpeziesName() = " + spezies.getSpeziesName());
        System.out.println("spezies.getRasseCollection() = " + spezies.getRasseCollection());
        System.out.println("**********  Rasse related Attributes ***** ");
        System.out.println("rasse = " + rasse);
        System.out.println("rasse.getRasseId() = " + rasse.getRasseId());
        System.out.println("rasse.getRasseName() = " + rasse.getRasseName());
        System.out.println("rasse.getSpeziesSpeziesId() = " + rasse.getSpeziesSpeziesId());
        System.out.println("rasse.getPatientCollection() = " + rasse.getPatientCollection());
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
//        System.out.println("spezies.getSpeziesId() = " + spezies.getSpeziesId());
        for (Rasse r : list) {
//            System.out.println("r.getRasseName() = " + r.getRasseName() + "  r.getSpeziesSpeziesId().getSpeziesId() = " + r.getSpeziesSpeziesId().getSpeziesId());
            /*
                use nullsafe equals of Integer-Type
             */
            if (Objects.equals(spezies.getSpeziesId(), r.getSpeziesSpeziesId().getSpeziesId())) {
//                System.out.println("" + r.getRasseName() + "-" + r.getSpeziesSpeziesId().getSpeziesId());
                alleRasseTypenMap.put(r.getRasseName(), r.getRasseId());
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
