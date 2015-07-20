/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kynomics.control;

import com.kynomics.daten.Auftrag;
import com.kynomics.daten.Auftragposition;
import com.kynomics.daten.Auftragtyp;
import com.kynomics.daten.Halter;
import com.kynomics.daten.Patient;
import com.kynomics.daten.Untersuchung;
import com.kynomics.daten.Untersuchungstyp;
import com.kynomics.daten.finder.Auftragtreffer;
import com.kynomics.daten.finder.SuchkriterienAuftrag;
import com.kynomics.daten.finder.SuchkriterienUntersuchung;
import com.kynomics.daten.finder.Untersuchungtreffer;
import com.kynomics.daten.wrapper.AuftragAuftragPositionenWrapper;
import com.kynomics.lib.TransmitterSessionBeanRemote;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 *
 * @author dboehm
 */
@Named(value = "auftragController")
@SessionScoped
public class AuftragController implements Serializable {
    
    private static final long serialVersionUID = 1L;
    @EJB
    private TransmitterSessionBeanRemote transmitterSessionBeanRemote;
    
    private Auftrag currentAuftrag;
    private Auftragposition currentAuftragposition;
    private Auftragtyp auftragTyp;
    private Untersuchung currentUntersuchung;
    private List<Auftrag> auftragList;
    private List<Auftragposition> auftragpositionenList;
    private List<Untersuchung> untersuchungList;
    private List<Auftragtyp> auftragTypList;
    private List<Patient> patientList;
    
    private HalterController halterController;

    /**
     * the UntersuchungsTypen - Map for the h:selectOneMenu ...
     */
    private final Map<String, Integer> alleUntersuchungsTypenMap;
    /**
     * the AuftragTypen - Map for the h:selectOneMenu ...
     */
    private final Map<String, Integer> alleAuftragTypenMap;

    /**
     * the Patienten - Map for the h:selectOneMenu ...
     */
    private Map<String, Integer> allePatientenVonHalterTypenMap;

    /**
     * the SuchkriterienUTyp suchkriterienUTyp is set to a private property,
     * which can be used to reload each List utypList after persisting changes
     * in order to keep the list updated all the time.
     */
    private SuchkriterienUntersuchung suchkriterienUntersuchung
            = new SuchkriterienUntersuchung();
    
    private SuchkriterienAuftrag suchkriterienAuftrag
            = new SuchkriterienAuftrag();

    /**
     * The List milestoneTrefferList is set as private property, and can be used
     * to reload each List milestoneList after persisting changes in order to
     * keep the list updated all the time.
     */
    private List<Untersuchungtreffer> untersuchungTrefferList = null;
    private List<Auftragtreffer> auftragTrefferList = null;

    /**
     * Creates a new instance of AuftragController
     */
    public AuftragController() throws NamingException {
        this.alleUntersuchungsTypenMap = new HashMap();
        this.alleAuftragTypenMap = new HashMap<>();
        this.auftragTyp = new Auftragtyp();
        this.currentUntersuchung = new Untersuchung();
        this.currentAuftrag = new Auftrag();
        this.currentAuftrag.setHalterId(new Halter());
        this.untersuchungList = new ArrayList<>();
        this.currentAuftragposition = new Auftragposition();
        this.auftragpositionenList = new ArrayList<>();
        this.auftragList = new ArrayList<>();
        this.auftragTypList = new ArrayList<>();
        this.patientList = new ArrayList<>();
        this.allePatientenVonHalterTypenMap = new HashMap<>();
    }
    
    @PostConstruct
    public void init() {
        BeanManager bm = null;
        try {
            bm = (BeanManager) new InitialContext().lookup("java:comp/BeanManager");
            // OR // bm = CDI.current().getBeanManager();
           /*
             * TWO ways to go on with the BeanManager to access CDI beans
             * 1: Type-based-lookup
             */
            Bean<HalterController> bean = (Bean<HalterController>) bm.getBeans(HalterController.class).iterator().next();
            CreationalContext ctx = bm.createCreationalContext(bean);
            halterController = (HalterController) bm.getReference(bean, HalterController.class, ctx);

            /*
             * 2. Name-based-lookup (NOT WORKING !!!)
             */
//            Bean bean = bm.getBeans("HalterController").iterator().next();
//            CreationalContext ctx = bm.createCreationalContext(bean);
//            halterController = (HalterController) bm.getReference(bean, bean.getClass(), ctx);
        } catch (NamingException e) {
            throw new EJBException(e);
        }
    }
    
    public Auftrag getCurrentAuftrag() {
        return currentAuftrag;
    }
    
    public void setCurrentAuftrag(Auftrag currentAuftrag) {
        this.currentAuftrag = currentAuftrag;
    }
    
    public List<Auftrag> getAuftragList() {
        return auftragList;
    }
    
    public void setAuftragList(List<Auftrag> auftragList) {
        this.auftragList = auftragList;
    }
    
    public List<Auftragposition> getAuftragpositionenList() {
        return auftragpositionenList;
    }
    
