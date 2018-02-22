
angular.module('app')

      .controller('writeTestController', function ( $scope, localStorageService, $http , $modal) {
    	  $scope.test = localStorageService.get('test');
    	    $scope.testCaseDetails = $scope.test.testCaseDetails;
    	    $scope.selectedAll = false;
    	    $scope.addNew = function(){
    	    	var tsno;
    	    	try{
    	    		 tsno=$scope.testCaseDetails.length;
    	    	} catch(e){
    	    		$scope.testCaseDetails=[];
    	    		tsno =0;
    	    	}
    	    	
    	        $scope.testCaseDetails.push({ 
    	            'stepno': "TS_"+(tsno+1), 
    	            'teststep': $scope.testCaseDetail.teststep,
    	            'expectResult': $scope.testCaseDetail.expectResult,
    	        });
    	        $scope.testCaseDetail.teststep= '';
    	        $scope.testCaseDetail.expectResult='';
    	        $scope.test.testCaseDetails=$scope.testCaseDetails;
    	        localStorageService.set('test', $scope.test);
    	    };

    	    $scope.remove = function(){
    	        var newDataList=[];
    	        $scope.selectedAll = false;
    	        angular.forEach($scope.testCaseDetails, function(selected){
    	            if(!selected.selected){
    	                newDataList.push(selected);
    	            }
    	        }); 
    	        $scope.testCaseDetails = newDataList;
    	        $scope.test.testCaseDetails=$scope.testCaseDetails;
    	        localStorageService.set('test', $scope.test);
    	    };
    	   
    	    
    	    $scope.checkAll = function () {
    	    	//console.log("checkAll"+ $scope.selectedAll);
    	        angular.forEach($scope.testCaseDetails, function(testCaseDetail) {
    	        	testCaseDetail.selected = $scope.selectedAll;
    	        });
    	    };
    	    $scope.save= function (test ,app) {
    	    	//console.log(test);
    	    	
    	    	  $http({
    	   	        method : 'POST',
    	   	        url : './saveWriteManualTestCaseServlet',
    	   	        data :{'testcase':test,'testcasename':test.name, 'app':app}
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
    	   		    		$scope.test.testCase=test;
    	   		    		$scope.test.name=test.name;
    	   		    		localStorageService.set('test', $scope.test);
    	   		    		var modalInstance = $modal.open({
    	   		   		      templateUrl: 'myModalContent.html',
    	   		   		      controller: 'TestCaseConfirmController',
    	   		   		      backdrop: 'static'
    	   		   		    });
    	   		    	} else {
    						$scope.exist= true;
    					}
    		   		     
    	   		    }, function(error, status, headers, config) {
    	   		            // called asynchronously if an error occurs
    	   		            // or server returns response with an error status.
    	   		    	
    	   		    });
    	   	  };
    	    
    	    

      })
      
  

