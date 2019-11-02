class PercentageCalculator {
	
    constructor() {
    }
    
    setPercentagesForDrugFromDrugPlanItem(drugplanitems, drugplanitem, drug) {
    	var percentages = this.calculatePercentages(drug.halfTimePeriod);
    	for (var i = 0; i < drugplanitems.length; i++) { 
    		if (drugplanitems[i].userDrugPlanItemId == drugplanitem.userDrugPlanItemId) {
        		console.log("PercentageCalculator - item found");
    			for (var j = 0; j < percentages.length; j++) {
    				drugplanitems[i].percentage = percentages[j];
    			}
    		}
    	}
    	return drugplanitems;
    }
    
    calculatePercentages(halfTimePeriod) {
    	var percentages=[];
    	var percentage = 100;
    	for (var i = 0; i < 10; i++) {
    		percentage = this.calculatePercentageForHour(i, halfTimePeriod);
    		console.log("i=" + i + ", " + percentage);
    		if (percentage === 0) {
    		    break;
    		}
    		percentages.push(percentage);
    	}
    	return percentages;
    }
   
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