    public void setAuftragpositionenList(List<Auftragposition> auftragpositionenList) {
        this.auftragpositionenList = auftragpositionenList;
    }
    
    public Auftragtyp getAuftragTyp() {
        return auftragTyp;
    }
    
    public void setAuftragTyp(Auftragtyp auftragTyp) {
        this.auftragTyp = auftragTyp;
    }
    
    public Auftragposition getCurrentAuftragposition() {
        return currentAuftragposition;
    }
    
    public void setCurrentAuftragposition(Auftragposition currentAuftragposition) {
        this.currentAuftragposition = currentAuftragposition;
    }
    
    public Untersuchung getCurrentUntersuchung() {
        return currentUntersuchung;
    }
    
    public void setCurrentUntersuchung(Untersuchung currentUntersuchung) {
        this.currentUntersuchung = currentUntersuchung;
    }
    
    public Map<String, Integer> getAlleUntersuchungsTypenMap() {
        List<Untersuchungstyp> tempList = transmitterSessionBeanRemote.initializeUntersuchungstypen();
        for (Untersuchungstyp next : tempList) {
            this.alleUntersuchungsTypenMap.put(next.getUntersuchungtypName(), next.getUntersuchungtypId());
        }
        return alleUntersuchungsTypenMap;
    }
    
    public Map<String, Integer> getAlleAuftragTypenMap() {
        auftragTypList = transmitterSessionBeanRemote.initializeAuftragtypen();
        for (Auftragtyp next : auftragTypList) {
            this.alleAuftragTypenMap.put(next.getAuftragtypName(), next.getAuftragtypId());
        }
        return alleAuftragTypenMap;
    }
    
    public List<Untersuchung> getUntersuchungList() {
        return untersuchungList;
    }
    
    public void setUntersuchungList(List<Untersuchung> untersuchungList) {
        this.untersuchungList = untersuchungList;
    }
    
    public List<Patient> getPatientList() {
        return patientList;
    }
    
    public void setPatientList(List<Patient> patientList) {
        this.patientList = patientList;
    }
    
    public Map<String, Integer> getAllePatientenVonHalterTypenMap() {
        patientList = transmitterSessionBeanRemote.patientGet();
        
        for (Patient p : patientList) {
            System.out.println("Patients = " + p);
            if (Objects.equals(p.getHalterHalterId().getHalterId(), currentAuftrag.getHalterId().getHalterId())) {
                this.allePatientenVonHalterTypenMap.put(p.getPatientName(), p.getPatientId());
            }
        }
        
        return allePatientenVonHalterTypenMap;
    }
    
    public void setAllePatientenVonHalterTypenMap(Map<String, Integer> allePatientenVonHalterTypenMap) {
        this.allePatientenVonHalterTypenMap = allePatientenVonHalterTypenMap;
    }
    
    public String sucheUntersuchung() {
        /*
         check the attributes first
         */
        System.out.println("currentUTyp.toString() = " + currentUntersuchung.toString());
        
        suchkriterienUntersuchung.setUntersuchungId(currentUntersuchung.getUntersuchungId());
        suchkriterienUntersuchung.setUntersuchungName(currentUntersuchung.getUntersuchungName());
        suchkriterienUntersuchung.setUntersuchungDauer(currentUntersuchung.getUntersuchungDauer());
        suchkriterienUntersuchung.setUntersuchungPreis(currentUntersuchung.getUntersuchungPreis());
        if (suchkriterienUntersuchung.toString().length() == 0) {
            System.out.println("WhereClause is empty: '" + suchkriterienUntersuchung + "'");
        } else {
            System.out.println("WhereClause: '" + suchkriterienUntersuchung + "'");
        }
        untersuchungTrefferList = transmitterSessionBeanRemote.sucheUntersuchung(suchkriterienUntersuchung);
        untersuchungList.clear();
        for (Untersuchungtreffer ut : untersuchungTrefferList) {
            Untersuchung untersuchung = details(ut);
            untersuchungList.add(untersuchung);
        }
        
        return null;
    }
    
    public String sucheAuftrag() {
        /*
         check the attributes first
         */
        System.out.println("currentAuftrag.toString() = " + currentAuftrag.toString());
        suchkriterienAuftrag.setAuftragId(currentAuftrag.getAuftragId());
        suchkriterienAuftrag.setAuftragText(currentAuftrag.getAuftragText());
        suchkriterienAuftrag.setAuftragstypId(currentAuftrag.getAuftragstypId());
        suchkriterienAuftrag.setAuftragStart(currentAuftrag.getAuftragStart());
        suchkriterienAuftrag.setAuftragEnde(currentAuftrag.getAuftragEnde());
        suchkriterienAuftrag.setHalterId(currentAuftrag.getHalterId());
        
        if (suchkriterienAuftrag.toString().length() == 0) {
            System.out.println("WhereClause is empty: '" + suchkriterienAuftrag + "'");
        } else {
            System.out.println("WhereClause: '" + suchkriterienAuftrag + "'");
        }
        auftragTrefferList = transmitterSessionBeanRemote.sucheAuftrag(suchkriterienAuftrag);
        auftragList.clear();
        for (Auftragtreffer at : auftragTrefferList) {
            Auftrag auftrag = details(at);
            auftragList.add(auftrag);
        }
        
        return null;
    }
    
