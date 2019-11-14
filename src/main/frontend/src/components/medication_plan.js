import axios from "axios";
import React from "react";

import {Link} from "react-router-dom";

import {translate} from "react-i18next";
import Loading from "./loading";
import User from "./../util/User";
import moment from 'moment';
import EmptyList from "./empty_list";

// See https://facebook.github.io/react/docs/forms.html for documentation about forms.
class MedicationPlan extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            drugsplanned: [],
            date : new Date()
        };

    }

    // This function is called before render() to initialize its state.
    componentWillMount() {
        console.log("componentWillMount");
        this.getData();
    }

    getData() {
        this.state.loading = true;
        this.setState(this.state);
        console.log("getting userdrugplanned");
        axios.get("/drug/list/userdrugplanned/date", {
                        params: {
                          date: this.state.date
                        }
                      }).then(({ data }) => {
         this.state.drugsplanned = data.value;
         this.state.loading = false;
         this.setState(this.state);
        });
    }

    // for html conversion
    createMarkup(text) {
        return { __html: text };
    }

    changeDate (incrementBy) {
        console.log("setDate");
        this.state.date.setTime(this.state.date.getTime() + incrementBy * 86400000);
        this.setState(this.state);
        this.getData();
    }

    formatDate(datetime) {
        var formatted_date = moment(datetime).format("HH:mm");
        return formatted_date;
    }


    renderDrugsPlanned(drugsplanned) {   
        return drugsplanned.map(drugplanned => {
            return (
                        <tr key={drugplanned.id} className="table-line-hover">
                                <td className="td-style">
                                        <div className="progress">
                                          <div className="progress-bar" style={{ width: "50%" }}></div>
                                        </div>
                                </td>
                                <td className="td-style">
                                        <input type="checkbox" value=""></input>
                                </td>
                                <td className="td-style">{this.formatDate(drugplanned.datetime_intake_planned)}</td>
                                <td className="td-style">{drugplanned.drug.name}</td>
                                </tr>                   
            );
        });
    }

    recalculatePlan() {
        console.log("recalculating user drug plan");
                axios.post('/drug/userdrugplanned/calculate/date', { date: moment(this.state.date).format("DD.MM.YYYY")}, {
            validateStatus: (status) => {
                console.log("status=" + status);
                return (status >= 200 && status < 300) || status == 400 || status == 401
            }
                })
     .then(({data, status}) => {
         console.log("status=" + status);
         const {t} = this.props;

         switch (status) {
             case 200:
                 console.log("case status 200");
                 this.getData();
                 break;
             case 400:
                 break;
             case 401:
                console.log(data, "not permitted");
                        break;
         }
        });
    }

    render() {
        const { t } = this.props;
        const drugsplanned = this.state.drugsplanned;
        var formatted_date = moment(this.state.date).format("DD.MM.YYYY");

        return (
            <div className="container no-banner">
                <div className="page-header">
                        <div className="text-date-change">
                            <button type="button" className="btn btn-sm btn-date-change" onClick={this.changeDate.bind(this, -1)}>
                            <span className="glyphicon glyphicon-triangle-left"></span>
                            </button>
                            <div className="mp-title">
                                <h3>{t("medicationPlan")}</h3>
                                <p>{" " + (t("for")) + " " + formatted_date}</p>
                            </div>
                            <button type="button" className="btn btn-sm btn-date-change" onClick={this.changeDate.bind(this, 1)}>
                            <span className="glyphicon glyphicon-triangle-right"></span>
                            </button>
                            <button type="button" className="btn btn-sm btn-add btn-add-drug">{t("addDrugsToMedicationPlan")}</button>
                            <button type="button" className="btn btn-sm btn-recalculate" onClick={() => this.recalculatePlan()}>{t("recalculatePlan")}
                                <span className="glyphicon glyphicon-white glyphicon-refresh"></span>
                                </button>
                        </div>
                </div>
                <div>
                    {drugsplanned.length > 1 && User.isAuthenticated() && (
                        <div>
                            <p>you have {drugsplanned.length} planned drugs</p>
                        </div>
                    )}

                    <div>
                        {this.state.loading && <Loading />}
                        {!this.state.loading && drugsplanned && drugsplanned.length == 0 && (
                            <EmptyList />
                        )}
                        {!this.state.loading && drugsplanned && drugsplanned.length > 0 && (
                            <table id="drugsplanned" className="table-style">
                                            <thead>
                                                    <th className="th-style">half-time-period</th>
                                                    <th className="th-style"></th>
                                                    <th className="th-style">time</th>
                                                    <th className="th-style">name</th>
                                            </thead>
                                            {this.renderDrugsPlanned(drugsplanned)}
                            </table>                           
                        )}
                    </div>
                </div>

            </div>
        );
    }
}

export default translate()(MedicationPlan);

