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
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
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
    private List<Milestone> milestoneOrderedSOPList;
    private Map<Integer, Milestone> milestoneOrderedSOPMap;

    /**
     * the Suchkriterien are set private properties, which can be used to reload
     * each list after persisting changes in order to keep the list updated all
     * the time.
     */
    private SuchkriterienUTyp suchkriterienUTyp = new SuchkriterienUTyp();
    private SuchkriterienMilestone suchKrMilestone = new SuchkriterienMilestone();

    /**
     * ALTERNATIVELY the TrefferLists can set as private properties, and can be
     * used to reload each list to keep the list updated all the time.
     */
    private List<MilestoneTreffer> milestoneTrefferList = null;
    private List<UTypTreffer> uTypTrefferList = null;

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
        milestoneOrderedSOPList = new LinkedList<>();
        milestoneOrderedSOPMap = new TreeMap<>();

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

    public List<Milestone> getMilestoneOrderedSOPList() {
        return milestoneOrderedSOPList;
    }

    public void setMilestoneOrderedSOPList(List<Milestone> milestoneOrderedSOPList) {
        this.milestoneOrderedSOPList = milestoneOrderedSOPList;
    }

    public Map<Integer, Milestone> getMilestoneOrderedSOPMap() {
        return milestoneOrderedSOPMap;
    }

    public void setMilestoneOrderedSOPMap(Map<Integer, Milestone> milestoneOrderedSOPMap) {
        this.milestoneOrderedSOPMap = milestoneOrderedSOPMap;
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
        suchkriterienUTyp.setUntersuchungtypId(currentUTyp.getUntersuchungtypId());
        suchkriterienUTyp.setUntersuchungtypName(currentUTyp.getUntersuchungtypName());
        suchkriterienUTyp.setUntersuchungtypGen(currentUTyp.getUntersuchungtypGen());
        suchkriterienUTyp.setUntersuchungtypMut(currentUTyp.getUntersuchungtypMut());
        if (suchkriterienUTyp.toString().length() == 0) {
            System.out.println("WhereClause is empty: '" + suchkriterienUTyp + "'");
        } else {
            System.out.println("WhereClause: '" + suchkriterienUTyp + "'");
        }
        uTypTrefferList = transmitterSessionBeanRemote.sucheUntersuchungstyp(suchkriterienUTyp);
        utypList.clear();
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
        suchKrMilestone.setMilestoneId(currentMilestone.getMilestoneId());
        suchKrMilestone.setMilestoneName(currentMilestone.getMilestoneName());
//        suchKr.setMilestonetypId(currentMilestone.getMilestonetypId());

        if (suchKrMilestone.toString().length() == 0) {
            System.out.println("WhereClause is empty: '" + suchKrMilestone + "'");
        } else {
            System.out.println("WhereClause: '" + suchKrMilestone + "'");
        }
        milestoneTrefferList = transmitterSessionBeanRemote.sucheMilestone(suchKrMilestone);
        milestoneList.clear();
        for (MilestoneTreffer mst : milestoneTrefferList) {
            Milestone ms = details(mst);
            milestoneList.add(ms);
        }
        return null;
    }

