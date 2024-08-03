<template>
  <div class="main">
    <div class="content">
    <div><h1>Items</h1></div>
    <div v-if="errorMessage" class="error">{{ errorMessage }}</div>
	<div><input v-model="itemsFilter" type="text" placeholder="Search by Item name"></div>
    <table >
      <thead>
        <tr>
          <th @click="sortBy('name')">Name <span v-if="currentSort === 'name'">{{ currentSortDir === 'asc' ? '▲' : '▼' }}</span>
		 </th>
          <th @click="sortBy('itemType')">Type <span v-if="currentSort === 'itemType'">{{ currentSortDir === 'asc' ? '▲' : '▼' }}</span></th>
          <th @click="sortBy('amount')">Weight <span v-if="currentSort === 'amount'">{{ currentSortDir === 'asc' ? '▲' : '▼' }}</span></th>
          <th @click="sortBy('unit')">Unit <span v-if="currentSort === 'unit'">{{ currentSortDir === 'asc' ? '▲' : '▼' }}</span></th>
          <th @click="sortBy('itemCount')">Number of Items <span v-if="currentSort === 'itemCount'">{{ currentSortDir === 'asc' ? '▲' : '▼' }}</span></th>
          <th>Actions</th>
        </tr>
      </thead>
      <tbody>
        <tr  v-if="!editedItem">
          <td><input v-model="newItem.name" type="text" placeholder="Name"></td>

          <td>
            <select id="optionsItemType" v-model="newItem.itemType.id">
              <option v-for="itemType in itemTypes" :key="itemType.value" :value="itemType.value">{{ itemType.text }}</option>
            </select>
          </td>
           <td><input v-model.number="newItem.amount" type="number" placeholder="Weight"></td>
          <td>
            <select id="optionsUnit" v-model="newItem.unit.name">
              <option v-for="unit in units" :key="unit.value" :value="unit.value">{{ unit.text }}</option>
            </select>
          </td>
           <td><input v-model.number="newItem.itemCount" type="integer" placeholder="Number of items"></td>
          <td>
            <button class="actionbutton" @click="addItem()">Add New</button>
          </td>

        </tr>
		<tr  v-if="editedItem">
		      <td><input v-model="editedItem.name" type="text" placeholder="Name"></td>

		      <td>
		        <select id="optionsItemType" v-model="editedItem.itemType.id">
		          <option v-for="itemType in itemTypes" :key="itemType.value" :value="itemType.value">{{ itemType.text }}</option>
		        </select>
		      </td>
		       <td><input v-model.number="editedItem.amount" type="number" placeholder="Weight"></td>
		      <td>
		        <select id="optionsUnit" v-model="editedItem.unit.name">
		          <option v-for="unit in units" :key="unit.value" :value="unit.value">{{ unit.text }}</option>
		        </select>
		      </td>
		       <td><input v-model.number="editedItem.itemCount" type="integer" placeholder="Number of items"></td>
		      <td>
		       <button class="actionbutton"  @click="updateItem(editedItem)">Save</button>
			   <button class="actionbutton"  @click="cancelEdit">Cancel</button>
		      </td>

		    </tr>
        <tr :class="getHighlightClass(item.id)" v-for="item in paginatedItems" :key="item.id">
          <td>{{item.name}}</td>
          <td>
            {{item.itemType.name}}
          </td>
           <td>{{item.amount}}</td>
          <td>
           {{ item.unit.name }}          
          </td>
          <td>{{item.itemCount}}</td>
          <td>
			<button class="actionbutton" v-if="editedItem == null"  @click="editItem(item)">Edit</button>
			<button class="actionbutton" v-if="editedItem != null && editedItem.id === item.id"  @click="cancelEdit">Cancel</button>
            <button class="actionbutton"  v-if="editedItem == null"  @click="deleteItem(item.id)">Delete</button>
          </td>
        </tr>
      </tbody>
    </table>

	<div class="pagination">
	
	  <button :class="currentPage === 1 ?'pagingButton_disabled':'pagingButton'" @click="prevPage" :disabled="currentPage === 1">Previous</button>
	  <span>Page {{ currentPage }} of {{ totalPages }} (Items per page: {{itemsPerPage}})</span>
	  <button  :class="currentPage === totalPages?'pagingButton_disabled':'pagingButton'" @click="nextPage" :disabled="currentPage === totalPages">Next</button>
	 
	</div>

    </div>
  </div>
