
angular.module('app')

  .controller('RoboTCController', function ( $http ,$scope,localStorageService, $modal) {
  	 $scope.autrs=[];
  	 $scope.test =  localStorageService.get('test');
  	 var jsonData =  $scope.test.autinfo;
  	 var auttestcases=jsonData.appTestSuites;
  	 for (var i = 0; i < auttestcases.length; i++) {
  		 var data=auttestcases[i].split(".");
  		 var l=data.length;
  		 $scope.autrs.push({ 'id':auttestcases[i], 'tcname': data[l-1],'select':false});

  	 }

  	 $scope.selectedAll = false;
  	 $scope.save = function(testsuite, app){
  		 if(testsuite){
  			var tsName= testsuite.toString();
  		 	var newDataList=[];
  	        angular.forEach($scope.autrs, function(tc){
  	            if(tc.select==true){
  	                newDataList.push(tc);

  	            }
  	        });
  	        newDataList.tsName=tsName;
  	        $scope.robotestDetails = newDataList;
  	        //console.log( $scope.robotestDetails )
  	        $http({
  	   	        method : 'POST',
  	   	        url : './saveRoboTestSuiteServlet',
  	   	        data :{'test':$scope.robotestDetails ,'testname':tsName, 'app':app}
  	   		    }).then(function(success, status, headers, config) {
  	   		    //	console.log(data);
  	   		    	var response;
  			    	if(typeof success =='object') {
  			    		// It is JSON
  			    		response=success.data;
  			    	} else if(typeof success == 'string'){
  			    		// It is string
  			    		response=success;
  			    	}
  	   		    	if(response=="Success"){
  	   		    		$scope.test.name=$scope.robotestDetails.tsName;
  	   		    		$scope.test.testsuite=$scope.robotestDetails;
  	   		    		localStorageService.set('test',$scope.test);
  	   		    		var modalInstance = $modal.open({
  	   		   		      templateUrl: 'TestNameComformModel.html',
  	   		   		      controller: 'TestNameComformController',
  	   		   		      backdrop: 'static'
  	   		   		    });
  	   		    	} else {
  						$scope.exist= true;
  					}

  	   		    },function(error, status, headers, config) {
  	   		            // called asynchronously if an error occurs
  	   		            // or server returns response with an error status.

  	   		    });




  		 } else {

  		 }

  	    };

  	    $scope.isAll = false;
  	    $scope.selectAllRows = function() {
  	      //check if all selected or not
  	        angular.forEach($scope.autrs, function(row) {
  	        	if(row.select != true){
  	        		row.select = true;
  	        	}
  	          //console.log("checkAll ::" );
  	         // console.log(  row );
  	        });
  	        $scope.isAll = true;
  	    };
  	    $scope.unselectAllRows = function() {
  		        //set all row unselected
  		        angular.forEach($scope.autrs, function(row) {
  		        	if(row.select == true){
  		        		row.select = false;
  		        	}
  		          //console.log("uncheckAll ::" );
  		          //console.log(  row );
  		        });
  		        $scope.isAll = false;
  		};
  	    $scope.selectRow = function(val) {
  	    	 if(val!=null){
  	    		 //console.log("selectRow ::"+ val);
  		    	 if (val.select==true) {
  		    		 val.select = false;
  		    		 //console.log("uncheck ::");
  		    		 //console.log( val);
  		    	 }else {
  					 val.select = true;
  					// console.log("check ::");
  					 //console.log( val);
  				}
  	    	 }
  	    };
	  	  $scope.isSelectedRow = function() {
		    	 if( $scope.disableButton == true){
		    		 $scope.showError=true;
		    	 } else {
		    		 $scope.showError=false;
		    	 }
		    };
  	    $scope.disableButton = true;
  	    $scope.$watch('autrs', function(val){
  	      if(val){
  		        $scope.disableButton = true;
  		        for (var i in val){
  			          if(val.hasOwnProperty(i)){
  			        	  //console.log( val[i]);
  			        	  if(val[i].select==true){
  			        		  $scope.disableButton = false;
  			        		  $scope.showError=false;
  			        	  }
  			          }
  		        }
  	      }
  	      //console.log("uncheckAll ::"+ $scope.disableButton);
  	    }, true)
  })