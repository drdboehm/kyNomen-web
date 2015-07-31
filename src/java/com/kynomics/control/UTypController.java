/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kynomics.control;

import com.kynomics.daten.Milestone;
import com.kynomics.daten.Milestonetyp;
import com.kynomics.daten.Untersuchung;
import com.kynomics.daten.Untersuchungstyp;
import com.kynomics.daten.UntersuchungstypMilestone;
import com.kynomics.daten.UntersuchungstypMilestonePK;
import com.kynomics.daten.finder.MilestoneTreffer;
import com.kynomics.daten.finder.SuchkriterienMilestone;
import com.kynomics.daten.finder.SuchkriterienUTyp;
import com.kynomics.daten.finder.UTypTreffer;
import com.kynomics.daten.wrapper.UTypMileStoneWrapper;
import com.kynomics.daten.wrapper.UntersuchungWrapper;
import com.kynomics.lib.TransmitterSessionBeanRemote;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.html.HtmlInputText;
import javax.faces.component.html.HtmlSelectOneMenu;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;

/**
 * UTypController is a Controller class following the MVC pattern. The
 * UTypController class is performing updates an rendering of the corresponding
 * view.
 *
 *
 * @author dboehm
 * @version 1.0 June 20, 2015
 */
@Named(value = "uTypController")
@SessionScoped
public class UTypController implements Serializable {

    /**
     * Untersuchungstyp currentUTyp is always set to the actual selected or
     * edited entity Untersuchungstyp which can be changed either in the
     * insert-Form or selected and set from a corresponding List utypList
     */
    private Untersuchungstyp currentUTyp;

    /**
     * List utypList hold all Untersuchungstyp entities which are shown in the
     * h:datatable of the Frontend
     */
    private List<Untersuchungstyp> utypList;

    /**
     * Milestone currentMilestone is always set to the actual selected or edited
     * entity Milestone which can be changed either in the insert-Form or
     * selected and set from a corresponding List milestoneList
     */
    private Milestone currentMilestone;

    /**
     * List milestoneList hold all Milestone entities which are shown in the
     * h:datatable of the Frontend
     */
    private List<Milestone> milestoneList;

    /**
     * Untersuchung currentUntersuchung is always set to the actual selected or
     * edited entity Untersuchung which can be changed either in the insert-Form
     * or selected and set from a corresponding List untersuchungList
     */
    private Untersuchung currentUntersuchung;

    /**
     * List untersuchungList hold all Untersuchung entities which are shown in
     * the h:datatable of the Frontend
     */
    private List<Untersuchung> untersuchungList;

    /**
     * List milestoneOrderedSOPList is the List which has the natural order of
     * Insertion and which correspond to the order of the Milestones in a SOP
     * Standard-Operation-Procedure.
     */
    private List<Milestone> milestoneOrderedSOPList;

    /**
     * The Milestonetyp milestonetyp is an entity from the database and
     * indicates which Milestonetyp the Mielstone belongs to
     */
    private Milestonetyp milestonetyp;

    /**
     * an order flag which indicates, whether the order in sorting is ascending
     * or descending
     */
    private Boolean sortOrderDesc = false;

    /**
     * the SuchkriterienUTyp suchkriterienUTyp is set to a private property,
     * which can be used to reload each List utypList after persisting changes
     * in order to keep the list updated all the time.
     */
    private SuchkriterienUTyp suchkriterienUTyp = new SuchkriterienUTyp();

    /**
     * the SuchkriterienMilestone suchKrMilestone is set to a private property,
     * which can be used to reload each List milestoneList after persisting
     * changes in order to keep the list updated all the time.
     */
    private SuchkriterienMilestone suchKrMilestone = new SuchkriterienMilestone();

    /**
     * The List milestoneTrefferList is set as private property, and can be used
     * to reload each List milestoneList after persisting changes in order to
     * keep the list updated all the time.
     */
    private List<MilestoneTreffer> milestoneTrefferList = null;

