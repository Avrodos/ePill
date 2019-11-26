package com.doccuty.epill.userdrugplan;

/**
 * Request parameter for i.e. "calculation" of user drug plan
 *
 * i.e.
 * {"UserDrugPlanRequestParameter": {
                   "idUser":2,"date":"17.11.2019 11:00"}
                }
 *
 * @author cs
 *
 */
public class UserDrugPlanRequestParameter {
        int idUser;
        String date;

        public int getIdUser() {
                return idUser;
        }
        public void setIdUser(int idUser) {
                this.idUser = idUser;
        }
        public String getDate() {
                return date;
        }
        public void setDate(String date) {
                this.date = date;
        }
}


