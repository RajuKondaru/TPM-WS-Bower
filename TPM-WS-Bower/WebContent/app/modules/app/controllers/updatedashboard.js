
  angular.module('app')
 .controller('UpdateDashboardController', function ($scope,$http, PagerService, $state, $rootScope,localStorageService) {
      	   var activeApps=[];
      	   var archiveApps=[];
      	   var trs=[];
      	  
      	   $scope.test= localStorageService.get('test');
      	   $scope.apps=  localStorageService.get('app');
    	   $scope.devices=  localStorageService.get('device');
    	   $scope.res=  localStorageService.get('results');
      	   
      	  /* $scope.apps=  $app.get();
      	   $scope.devices=  $device.get();
      	   $scope.res=  $results.get();*/
      	   //console.log( $scope.res);

      	   angular.forEach($scope.apps,function(avalue,index){
      		   if(avalue.status.toLowerCase()=="active"){
      			   var up_date = getTimeStamp(avalue.uploadedDate);
      			   //console.log( avalue.uploadedDate);
      			   avalue.uploadDate= new Date(avalue.uploadedDate).toString();
      			   avalue.uploadedDate=up_date;
      			   activeApps.push(avalue);
      		   } else {
      			   var up_date=getTimeStamp(avalue.uploadedDate);
      			   avalue.uploadDate= new Date(avalue.uploadedDate).toString();
      			   avalue.uploadedDate=up_date;
      			   archiveApps.push(avalue);
      		   }

             });

      	   if($scope.res.length!=0){
      		   angular.forEach( $scope.res, function(rvalue,index){
      			   var up_date = getTimeStamp(rvalue.excTime);
      			   //console.log(up_date);
      			   rvalue.excDate= new Date(rvalue.excTime);
      			  // console.log(rvalue.excDate);
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

      		   trs.sort(function(a,b){
      			   // Turn your strings into dates, and then subtract them
      			   // to get a value that is either negative, positive, or zero.
      			   return b.excDate - a.excDate;
      			 });
      		   //console.log(trs);
      		   $scope.ftrs = trs;
      		   $scope.reportError = false;
      	   } else {
      		   $scope.reportError = true;


      	   }

      	   var results;
      	   $scope.activeappinfo = activeApps;
      	   $scope.archiveappinfo = archiveApps;
      	   $scope.totalResults={}
      	   $scope.$watch(function(scope) { return scope.ftrs },
      				function(newValue, oldValue) {
      					results = newValue;
      					if(results){
      						if(results!='null'){
      								$scope.totalResult=results.slice();
      								  var totalRs=[];
      							        angular.forEach($scope.totalResult,function(result,index){
      							   		 	 angular.forEach($scope.apps,function(app,index){
      								   		   if(app.id==result.appId){
      								   			   result.app=app;
      								   			   //console.log(result.app);
      								   			   totalRs.push(result);
      								   		   }
      								   		 });
      							   	 	});
      							    //console.log(totalRs);
      							    var vm = this;
      						        vm.dummyItems =totalRs; // dummy array of items to be paged
      						        vm.pager = {};
      						        vm.setPage = setPage;
      						        initController();
      						        function initController() {
      						            // initialize to page 1
      						            vm.setPage(1);
      						        }
      						        function setPage(page) {
      						            if (page < 1 || page > vm.pager.totalPages) {
      						                return;
      						            }
      						            // get pager object from service
      						            vm.pager = PagerService.GetPager(vm.dummyItems.length, page);
      						            // get current page of items
      						            $scope.totalResults = vm.dummyItems.slice(vm.pager.startIndex, vm.pager.endIndex + 1);

      						        }
      						  

      						        $scope.vm=vm;
      						      
      						      $scope.test.results=  results;
      					      
      					        localStorageService.set('test',$scope.test);

      						}

      					}


      	         	}
      	    );

      	   if(activeApps.length==0){
      		   $scope.errorActive = true;

      	   } else {
      		   $scope.errorActive = false;
      	   }
      	   if(archiveApps.length==0){
      		   $scope.errorArchive = true;
      	   } else {
      		   $scope.errorArchive = false;

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
      	 		    	$scope.test.testResult = result.testResult;
      	 		    	if($scope.test.senario=='manual' || $scope.test.senario=='Manual' ) {
      	 		    		var s = $scope.result.test;
      	 		    		$scope.result = eval('(' + s + ')');
      	 		    		console.log($scope.result);
      	 		    		
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
      	/*$scope.isShow= function(){
      		var ele1 = angular.element($('#active'));
      		var ele2 = angular.element($('#archive'));
      		if(ele1.attr('class')=='active'){
      			ele1.removeClass('active');
      			ele2.addClass("active");
      		} else {
      			ele2.removeClass('active');
      			ele1.addClass("active");
      		}
      	}*/
      	  // $data.set($scope.test);
      	
      	 localStorageService.set('test',$scope.test);

         });
 