    /**
     * The List uTypTrefferList is set as private property, and can be used to
     * reload each List utypList after persisting changes in order to keep the
     * list updated all the time.
     */
    private List<UTypTreffer> uTypTrefferList = null;

    /**
     * The TransmitterSessionBeanRemote transmitterSessionBeanRemote is an EJB
     * which is injected to perform the operation on the database-model which
     * can be on another host
     */
    @EJB
    private TransmitterSessionBeanRemote transmitterSessionBeanRemote;

    /**
     * the MilestoneTypen - Map for the h:selectOneMenu ...
     */
    private Map<String, Integer> alleMilestoneTypenMap;

    /**
     * The default constructor, creates an instance of UntersuchungsController
     * and initializes the attributes Untersuchungstyp currentUTyp, Milestone
     * currentMilestone, Untersuchung currentUntersuchung, Milestonetyp
     * milestonetyp, as well as List milestoneList, milestoneOrderedSOPList and
     * HashMap alleMilestoneTypenMap
     */
    public UTypController() {
        currentUTyp = new Untersuchungstyp();
        currentMilestone = new Milestone();
        currentUntersuchung = new Untersuchung();
        milestonetyp = new Milestonetyp();
        milestoneList = new ArrayList<>();
        currentMilestone.setMilestonetypId(milestonetyp);
        milestoneOrderedSOPList = new ArrayList<>();
        alleMilestoneTypenMap = new HashMap();
    }

    /**
     * initializes List of Untersuchungstyp utypList and List of Milestone
     * milestoneList following creation of the controller instance
     */
    @PostConstruct
    public void init() {
//        utypList = transmitterSessionBeanRemote.initializeUntersuchungstypen();
        utypList = new ArrayList<>();
//        milestoneList = transmitterSessionBeanRemote.initializeAllMilestones();
        milestoneList = new ArrayList<>();
    }