</template>

<script>
/*eslint no-mixed-spaces-and-tabs: ["error", "smart-tabs"]*/

import axios from '../axios';

export default {
  name: 'ItemsComponent',
  data() {
    return {
      items: [],
      units: [],
      itemTypes: [],
      newItem: {
        name: '',
        amount: 0,
        itemType: {
          id: ''
          },
        unit: {
          name: ''
          },
        },
	  editedItem: null,  

	  itemsPerPageOptions:[5,10,15,20,25,30],
      preSelectedUnit:'',
      preSelectedItemtype:'',
	  errorMessage: '',
	  currentSort: '',
	  currentSortDir:'',
	  currentPage: 1,
	  itemsPerPage: 10,
	  itemsFilter: '',
	  highlightedItem:null,
	  highlightedType:''
	  
    };

  },

  mounted() {
	this.currentSort=localStorage.getItem("ItemsColumnsSort")?localStorage.getItem("ItemsColumnsSort"):"name";
	this.currentSortDir = localStorage.getItem("ItemsColumnsSortDir")?localStorage.getItem("ItemsColumnsSortDir"):"asc";
	this.currentPage = localStorage.getItem("ItemsCurrentPage")?localStorage.getItem("ItemsCurrentPage"):1;
	
    this.fetchData();
	
  },
  computed: {
	
     sortedItems() {
	   let itemsCopy = [...this.items];
       return itemsCopy.sort((a, b) => {
         let modifier = 1;
         if (this.currentSortDir === 'desc') modifier = -1;
		 let valA= "";
		 let valB= "";
		 if(this.currentSort === 'itemType'){
			valA = a.itemType.name;
			valB = b.itemType.name;
		 }
		 else if(this.currentSort === 'unit'){
                valA = a.unit.name;
                valB = b.unit.name;
		 }
		 else{
			valA= a[this.currentSort];
			valB= b[this.currentSort];
		 }
		 if (typeof valA === 'string' && typeof valB === 'string') {
			valA = valA.toLowerCase();
			valB = valB.toLowerCase();
		 }
		 if (valA< valB) return -1 * modifier;
		 if (valA >valB) return 1 * modifier;
         return 0;
       });
     },
	 paginatedItems() {
	     const start = (this.currentPage - 1) * this.itemsPerPage;
	     const end = start + this.itemsPerPage;
	     return this.filteredItems.slice(start, end);
	},
	totalPages() {
	   return Math.ceil(this.filteredItems.length / this.itemsPerPage);
	},
	filteredItems(){
		
		if(this.itemsFilter != null && this.itemsFilter !=''){
			return this.sortedItems.filter(item =>
			        item.name.toLowerCase().includes(this.itemsFilter.toLowerCase())
			 );
		}
		return this.sortedItems;
	}
  },
  methods: {
	
	editItem(item) {
	  this.highlightedItem=item.id;
	  this.highlightedType="editmode";
	  this.editedItem =  { ...item };
	},
	cancelEdit() {
	     this.editedItem = null;
		 this.highlightedItem='';
		 this.highlightedType='';
	 },
	sortBy(column){
		
		if (this.currentSort === column) {
		        this.currentSortDir = this.currentSortDir === 'asc' ? 'desc' : 'asc';
				localStorage.setItem("ItemsColumnsSortDir",this.currentSortDir )				
		    }
	    this.currentSort=column;
		localStorage.setItem("ItemsColumnsSort",column);
		
	},
	getHighlightClass(itemId) {
	     if (this.highlightedItem === itemId) {
	       return "highlight_"+this.highlightedType;
	     }
	     return '';
	   },
	nextPage() {
	      if (this.currentPage < this.totalPages) {
	        this.currentPage++;
			localStorage.setItem("ItemsItemsPerPage",this.currentPage);
	      }
	    },
	prevPage() {
	      if (this.currentPage > 1) {
	        this.currentPage--;
			localStorage.setItem("ItemsItemsPerPage",this.currentPage);
	      }
    },
    higlightItem(itemId,highlightedType){
		this.highlightedItem=itemId;
		this.highlightedType=highlightedType;
		setTimeout(() => {
		       this.highlightedItem = null;
			   this.highlightedType='';
		 }, 3000);
	},
   addItem(){
      axios.post('/items', this.newItem)
        .then(response => {
			axios.get('items/'+response.data.id).then(getResponse =>{			
				this.items.push(getResponse.data);
				  this.higlightItem(getResponse.data.id,"saved");
			        this.resetNewItem();
			});
        
        })
        .catch(error => {
          console.error("Error adding new unit:", error);
          this.setErrorMessage(error,"Error adding unit. Please try again later.");
        });
    },
    resetNewItem() {
      this.newItem= {

        name: '',
        amount: 0,
        itemType: {
          id: this.preSelectedItemtype
          },
        unit: {
          name: this.preSelectedUnit
          },

        };
    },
    async fetchData() {
      try {
        const response = await axios.get(`/units`);
         this.units = response.data.map(option => ({
          value: option.name,
          text: option.name
        }));
        if (this.units.length > 0) {
          this.preSelectedUnit = this.units[0].value;
        }
        this.newItem.unit.name=this.preSelectedUnit;

       const itemtyperesponse = await axios.get(`/itemTypes`);
        this.itemTypes = itemtyperesponse.data.map(option => ({
          value: option.id,
          text: option.name
        }));
         if (this.itemTypes.length > 0) {
          this.preSelectedItemtype = this.itemTypes[0].value;
        }
        this.newItem.itemType.id=this.preSelectedItemtype;

        const itemresponse = await axios.get(`/items`);

        this.items = itemresponse.data;
		this.itemsToSort=[...this.items];
      } catch (error) {
        console.error('Error fetching data:', error);
        this.setErrorMessage(error,"Error fetching data. Please try again later.");
      }
    },
   async updateItem(item) {
	  this.higlightItem(item.id,"saved");
      axios.put(`/items/`+item.id, item)
        .then(response => {
          // Handle success
          console.log("Update erfolgreich:", response);
		  this.editedItem = null;
		  this.originalItem = null;
          this.fetchData();
		  
		  
        })
        .catch(error => {
          // Handle error
          console.error("Update fehlgeschlagen:", error);
		  this.higlightItem(item.id,"error");
          this.setErrorMessage(error,"Could not update ItemType");
        });
    },
    async deleteItem(itemId) {
		if (window.confirm('Are you sure you want to delete this item?')) {
			 this.higlightItem(itemId,"deleted");
						 
		     await axios.delete(`/items/${itemId}`)
			 
		        .then(response =>  {
		          // Handle success
		          console.log("Delete erfolgreich:", response);
				  
		          this.fetchData();
		        })
		        .catch(error => {
		          // Handle error
		          console.error("Delete fehlgeschlagen:", error);
				  this.higlightItem(itemId,"error");
		          this.setErrorMessage(error,"Could not delete ItemType");
		        });
		}
    },

    setErrorMessage(error, defaultMessage){
          if( error.response != null &&  error.response.data != null  && error.response.data.message != null){
            this.errorMessage =error.response.data.message;
          }
          else{
            this.errorMessage=defaultMessage;
          }
    }

  }
};
</script>
