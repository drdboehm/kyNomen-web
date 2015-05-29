/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kynomics.control;

import com.kynomics.daten.Milestone;
import com.kynomics.daten.Milestonetyp;
import com.kynomics.daten.Untersuchungstyp;
import com.kynomics.daten.finder.MilestoneTreffer;
import com.kynomics.daten.finder.SuchkriterienMilestone;
import com.kynomics.daten.finder.SuchkriterienUTyp;
import com.kynomics.daten.finder.UTypTreffer;
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
    private Milestonetyp milestonetyp;
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
        milestonetyp = new Milestonetyp();
        currentMilestone.setMilestonetypId(milestonetyp);

        this.alleMilestoneTypenMap = new HashMap();
    }

    @PostConstruct
    public void init() {
//        utypList = transmitterSessionBeanRemote.initializeUntersuchungstypen();
        utypList = new ArrayList<>();
//        milestoneList = transmitterSessionBeanRemote.initializeAllMilestones();
        milestoneList = new ArrayList<>();
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

    public Milestonetyp getMilestonetyp() {
        return milestonetyp;
    }

    public void setMilestonetyp(Milestonetyp milestonetyp) {
        this.milestonetyp = milestonetyp;
    }

    public String saveUTyp() {
        System.out.println("currentUTyp= " + currentUTyp);
        UTypMileStoneWrapper wrapper = new UTypMileStoneWrapper(currentUTyp, null);
        boolean storeEjb = transmitterSessionBeanRemote.storeEjb(wrapper);
        if (storeEjb) {
            init();
        }
        return null;
    }

    public String sucheUTyp() {
        /*
         check the attributes first
         */
        System.out.println("currentUTyp.toString() = " + currentUTyp.toString());
        SuchkriterienUTyp suchKr = new SuchkriterienUTyp();
        suchKr.setUntersuchungtypId(currentUTyp.getUntersuchungtypId());
        suchKr.setUntersuchungtypName(currentUTyp.getUntersuchungtypName());
        suchKr.setUntersuchungtypGen(currentUTyp.getUntersuchungtypGen());
        suchKr.setUntersuchungtypMut(currentUTyp.getUntersuchungtypMut());
        if (suchKr.toString().length() == 0) {
            System.out.println("WhereClause is empty: '" + suchKr + "'");
        } else {
            System.out.println("WhereClause: '" + suchKr + "'");
        }
        List<UTypTreffer> uTypTrefferList = transmitterSessionBeanRemote.sucheUntersuchungstyp(suchKr);
        resetLists();
        for (UTypTreffer utt : uTypTrefferList) {
            Untersuchungstyp ut = details(utt);
            utypList.add(ut);
        }

        return null;
    }

    public String saveMS() {
        System.out.println("currentMilestone = " + currentMilestone);
        System.out.println("milestonetyp = " + milestonetyp);
        /*
         we get the milestonetypId set in the milestontyp object only as an Integer, 
         so we need to get the details from that object, either by extracting from the  
         List<Milestonetyp> tempList in public Map<String, Integer> getAlleMilestoneTypenMap()
         Method OR by  public <T extends Object> T findById(Class<T> entityClass, Integer primaryKey) 
         implementation from interface TransmitterSessionBeanRemote and set it to currentMilestone
         */
        currentMilestone.setMilestonetypId(transmitterSessionBeanRemote.findById(Milestonetyp.class, milestonetyp.getMilestonetypId()));
        System.out.println("niw currentMilestone is set properly = " + currentMilestone);
        UTypMileStoneWrapper wrapper = new UTypMileStoneWrapper(null, currentMilestone);
        if (transmitterSessionBeanRemote.storeEjb(wrapper)) {
//            init();
        }
        return null;
    }

    public String sucheMS() {
        /*
         check the attributes first
         */
        /*
         we get the milestonetypId set in the milestontyp object only as an Integer, 
         so we need to get the details from that object, either by extracting from the  
         List<Milestonetyp> tempList in public Map<String, Integer> getAlleMilestoneTypenMap()
         Method OR by  public <T extends Object> T findById(Class<T> entityClass, Integer primaryKey) 
         implementation from interface TransmitterSessionBeanRemote and set it to currentMilestone
         */
//        currentMilestone.setMilestonetypId(transmitterSessionBeanRemote.findById(Milestonetyp.class, milestonetyp.getMilestonetypId()));
        System.out.println("currentMilestone.toString() = " + currentMilestone.toString());
        SuchkriterienMilestone suchKr = new SuchkriterienMilestone();
        suchKr.setMilestoneId(currentMilestone.getMilestoneId());
        suchKr.setMilestoneName(currentMilestone.getMilestoneName());
//        suchKr.setMilestonetypId(currentMilestone.getMilestonetypId());

        if (suchKr.toString().length() == 0) {
            System.out.println("WhereClause is empty: '" + suchKr + "'");
        } else {
            System.out.println("WhereClause: '" + suchKr + "'");
        }
        List<MilestoneTreffer> milestoneTrefferList = transmitterSessionBeanRemote.sucheMilestone(suchKr);

        resetLists();
        for (MilestoneTreffer mst : milestoneTrefferList) {
            Milestone ms = details(mst);
            milestoneList.add(ms);
        }
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

    public void saveEntity(Untersuchungstyp object) {
        UTypMileStoneWrapper wrapper = new UTypMileStoneWrapper(object, null);
        boolean success = transmitterSessionBeanRemote.storeEjb(wrapper);
        if (success) {
            object.setEdited(false);
        } else {
// send a message
        }
    }

    public String deleteEntity(Integer uTypId) {
        System.out.println("Delete entity  with Id " + uTypId);
        Untersuchungstyp deleteById = transmitterSessionBeanRemote.deleteById(Untersuchungstyp.class, uTypId);
        if (deleteById != null) {
            System.out.println("Entity Details deleted from database: " + deleteById);
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
        if (utypList.remove(deleteById)) {
            System.out.println("Entity Details deleted from list: " + deleteById);

        }
        return null;
    }

    private Untersuchungstyp details(UTypTreffer utt) {
        return transmitterSessionBeanRemote.findById(Untersuchungstyp.class, utt.getUntersuchungtypId());
    }

    private Milestone details(MilestoneTreffer mst) {
        return transmitterSessionBeanRemote.findById(Milestone.class, mst.getMilestoneId());
    }
}
