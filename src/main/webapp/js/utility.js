/**
 * @description Nilaya Foundation Donation Portal Utility Scripts
 * @author Shrinivas Katti
 */
 
 function UserAction(f) {
		//alert("in Form submit");
		var formData = new FormData( document.getElementById("donorForm") );
		var object = {};
		formData.forEach(function(value, key){
		    object[key] = value;
		});
		var json = JSON.stringify(object);
		
		console.log(json);
	    var xhttp = new XMLHttpRequest();
	    xhttp.onreadystatechange = function() {
	         if (this.readyState == 4 && this.status == 200) {
	             //alert(this.responseText);
	             
	             document.getElementById("receiptStatus").style.color="green";
	             document.getElementById("receiptStatus").innerHTML="RECEIPT SENT TO DONOR SUCCESSFULLY!";
	             document.getElementById("successModal").click();
	             //location.reload();
	         } 
	         if (this.readyState == 4 && this.status != 200) {
	        	 console.log(this.responseText)
	        	  document.getElementById("receiptStatus").style.color="red";
	        	 document.getElementById("receiptStatus").innerHTML="ERROR IN PROCESSING REQUEST.<br><br> CONTACT DEVELOPER.";
	             document.getElementById("successModal").click();
	         }
	    };
	    xhttp.open("POST", location.origin+"/"+location.pathname.split("/")[1]+"/webapi/receipt/setDetails", false);
	    xhttp.setRequestHeader("Content-type", "application/json");
	    xhttp.send(json);
}
	
/**
 *
 */	
function setDateRange(){
	//"2022-03-31"
	console.log("loading");
	const d = new Date();
	var month = d.getMonth() + 1;
	var monthInString;
	var minFinancialYearStart = d.getFullYear();
	if(month<10){
		monthInString = "0"+month;
	}
	
	var date =d.getDate();
	if(date<10){
		date = "0"+date;
	}
	
	var maxDate = minFinancialYearStart + "-" + monthInString + "-" + date;
	//In current year, year value remains the same
	//In next year
	if(month < 4)
		{
		minFinancialYearStart = d.getFullYear() - 1;
		}
	var minDate = (minFinancialYearStart)+"-04-01";
	document.getElementById("donationDate").max = maxDate;
	document.getElementById("donationDate").min = minDate;
}

/**
 */

function getDonorDataAll() {
		
		
	    var xhttp = new XMLHttpRequest();
	    xhttp.onreadystatechange = function() {
	         if (this.readyState == 4 && this.status == 200) {
	             //alert(this.responseText);
	            const donorjson = JSON.parse(this.responseText)
 
	       		
	            updateDonortable(donorjson)	     
	         } 
	       
	    };
	    
	    xhttp.open("GET", location.origin+"/"+location.pathname.split("/")[1]+"/webapi/receipt/getData", false);
	    xhttp.setRequestHeader("Accept", "application/json");
	   xhttp.send();
}

/**

 */
function updateDonortable(donorjson){
		var table = document.getElementById("donortablebody");
		const donorHeader = '["receiptNumber","donorName","donorPAN","donorMobileNumber","donorEmail","donationAmount","donationDate","volunteerEmail"]';
		const donorHeaderJson = JSON.parse(donorHeader);
		console.log("updateDonortable");
		for (let i in donorjson) {
			//for(let i = 0; i < 100; i++){
			var donor = donorjson[i];
			var text ="donorEmail";
			var tr = document.createElement('tr');  
			console.log("Vaue:"+donor['donorEmail']);
			for(var j in donorHeaderJson){
				console.log(j);
				var td1 = document.createElement('td');
				var text1 = document.createTextNode(donor[donorHeaderJson[j]]);
				td1.appendChild(text1);
				tr.appendChild(td1);
			} 
			
			table.appendChild(tr);
			//console.log(donorjson[i].$text);
		}
}

/**

 */
