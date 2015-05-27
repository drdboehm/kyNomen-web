/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kynomics.control;

import com.kynomics.daten.Halter;
import com.kynomics.daten.Milestone;
import com.kynomics.daten.Milestonetyp;
import com.kynomics.daten.Untersuchungstyp;
import com.kynomics.daten.wrapper.UTypMileStoneWrapper;
import com.kynomics.lib.TransmitterSessionBeanRemote;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;

/**
 *
 * @author dboehm
 */
@Named(value = "uTypController")
@SessionScoped
public class UTypController implements Serializable {

    private Untersuchungstyp currentUTyp;
    private List<Untersuchungstyp> utypList;
    private Milestone currentMilestone;
    private List<Milestone> milestoneList;
    private Boolean sortOrderDesc = false;

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
        currentMilestone = new Milestone();
        milestoneList = new ArrayList<>();

        this.alleMilestoneTypenMap = new HashMap();
    }

    @PostConstruct
    public void init() {
        utypList = transmitterSessionBeanRemote.initializeUntersuchungstypen();
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

    public List<Untersuchungstyp> getUtypList() {
        return utypList;
    }

    public void setUtypList(List<Untersuchungstyp> utypList) {
        this.utypList = utypList;
    }

    public Boolean getSortOrderDesc() {
        return sortOrderDesc;
    }

    public void setSortOrderDesc(Boolean sortOrderDesc) {
        this.sortOrderDesc = sortOrderDesc;
    }

    public String saveUTyp() {
        System.out.println("currentUTyp= " + currentUTyp);
        UTypMileStoneWrapper wrapper = new UTypMileStoneWrapper(currentUTyp, null);
        transmitterSessionBeanRemote.storeEjb(wrapper);
        return null;
    }

    public String resetLists() {
        currentMilestone = new Milestone();
        currentUTyp = new Untersuchungstyp();
        milestoneList.clear();
        utypList.clear();
        return null;
    }

     public void editEntity(Untersuchungstyp uTyp) {
        uTyp.setEdited(true);
    }
    public String sortUTypById() {
        Collections.sort(utypList, new Comparator<Untersuchungstyp>() {
            @Override
            public int compare(Untersuchungstyp o1, Untersuchungstyp o2) {
                Integer diff = (o1.getUntersuchungtypId() - o2.getUntersuchungtypId());
                diff = sortOrderDesc ? (diff) : (-diff);
                return diff;
            }
        });
        sortOrderDesc = !sortOrderDesc;
        return null;
    }

    public String sortUTypByName() {
        Collections.sort(utypList, new Comparator<Untersuchungstyp>() {
            @Override
            public int compare(Untersuchungstyp o1, Untersuchungstyp o2) {
                Integer diff = (o1.getUntersuchungtypName().compareTo(o2.getUntersuchungtypName()));
                diff = sortOrderDesc ? (diff) : (-diff);
                return diff;
            }
        });
        sortOrderDesc = !sortOrderDesc;
        return null;
    }

    public String sortUTypByGen() {
        Collections.sort(utypList, new Comparator<Untersuchungstyp>() {
            @Override
            public int compare(Untersuchungstyp o1, Untersuchungstyp o2) {
                Integer diff = (o1.getUntersuchungtypGen().compareTo(o2.getUntersuchungtypGen()));
                diff = sortOrderDesc ? (diff) : (-diff);
                return diff;
            }
        });
        sortOrderDesc = !sortOrderDesc;
        return null;
    }
    public String sortUTypByMut() {
        Collections.sort(utypList, new Comparator<Untersuchungstyp>() {
            @Override
            public int compare(Untersuchungstyp o1, Untersuchungstyp o2) {
                Integer diff = (o1.getUntersuchungtypMut().compareTo(o2.getUntersuchungtypMut()));
                diff = sortOrderDesc ? (diff) : (-diff);
                return diff;
            }
        });
        sortOrderDesc = !sortOrderDesc;
        return null;
    }
}