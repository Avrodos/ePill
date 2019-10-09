import React from "react";
import {translate} from "react-i18next";

class Clock extends React.Component {
      constructor(props) {
        super(props);
        this.state = {
          showSeconds: false,
          time: this.getCurrentTimeString(false)
        };
      }
      
      componentWillReceiveProps(props) {
		  this.setState({ showSeconds: props.showSeconds });  
	  }
      
      componentDidMount() {
        this.intervalID = setInterval(
          () => this.tick(),
          10000
        );
      }
      
      componentWillUnmount() {
        clearInterval(this.intervalID);
      }
      
      getCurrentTimeString(showSeconds) {
    	  var currentDate = new Date();
    	  var timeString = this.formatNumber(currentDate.getHours()) + ":" + this.formatNumber(currentDate.getMinutes());
    	  if (showSeconds) {
    		  timeString = timeString + ":" + this.formatNumber(currentDate.getSeconds());
    	  } 
    	  return timeString;
      }
      
      formatNumber(num) {
    	  if (num < 10) {
    		  return "0" + num;
    	  } else {
    		  return num;
    	  }
      }

      tick() {
        this.setState({
          time: this.getCurrentTimeString(this.state.showSeconds)
        });
      }
      
      render() {
        return (
            this.state.time
        );
      }
    }

export default translate()(Clock);