//    public String resetLists() {
//        currentMilestone = new Milestone();
//        currentUTyp = new Untersuchungstyp();
//        milestoneList.clear();
//        utypList.clear();
//        return null;
//    }
    public void editUTyp(Untersuchungstyp uTyp) {
        uTyp.setEdited(true);
    }

    public void editMS(Milestone ms) {
        ms.setEdited(true);
    }

    public String saveMS(Milestone ms) {
        System.out.println("The milestone: " + ms);
        Milestonetyp findById = transmitterSessionBeanRemote.findById(Milestonetyp.class, ms.getMilestonetypId().getMilestonetypId());
        ms.setMilestonetypId(findById);
        System.out.println("The milestone: " + ms);
        UTypMileStoneWrapper wrapper = new UTypMileStoneWrapper(null, ms);
        boolean success = transmitterSessionBeanRemote.storeEjb(wrapper);
        if (success) {
            ms.setEdited(false);
        } else {
            // send a message
        }
        return null;
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

    public void saveUTyp(Untersuchungstyp object) {
        UTypMileStoneWrapper wrapper = new UTypMileStoneWrapper(object, null);
        boolean success = transmitterSessionBeanRemote.storeEjb(wrapper);
        if (success) {
            object.setEdited(false);
        } else {
// send a message
        }
    }

    public String deleteUTyp(Integer uTypId) {
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

    public String deleteMS(Integer msId) {
        System.out.println("Delete Milestone with Id " + msId);
        Milestone deleteById = transmitterSessionBeanRemote.deleteById(Milestone.class, msId);
        if (deleteById != null) {
//            System.out.println("Entity Details deleted from database: " + deleteById);
        }
        if (milestoneList.remove(deleteById)) {
//            System.out.println("Entity Details deleted from list: " + deleteById);

        }
        return null;
    }

    public String selectUTyp(Untersuchungstyp utyp) {
        utyp.setSelected(true);
        System.out.println("selected UTyp : " + utyp);
        currentUTyp = utyp;
        return null;
    }

    public String selectMSOrdered(Milestone ms) {
        ms.setSelected(true);
        milestoneOrderedSOPList.add(ms);
        milestoneOrderedSOPMap.put(milestoneOrderedSOPList.size(), ms);
        return null;
    }

    public String deSelectMSOrdered(Milestone ms) {
        removeMSFromOrderedSOPList(ms);
        ms.setSelected(false);
        return null;
    }

    public String removeMSFromOrderedSOPList(Milestone ms) {
        int indexOf = milestoneOrderedSOPList.indexOf(ms);
        milestoneOrderedSOPList.remove(indexOf);
        // we need to set the ms in the milestoneList to selected=false
        indexOf = milestoneList.indexOf(ms);
        milestoneList.get(indexOf).setSelected(false);
        return null;
    }

    public String clearSOPList() {
        milestoneOrderedSOPList.clear();
        milestoneOrderedSOPMap.clear();
        //deselect selected Milestones in milestoneList
        for (Milestone ms : milestoneList) {
            ms.setSelected(false);
        }
        return null;
    }

    public String deSelectUTyp(Untersuchungstyp utyp) {
        utyp.setSelected(false);
        /* 
         * empty the and utypList as 
         * well reset the currentUTyp 
         */
        utypList.clear();
        currentUTyp = new Untersuchungstyp();
        /*
         * refill the uTypTrefferList to previous search
         */
        for (UTypTreffer utt : uTypTrefferList) {
            Untersuchungstyp ut = details(utt);
            utypList.add(ut);
        }
        return null;
    }

    public String sumMsAllTimes() {
        // take the milestoneOrderedSOPList and summarize the fields
        String s = "";
        Long l = 0l;
        for (Milestone ms : milestoneOrderedSOPList) {
            Date milestoneAlltime = ms.getMilestoneAlltime();
            l += (milestoneAlltime.getHours()* 60 + milestoneAlltime.getMinutes());
        }
        return s = String.valueOf(l); 
    }
    
    public String sumMsHandsonTimes() {
        // take the milestoneOrderedSOPList and summarize the fields
        String s = "";
        Long l = 0l;
        for (Milestone ms : milestoneOrderedSOPList) {
            Date milestoneHandsontime = ms.getMilestoneHandsontime();
            l += (milestoneHandsontime.getHours()* 60 + milestoneHandsontime.getMinutes());
        }
        return s = String.valueOf(l); 
    }
    
    public String  sumMsManHour() {
        // take the milestoneOrderedSOPList and summarize the fields
        String s = "";
        Long l = 0l;
        for (Milestone ms : milestoneOrderedSOPList) {
            Date milestoneHandsontime = ms.getMilestoneHandsontime();
            l += (milestoneHandsontime.getHours()* 60 + milestoneHandsontime.getMinutes()) * ms.getMilestoneMannumber();
        }
        return s = String.valueOf(l); 
    }
   
    public String  sumMsCost() {
        // take the milestoneOrderedSOPList and summarize the fields
        Double d = 0.0;
        for (Milestone ms : milestoneOrderedSOPList) {
            String milestonecost = ms.getMilestonecost();
            d += Double.parseDouble(milestonecost);
        }
        return String.format("%.2f €", d); 
    }
    
    
    private Untersuchungstyp details(UTypTreffer utt) {
        return transmitterSessionBeanRemote.findById(Untersuchungstyp.class, utt.getUntersuchungtypId());
    }

    private Milestone details(MilestoneTreffer mst) {
        return transmitterSessionBeanRemote.findById(Milestone.class, mst.getMilestoneId());
    }

}
