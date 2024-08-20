<template>
  <div class="main">
    <div class="content">
    <div><h1>Price History</h1></div>
    <div v-if="errorMessage" class="error">{{ errorMessage }}</div>
    <div >
	
    <table>
      <thead>
        <tr>
          <th>Select Metal</th>
		  <th>Date Range</th>
		  
        </tr>
      </thead>
      <tbody>
      <tr>
     
        <td>
        <select id="metals" v-model="currentMetal" @change="setCurrentFilter($event)" >
          <option v-for="metal in metals"  :key="metal.id" :value="metal.id">{{ metal.name }}</option>
        </select>
        </td>
		<td>
			<el-date-picker v-model="dateRange"
					type="datetimerange"
					range-separator="to"
					start-placeholder="Begin Date"
					end-placeholder="Ende Date"
					format="YYYY-MM-DD HH:mm:ss"
					value-format="YYYY-MM-DDTHH:mm:ss.SSS"
					@change="dateChanged()"
					:clearable="false"
					:unlink-panels="true"
					:shortcuts="dateTimeShortcuts"
					
					/>
		</td>
	    
      </tr>
      </tbody>
      </table>
	  
   </div>
    </div>
  </div>
</template>

<script>
/*eslint no-mixed-spaces-and-tabs: ["error", "smart-tabs"]*/
import axios from '../axios';
export default {
  name: 'PricesComponent',

  data() {
    return {
		errorMessage:null,
		metals: [],
		currentMetal:'',
		priceHistories:[],
		dateRange: this.getDefaultDateRange(),		
	  
    };

  },

  mounted() {
	this.currentMetal = this.getCurrentMetal();
	this.fetchData();
  },

computed:{ 
	dateTimeShortcuts(){
		let currentDate = new Date();
		let date24HoursAgo = new Date();
		date24HoursAgo.setHours(date24HoursAgo.getHours()-24);
		return [
			{
				text: 'Current Year',
				value: [new Date(currentDate.getFullYear(),0,1), currentDate],
			},
			{
				text: 'Current Month',
				value: [new Date(currentDate.getFullYear(),currentDate.getMonth(),1), currentDate],
			},
			{
				text: 'Today',
				value: [new Date(currentDate.getFullYear(),currentDate.getMonth(),currentDate.getDay()), currentDate],
			},
			{
				text: 'Last 24 hours',
				value: [date24HoursAgo, currentDate],
			},
		];
	}
},  
methods: {
	getDefaultDateRange(){
		return 	[this.formatDateCustom(new Date(new Date().getFullYear(),0,1)), this.formatDateCustom(new Date())];

	},
	formatDateCustom(date){
		var month=date.getMonth()+1;
		var day=date.getDate();
		var hour =date.getHours();
		var minutes =date.getMinutes();
		var seconds =date.getSeconds();
		if(month <10){
			month ="0"+month;
		}
		if(day <10){
			day ="0"+day
		}
		if(hour <10){
			hour="0"+hour;
	     }
	     if(minutes <10){
			minutes="0"+minutes;
		}
		if(seconds <10){
			seconds="0"+seconds;
		}
	
	     return `${date.getFullYear()}-${month}-${day}T${hour}:${minutes}:${minutes}.000`;
	},
	
	addOffsetToDateString(dateString){
		let offset = new Date().getTimezoneOffset();
		let hours = String(Math.floor(Math.abs(offset) / 60)).padStart(2, '0');
		let minutes = String(Math.abs(offset) % 60).padStart(2, '0');
		let sign = offset > 0 ? "-" : "%2b";
		let timeZoneFormat = `${sign}${hours}:${minutes}`;
		return `${dateString}${timeZoneFormat}`;
	 },
	 getCurrentMetal(){
	      return localStorage.getItem("PriceHistoryCurrentMetal")?localStorage.getItem("PriceHistoryCurrentMetal"):"";
	  },
	setCurrentFilter(event){
		localStorage.setItem("PriceHistoryCurrentMetal",event.target.value);
		this.currentMetal=event.target.value;
		this.fetchData();
	},
	dateChanged(){
			this.fetchPriceHistories();		
	},
	async fetchData() {
		this.errorMessage='';
		var errorMessage="Error fetching data. Please try again later.";
		try{
			errorMessage="Error fetching metals. Please try again later.";
			const metalsResponse = await axios.get(`/materials`);
			this.metals = metalsResponse.data.map(metal => ({
				id: metal.id,
				name:  metal.name,
			}));
			if((!this.currentMetal || this.currentMetal == '') && this.metals.length>0){
				this.currentMetal=this.metals[0].id;
			}
			this.fetchPriceHistories();
		} catch (error) {+
			console.error('Error fetching data:', error);
			this.setErrorMessage(error,errorMessage);
		}
	},
	async fetchPriceHistories(){
		this.errorMessage='';
		var errorMessage="Error fetching price history. Please try again later.";
		try{
			let startDate =this.addOffsetToDateString(this.dateRange[0]);
			let endDate =this.addOffsetToDateString(this.dateRange[1]);
			const priceHistoryResponse = await axios.get(`/priceHistory/${this.currentMetal}?startDate=${startDate}&endDate=${endDate}`);
			this.priceHistories = priceHistoryResponse.data.priceHistories;
		}
		catch(error){
			console.error('Error fetching data:', error);
			this.setErrorMessage(error,errorMessage);
		}
	},
	setErrorMessage(error, defaultMessage){
		if( error.response != null &&  error.response.data != null  && error.response.data.message != null){
			this.errorMessage =error.response.data.message;
		}
		else{
			this.errorMessage=defaultMessage;
		}
	},
}
};
</script>
