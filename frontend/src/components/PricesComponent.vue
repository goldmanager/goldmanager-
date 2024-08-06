<template>
  <div class="main">
    <div class="content">
    <div><h1>Prices</h1></div>
    <div v-if="errorMessage" class="error">{{ errorMessage }}</div>
    <div v-if="currentViewType==='PriceList'">
		<div><input v-model="searchQueryPriceList" type="text" placeholder="Search by item name"></div>
    <table>
      <thead>
        <tr>
          <th>Total Price</th>
          <th>Filter by Metal</th>
          <th>Switch View Type</th>
        </tr>
      </thead>
      <tbody>
      <tr>
        <td>{{ formatPrice(priceList.totalPrice) }}</td>
        <td>
        <select id="metals" v-model="currentFilter" @change="setCurrentFilter($event)" >
          <option value="">None</option>
          <option v-for="metal in metals" :key="metal.id" :value="metal.id">{{ metal.name }}</option>
        </select>
        </td>
        <td>
        <select id="viewtype" v-model="currentViewType" @change="setCurrentViewType($event)" >
          <option v-for="viewType in viewTypes" :key="viewType.id" :value="viewType.id">{{ viewType.label }}</option>
        </select>
        </td>
      </tr>
      </tbody>
      </table>
      <div  v-if="priceList.prices.length >0">
      <table>
        <thead>
          <tr>
            <th @click="sortPriceListBy('name')">Name <span v-if="currentPriceListSort === 'name'">{{ currentPriceListSortDir === 'asc' ? '▲' : '▼' }}</span></th>
            <th @click="sortPriceListBy('amount')">Weight <span v-if="currentPriceListSort === 'amount'">{{ currentPriceListSortDir === 'asc' ? '▲' : '▼' }}</span></th>
            <th @click="sortPriceListBy('unit')">Unit <span v-if="currentPriceListSort === 'unit'">{{ currentPriceListSortDir === 'asc' ? '▲' : '▼' }}</span></th>
            <th @click="sortPriceListBy('itemCount')">Number of Items <span v-if="currentPriceListSort === 'itemCount'">{{ currentPriceListSortDir === 'asc' ? '▲' : '▼' }}</span></th>
            <th @click="sortPriceListBy('price')">Unit Price <span v-if="currentPriceListSort === 'price'">{{ currentPriceListSortDir === 'asc' ? '▲' : '▼' }}</span></th>
            <th @click="sortPriceListBy('priceTotal')">Total Price <span v-if="currentPriceListSort === 'priceTotal'">{{ currentPriceListSortDir === 'asc' ? '▲' : '▼' }}</span></th>
            <th @click="sortPriceListBy('metal')">Metal <span v-if="currentPriceListSort === 'metal'">{{ currentPriceListSortDir === 'asc' ? '▲' : '▼' }}</span></th>
          </tr>
        </thead>
        <tbody>

          <tr v-for="price in paginatedPriceList.prices" :key="price.item.id">
            <td>{{ price.item.name }}</td>
            <td>{{ price.item.amount }}</td>
            <td>{{ price.item.unit.name }}</td>
            <td>{{ price.item.itemCount }}</td>
            <td>{{ formatPrice(price.price) }}</td>
            <td>{{ formatPrice(price.priceTotal) }}</td>
            <td>{{ price.item.itemType.material.name }}</td>
          </tr>
        </tbody>
      </table>
      <div class="pagination" v-if="totalPriceListPages > 0">
		<button :class="currentPriceListPage === 1 ?'pagingButton_disabled':'pagingButton'" @click="prevPriceListPage" :disabled="currentPriceListPage === 1">Previous</button>
		<span>Page {{ currentPriceListPage }} of {{ totalPriceListPages }}</span>
		<button  :class="currentPriceListPage === totalPriceListPages?'pagingButton_disabled':'pagingButton'" @click="nextPriceListPage" :disabled="currentPriceListPage === totalPriceListPages">Next</button>
		<span>(Items per page: {{priceListPageSize}})</span>		 
	</div>
    </div>
	</div>
   <div v-else >
    <table>
      <thead>
        <tr>

          <th>Switch View Type</th>
        </tr>
      </thead>
      <tbody>
      <tr>
        <td>
        <select id="viewtype" v-model="currentViewType" @change="setCurrentViewType($event)" >
          <option v-for="viewType in viewTypes" :key="viewType.id" :value="viewType.id">{{ viewType.label }}</option>
        </select>
        </td>
      </tr>
      </tbody>
      </table>
      <div v-for="(group, groupName) in priceGroups" :key="groupName">
      <H2>{{ groupName }}</H2>
      <table>
      <thead>
        <tr>
          <th>Total Price</th>
          <th>Total Weight</th>

        </tr>
      </thead>
      <tbody>
      <tr>
        <td>{{ formatPrice(group.totalPrice) }}</td>
        <td>{{ formatPrice(group.amount) }} Oz</td>

      </tr>
      </tbody>
      </table>


        <table v-if="group.prices.length >0">
        <thead>
          <tr>
            <th>Name</th>
            <th>Weight</th>
            <th>Unit</th>
            <th>Number of Items</th>
            <th>Unit Price</th>
            <th>Total Price </th>
            <th>Metal</th>
          </tr>
        </thead>
        <tbody>

          <tr v-for="price in group.prices" :key="price.item.id">
            <td>{{ price.item.name }}</td>
            <td>{{ price.item.amount }}</td>
            <td>{{ price.item.unit.name }}</td>
            <td>{{ price.item.itemCount }}</td>
            <td>{{ formatPrice(price.price) }}</td>
            <td>{{ formatPrice(price.priceTotal) }}</td>
            <td>{{ price.item.itemType.material.name }}</td>
          </tr>
        </tbody>
      </table>
      </div>
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
      viewTypes:[
      {
       id: 'PriceList',
       label: 'Price list'

      },
      {
       id: 'GroupByMetal',
       label: 'Group by Metal'
      }

      ],
      priceList: {totalPrice:0, prices:[]},
      priceGroups:{},
      metals: [],
      currentViewType: 'PriceList',
      errorMessage: '',
      currentFilter: '',
	  searchQueryPriceList:'',
	  priceListPageSize:10,
	  currentPricePageNumber: 1,
	  currentPriceListSortDir:'',
	  currentPriceListSort:'name'
    };

  },

  mounted() {
    this.currentFilter=this.getCurrentFilter();
    this.currentViewType=this.getCurrentViewType();
	this.currentPriceListSort=localStorage.getItem("PriceListColumnsSort")?localStorage.getItem("PriceListColumnsSort"):"name";
	this.currentPriceListSortDir = localStorage.getItem("PriceListColumnsSortDir")?localStorage.getItem("PriceListColumnsSortDir"):"asc";
    this.fetchData();

  },
  computed: {
	sortedPriceList() {
		
		let sortedPrices =[...this.priceList.prices];
		return sortedPrices.sort((a, b) => {
			let modifier = 1;
			if (this.currentPriceListSortDir === 'desc') modifier = -1;
			let valA = "";
			let valB = "";
			if (this.currentPriceListSort === 'name') {
				valA = a.item.name;
				valB = b.item.name;
			}
			else if (this.currentPriceListSort === 'amount') {
				valA = a.item.amount;
				valB = b.item.amount;
			}
			else if (this.currentPriceListSort === 'unit') {
				valA = a.item.unit.name;
				valB = b.item.unit.name;
			}
			else if (this.currentPriceListSort === 'itemCount') {
				valA = a.item.itemCount;
				valB = b.item.itemCount;
			}
			else if (this.currentPriceListSort === 'metal') {
				valA = a.item.itemType.material.name;
				valB = b.item.itemType.material.name;
			}							
			else {
				valA = a[this.currentPriceListSort];
				valB = b[this.currentPriceListSort];
			}
			if (typeof valA === 'string' && typeof valB === 'string') {
				valA = valA.toLowerCase();
				valB = valB.toLowerCase();
			}
			if (valA < valB) return -1 * modifier;
			if (valA > valB) return 1 * modifier;
			return 0;
		});
		
	},
	filteredPriceList() {
		if (this.searchQueryPriceList != null && this.searchQueryPriceList != '') {		
			return this.sortedPriceList.filter(price =>
			price.item.name.toLowerCase().includes(this.searchQueryPriceList.toLowerCase())
			);
		}
		return this.sortedPriceList;
	},
	paginatedPriceList() {
		let result = {};
		result["totalPrice"] = this.priceList.totalPrice;
		let start = (this.currentPriceListPage - 1) * this.priceListPageSize;
		let end = start + this.priceListPageSize;
		result["prices"]= this.filteredPriceList.slice(start, end);
		return result;
	},
	totalPriceListPages() {
		return Math.ceil(this.filteredPriceList.length / this.priceListPageSize);
	},
	currentPriceListPage() {
		if(this.currentPricePageNumber > this.totalPriceListPages){
			return 1;
		}
		return this.currentPricePageNumber;
	},
},
  methods: {
    getCurrentFilter(){
      return localStorage.getItem("PriceMaterialFilter")?localStorage.getItem("PriceMaterialFilter"):"";
    },

    setCurrentFilter(event){

      localStorage.setItem("PriceMaterialFilter",event.target.value);
      this.currentFilter=event.target.value;
      this.fetchData();
    },
    getCurrentViewType(){
      return localStorage.getItem("PriceViewType")?localStorage.getItem("PriceViewType"):"PriceList";
    },
    setCurrentViewType(event){

      localStorage.setItem("PriceViewType",event.target.value);
      this.currentViewType=event.target.value;
      this.fetchData();
    },
    async fetchData() {
      this.errorMessage='';
      this.priceList={totalPrice:0, prices:[]};
      this.priceGroups={};

      var errorMessage="Error fetching data. Please try again later.";

      if(this.currentViewType === "PriceList"){
         console.log("PriceList");
         this.fetchPriceList();
		 this.currentPricePageNumber=1;
      }
      else{
        this.fetchPriceGroupsByMetal();
      }

      try{
        errorMessage="Error fetching metals. Please try again later.";
        const response = await axios.get(`/materials`);
        this.metals = response.data.map(metal => ({
          id: metal.id,
          name: metal.name
        }));

      } catch (error) {
        console.error('Error fetching data:', error);
        this.setErrorMessage(error,errorMessage);
      }
    },
   async fetchPriceList(){
      var errorMessage="Error fetching data. Please try again later.";
      try{
          if(this.currentFilter != "" && this.currentFilter != null){

            errorMessage="No price for selected metal available.";
            console.log('currentFilter:',this.currentFilter);
            const response = await axios.get(`/prices/material/`+this.currentFilter);

            this.priceList = response.data;
            console.log('priceList:',this.priceList);
          }
          else{

            const response =  await axios.get(`/prices`);

            this.priceList = response.data;
            console.log('priceList:',this.priceList);
          }
      }
      catch(error){
        console.error('Error fetching data:', error);
        this.setErrorMessage(error,errorMessage);
      }
    },

   async fetchPriceGroupsByMetal(){
      var errorMessage="No price groups available.";
      try{
         console.log('currentFilter:',this.currentFilter);
         const response = await axios.get(`/prices/groupBy/material`);
         this.priceGroups = response.data.priceGroups;
         console.log('priceGroups:',this.priceGroups);

      }
      catch(error){
        console.error('Error fetching data:', error);
        this.setErrorMessage(error,errorMessage);
      }
    },
    formatPrice(value) {
      return Number(value).toFixed(2)
    },

    setErrorMessage(error, defaultMessage){
          if( error.response != null &&  error.response.data != null  && error.response.data.message != null){
            this.errorMessage =error.response.data.message;
          }
          else{
            this.errorMessage=defaultMessage;
          }
    },
	sortPriceListBy(column){			
		if (this.currentPriceListSort === column) {
			this.currentPriceListSortDir = this.currentPriceListSortDir === 'asc' ? 'desc' : 'asc';
			localStorage.setItem("PriceListColumnsSortDir",this.currentSortDir )				
		}
		this.currentPriceListSort=column;
		localStorage.setItem("PriceListColumnsSort",column);
		
	},
	nextPriceListPage() {
		if (this.currentPriceListPage < this.totalPriceListPages) {
			this.currentPricePageNumber = this.currentPriceListPage+1;
		}
	},
	prevPriceListPage() {
		
		if (this.currentPriceListPage > 1) {
			this.currentPricePageNumber = this.currentPriceListPage-1;
		}
		
	}		
  }
};
</script>
