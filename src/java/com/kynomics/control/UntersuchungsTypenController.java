/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kynomics.control;

import com.kynomics.daten.Untersuchung;
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
@Named(value = "untersuchungsTypenController")
@SessionScoped
public class UntersuchungsTypenController implements Serializable {

    private Untersuchungstyp currentUntersuchungstyp;
    @EJB
    private TransmitterSessionBeanRemote transmitterSessionBeanRemote;

    /* 
     the UntersuchungenTypen - Map for the  <h:selectOneMenu ... 
     */
    private final Map<String, Integer> alleUntersuchungsTypenMap = new HashMap();

    /**
     * Creates a new instance of UntersuchungsController
     */
    public UntersuchungsTypenController() {
    }

//
//    public Map<String, Integer> getAlleUntersuchungsTypenMap() {
//        return alleUntersuchungsTypenMap;
//    }

    public Untersuchungstyp getCurrentUntersuchungstyp() {
        return currentUntersuchungstyp;
    }

    public void setCurrentUntersuchungstyp(Untersuchungstyp currentUntersuchungstyp) {
        this.currentUntersuchungstyp = currentUntersuchungstyp;
    }

    public Map<String, Integer> getAlleUntersuchungsTypenMap() {
        List<Untersuchungstyp> tempList = transmitterSessionBeanRemote.initializeUntersuchungstypen();
        for (Untersuchungstyp next : tempList) {
            this.alleUntersuchungsTypenMap.put(next.getUntersuchungtypName(), next.getUntersuchungtypId());
        }
        return alleUntersuchungsTypenMap;
    }

}
