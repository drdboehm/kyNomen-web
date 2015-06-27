/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kynomics.control;

import com.kynomics.daten.Auftrag;
import com.kynomics.daten.Auftragposition;
import com.kynomics.daten.Auftragtyp;
import com.kynomics.daten.Untersuchung;
import com.kynomics.daten.Untersuchungstyp;
import com.kynomics.daten.finder.SuchkriterienUntersuchung;
import com.kynomics.daten.finder.Untersuchungtreffer;
import com.kynomics.lib.TransmitterSessionBeanRemote;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.naming.Context;
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
    
//    @Resource
//    @EJB
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
     * the SuchkriterienUTyp suchkriterienUTyp is set to a private property,
     * which can be used to reload each List utypList after persisting changes
     * in order to keep the list updated all the time.
     */
    private SuchkriterienUntersuchung suchkriterienUntersuchung
            = new SuchkriterienUntersuchung();

    /**
     * The List milestoneTrefferList is set as private property, and can be used
     * to reload each List milestoneList after persisting changes in order to
     * keep the list updated all the time.
     */
    private List<Untersuchungtreffer> untersuchungTrefferList = null;

    /**
     * Creates a new instance of AuftragController
     */
    public AuftragController() throws NamingException {
        this.alleUntersuchungsTypenMap = new HashMap();
        this.alleAuftragTypenMap = new HashMap<>();
        this.auftragTyp = new Auftragtyp();
        this.currentUntersuchung = new Untersuchung();
        this.currentAuftrag = new Auftrag();
        this.untersuchungList = new ArrayList<>();
        this.auftragList = new ArrayList();
        this.auftragTypList = new ArrayList<>();
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

    private Untersuchung details(Untersuchungtreffer ut) {
        return transmitterSessionBeanRemote.findById(Untersuchung.class, ut.getUntersuchungId());
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

    public String deSelectUntersuchung(Untersuchung untersuchung) {
        int indexOf = untersuchungList.indexOf(untersuchung);
        untersuchung.setSelected(false);
        currentUntersuchung = new Untersuchung();
        System.out.println("deselected Untersuchung : " + untersuchung);
        untersuchungList.remove(indexOf);
        untersuchungList.add(indexOf, untersuchung);
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
//        currentAuftrag.setHalterId(how to get the currentHalter here ??);
//        try {
//            Context ctx = new InitialContext();
//            this.halterController = (HalterController) ctx.lookup("java:comp/env/halterController");
//        } catch (NamingException ex) {
//            Logger.getLogger(AuftragController.class.getName()).log(Level.SEVERE, null, ex);
//        }

        System.out.println("halterController.getCurrentHalter()= " + halterController.getCurrentHalter());
        System.out.println("currentAuftrag= " + currentAuftrag);

        return null;
    }

}