function getDonorDetailsOnPAN(){
		var panDom = document.getElementById("donorPAN");
		var emailDom = document.getElementById("donorEmail");
		var nameDom = document.getElementById("donorName");
		var mobileDom = document.getElementById("donorMobileNumber");
		
		var panValue = panDom.value;
		panDom.value = panDom.value.toUpperCase();
		 var xhttp = new XMLHttpRequest();
	    xhttp.onreadystatechange = function() {
	         if (this.readyState == 4 && this.status == 200) {
	             //alert(this.responseText);
	            const donorjson = JSON.parse(this.responseText)
 				console.log(donorjson);
 				var jsonObj = donorjson[0];
 				
 				if(jsonObj != null){
	 				
	 				emailDom.value = jsonObj.donorEmail;
	 				nameDom.value = jsonObj.donorName;
	 				mobileDom.value = jsonObj.donorMobileNumber;
	 				
	 				emailDom.readOnly = true;
 					nameDom.readOnly = true;
 					mobileDom.readOnly = true;
 				}else{
 				
 				
	 				emailDom.value = null;
	 				nameDom.value = null;
	 				mobileDom.value = null;
	 				
 					emailDom.readOnly = false;
 					nameDom.readOnly = false;
 					mobileDom.readOnly = false;
 					
 				}
	         } 
	       
	    };
	    
	    xhttp.open("GET", location.origin+"/"+location.pathname.split("/")[1]+"/webapi/receipt/getData/"+panValue, false);
	    xhttp.setRequestHeader("Accept", "application/json");
	   xhttp.send();
}

/**
 */
function getDonorDetailsOnName(){
	
	$(".autocomplete").autocomplete({
		    source:  function (request, response) {
	            $.ajax({
	                url: location.origin+"/"+location.pathname.split("/")[1]+"/webapi/receipt/getData/name",
	                dataType: "json",
	                data: { term: request.term },
	                success: function (data) {
						$("#newDonor").prop("checked", false);
	                	donorList = data;
						console.log(data);
						console.log(typeof data);
						jsonObj = [];
						$(data).each(function (index, item) {

				               // each iteration
				            console.log(item)
				            jsonObj.push(item.donorName);
				           });
						console.log(jsonObj)
						if(jsonObj.length == 0){
							$("#newDonor").prop("checked", true);
						}
	                    response(jsonObj);
	                }
	            });
	        },
	        select: function (event , ui){
	        	console.log(ui);
	        	console.log("Donors")
	        	console.log(donorList);
	        	var donorname = ui.item.value;
	        	
		        var panDom = document.getElementById("donorPAN");
		        var emailDom = document.getElementById("donorEmail");
		        var nameDom = document.getElementById("donorName");
		        var mobileDom = document.getElementById("donorMobileNumber");
		        		
	        	if("New".localeCompare(donorname) == 0) {
					 console.log("New Donor")
					emailDom.value = null;
	 				panDom.value = null;
	 				mobileDom.value = null;
	 				nameDom.value = "";
	 				
 					emailDom.readOnly = false;
 					panDom.readOnly = false;
 					mobileDom.readOnly = false;
				} else {
					$(donorList).each(function (index, item) {

		               // each iteration
		            //console.log(item)
		            var name = item.donorName;
		            if(name.localeCompare(donorname) == 0){
		            	
		            	var panDom = document.getElementById("donorPAN");
		        		var emailDom = document.getElementById("donorEmail");
		        		var nameDom = document.getElementById("donorName");
		        		var mobileDom = document.getElementById("donorMobileNumber");
		        		
		        		emailDom.value = item.donorEmail;
		        		panDom.value = item.donorPAN;
		 				mobileDom.value = item.donorMobileNumber;
		 				
		 				emailDom.readOnly = true;
		 				panDom.readOnly = true;
	 					mobileDom.readOnly = true;
		            }
		            
		         });	
				}
	        	
	        }
		});
}