    private Untersuchung details(Untersuchungtreffer ut) {
        return transmitterSessionBeanRemote.findById(Untersuchung.class, ut.getUntersuchungId());
    }
    
    private Auftrag details(Auftragtreffer at) {
        return transmitterSessionBeanRemote.findById(Auftrag.class, at.getAuftragId());
    }
    
    public String selectUntersuchung(Untersuchung untersuchung) {
        int indexOf = untersuchungList.indexOf(untersuchung);
        untersuchung.setSelected(true);
        currentUntersuchung = untersuchung;
        System.out.println("selected Untersuchung : " + untersuchung);
        untersuchungList.remove(indexOf);
        untersuchungList.add(indexOf, untersuchung);
        return null;
    }
    
    public String selectAuftrag(Auftrag auftrag) {
        /* 
         * System.out.println(auftrag.getHalterId());
         * gives access to a full named Halter, but selected attribure is not set to true, 
         * it is transient and need to be set true to have it rendered in the 
         * <h:form id = "showOrSelectSummeryForm" > of controlAuftrag.xhtml
         */
//        System.out.println("auftrag.getHalterId() = " + auftrag.getHalterId());
        /* 
         * find the Auftrag in List, store index temporally before we change it
         */
        int indexOf = auftragList.indexOf(auftrag);
        
        auftrag.setSelected(true);
        auftrag.getHalterId().setSelected(true);
        /*
         * now check if the Halter is selected
         */
//        System.out.println("auftrag.getHalterId() = " + auftrag.getHalterId());

        /*
         * remove the old auftrag and add the new auftrag by index
         */
        auftragList.remove(indexOf);
        auftragList.add(indexOf, auftrag);
        /*
         * Also we need to set the currentHalter in halterController-Bean
         */
        halterController.setCurrentHalter(auftrag.getHalterId());
        currentAuftrag = auftrag;
        return null;
    }
    
    public String deSelectUntersuchung(Untersuchung untersuchung) {
        int indexOf = untersuchungList.indexOf(untersuchung);
        untersuchung.setSelected(false);
        currentUntersuchung = new Untersuchung();
        System.out.println("deselected Untersuchung : " + untersuchung);
        untersuchungList.remove(indexOf);
        untersuchungList.add(indexOf, untersuchung);
        return null;
    }
    
    public String deSelectAuftrag(Auftrag auftrag) {
        int indexOf = auftragList.indexOf(auftrag);
        auftrag.setSelected(false);
        currentAuftrag = new Auftrag();
        auftragList.remove(indexOf);
        auftragList.add(indexOf, auftrag);
        return null;
    }
    
    public String saveAuftrag() {
        /*
         * log currentAuftrag as far as ist exists 
         */
        /* AuftragTyp auftragTyp is an emty object with an Id only, 
         * get the real object to set in currentAuftrag by the index from 
         * auftragTypList
         */
        currentAuftrag.setAuftragstypId(auftragTypList.get(auftragTyp.getAuftragtypId() - 1));
        currentAuftrag.setHalterId(halterController.getCurrentHalter());
        System.out.println("halterController.getCurrentHalter()= " + halterController.getCurrentHalter());
        System.out.println("currentAuftrag= " + currentAuftrag);
        AuftragAuftragPositionenWrapper wrapper = new AuftragAuftragPositionenWrapper(currentAuftrag, null);
        transmitterSessionBeanRemote.storeEjb(wrapper);
        return null;
    }
    
    public String addAuftragposition() {
        currentAuftragposition.setAuftragpositionNr(auftragpositionenList.size() + 1);
        /*
         * a Patient was choosen by name from the <h:selectOneMenu and an Integer 
         * as currentAuftragposition.getPatientId.getPatientId was set only and transmitted
         * TWO POSSIBILITIES
         * 1. take the patient from the patientList by this patientId
         * 2. get the Patient by Id from persistence which would be overhead and useless traffic
         * IMPLEMENT the strategy 1 because patientList hols all Patient.
         * SAME is true for currentUntersuchung when we set to currentAuftragposition
         */
        currentAuftragposition.setUntersuchungId(currentUntersuchung);
        currentAuftragposition.setAuftragId(currentAuftrag);
        // iterate over patientList
        for (Patient p : patientList) {
            if (Objects.equals(p.getPatientId(), currentAuftragposition.getPatientId().getPatientId())) {
                p.setSelected(true);
                halterController.setCurrentPatient(p);
                currentAuftragposition.setPatientId(p);
            }
        }
        System.out.println("currentAuftragposition = " + currentAuftragposition);
        auftragpositionenList.add(currentAuftragposition);
        // empty the currentAuftragposition before next transmission
        currentAuftragposition = new Auftragposition();
        return null;
        
    }
    
}
