/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kynomics.control;

import com.kynomics.daten.Untersuchung;
import com.kynomics.daten.Untersuchungstyp;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author dboehm
 */
@Named(value = "untersuchungenController")
@SessionScoped
public class UntersuchungenController implements Serializable {
    
    private Untersuchung currentUntersuchung;
    private List<Untersuchung> untersuchungenList;
    private Untersuchungstyp currentUntersuchungstyp;

    
    /* 
     the UntersuchungenTypen - Map for the  <h:selectOneMenu ... 
     */
    private final Map<String, Integer> alleUntersuchungsTypenMap = new HashMap();
    
    /**
     * Creates a new instance of UntersuchungsController
     */
    public UntersuchungenController() {
    }

    public Untersuchung getCurrentUntersuchung() {
        return currentUntersuchung;
    }

    public void setCurrentUntersuchung(Untersuchung currentUntersuchung) {
        this.currentUntersuchung = currentUntersuchung;
    }

    public List<Untersuchung> getUntersuchungenList() {
        return untersuchungenList;
    }

    public void setUntersuchungenList(List<Untersuchung> untersuchungenList) {
        this.untersuchungenList = untersuchungenList;
    }

    public Map<String, Integer> getAlleUntersuchungsTypenMap() {
        return alleUntersuchungsTypenMap;
    }

    public Untersuchungstyp getCurrentUntersuchungstyp() {
        return currentUntersuchungstyp;
    }

    public void setCurrentUntersuchungstyp(Untersuchungstyp currentUntersuchungstyp) {
        this.currentUntersuchungstyp = currentUntersuchungstyp;
    }

    
   

  
    
}
