import React from "react";
import {translate} from "react-i18next";

//see: https://codepen.io/tantata/pen/QGVMza?editors=1111
class CheckBox extends React.Component{
         constructor(props) {
            super(props);
                console.log("checked=" + this.props.checked + ", id=" + this.props.id);
            this.state = {
                        isChecked: this.props.checked,
                        id: this.props.id
            };
            //this.handleChecked = this.handleChecked.bind(this); // set this, because you need get methods from CheckBox
         }

          handleChecked () {
            this.setState({isChecked: !this.state.isChecked});
            console.log("changed to " + this.state.isChecked);
          }

          onChangeChecked(event) {
                // pass isChecked and id of changed CheckBox to parent
                //const fieldName = event.target.name;
                //const fieldValue = event.target.value;
                this.props.onChange(this.props.checked, this.props.id);
          }

          render(){
            return (
	            <label className="drug-taken-checkbox">
	               <input type="checkbox" onChange={this.onChangeChecked.bind(this)} checked={this.state.isChecked}/>
	            	   <span className="check"></span>
	            </label>
            );
          }
}

export default translate()(CheckBox);