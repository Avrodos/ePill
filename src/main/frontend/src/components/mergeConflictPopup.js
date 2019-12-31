import React from "react";
import {translate} from "react-i18next";

class mergeConflictPopup extends React.Component {

    constructor(props) {
        super(props);
        this.state = {};
    }

    render() {
        const {t} = this.props;

        return (
            <div>
                <div className="form-group col-lg-6 col-md-6">
                    <p><b>{t("whatToKeep")}</b></p>
                    <p>{t("keepExplanation")}</p>
                    <ul className="list-inline">
                        <li className="col-lg-4 col-md-4 col-xs-4 list-group-item">
                            <label htmlFor="whatToKeep-ePill" className="radio-inline">
                                <input type="radio" value="1" id="whatToKeep-ePill" name="whatToKeep"
                                       checked={this.props.overwriteOnImport == true} onChange={this.props.keepChange}/>
                                {t('keepePill')}
                            </label>
                        </li>
                        <li className="col-lg-4 col-md-4 col-xs-4 list-group-item">
                            <label htmlFor="whatToKeep-TPA" className="radio-inline">
                                <input type="radio" value="0" id="whatToKeep-TPA" name="whatToKeep"
                                       checked={this.props.overwriteOnImport == false}
                                       onChange={this.props.keepChange}/>
                                {t('keepTPA')}
                            </label>
                        </li>
                    </ul>
                    <button type="submit" className="btn btn-primary"
                            onClick={this.props.handleMCSubmit}>{t('confirm')} </button>
                </div>
            </div>
        )
    }

}

export default translate()(mergeConflictPopup);
