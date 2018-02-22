
angular.module('app')
 
.controller('ExecutionConfirmController', function($scope, $modalInstance, $modal, localStorageService, $http, $state) {
	 $scope.test = localStorageService.get('test');
	 var isSuccess = false;
	 if($scope.test.senario.toLowerCase()=='manual' ){
		 $http({
		        method : 'POST',
		        url : './saveManualTestCaseResultServlet',
		        data :{'testcase':$scope.test.testCase,'testName':$scope.test.name, 'app': $scope.test.app, 'deviceId':$scope.test.device.DeviceUDID}
			    }).then(function(success, status, headers, config) {
			    	//console.log(data);
			    	var response;
	 		    	if(typeof success =='object') {
	 		    		// It is JSON
	 		    		response=success.data;
	 		    	} else if(typeof success == 'string'){
	 		    		// It is string
	 		    		response=success;
	 		    	}
			    	if(response=="Success"){
			    		$http({
				 	        method : 'POST',
				 	        url : './getManualTestCaseResultServlet',
				 	        data :{'testcasename':$scope.test.name, 'app': $scope.test.app, 'deviceId':$scope.test.device.DeviceUDID}
				 		    }).then(function(success, status, headers, config) {
				 		    	//console.log(success);
				 		    	var response;
				 		    	if(typeof success =='object') {
				 		    		// It is JSON
				 		    		response=success.data;
				 		    	} else if(typeof success == 'string'){
				 		    		// It is string
				 		    		response=success;
				 		    	}
				 		    	if(response!='failed'){
				 		    		var exTime = getTimeStamp(response.excTime);
					 		    	$scope.test.excTime=exTime;
					 		    	var tco=response.test;
					 		    	var tcase = eval(tco);
					 		    	$scope.tresult=tcase;
					 		    	$scope.test.result=$scope.tresult;
					 		    	$scope.test.testResult=response.testResult;
					 		    	localStorageService.set('test',$scope.test);
					 		    	isSuccess = true;
					 		    	console.log($scope.test.result);
				 		    	} else {
				 		    		/*var modalInstance = $modal.open({
				  	   		   		    templateUrl: 'testCompleteModel.html',
					   		   		    controller: 'ExecutionConfirmController',
					   		   		    backdrop: 'static'
					   		   		});*/
				 		    	}


				 		    }, function(error, status, headers, config) {
				 		            // called asynchronously if an error occurs
				 		            // or server returns response with an error status.

				 		    });

			    	}



			    }, function(error, status, headers, config) {
			            // called asynchronously if an error occurs
			            // or server returns response with an error status.

			    });


		} else if($scope.test.senario=='automation' || $scope.test.senario=='Automation' ) {
				$http({
		 	        method : 'POST',
		 	        url : './getTestResult',
		 	        data :{'testName':$scope.test.name, 'app': $scope.test.app, 'deviceId':$scope.test.device.DeviceUDID}
		 		    }).then(function(success, status, headers, config) {
		 		    	var response;
		 		    	if(typeof success =='object') {
		 		    		// It is JSON
		 		    		response=success.data;
		 		    	} else if(typeof success == 'string'){
		 		    		// It is string
		 		    		response=success;
		 		    	}
		 		    	var exTime = getTimeStamp(response.excTime);
		 		    	$scope.test.excTime=exTime;

		 		    	$scope.result=response;
		 		    	
		 		    	//console.log($scope.result);
		 		    	if($scope.test.type=='robotium' || $scope.test.type=='Robotium' ) {

		 		    	} else if($scope.test.type=='appium' || $scope.test.type=='Appium' )  {
		 		    		console.log($scope.result.sessionId);
		 		    		$http({
		 			 	        method : 'POST',
		 			 	        url : './getScreenShotsInfo',
		 			 	        data :{'sessionId':$scope.result.sessionId}
		 			 		    }).then(function(success, status, headers, config) {
		 			 		    	var response;

		 			 		    	if(typeof success =='object') {
		 			 		    	  // It is JSON
		 			 		    	  response=success.data;

		 			 		    	} else if(typeof success == 'string'){
		 			 		    		 // It is string
		 			 		    		response=success;
		 			 		    	}
		 			 		    	if(response!='failed'){
		 			 		    		for (var key in response) {
				 			 		    	  if (response.hasOwnProperty(key)) {
				 			 		    	    var val = response[key];
				 			 		    	    console.log(val);
				 			 		    	    //data[key]=document.location.origin+"/ScreenShotStream?fileName="+val;
				 			 		    	  response[key]=document.location.origin+"/TestPaceMobile/ScreenShotStream?fileName="+val;
				 			 		    	  }
				 			 		    	}
		 			 		    	}


		 			 		    	$scope.test.screens=response;
		 			 		    	localStorageService.set('test',$scope.test);
		 					    }, function(error, status, headers, config) {
		 			 		            // called asynchronously if an error occurs
		 			 		            // or server returns response with an error status.

		 			 		    });


		 				}


		 		    	//document.location.origin+"/ScreenShotStream?fileName=screen_9_58378329-f626-462b-bad7-444a6b53481e.png
		 		    	//screen_9_58378329-f626-462b-bad7-444a6b53481e.png
		 		    	//$scope.result.filename=document.location.origin+"/VideoStream?fileName="+$scope.result.filename;
		 		    	$scope.result.filename=document.location.origin+"/TestPaceMobile/VideoStream?fileName="+$scope.result.filename;

		 				$scope.test.result= $scope.result;
		 				localStorageService.set('test',$scope.test);
				    }, function(error, status, headers, config) {
		 		            // called asynchronously if an error occurs
		 		            // or server returns response with an error status.

		 		    });


		}
		 $scope.ok = function () {
			 	$scope.test =  localStorageService.get('test');
			    $modalInstance.dismiss('cancel');
			    localStorageService.set('test',$scope.test);
			    //$location.path('/detailedreports');
			    $state.go('home.detailedreports');
			  };


})