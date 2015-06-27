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
import com.kynomics.daten.wrapper.SpeziesRasseWrapper;
import com.kynomics.lib.TransmitterSessionBeanRemote;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;
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

    private Halter currentHalter;
    private Patient currentPatient;
    private Spezies spezies;
    private Rasse rasse;
    private Haltertyp haltertyp;
    private Halteradresse currentHalteradresse;
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
     * the Suchkriterien are set private properties, which can be used to reload
     * each list after persisting changes in order to keep the list updated all
     * the time.
     */
    private SuchkriterienHalteradresse suchKrHalteradresse = new SuchkriterienHalteradresse();
    private SuchkriterienPatient suchKrPatient = new SuchkriterienPatient();
    private SuchkriterienHalter suchKrHalter = new SuchkriterienHalter();

    /**
     * the default constructor
     */
    public HalterController() {
        currentHalter = new Halter();
        currentPatient = new Patient();
        haltertyp = new Haltertyp();
        spezies = new Spezies();
        currentHalteradresse = new Halteradresse();
        adressTyp = new Adresstyp();
        rasse = new Rasse();
        patientList = new ArrayList<>();
        halterList = new ArrayList<>();
        halteradresseList = new ArrayList<>();

    }

    @PostConstruct
    public void init() {
    }

    /* 
     the HalterTypen - Map for the  <h:selectOneMenu ... 
     */
    private final Map<String, Integer> alleHalterTypenMap = new TreeMap();

    /* 
     the HalterAdressTypen - Map for the  <h:selectOneMenu ... as label, value
     */
    private final Map<String, Integer> alleAdressTypenMap = new TreeMap();

    /* 
     the SpeziesTypen - Map for the  <h:selectOneMenu ... 
     */
    private final Map<String, Integer> alleSpeziesTypenMap = new TreeMap();

    /* 
     the RasseTypen - Map for the  <h:selectOneMenu ... 
     */
    private final Map<String, Integer> alleRasseTypenMap = new TreeMap();

    /*
     Getters and setters
     */
    public Patient getCurrentPatient() {
        return currentPatient;
    }

    public void setCurrentPatient(Patient currentPatient) {
        this.currentPatient = currentPatient;
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

    public Halter getCurrentHalter() {
        return currentHalter;
    }

    public void setCurrentHalter(Halter currentHalter) {
        this.currentHalter = currentHalter;
    }

    public Haltertyp getHaltertyp() {
        return haltertyp;
    }

    public void setHaltertyp(Haltertyp haltertyp) {
        this.haltertyp = haltertyp;
    }

    public Halteradresse getCurrentHalteradresse() {
        return currentHalteradresse;
    }

    public void setCurrentHalteradresse(Halteradresse currentHalteradresse) {
        this.currentHalteradresse = currentHalteradresse;
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
        currentHalter.setHaltertypId(haltertyp);
        HalterAdresssenPatientWrapper wrapper = new HalterAdresssenPatientWrapper(currentHalter, null, null);
        transmitterSessionBeanRemote.storeEjb(wrapper);
        sucheHalter();
        return null;
    }

    public String savePatient() {

        Halter h = transmitterSessionBeanRemote.findById(Halter.class, currentHalter.getHalterId());
        Rasse r = transmitterSessionBeanRemote.findById(Rasse.class, rasse.getRasseId());
        currentPatient.setRasseRasseId(r);
        currentPatient.setHalterHalterId(h);
        HalterAdresssenPatientWrapper wrapper = new HalterAdresssenPatientWrapper(null, null, currentPatient);
        transmitterSessionBeanRemote.storeEjb(wrapper);
        System.out.println("Patient: " + currentPatient);
        logAttributes();
        suchePatient();
        return null;
    }

    public String saveSpezies() {
        /**
         * speziesID != null indicates an existing Spezies which gets a new
         * Rasse entry if rasseId == 0 and rasseName is not empty
         * (rasseName.lenght != 0) speziesId == 0 indicates a new spezies which
         * has a name field only and perhaps a rasseName with rasseId == 0
         * indicating a new Rasse entry which boths entitys an needs to be
         * persisted. A rasseId == 0 together with an empty name indicates a
         * missing rasse entity
         *
         * we implement this logic in the EJB
         */
        System.out.println("Spezies: " + spezies);
        System.out.println("Rasse " + rasse);
        Integer id;
        if ((id = spezies.getSpeziesId()) != 0) {
            // get the Spezies-Entity by Id because it exists and need to be set as a  
            // foreign key in the corresponding Rasse entity
            Spezies s = transmitterSessionBeanRemote.findById(Spezies.class, id);
            rasse.setSpeziesSpeziesId(s);
        }
        if (rasse.getRasseName().length() > 0 && spezies.getSpeziesName().length() > 0) {
            SpeziesRasseWrapper wrapper = new SpeziesRasseWrapper(spezies, rasse);
            if (transmitterSessionBeanRemote.storeEjb(wrapper)) {
                return null; // success 
            } else {
                // give a note, that RasseName can not be empty
                return null; // failure
            }

        }
        return null; //default
    }

    public String saveAdresse() {
        currentHalteradresse.setAdresstypId(adressTyp);
        currentHalteradresse.setHalterId(currentHalter);
        HalterAdresssenPatientWrapper wrapper = new HalterAdresssenPatientWrapper(null, currentHalteradresse, null);
        transmitterSessionBeanRemote.storeEjb(wrapper);
        sucheAdresse();
        return null;
    }

    public String resetLists() {
        halterList.clear();
        patientList.clear();
        halteradresseList.clear();
        currentHalter = new Halter();
        currentPatient = new Patient();
        currentHalteradresse = new Halteradresse();
        return null;
    }

    public String resetSpeziesRasse() {
        spezies.setSpeziesId(0);
        return null;
    }

    public String sucheHalter() {
        /*
         check the attributes first
         */
//        System.out.println("halter.toString() = " + currentHalter.toString());

        suchKrHalter.setHalterId(currentHalter.getHalterId());
        suchKrHalter.setTeilVonHalterName(currentHalter.getHalterName());
        suchKrHalter.setTeilDerBeschreibung(currentHalter.getHalterBemerkung());
        if (suchKrHalter.toString().length() == 0) {
            System.out.println("WhereClause is empty: '" + suchKrHalter + "'");
        } else {
            System.out.println("WhereClause: '" + suchKrHalter + "'");
        }
        halterTrefferList = transmitterSessionBeanRemote.sucheHalter(suchKrHalter);
        resetLists();
        for (Haltertreffer ht : halterTrefferList) {
            Halter h = this.details(ht);
            System.out.println("Haltertyp-Details  in Halter before adding to halterList : " + h.getHaltertypId().getHaltertypName());
            halterList.add(h);
            /*
             Call to .size() here should resolve the lazy loaded relationship, thus avoiding the NullpointerException
             */
//            if (h.getPatientCollection() != null) {
//                System.out.println(ht.toString() + " - Size PatientList: " + h.getPatientCollection().size());
//                patientList.addAll(h.getPatientCollection());
//            }
//
//            if (h.getHalteradresseCollection() != null) {
//                System.out.println(ht.toString() + " - Size AdressList: "
//                        + h.getHalteradresseCollection().size());
//                halteradresseList.addAll(h.getHalteradresseCollection());
//            }
        }
        return null;
    }

    public String suchePatient() {
        // check the attributes first
//        System.out.println("patient.toString() = " + currentPatient.toString());

        suchKrPatient.setPatientId(currentPatient.getPatientId());
        suchKrPatient.setPatientRuf(currentPatient.getPatientRuf());
        suchKrPatient.setPatientName(currentPatient.getPatientName());
        suchKrPatient.setPatientChip(currentPatient.getPatientChip());
        suchKrPatient.setPatientTatoonr(currentPatient.getPatientTatoonr());
        suchKrPatient.setPatientZuchtbuchnr(currentPatient.getPatientZuchtbuchnr());
        if (suchKrPatient.toString().length() == 0) {
            System.out.println("WhereClause is empty: '" + suchKrPatient + "'");
        } else {
            System.out.println("WhereClause: '" + suchKrPatient + "'");
        }
        patientenTrefferList = transmitterSessionBeanRemote.suchePatient(suchKrPatient);
        resetLists();
        for (Patiententreffer pt : patientenTrefferList) {
            Patient p = this.details(pt);
            patientList.add(p);
//            Halter h = p.getHalterHalterId();
//            halterList.add(h);
//            Collection<Halteradresse> halteradresseCollection = p.getHalterHalterId().getHalteradresseCollection();
//            halteradresseList.addAll(halteradresseCollection);
        }
        return null;
    }

    public String sucheAdresse() {
        // check the attributes first
//        System.out.println("halteradresse.toString() = " + currentHalteradresse.toString());

        suchKrHalteradresse.setHalteradresseId(currentHalteradresse.getHalteradresseId());
        suchKrHalteradresse.setHalterStrasse(currentHalteradresse.getHalterStrasse());
        suchKrHalteradresse.setHalterPlz(currentHalteradresse.getHalterPlz());
        suchKrHalteradresse.setHalterOrt(currentHalteradresse.getHalterOrt());
        suchKrHalteradresse.setHalterTel(currentHalteradresse.getHalterTel());
        suchKrHalteradresse.setEmail(currentHalteradresse.getEmail());
        if (suchKrHalteradresse.toString().length() == 0) {
            System.out.println("WhereClause is empty: '" + suchKrHalteradresse + "'");
        } else {
            System.out.println("WhereClause: '" + suchKrHalteradresse + "'");
        }
        halteradresseTrefferList = transmitterSessionBeanRemote.sucheHalterAdresse(suchKrHalteradresse);
        resetLists();
        for (HalteradresseTreffer hat : halteradresseTrefferList) {
            Halteradresse ha = this.details(hat);
            halteradresseList.add(ha);
//            halterList.add(ha.getHalterId());
//            Collection<Patient> patientCollection = ha.getHalterId().getPatientCollection();
//            patientList.addAll(patientCollection);
        }
        return null;
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

        }
        return null;
    }

    public void editHalter(Halter halter) {
        halter.setEdited(true);
    }

    public String deletePatient(Integer patientId) {
        System.out.println("Delete patient with Id " + patientId);
        Patient deleteById = transmitterSessionBeanRemote.deleteById(Patient.class, patientId);
        if (deleteById != null) {
            System.out.println("Patient Details deleted from database: " + deleteById);
        }
        if (patientList.remove(deleteById)) {
            System.out.println("Patient Details deleted from patientList: " + deleteById);
        }
        return null;
    }

    public void editPatient(Patient patient) {
        patient.setEdited(true);
    }

    public void editHalteradresse(Halteradresse ha) {
        ha.setEdited(true);
    }

    public String deleteHalteradresse(Integer halteradresseId) {
        System.out.println("Delete halteradresse with Id " + halteradresseId);
        Halteradresse deleteById = transmitterSessionBeanRemote.deleteById(Halteradresse.class, halteradresseId);
        if (deleteById != null) {
            System.out.println("Halteradresse Details deleted from database: " + deleteById);
        }
        if (halteradresseList.remove(deleteById)) {
            System.out.println("Halteradresse Details deleted from halteradresseList: " + deleteById);
        }
        return null;
    }

    public void saveHalter(Halter halter) {
        System.out.println("Halter : " + halter);
        HalterAdresssenPatientWrapper wrapper = new HalterAdresssenPatientWrapper(halter, null, null);
        boolean success = transmitterSessionBeanRemote.storeEjb(wrapper);
        if (success) {
            halter.setEdited(false);
            sucheHalter();
        } else {
            // send a message
        }
    }

    public void savePatient(Patient patient) {
        System.out.println("Patient : " + patient);
        HalterAdresssenPatientWrapper wrapper = new HalterAdresssenPatientWrapper(null, null, patient);
        boolean success = transmitterSessionBeanRemote.storeEjb(wrapper);
        if (success) {
            patient.setEdited(false);
            suchePatient();
        } else {
            // send a message
        }
    }

    public void saveHalteradresse(Halteradresse ha) {
        System.out.println("Halteradresse : " + ha);
        HalterAdresssenPatientWrapper wrapper = new HalterAdresssenPatientWrapper(null, ha, null);
        boolean success = transmitterSessionBeanRemote.storeEjb(wrapper);
        if (success) {
            ha.setEdited(false);
            sucheAdresse();
        } else {
            // send a message
        }
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

    public void selectHalter(Halter halter) {
        halter.setSelected(true);
        System.out.println("selected Halter : " + halter);
        currentHalter = halter;
        patientList.clear();
        halteradresseList.clear();
        for (Halter h : halterList) {
//            System.out.println("Halter: " + h);
            if (h.isSelected()) {
                fillOtherListsBySelected(h);
            }
        }
    }

    public void selectPatient(Patient patient) {
        patient.setSelected(true);
        System.out.println("selected Patient : " + patient);
        currentPatient = patient;
        halterList.clear();
        halteradresseList.clear();
        for (Patient p : patientList) {
            if (p.isSelected()) {
                fillOtherListsBySelected(p);
            }

        }
    }

    public void selectHalteradresse(Halteradresse ha) {
        ha.setSelected(true);
        System.out.println("selected Halteradresse : " + ha);
        currentHalteradresse = ha;
        patientList.clear();
        halterList.clear();
        for (Halteradresse hal : halteradresseList) {
//            System.out.println("Halter: " + h);
            if (hal.isSelected()) {
                fillOtherListsBySelected(hal);
            }
        }

    }

    public String fillOtherListsBySelected(Halter h) {
        patientList.addAll(h.getPatientCollection());
        halteradresseList.addAll(h.getHalteradresseCollection());
        return null;
    }

    public String fillOtherListsBySelected(Patient p) {
        halterList.add(p.getHalterHalterId());
        halteradresseList.addAll(p.getHalterHalterId().getHalteradresseCollection());
        return null;
    }

    public String fillOtherListsBySelected(Halteradresse ha) {
        patientList.addAll(ha.getHalterId().getPatientCollection());
        halterList.add(ha.getHalterId());
        return null;
    }

    public void deSelectHalter(Halter halter) {
        halter.setSelected(false);
        currentHalter = new Halter();
        patientList.clear();
        halteradresseList.clear();
        for (Halter h : halterList) {
//            System.out.println("Halter: " + h);
            if (h.isSelected()) {
                fillOtherListsBySelected(h);
            }
        }
    }

    public void deSelectPatient(Patient patient) {
        patient.setSelected(false);
        currentPatient = new Patient();
        halterList.clear();
        halteradresseList.clear();
        for (Patient p : patientList) {
            if (p.isSelected()) {
                fillOtherListsBySelected(p);
            }
        }
    }

    public void deSelectHalteradresse(Halteradresse ha) {
        ha.setSelected(false);
        currentHalteradresse = new Halteradresse();
        halterList.clear();
        patientList.clear();
        for (Halteradresse hal : halteradresseList) {
            if (hal.isSelected()) {
                fillOtherListsBySelected(hal);
            }
        }
    }

    /**
     * create and returns a Map with String of all Halter -Objects to show in
     * the h:selectOneMenu ...
     * and the HaltertypId as key
     *
     * @return Map
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
            alleRasseTypenMap.put("neue Rasse anlegen", 0);
        }
        return alleRasseTypenMap;
    }

    public Map<String, Integer> getAlleSpeziesTypenMap() {
        List<Spezies> list = transmitterSessionBeanRemote.initializeSpeziesTypen();
        alleSpeziesTypenMap.clear();
        for (Spezies s : list) {
            alleSpeziesTypenMap.put(s.getSpeziesName(), s.getSpeziesId());
            alleSpeziesTypenMap.put("neue Spezies anlegen", 0);
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

    public String sortHalterById() {
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
        System.out.println("halter = " + currentHalter);
        System.out.println("halter.getHalterId() = " + currentHalter.getHalterId());
        System.out.println("halter.getHalterName() = " + currentHalter.getHalterName());
        System.out.println("halter.getHalterBemerkung() = " + currentHalter.getHalterBemerkung());
        System.out.println("halter.getHaltertypId() = " + currentHalter.getHaltertypId());
        System.out.println("haltertyp = " + haltertyp);
        System.out.println("haltertyp.getHaltertypId() = " + haltertyp.getHaltertypId());
        System.out.println("haltertyp.getHaltertypName() = " + haltertyp.getHaltertypName());
        System.out.println("**********  Patient related Attributes ***** ");
        System.out.println("patient = " + currentPatient);
        System.out.println("patient.getPatientId() = " + currentPatient.getPatientId());
        System.out.println("patient.getPatientRuf() = " + currentPatient.getPatientRuf());
        System.out.println("patient.getPatientName() = " + currentPatient.getPatientName());
        System.out.println("patient.getPatientChip() = " + currentPatient.getPatientChip());
        System.out.println("patient.getPatientTatoonr() = " + currentPatient.getPatientTatoonr());
        System.out.println("patient.getPatientZuchtbuchnr() = " + currentPatient.getPatientZuchtbuchnr());
        System.out.println("patient.getPatientGeb() = " + currentPatient.getPatientGeb());
        System.out.println("patient.getRasseRasseId() = " + currentPatient.getRasseRasseId());
        System.out.println("patient.getHalterHalterId() = " + currentPatient.getHalterHalterId());
        System.out.println("patient.getAuftragpositionCollection() = " + currentPatient.getAuftragpositionCollection());
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
