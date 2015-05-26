/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kynomics.control;

import com.kynomics.daten.Milestone;
import com.kynomics.daten.Milestonetyp;
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
@Named(value = "uTypController")
@SessionScoped
public class UTypController implements Serializable {

    private Untersuchungstyp currentUTyp;
    private List<Milestone> milestoneList;
    private Milestone currentMilestone;

    @EJB
    private TransmitterSessionBeanRemote transmitterSessionBeanRemote;
    
    /* 
     the MilestoneTypen - Map for the  <h:selectOneMenu ... 
     */
    private Map<String, Integer> alleMilestoneTypenMap;

    /**
     * Creates a new instance of UntersuchungsController
     */
    public UTypController() {
        currentUTyp = new Untersuchungstyp();
        this.alleMilestoneTypenMap = new HashMap();
    }

    public Map<String, Integer> getAlleMilestoneTypenMap() {
        List<Milestonetyp> tempList = transmitterSessionBeanRemote.initializeMilestoneTypen();
        for (Milestonetyp mst : tempList) {
            alleMilestoneTypenMap.put(mst.getMilestonetypName(), mst.getMilestonetypId());
        }
        return alleMilestoneTypenMap;
    }

    public void setAlleMilestoneTypenMap(Map<String, Integer> alleMilestoneTypenMap) {

        this.alleMilestoneTypenMap = alleMilestoneTypenMap;
    }

    public Untersuchungstyp getCurrentUTyp() {
        return currentUTyp;
    }

    public void setCurrentUTyp(Untersuchungstyp currentUTyp) {
        this.currentUTyp = currentUTyp;
    }

    public List<Milestone> getMilestoneList() {
        return milestoneList;
    }

    public void setMilestoneList(List<Milestone> milestoneList) {
        this.milestoneList = milestoneList;
    }

    public Milestone getCurrentMilestone() {
        return currentMilestone;
    }

    public void setCurrentMilestone(Milestone currentMilestone) {
        this.currentMilestone = currentMilestone;
    }

    
    public String saveUTyp() {
        System.out.println("currentUTyp= " + currentUTyp);        
//        UTypMileStoneWrapper wrapper = new UTypMileStoneWrapper(currentUTyp, null);
//        transmitterSessionBeanRemote.storeEjb(wrapper);
        return "controlUTyp";
    }
}
