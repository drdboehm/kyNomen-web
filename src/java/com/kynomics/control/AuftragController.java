/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kynomics.control;

import com.kynomics.daten.Auftrag;
import com.kynomics.daten.Auftragposition;
import com.kynomics.daten.Halter;
import com.kynomics.daten.Patient;
import com.kynomics.daten.Untersuchungstyp;
import com.kynomics.lib.TransmitterSessionBeanRemote;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;

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

    private Halter currentHalter;
    private Patient currentPatient;
    private Auftrag currentAuftrag;
    private List<Auftrag> auftragList;
    private List<Auftragposition> auftragpositionenList;

    private final Map<String, Integer> alleUntersuchungsTypenMap;
    

    /**
     * Creates a new instance of AuftragController
     */
    public AuftragController() {
        this.alleUntersuchungsTypenMap = new HashMap();
    }

    public Halter getCurrentHalter() {
        return currentHalter;
    }

    public void setCurrentHalter(Halter currentHalter) {
        this.currentHalter = currentHalter;
    }

    public Patient getCurrentPatient() {
        return currentPatient;
    }

    public void setCurrentPatient(Patient currentPatient) {
        this.currentPatient = currentPatient;
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

     public Map<String, Integer> getAlleUntersuchungsTypenMap() {
        List<Untersuchungstyp> tempList = transmitterSessionBeanRemote.initializeUntersuchungstypen();
        for (Untersuchungstyp next : tempList) {
            this.alleUntersuchungsTypenMap.put(next.getUntersuchungtypName(), next.getUntersuchungtypId());
        }
        return alleUntersuchungsTypenMap;
    }
}
