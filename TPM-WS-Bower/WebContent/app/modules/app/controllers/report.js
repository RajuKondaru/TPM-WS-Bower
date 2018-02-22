
angular.module('app')
.controller('ReportController', function ( $scope, $http, $state, localStorageService) {
      	 $scope.test = localStorageService.get('test');
      	 $scope.apps=  localStorageService.get('app');
      	 var totalRs=[];
      	 $scope.totalResult=$scope.test.results;
      	 angular.forEach($scope.totalResult,function(result,index){

      		 angular.forEach($scope.apps,function(app,index){
      		   if(app.id==result.appId){
      			   result.app=app;
      			   totalRs.push(result);
      		   }
      		 });
      	 });
      	 $scope.totalResults=totalRs;
      	 console.log($scope.test);
      	//localStorageService.set('test', $scope.test);

      	 if($scope.totalResults.length!=0){
      		  angular.forEach( $scope.res,function(rvalue,index){
      			   var up_date = getTimeStamp(rvalue.excTime);
      			   rvalue.excTime=up_date;
      			   angular.forEach($scope.apps,function(avalue,index){
      				   if(avalue.id==rvalue.appId){
      					   rvalue.AppName=avalue.appName;
      					   rvalue.icon=avalue.icon;
      					   rvalue.version=avalue.version;
      					   angular.forEach($scope.devices,function(dvalue,index){
      						   //console.log(rvalue.deviceId);
      						  // console.log(dvalue.deviceUdid);
      						   if(dvalue.deviceUdid==rvalue.deviceId){
      							   rvalue.deviceName=dvalue.manufacturer+" "+dvalue.model;
      							   trs.push(rvalue);
      							  // console.log(trs);
      						   }

      				       });

      				   }

      		       });

      		   });
      		  $scope.reportsError= false;
      	 } else{
      		 $scope.reportsError= true;
      	 }
      	$scope.test.getReport= function(result){

   		   $http({
   	 	        method : 'POST',
   	 	        url : './TestResult',
   	 	        data :{'testName':result.testName, 'appName': result.AppName, 'deviceName':result.deviceName}
   	 		    })
   	 		    .then(function(success, status, headers, config) {

   	 		    	var response;
   	 		    	if(typeof success =='object') {
   	 		    		// It is JSON
   	 		    		response=success.data;
   	 		    	} else if(typeof success == 'string'){
   	 		    		// It is string
   	 		    		response=success;
   	 		    	}
   	 		    	var appInfo = response.app;
   	 		    	var deviceInfo = response.device;
   	 		    	var resultInfo = response.result;

   	 	    		var app = JSON.parse(appInfo);
   	 	    		var device = JSON.parse(deviceInfo);
   	 	    		var result = JSON.parse(resultInfo);
   	 	    		//var res = eval(result);


   	 		    	$scope.test.app=app;
   	 		    	$scope.test.device=device;
   	 		    	$scope.result=result;
   	 				var exTime = getTimeStamp($scope.result.excTime);
   	 		    	$scope.test.excTime=exTime;
   	 		    	$scope.test.type=result.testType;
   	 		    	if($scope.test.type!='execution' || $scope.test.type!='Execution' ) {
   	 		    		//$scope.result.filename=document.location.origin+"/VideoStream?fileName="+$scope.result.filename;
   	 		    		$scope.result.filename=document.location.origin+"/TestPaceMobile/VideoStream?fileName="+$scope.result.filename;
   	 		    	}

   	 		    	$scope.test.name=result.testName;
   	 		    	$scope.test.senario=result.testSenario;
   	 		    	if($scope.test.senario=='manual' || $scope.test.senario=='Manual' ) {
   	 		    		var s =$scope.result.test;
   	 		    		$scope.result = eval('(' + s + ')');

   	 		    		$scope.result.testResult = result.testResult;
   	 		    		//console.log($scope.result.testResult);
   	 		    		/*if(s.includes("pending")){
   	 		    			$scope.result.testResult="Failed";
   	 		    		}else if(s.includes("fail")){
   	 		    			$scope.result.testResult="Failed";
   	 		    		} else {
   	 		    			$scope.result.testResult="Passed";
   						}*/
   	 		    	}

   	 		    	$scope.test.result= $scope.result;
   	 		    	//console.log($scope.test.type);
   	 		    	if($scope.test.type == 'appium' || $scope.test.type == 'Appium' )  {
   	 		    		//console.log($scope.result.sessionId);
   	 		    		$http({
   	 			 	        method : 'POST',
   	 			 	        url : './getScreenShotsInfo',
   	 			 	        data :{'sessionId':$scope.result.sessionId}
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
   	 			 		    	if(response!='failed'){
   	 			 		    		for (var key in response) {
   	 		 			 		    	  if (response.hasOwnProperty(key)) {
   	 		 			 		    	    var val = response[key];
   	 		 			 		    	    //console.log(val);
   	 		 			 		    	    //data[key]=document.location.origin+"/ScreenShotStream?fileName="+val;
   	 		 			 		    	 response[key]=document.location.origin+"/TestPaceMobile/ScreenShotStream?fileName="+val;
   	 		 			 		    	  }
   	 		 			 		    	}

   	 			 		    	}
   	 			 		    	$scope.test.screens=response;
   	 			 		    	//console.log($scope.test.screens);

   	 			 		    	//$data.set($scope.test);
   	 			 		    	
   	 			 		    	localStorageService.set('test',$scope.test);
   	 			 		    	$state.go('home.detailedreports');

   	 					    }, function(error, status, headers, config) {
   	 			 		            // called asynchronously if an error occurs
   	 			 		            // or server returns response with an error status.

   	 			 		    });


   	 				} else if($scope.test.type != 'appium' || $scope.test.type !='Appium') {

   		 		    	 //$data.set($scope.test);
   	 					
   	 					 localStorageService.set('test',$scope.test);
   		 		    	 $state.go('home.detailedreports');
   	 		    	}


   			    }, function(error, status, headers, config) {
   	 		            // called asynchronously if an error occurs
   	 		            // or server returns response with an error status.

   	 		    });


   		 }



      })