    /**
     * The getter method of the Map of Milestypen filled by calling
     * transmitterSessionBeanRemote.initializeMilestoneTypen();
     *
     * @return Map of Milestypen for selectOneMenu
     */
    public Map<String, Integer> getAlleMilestoneTypenMap() {
        List<Milestonetyp> tempList = transmitterSessionBeanRemote.initializeMilestoneTypen();
        for (Milestonetyp mst : tempList) {
            alleMilestoneTypenMap.put(mst.getMilestonetypName(), mst.getMilestonetypId());
        }
        return alleMilestoneTypenMap;
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

    public Untersuchung getCurrentUntersuchung() {
        return currentUntersuchung;
    }

    public void setCurrentUntersuchung(Untersuchung currentUntersuchung) {
        this.currentUntersuchung = currentUntersuchung;
    }

    public List<Untersuchung> getUntersuchungList() {
        return untersuchungList;
    }

    public void setUntersuchungList(List<Untersuchung> untersuchungList) {
        this.untersuchungList = untersuchungList;
    }

    /*
     * Own logic
     */
    public void validateUTyp(FacesContext facesContext, UIComponent uIComponent,
            Object value) throws ValidatorException {
        /**
         * FIRST Check, if you arrive from a Save-Button. relying on the Howto
         * from:
         * http://stackoverflow.com/questions/10428899/how-can-a-custom-validator-know-which-commandbutton-was-clicked
         */
        Map<String, String> params = facesContext.getExternalContext().getRequestParameterMap();
        /*
         * validate only in case of saveButton-Type
         */
        if (params.containsKey("uTypForm:saveUTypButton")) {
            /* first check, what type the uiComponent is, before casting will be done
             * there will be a better solution, but here hard code the Id of the SelectOneMenu to decide,
             whether cast shoud be done to HtmlSelectOneMenu or HtmlInputText
             */
            if (uIComponent.getClientId().contains("uTypForm:inUntersuchungsTyp")) {
                String s = (String) value;
                if (s.length() == 0) {
                    HtmlInputText htmlInputText = (HtmlInputText) uIComponent;
                    FacesMessage facesMessage = new FacesMessage(htmlInputText.getLabel() + " eingeben");
                    throw new ValidatorException(facesMessage);
                } else if (s.length() < 3) {
                    HtmlInputText htmlInputText = (HtmlInputText) uIComponent;
                    FacesMessage facesMessage = new FacesMessage(htmlInputText.getLabel() + " ist zu kurz");
                    throw new ValidatorException(facesMessage);
                }
            }
        }
    }

    public void validateMilestone(FacesContext facesContext, UIComponent uIComponent,
            Object value) throws ValidatorException {
        /**
         * FIRST Check, if you arrive from a Save-Button. relying on the Howto
         * from:
         * http://stackoverflow.com/questions/10428899/how-can-a-custom-validator-know-which-commandbutton-was-clicked
         */
        Map<String, String> params = facesContext.getExternalContext().getRequestParameterMap();
        /*
         * validate only in case of saveButton-Type
         */
        if (params.containsKey("milestoneForm:saveMSButton")) {
            /* first check, what type the uiComponent is, before casting will be done
             * there will be a better solution, but here hard code the Id of the SelectOneMenu to decide,
             whether cast shoud be done to HtmlSelectOneMenu or HtmlInputText
             */
            System.out.println("SAVE Button prssed from " + uIComponent.getClientId() + " Value = " + value);
            if (uIComponent.getClientId().equals("milestoneForm:inMilestoneTyp") & value == null) {

                HtmlSelectOneMenu htmlSelectOneMenu = (HtmlSelectOneMenu) uIComponent;
                FacesMessage facesMessage = new FacesMessage(htmlSelectOneMenu.getLabel() + " auswählen");
                throw new ValidatorException(facesMessage);
            } else if (uIComponent.getClientId().contains("milestoneForm:inMilestone")) {
                String s = (String) value;
                if (s.length() == 0) {
                    HtmlInputText htmlInputText = (HtmlInputText) uIComponent;
                    FacesMessage facesMessage = new FacesMessage(htmlInputText.getLabel() + " eingeben");
                    throw new ValidatorException(facesMessage);
                } else if (s.length() < 3) {
                    HtmlInputText htmlInputText = (HtmlInputText) uIComponent;
                    FacesMessage facesMessage = new FacesMessage(htmlInputText.getLabel() + " ist zu kurz");
                    throw new ValidatorException(facesMessage);
                }
            }
        }
    }

    public void validateUntersuchung(FacesContext facesContext, UIComponent uIComponent,
            Object value) throws ValidatorException {
        /**
         * FIRST Check, if you arrive from a Save-Button. relying on the Howto
         * from:
         * http://stackoverflow.com/questions/10428899/how-can-a-custom-validator-know-which-commandbutton-was-clicked
         */
        Map<String, String> params = facesContext.getExternalContext().getRequestParameterMap();
        /*
         * validate only in case of saveButton-Type
         */
        if (params.containsKey("uSOPForm:saveMSButton")) {
            /* first check, what type the uiComponent is, before casting will be done
             * there will be a better solution, but here hard code the Id of the SelectOneMenu to decide,
             whether cast shoud be done to HtmlSelectOneMenu or HtmlInputText
             */
            System.out.println("SAVE Button prssed from " + uIComponent.getClientId() + " Value = " + value);
            if (uIComponent.getClientId().equals("uSOPForm:inMilestoneTyp") & value == null) {

                HtmlSelectOneMenu htmlSelectOneMenu = (HtmlSelectOneMenu) uIComponent;
                FacesMessage facesMessage = new FacesMessage(htmlSelectOneMenu.getLabel() + " auswählen");
                throw new ValidatorException(facesMessage);
            } else if (uIComponent.getClientId().contains("uSOPForm:inMilestone")) {
                String s = (String) value;
                if (s.length() == 0) {
                    HtmlInputText htmlInputText = (HtmlInputText) uIComponent;
                    FacesMessage facesMessage = new FacesMessage(htmlInputText.getLabel() + " eingeben");
                    throw new ValidatorException(facesMessage);
                } else if (s.length() < 3) {
                    HtmlInputText htmlInputText = (HtmlInputText) uIComponent;
                    FacesMessage facesMessage = new FacesMessage(htmlInputText.getLabel() + " ist zu kurz");
                    throw new ValidatorException(facesMessage);
                }
            }
        }
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

    public String saveUntersuchung() {
        /*
         * sets and persists the entity Untersuchung currentUntersuchung. The FrontEnd 
         * has NOT set and changed the currentUTyp.getUntersuchungtypName() and just 
         * transfers it to the object Untersuchung currentUntersuchung which will 
         * we be persisted as it is.
         */
        currentUntersuchung.setUntersuchungName(currentUTyp.getUntersuchungtypName());
        System.out.println("currentUntersuchung.getUntersuchungName()= "
                + currentUntersuchung.getUntersuchungName());
        /*
         *  SAME is true for the currentUntersuchung.setUntersuchungstypUntersuchungtypId 
         * which will be set by using the setter and the instance  currentUTyp
         */
        currentUntersuchung.setUntersuchungstypUntersuchungtypId(currentUTyp);
        System.out.println("currentUntersuchung.getUntersuchungstypUntersuchungtypId()= "
                + currentUntersuchung.getUntersuchungstypUntersuchungtypId());
        /*
         * currentUntersuchung.getUntersuchungDauer() and 
         * currentUntersuchung.getUntersuchungPreis() are transferred from the Frontend 
         * by user settings.
         */
        System.out.println("currentUntersuchung.getUntersuchungDauer()= "
                + currentUntersuchung.getUntersuchungDauer());
        System.out.println("currentUntersuchung.getUntersuchungPreis()= "
                + currentUntersuchung.getUntersuchungPreis());

        UntersuchungWrapper wrapper = new UntersuchungWrapper(currentUntersuchung);
        boolean storeEjb = transmitterSessionBeanRemote.storeEjb(wrapper);
        if (storeEjb) {
//    init();
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

    public String saveSOPList(List<Milestone> list) {
        /* To avoid traffic, create a selectedUtypList which consists of selected
         * Untersuchungstyp only
         */
        List<Untersuchungstyp> selectedUtypList = new ArrayList<>();
        for (Untersuchungstyp ut : utypList) {
            if (ut.isSelected()) {
                selectedUtypList.add(ut);
            }
        }
        System.out.println("selectedUtypList.size()=" + selectedUtypList.size());
        System.out.println("List<Milestone> list.size()=" + list.size());
        boolean success = false;
        for (Untersuchungstyp ut : selectedUtypList) {
            UntersuchungstypMilestone utms = null;
            for (int i = 0; i < list.size(); i++) {
                UntersuchungstypMilestonePK utmsPK = new UntersuchungstypMilestonePK(ut.getUntersuchungtypId(), list.get(i).getMilestoneId());
                utms = new UntersuchungstypMilestone(utmsPK);
                utms.setMilestoneorderpos(i + 1);
                success = transmitterSessionBeanRemote.storeEjb(utms);
                if (!success) {
                    return "error.xhtml";
                }
            }
        }
        return null;
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
        currentUTyp = utyp;
        System.out.println("selected UTyp : " + utyp);
        /* now, do some more logic here
         1. need to check, whether Untersuchungstyp utyp has
         a Collection loaded already
         */
        List<UntersuchungstypMilestone> untersuchungstypMilestoneCollection
                = (List<UntersuchungstypMilestone>) utyp.getUntersuchungstypMilestoneCollection();

        /* To be sure that 
         * Collection<UntersuchungstypMilestone> untersuchungstypMilestoneCollection 
         * is sorted by utms.getMilestoneorderpos(), cast it to 
         * List<UntersuchungstypMilestone> and sort by using a custom Comparator using the 
         * 
         */
        Collections.sort(untersuchungstypMilestoneCollection, new Comparator<UntersuchungstypMilestone>() {

            @Override
            public int compare(UntersuchungstypMilestone o1, UntersuchungstypMilestone o2) {
                return o1.getMilestoneorderpos().compareTo(o2.getMilestoneorderpos());
            }
        });

        /* what is in the List and later on
         1. build the private List<Milestone> milestoneOrderedSOPList which is displayed
         in the JSF
         */
        for (UntersuchungstypMilestone utms : untersuchungstypMilestoneCollection) {
            System.out.println("utms.getMilestone()=" + utms.getMilestone()
                    + "utms.getUntersuchungstyp()=" + utms.getUntersuchungstyp()
                    + "utms.getMilestoneorderpos()=" + utms.getMilestoneorderpos());
            utms.getMilestone().setSelected(true);
            milestoneOrderedSOPList.add(utms.getMilestone());
            milestoneList.add(utms.getMilestone());
        }
        return null;
    }

    public String selectMSOrdered(Milestone ms) {
        currentMilestone = ms;
        ms.setSelected(true);
        milestoneOrderedSOPList.add(ms);
        return null;
    }

    public String deSelectMSOrdered(Milestone ms) {
        removeMSFromOrderedSOPList(ms);
        ms.setSelected(false);
        return null;
    }

    public String removeMSFromOrderedSOPList(Milestone ms) {
       /*
        * remove the ms from milestoneOrderedSOPList
        */
        milestoneOrderedSOPList.remove(ms);
        /*
         * we need to set the ms in the milestoneList to selected=false
         */
        int indexOf = milestoneList.indexOf(ms);
        System.out.println("Index in milestoneList=" + indexOf);
        milestoneList.get(indexOf).setSelected(false);
        /* create the corresponding UntersuchungstypMilestoneCollection
         * from Collection<Milestone> milestoneOrderedSOPList to later persist changes
         * We could use the public String saveSOPList(List<Milestone> list) method,
         * but this would cause network traffic every time we sitched the Select/Deselect 
         * Button! Better would be to hold the Collections in memory and finally 
         * save by calling saveSOPList(List<Milestone> list) method ONCE, when we really need/want 
         * to save changes. For now, we will produce a small portion of redundant code, but we
         * decide to do so and adapt later
         */

        saveSOPList(milestoneOrderedSOPList);
        int milestoneorderposition = 1;
        UntersuchungstypMilestone utms = null;
        Collection<UntersuchungstypMilestone> untersuchungstypMilestoneCollection = new ArrayList<>();
        for (Milestone temp : milestoneOrderedSOPList) {
            UntersuchungstypMilestonePK utmsPK = new UntersuchungstypMilestonePK(currentUTyp.getUntersuchungtypId(), temp.getMilestoneId());
            utms = new UntersuchungstypMilestone(utmsPK);
            utms.setMilestoneorderpos(milestoneorderposition);
            milestoneorderposition++;
            untersuchungstypMilestoneCollection.add(utms);
        }
        currentUTyp.setUntersuchungstypMilestoneCollection(untersuchungstypMilestoneCollection);
        System.out.println("Size of milestoneOrderedSOPList=" + milestoneOrderedSOPList.size());
        /* last but not least :-) 
         * take the utypList replace the currentUTyp in this list for later 
         * persitence
         */
        System.out.println("utypList.contains(currentUTyp)=" + utypList.contains(currentUTyp));
        if (utypList.contains(currentUTyp)) {
            int indexOf1 = utypList.indexOf(currentUTyp);
            System.out.println("utypList.indexOf(currentUTyp)=" + indexOf1);
            utypList.remove(indexOf1);
            utypList.add(indexOf1, currentUTyp);
        }
        return null;
    }

    public String clearSOPList() {
        milestoneOrderedSOPList.clear();
        //deselect selected Milestones in milestoneList
        for (Milestone ms : milestoneList) {
            ms.setSelected(false);
        }
        return null;
    }

    public String deSelectUTyp(Untersuchungstyp utyp) {
        // set utyo.setSelected(false);

        int indexOf = utypList.indexOf(utyp);
        utypList.remove(utyp);
        utyp.setSelected(false);
        utypList.add(indexOf, utyp);
        /* 
         * empty the and utypList as well reset the currentUTyp 
         * also milestoneOrderedSOPList
         * BETTER implementation would be just to replace the Untersuchungstyp
         * utyp with utyp.setSelected(false); in uTypList
         */
        milestoneOrderedSOPList.clear();
        milestoneList.clear();
        currentUTyp = new Untersuchungstyp();
        /*
         * refill the uTypTrefferList to previous search
         * NOT refill, better replace deselected Untersuchungstyp utyp in uTypList
         */
//        for (UTypTreffer utt : uTypTrefferList) {
//            Untersuchungstyp ut = details(utt);
//
//            utypList.add(ut);
//        }
        return null;
    }

    public String sumMsAllTimes() {
        // take the milestoneOrderedSOPList and summarize the fields
        String s = "";
        Long l = 0l;
        for (Milestone ms : milestoneOrderedSOPList) {
            Date milestoneAlltime = ms.getMilestoneAlltime();
            l += (milestoneAlltime.getHours() * 60 + milestoneAlltime.getMinutes());
        }
        return s = String.valueOf(l);
    }

    public String sumMsHandsonTimes() {
        // take the milestoneOrderedSOPList and summarize the fields
        String s = "";
        Long l = 0l;
        for (Milestone ms : milestoneOrderedSOPList) {
            Date milestoneHandsontime = ms.getMilestoneHandsontime();
            l += (milestoneHandsontime.getHours() * 60 + milestoneHandsontime.getMinutes());
        }
        return s = String.valueOf(l);
    }

    public String sumMsManHour() {
        // take the milestoneOrderedSOPList and summarize the fields
        String s = "";
        Long l = 0l;
        for (Milestone ms : milestoneOrderedSOPList) {
            Date milestoneHandsontime = ms.getMilestoneHandsontime();
            l += (milestoneHandsontime.getHours() * 60 + milestoneHandsontime.getMinutes()) * ms.getMilestoneMannumber();
        }
        return s = String.valueOf(l);
    }

    public String sumMsCost() {
        // take the milestoneOrderedSOPList and summarize the fields
        Double d = 0.0;
        for (Milestone ms : milestoneOrderedSOPList) {
            String milestonecost = ms.getMilestonecost();
            d += Double.parseDouble(milestonecost);
        }
        return String.format("%.2f €", d);
    }

    public String resetLists() {
        currentUTyp = new Untersuchungstyp();
        currentMilestone = new Milestone();

        return null;
    }

    public void resetLists(List<Untersuchungstyp> utypList, List<Milestone> milestoneList) {
        if (utypList != null) {
            this.utypList.clear();
        }
        if (milestoneList != null) {
            this.milestoneList.clear();
        }
        milestoneOrderedSOPList.clear();
    }

    public void resetCurrents(Untersuchungstyp currentUTyp, Milestone currentMilestone) {
        if (currentUTyp != null) {
            this.currentUTyp = new Untersuchungstyp();
//            this.currentUTyp.s;
        }
        if (currentMilestone != null) {
            this.currentMilestone = new Milestone();
            this.currentMilestone.setMilestonetypId(new Milestonetyp());

        }
    }

    private Untersuchungstyp details(UTypTreffer utt) {
        return transmitterSessionBeanRemote.findById(Untersuchungstyp.class, utt.getUntersuchungtypId());
    }

    private Milestone details(MilestoneTreffer mst) {
        return transmitterSessionBeanRemote.findById(Milestone.class, mst.getMilestoneId());
    }

}
