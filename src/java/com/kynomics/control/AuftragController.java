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
import com.kynomics.daten.finder.AuftragpositionTreffer;
import com.kynomics.daten.finder.Auftragtreffer;
import com.kynomics.daten.finder.SuchkriterienAuftrag;
import com.kynomics.daten.finder.SuchkriterienAuftragposition;
import com.kynomics.daten.finder.SuchkriterienUntersuchung;
import com.kynomics.daten.finder.Untersuchungtreffer;
import com.kynomics.daten.wrapper.AuftragAuftragPositionenWrapper;
import com.kynomics.daten.wrapper.UntersuchungWrapper;
import com.kynomics.lib.TransmitterSessionBeanRemote;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
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
import org.apache.commons.lang3.time.DateUtils;

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
    private Map<String, Integer> allePatientenVonHalterMap;

    /**
     * the SuchkriterienUTyp suchkriterienUTyp is set to a private property,
     * which can be used to reload each List utypList after persisting changes
     * in order to keep the list updated all the time.
     */
    private SuchkriterienUntersuchung suchkriterienUntersuchung
            = new SuchkriterienUntersuchung();

    private SuchkriterienAuftrag suchkriterienAuftrag
            = new SuchkriterienAuftrag();

    private SuchkriterienAuftragposition suchkriterienAuftragposition
            = new SuchkriterienAuftragposition();

    /**
     * The List milestoneTrefferList is set as private property, and can be used
     * to reload each List milestoneList after persisting changes in order to
     * keep the list updated all the time.
     */
    private List<Untersuchungtreffer> untersuchungTrefferList = null;
    private List<Auftragtreffer> auftragTrefferList = null;
    private List<AuftragpositionTreffer> auftragpositionTrefferList = null;

    /**
     * Creates a new instance of AuftragController
     *
     * @throws javax.naming.NamingException
     */
    public AuftragController() throws NamingException {
        this.alleUntersuchungsTypenMap = new HashMap();
        this.alleAuftragTypenMap = new HashMap<>();
        this.auftragTyp = new Auftragtyp();
        this.currentUntersuchung = new Untersuchung();
        /*
         create a new Auftrag, create Halter, setStartDate and Enddate with 
         * org.apache.commons.lang3.time.DateUtils to add to weeks per default
         */
        this.currentAuftrag = new Auftrag();
        this.currentAuftrag.setHalterId(new Halter());
        this.currentAuftrag.setAuftragStart(new Date());
        this.currentAuftrag.setAuftragEnde(DateUtils.addWeeks(this.currentAuftrag.getAuftragStart(), 2));
        this.currentAuftrag.setAuftragstypId(new Auftragtyp());
        this.untersuchungList = new ArrayList<>();
        this.currentAuftragposition = new Auftragposition();
        this.auftragpositionenList = new ArrayList<>();
        this.auftragList = new ArrayList<>();
        this.auftragTypList = new ArrayList<>();
        this.patientList = new ArrayList<>();
        this.allePatientenVonHalterMap = new HashMap<>();
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
        /*
         * set current Date to currentAuftrag.setAuftragStart
         */
        currentAuftrag.setAuftragStart(new Date());
        /*
         * add to weeks to extrapolate AuftragEnde and set in currentAuftrag
         */
        currentAuftrag.setAuftragEnde(DateUtils.addWeeks(this.currentAuftrag.getAuftragStart(), 2));
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

    public Map<String, Integer> getAllePatientenVonHalterMap() {
        patientList = transmitterSessionBeanRemote.patientGet();
        /*
         * get selected Halter from halterController and set in auftrag
         */
        if (currentAuftrag.getHalterId() == null) {
            currentAuftrag.setHalterId(halterController.getCurrentHalter());
        }
        for (Patient p : patientList) {
            /*
             * if no Halter is selected so far 
             */
//            System.out.println("halterController.getCurrentHalter() = " + halterController.getCurrentHalter());
//            System.out.println("Patients = " + p + " currentAuftrag.getHalterId() = " + currentAuftrag.getHalterId());
//            if (Objects.equals(p.getHalterHalterId().getHalterId(), currentAuftrag.getHalterId().getHalterId())) {
            if (Objects.equals(p.getHalterHalterId().getHalterId(), halterController.getCurrentHalter().getHalterId())) {
                this.allePatientenVonHalterMap.put(p.getPatientName(), p.getPatientId());
            }
        }

        return allePatientenVonHalterMap;
    }

    public void setAllePatientenVonHalterMap(Map<String, Integer> allePatientenVonHalterMap) {
        this.allePatientenVonHalterMap = allePatientenVonHalterMap;
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

    public String sucheAuftragposition() {
        /*
         check the attributes first
         */
        System.out.println("currentAuftragposition.toString() = " + currentAuftragposition.toString());

        suchkriterienAuftragposition.setAuftragpositionId(currentAuftragposition.getAuftragpositionId());
        suchkriterienAuftragposition.setAuftragpositionNr(currentAuftragposition.getAuftragpositionNr());
        if (suchkriterienAuftragposition.toString().length() == 0) {
            System.out.println("WhereClause is empty: '" + suchkriterienAuftragposition + "'");
        } else {
            System.out.println("WhereClause: '" + suchkriterienAuftragposition + "'");
        }
        auftragpositionTrefferList = transmitterSessionBeanRemote.sucheAuftragposition(suchkriterienAuftragposition);
        auftragpositionenList.clear();
        for (AuftragpositionTreffer at : auftragpositionTrefferList) {
            Auftragposition auftragposition = details(at);
            auftragpositionenList.add(auftragposition);
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
        /*
         * reset the Lists as needed
         */
        resetLists(auftragList, auftragpositionenList, untersuchungList);
        for (Auftragtreffer at : auftragTrefferList) {
            Auftrag auftrag = details(at);
            auftragList.add(auftrag);
        }

        return null;
    }

    private Untersuchung details(Untersuchungtreffer ut) {
        return transmitterSessionBeanRemote.findById(Untersuchung.class, ut.getUntersuchungId());
    }

    private Auftragposition details(AuftragpositionTreffer at) {
        return transmitterSessionBeanRemote.findById(Auftragposition.class, at.getAuftragpositionId());
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

    public String selectAuftragposition(Auftragposition auftragposition) {
        auftragposition.setSelected(true);
        currentAuftragposition = auftragposition;
        currentUntersuchung = auftragposition.getUntersuchungId();
        untersuchungList.add(currentUntersuchung);

        return null;
    }

    public String deSelectAuftragposition(Auftragposition auftragposition) {
        resetCurrents(null, currentAuftragposition, null);
        boolean remove = untersuchungList.remove(auftragposition.getUntersuchungId());
        auftragposition.setSelected(!remove);

        return null;
    }

    public String selectAuftrag(Auftrag auftrag) {
        /* 
         * System.out.println(auftrag.getHalterId())
         * gives access to a full named Halter, but "selected" attribute is not set to true, 
         * it is transient and need to be set true to have it rendered in the 
         * <h:form id = "showOrSelectSummeryForm" > of controlAuftrag.xhtml
         */

        /*
         * We want to set the Auftrag - corresponding Halter as selected
         */
//        System.out.println("auftrag.getHalterId() = " + auftrag.getHalterId());
        auftrag.getHalterId().setSelected(true);
        // check that
//        System.out.println("auftrag.getHalterId() = " + auftrag.getHalterId());
        /*
         * We want to set the Auftrag as selected
         */
//        System.out.println("auftrag = " + auftrag);
        auftrag.setSelected(true);
        // check that
//        System.out.println("auftrag = " + auftrag);

        /*
         * Also we need to set the currentHalter in halterController-Bean
         */
        halterController.setCurrentHalter(auftrag.getHalterId());
        currentAuftrag = auftrag;
//        System.out.println("currentAuftrag = " + currentAuftrag);
        /*
         * the currentAuftrag has a collection of auftragpositionen, we are interested in
         */
        auftragpositionenList.addAll(currentAuftrag.getAuftragpositionCollection());
//        System.out.println("auftragpositionenList = " + auftragpositionenList);
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

        /*
         * reset the currentEntities as needed
         */
        resetCurrents(currentAuftrag, currentAuftragposition, currentUntersuchung);
        /*
         * clear the auftragpositionenList
         */
        resetLists(null, auftragpositionenList, untersuchungList);

        return null;
    }

    public String saveAuftrag() {
        /*
         * log currentAuftrag as far as ist exists 
         */
        /* AuftragTyp is set to currentAuftrag already by using JSF 
         * value="#{auftragController.currentAuftrag.auftragstypId.auftragtypId}
         * set the Halter in currentAuftrag from halterController.getCurrentHalter()
         */

        currentAuftrag.setHalterId(halterController.getCurrentHalter());
        System.out.println("currentAuftrag= " + currentAuftrag);
        /*
         * Now check the content if the auftragpositionenList
         */
        for (Auftragposition auftragposition : auftragpositionenList) {
            System.out.println("auftragposition = " + auftragposition);
        }
        /*
         * set the auftragpositionencollection in currentAuftrag
         */
        currentAuftrag.setAuftragpositionCollection(auftragpositionenList);
        System.out.println("currentAuftrag FINAL = " + currentAuftrag);
        AuftragAuftragPositionenWrapper wrapper = new AuftragAuftragPositionenWrapper(currentAuftrag, auftragpositionenList);
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

    public void resetLists(List<Auftrag> auftragList, List<Auftragposition> auftragpositionenList, List<Untersuchung> untersuchungList) {
        if (auftragList != null) {
            this.auftragList.clear();
        }
        if (auftragpositionenList != null) {
            this.auftragpositionenList.clear();
        }
        if (untersuchungList != null) {
            this.untersuchungList.clear();
        }
    }

    public void resetCurrents(Auftrag currentAuftrag, Auftragposition currentAuftragposition, Untersuchung currentUntersuchung) {
        if (currentAuftrag != null) {
            this.currentAuftrag = new Auftrag();
            this.currentAuftrag.setAuftragstypId(new Auftragtyp());
            this.currentAuftrag.setHalterId(new Halter());
        }
        if (currentAuftragposition != null) {
            this.currentAuftragposition = new Auftragposition();

        }
        if (currentUntersuchung != null) {
            this.currentUntersuchung = new Untersuchung();

        }
    }

    public String deleteAuftrag(Integer auftragId) {
        System.out.println("Delete Auftrag with Id " + auftragId);
        Auftrag deleteById = transmitterSessionBeanRemote.deleteById(Auftrag.class, auftragId);
        if (deleteById
                != null) {
            System.out.println("Auftrag Details deleted from database: " + deleteById);
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
        if (auftragList.remove(deleteById)) {
            System.out.println("Auftrag Details  deleted from auftragList: " + deleteById);

        }

        return null;
    }

    public String deleteAuftragposition(Auftragposition auftragposition) {
        System.out.println("Delete Auftragposition with Id " + auftragposition);
        /*
         * We should NOT delete the entity MANUALLY because it is a member of a collection 
         * of another entity, BUT we need to get the auftragposition reference 
         */
//        Auftragposition deleteById = transmitterSessionBeanRemote.deleteById(Auftragposition.class, auftragpositionId);
//        if (deleteById
//                != null) {
//            System.out.println("Auftragposition Details deleted from database: " + deleteById);
//        }
        System.out.println("SIZE of List BEFORE DELETION = " + auftragpositionenList.size());
        if (auftragpositionenList.remove(auftragposition)) {
            System.out.println("Auftragposition Details  deleted from auftragpositionenList: " + auftragposition);
        }
        System.out.println("SIZE of List AFTER DELETION = " + auftragpositionenList.size());
        /*
         * we have sucessfully deleted the auftragposition, but we have to delete it from the 
         * auftragpositioncollection in the auftrag entity AND persist it
         */
        /*
         * show the size of the list of the currentauftrag
         */
        System.out.println("SIZE of List in currentauftrag BEFORE SETTING = " + currentAuftrag.getAuftragpositionCollection().size());
        currentAuftrag.setAuftragpositionCollection(auftragpositionenList);
        System.out.println("SIZE of List in currentauftrag AFTER SETTING = " + currentAuftrag.getAuftragpositionCollection().size());
        AuftragAuftragPositionenWrapper wrapper = new AuftragAuftragPositionenWrapper(currentAuftrag, auftragpositionenList);
        transmitterSessionBeanRemote.storeEjb(wrapper);

        return null;
    }

    public void editAuftrag(Auftrag auftrag) {
        auftrag.setEdited(true);
    }

    public void editAuftragposition(Auftragposition auftragposition) {
        auftragposition.setEdited(true);
    }

     public void editUntersuchung(Untersuchung untersuchung) {
        untersuchung.setEdited(true);
    }
    
    
    public void saveAuftrag(Auftrag auftrag) {
        System.out.println("Auftrag : " + auftrag);
        AuftragAuftragPositionenWrapper wrapper = new AuftragAuftragPositionenWrapper(auftrag, null);
        boolean success = transmitterSessionBeanRemote.storeEjb(wrapper);
        if (success) {
            auftrag.setEdited(false);
            sucheAuftrag();
        } else {
            // send a message
        }
    }

     public void saveUntersuchung(Untersuchung untersuchung) {
        System.out.println("Untersuchung : " + untersuchung);
        UntersuchungWrapper wrapper = new UntersuchungWrapper(untersuchung);
        boolean success = transmitterSessionBeanRemote.storeEjb(wrapper);
        if (success) {
            untersuchung.setEdited(false);
            sucheUntersuchung();
        } else {
            // send a message
        }
    }
     
    public void saveAuftragposition(Auftragposition auftragposition) {
        System.out.println("Auftragposition : " + auftragposition);
        /*
         * the wrapper saves the auftragpositionenList only
         */
        AuftragAuftragPositionenWrapper wrapper = new AuftragAuftragPositionenWrapper(null, auftragpositionenList);
        boolean success = transmitterSessionBeanRemote.storeEjb(wrapper);
        if (success) {
            auftragposition.setEdited(false);
//            sucheAuftragpostion(); // not implemented so far 
        } else {
            // send a message
        }
    }

}
