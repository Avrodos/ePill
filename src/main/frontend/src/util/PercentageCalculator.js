class PercentageCalculator {
	
	 constructor() {
	    }

	    setPercentagesForDrugFromDrugPlanItem(drugplanitems, drugplanitem, drug) {
	        var percentages = this.calculatePercentages(drug.halfTimePeriod);
	        for (var i = 0; i < drugplanitems.length; i++) {
	                if (drugplanitems[i].userDrugPlanItemId == drugplanitem.userDrugPlanItemId) {
	                        console.log("PercentageCalculator - item found");
	                        //set percentages for the next intermediateStep items
	                        for (var j = 1; j < percentages.length; j++) {
	                                if (i + j < drugplanitems.length) {
	                                        drugplanitems[i+j].percentage = percentages[j];
	                                } else {
	                                        break;
	                                }
	                                if (!drugplanitems[i+j].intermediateStep) {
	                                        break;
	                                }
	                        }
	                        break;
	                }
	        }
	        return drugplanitems;
	    }

	    /**
	     * calculate percentages for the next hours depending on half time period
	     * hours are relative beginning with 0
	     *
	     */
	    calculatePercentages(halfTimePeriod) {
	        var percentages=[];
	        var percentage = 100;
	        for (var i = 0; i < 10; i++) {
	                percentage = this.calculatePercentageForHour(i, halfTimePeriod);
	                console.log("i=" + i + ", " + percentage + " -> " + Math.floor(percentage));
	                console.log("i=" + i + ", " + percentage);
	                percentages.push(Math.floor(percentage));
	                if (percentage === 0) {
	                    break;
	                }
	        }
	        return percentages;
	    }

	    /**
	     * calculate percentage for "relative" hour:
	     * hour = 0 -> percentage = 100
	     * ...
	     */
	    calculatePercentageForHour(hour, halfTimePeriod) {
	        var percentage = 0;
	        if (halfTimePeriod == 0) {
	                percentage = 0;
	        }
	        if (100 - 50 * hour / halfTimePeriod > 0) {
	                percentage = 100 - 50 * hour / halfTimePeriod;
	        } else {
	                percentage = 0;
	        }
	        return percentage;
	    }
}

// Singleton pattern in ES6.
export default PercentageCalculator;