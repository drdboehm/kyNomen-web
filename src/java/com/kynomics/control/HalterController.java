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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import javax.annotation.PostConstruct;
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
    private List<Halter> filteredHalterList;
    final SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
    private Boolean sortOrderDesc = false;

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

    @PostConstruct
    public void init() {
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

    public List<Halter> getFilteredHalterList() {
        return filteredHalterList;
    }

    public void setFilteredHalterList(List<Halter> filteredHalterList) {
        this.filteredHalterList = filteredHalterList;
    }

    /*
     Own Logic
     */
    public String saveHalter() {
//        System.out.println("Halter = " + halter);
//        System.out.println("Haltertyp = " + haltertyp);
//        System.out.println("checkHalterEntries() = " + checkHalterEntries());
//        if (!checkHalterEntries()) {
//            return "index";
//        }
        halter.setHaltertypId(haltertyp);
        HalterAdresssenPatientWrapper wrapper = new HalterAdresssenPatientWrapper(halter, null, null);
        transmitterSessionBeanRemote.storeEjb(wrapper);
        return "index";
    }

    private boolean checkHalterEntries() {
        boolean valid = true;
        /**
         * check required fields
         */
        System.out.println("halter.getHalterName().isEmpty() = " + halter.getHalterName().isEmpty());
        System.out.println("Haltertyp == null : " + haltertyp == null);
        if (halter.getHalterName().isEmpty()
                || halter.getHalterBemerkung().isEmpty()
                || haltertyp == null) {
            valid = false;
        }
        return valid;
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

    public String resetLists() {
        halterList.clear();
        patientList.clear();
        halteradresseList.clear();
        halter = new Halter();
        patient = new Patient();
        halteradresse = new Halteradresse();
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
        patientList = new ArrayList<>();
        halteradresseList = new ArrayList<>();
        for (Haltertreffer ht : halterTrefferList) {
            Halter h = this.details(ht);
            halterList.add(h);
            /*
             Call to .size() here should resolve the lazy loaded relationship, thus avoiding the NullpointerException
             */
            if (h.getPatientCollection() != null) {
                System.out.println(ht.toString() + " - Size PatientList: " + h.getPatientCollection().size());
                patientList.addAll(h.getPatientCollection());
            }

            if (h.getHalteradresseCollection() != null) {
                System.out.println(ht.toString() + " - Size AdressList: "
                        + h.getHalteradresseCollection().size());
                halteradresseList.addAll(h.getHalteradresseCollection());
            }
        }
        return "index.xhtml";
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
        halterList = new ArrayList<>();
        halteradresseList = new ArrayList<>();
        for (Patiententreffer pt : patientenTrefferList) {
            Patient p = this.details(pt);
            patientList.add(p);
            Halter h = p.getHalterHalterId();
            halterList.add(h);
            Collection<Halteradresse> halteradresseCollection = p.getHalterHalterId().getHalteradresseCollection();
            halteradresseList.addAll(halteradresseCollection);
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
        halterList = new ArrayList<>();
        patientList = new ArrayList<>();
        for (HalteradresseTreffer hat : halteradresseTrefferList) {
            Halteradresse ha = this.details(hat);
            halteradresseList.add(ha);
            halterList.add(ha.getHalterId());
            Collection<Patient> patientCollection = ha.getHalterId().getPatientCollection();
            patientList.addAll(patientCollection);
        }
        return "index";
    }

    public String deleteHalter(Integer halterId) {
        System.out.println("Delete halter with Id " + halterId);
        Halter deleteById = transmitterSessionBeanRemote.deleteById(Halter.class, halterId);
        if (deleteById != null) {
            System.out.println("Halter Details deleted from database: " + deleteById);
        }

        /*
         here we need to adjust the halterList, patientList and halteradresseList
         first of all: we have a DELETE ON CASCADE in our DELETE query!
         We need to decide, whether this should be like this!?
         If so, we have to possibilities to update the other lists.
         1. the search can be repeated at ALL, then all will be updated, BUT there will be traffic to the EJB and back! 
         2. we remove the CASCADED entries by hand here !
         3. third, we do not want to delete patienten and halteradresses !?
         */
        if (halterList.remove(deleteById)) {
            System.out.println("Halter Details deleted from halterList: " + deleteById);

        };
        return "index";
    }

    public void editHalter(Halter halter) {
        halter.setEdited(true);
    }

    public void saveHalter(Halter halter) {
        halter.setEdited(false);
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

    public String sortHalterByRanking() {
        Collections.sort(halterList, new Comparator<Halter>() {
            @Override
            public int compare(Halter o1, Halter o2) {
                Integer diff = (o1.getHalterId() - o2.getHalterId());
                diff = sortOrderDesc ? (diff) : (-diff);
                return diff;
            }
        });
        sortOrderDesc = !sortOrderDesc;
        return null;
    }

    public String sortHalterByName() {
        Collections.sort(halterList, new Comparator<Halter>() {
            @Override
            public int compare(Halter o1, Halter o2) {
                if (sortOrderDesc) {
                    return o1.getHalterName().compareTo(o2.getHalterName());
                } else {
                    return o2.getHalterName().compareTo(o1.getHalterName());
                }
            }
        });
        sortOrderDesc = !sortOrderDesc;
        return null;

    }

    public String sortHalterByBemerkung() {
        Collections.sort(halterList, new Comparator<Halter>() {
            @Override
            public int compare(Halter o1, Halter o2) {
                if (sortOrderDesc) {
                    return o1.getHalterBemerkung().compareTo(o2.getHalterBemerkung());
                } else {
                    return o2.getHalterBemerkung().compareTo(o1.getHalterBemerkung());
                }
            }
        });
        sortOrderDesc = !sortOrderDesc;
        return null;

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

}
