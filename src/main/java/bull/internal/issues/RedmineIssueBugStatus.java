/*
 * Copyright (c) 2018 Bull/Atos.
 * All rights reserved.
 */
package bull.internal.issues;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * A status line. An Issue, which is a RedmineIssueBug object, contains a list of RedmineIssueBugStatus objects.
 * The number of objects in this list varies according to the Issues. An Issue may also not have an object list,
 * if it has only one status, that of its creation.
 * @author Bull/Atos
 */

public class RedmineIssueBugStatus implements Comparable<RedmineIssueBugStatus>{
    private String statusNewNumber;
    private String statusOldNumber;
    private Date statusDate;

    /**
     * Creating a history line list associated with an Issue (one line only)
     * @param statusNewNumber the new status of the line
     * @param statusOldNumber the old status of the line
     * @param statusDate the date on which the status was updated
     */
    public RedmineIssueBugStatus(String statusNewNumber, String statusOldNumber, Date statusDate){
        this.statusNewNumber = statusNewNumber;
        this.statusOldNumber = statusOldNumber;
        this.statusDate = statusDate;
    }

    /**
     * @return String, the date as a formatted string
     */
    public String getStatusDateFormatted(){
        SimpleDateFormat formatTool = new SimpleDateFormat("yyyy-MM-dd");
        return formatTool.format(this.statusDate);
    }

    /**
     * @return String, depending on the status number, returns a readable status as a string.
     */
    public String getStatusNewNumber() {
        switch(this.statusNewNumber){
            case "1":
                return "Nouveau";
            case "3":
                return "Résolu";
            case "5":
                return "Terminé";
            case "6":
                return "Rejeté";
            case "9":
                return "Bloqué";
            case "10":
                return "Intégré";
            case "14":
                return "En cours";
            case "15":
                return "A relire";
            case "16":
                return "Implémenté";
            case "22":
                return "A vérifier";
            case "18":
                return "En attente de décision";
            case "21":
                return "A livrer";
            default:
                return this.statusNewNumber;
        }
    }

    /**
     * @return String, depending on the status number, returns a readable status as a string.
     */
    public String getStatusOldNumber() {
        switch(this.statusOldNumber) {
            case "1":
                return "Nouveau";
            case "3":
                return "Résolu";
            case "5":
                return "Terminé";
            case "6":
                return "Rejeté";
            case "9":
                return "Bloqué";
            case "10":
                return "Intégré";
            case "14":
                return "En cours";
            case "15":
                return "A relire";
            case "16":
                return "Implémenté";
            case "22":
                return "A vérifier";
            case "18":
                return "En attente de décision";
            case "21":
                return "A Livrer";
            default:
                return this.statusOldNumber;
        }
    }

    /**
     * @return check whether the status has been changed form new to integrated
     */
    public boolean checkIfStatusIsNewFromIntegrated(){
        return ((Integer.parseInt(this.statusOldNumber)+Integer.parseInt(this.statusNewNumber)) == 11);
    }

    @Override
    public int compareTo(final RedmineIssueBugStatus o) {
        return this.statusDate.compareTo(o.statusDate);
    }

    @Override
    public boolean equals(final Object obj) {
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
