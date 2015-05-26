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
import com.kynomics.lib.TransmitterSessionBeanRemote;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.List;
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

    private Halter halter;
    private Patient patient;
    private Auftrag auftrag;
    private List<Auftragposition> auftragpositionenList;
    
    
    

    /**
     * Creates a new instance of AuftragController
     */
    public AuftragController() {
    }

    public Halter getHalter() {
        return halter;
    }

    public void setHalter(Halter halter) {
        this.halter = halter;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public Auftrag getAuftrag() {
        return auftrag;
    }

    public void setAuftrag(Auftrag auftrag) {
        this.auftrag = auftrag;
    }

    public List<Auftragposition> getAuftragpositionenList() {
        return auftragpositionenList;
    }

    public void setAuftragpositionenList(List<Auftragposition> auftragpositionenList) {
        this.auftragpositionenList = auftragpositionenList;
    }

}
