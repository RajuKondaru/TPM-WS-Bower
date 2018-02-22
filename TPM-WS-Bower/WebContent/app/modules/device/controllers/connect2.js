
angular.module('app')

   .controller('ConnectController', function ($scope, localStorageService, $http, $interval, $modal, $ocLazyLoad, VncService) {
	   
	 VncService.init();
	 $scope.message=false;
   	 $scope.state = true;
   	 $scope.test = localStorageService.get('test');
  	 //console.log($scope.test);
  	 $scope.loaderMessage="Initializing device..!"
  	 $scope.vncStatus=false;
  	 $scope.appInstallStatus= false;
  
  	 var packageName=null;
  	 var promise;
  	 if($scope.test.devices.length==1){
  		$scope.test.device=$scope.test.devices[0];
  		/*$scope.test.devices=null;
  		$scope.test.results=null;*/
  	 }
  	 var deviceId=$scope.test.device.DeviceUDID;
  	 //console.log($scope.device);
  	 $scope.connect= false;

  	 $http({
  	        method : 'POST',
  	        url : './GetVNCConnection',
  	        data :{'deviceId':deviceId,'orientation':'PORTRAIT'}
       }).then(function(success, status, headers, config) {
      	 console.log(success.data);
  	    if(success.data=='Success'){

  	    	$scope.vncStatus= true;

  	    		if($scope.vncStatus && $scope.test.senario=='manual' && $scope.test.app.appType=='webapp' ){
  	    		 $scope.loaderMessage="Initializing Browser..!";
  	    		 $http({
  				        method : 'POST',
  				        url : './LaunchBrowser',
  				        data :{'deviceId':deviceId,'test':$scope.test}
  					    }).then(function(success, status, headers, config) {
  					    	if(success.data=='Success'){
  					    		$scope.message=true;

  					    	} else {
  					    		console.log(success.data);
  					    		$scope.test.stop();
  							}

  					    }, function(error, status, headers, config) {
  					            // called asynchronously if an error occurs
  					            // or server returns response with an error status.
  					    	console.log(error);
  					    	$scope.test.stop();
  					    });


  	    	} else if($scope.vncStatus && $scope.test.senario=='manual' && $scope.test.app.appType=='native' ){
  	    		 $scope.loaderMessage="Installing App..!";
  	    			$http({
  				        method : 'POST',
  				        url : './installApp',
  				        data :{'deviceId':deviceId,'appId': $scope.test.app.id.toString() }
  					    }).then(function(success, status, headers, config) {
  					    	if(success.data=='Success'){
  					    		$scope.appInstallStatus= true;

  					    	}
  					    	if($scope.appInstallStatus){
  					    		$scope.loaderMessage="Connecting Device..!"
  					    		 $http({
  								        method : 'POST',
  								        url : './LaunchApp',
  								        data :{'deviceId':deviceId,'test':$scope.test, 'app': $scope.test.app}
  									    }).then(function(success, status, headers, config) {
  									    	if(success.data=='Success'){
  									    			$scope.message = true;
  									    	} else {
  									    		console.log(success.data);
  									    		$scope.test.stop();
  											}

  									    }, function(error, status, headers, config) {
  									            // called asynchronously if an error occurs
  									            // or server returns response with an error status.
  									    	console.log(error);
  									    	$scope.test.stop();
  									    });
  					    	}
  					    }, function(error, status, headers, config) {
  				            // called asynchronously if an error occurs
  				            // or server returns response with an error status.
  					    	console.log(error);
  					    	$scope.test.stop();
  					    });
  	    	} 

  	    } else {
  	    	$scope.loaderMessage="Something went wrong! Please try again."
  	    }
       }, function(error, status, headers, config) {
              // called asynchronously if an error occurs
              // or server returns response with an error status.
    	   console.log(error);
    	   $scope.test.stop();
       });
 
  	 $scope.$watch(function(scope) { return scope.message },
  				function(newValue, oldValue) {
  					$scope.connectStatus = newValue;
  				
  					if($scope.connectStatus===true){
  						VncService.start($scope.test);
  						$scope.state= VncService.state;
  				        if($scope.test.senario=='manual'){
  				        	var countDownDate = new Date();
  					        var countDate=countDownDate.getTime()+(60*1000);
  					        var session = true;
  					        var tick = function() {
  					        	  // Get todays date and time
  					              var now = new Date().getTime();

  					              // Find the distance between now an the count down date
  					              var distance = countDate - now;

  					              // Time calculations for days, hours, minutes and seconds
  					              var days = Math.floor(distance / (1000 * 60 * 60 * 24));
  					              var hours = Math.floor((distance % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60));
  					              var minutes = Math.floor((distance % (1000 * 60 * 60)) / (1000 * 60));
  					              var seconds = Math.floor((distance % (1000 * 60)) / 1000);
  					              if (distance < 0 && session == true) {
  					            	  $scope.test.stop();
  					            	  $scope.clock =  'Your Session Expired' ;
  					            	  session = false;
  					              } else if (distance > 0) { 
  					            	  $scope.clock =  'Session Expires in '+minutes + "m " + seconds + "s" ;
  					              }

  					          }
  					          tick();
  					          promise = $interval(tick, 1000);

  				        }
  					} else {

  					}

  	         	}
  	    );
  	$scope.test.reConnect = function () {
  		VncService.restart();
  	}
  	 	$scope.test.stop = function () {
  	 		$scope.disconnectStatus = 'failed';
  	 		try{
  	 			VncService.stop();
  	 		}catch(err){

  	 		}
  	 		$scope.message = false;

  	 		$http({
  		        method : 'POST',
  		        url : './CloseVNCConnection',
  		        data :{'deviceId':deviceId}
  			    }).then(function(success, status, headers, config) {
  			    	$scope.disconnectStatus= success.data;
  			    //	console.log("Connection Closed ::"+$scope.disconnectStatus);
  			    	if($scope.disconnectStatus=='Success'){
  			    		if($scope.test.senario=='manual' && $scope.test.app.appType=='webapp' ){
  			    	 		$interval.cancel(promise);
  			    	 		//$scope.clock ='Your Session Stopped';
  			    	 		$http({
  						        method : 'POST',
  						        url : './BrowserCleanupActivity',
  						        data :{'deviceId':deviceId}
  							    }).then(function(success, status, headers, config) {
  							    	$scope.cleanupStatus= success.data;
  							    	if($scope.cleanupStatus=='Success'){
  							    		var modalInstance = $modal.open({
  					  	   		   		    templateUrl: 'testCompleteModel.html',
  						   		   		    controller: 'ExecutionConfirmController',
  						   		   		    backdrop: 'static'
  						   		   		});
  							    	}

  							    }, function(error, status, headers, config) {
  						            // called asynchronously if an error occurs
  						            // or server returns response with an error status.

  							    });
  			    		} else {
  			    			if($scope.test.senario=='manual' && $scope.test.app.appType=='native' ){
  			    				$interval.cancel(promise);
  				    	 		//$scope.clock ='Your Session Stopped';
  				    	 	}

  			    			$http({
  						        method : 'POST',
  						        url : './AppCleanupActivity',
  						        data :{'deviceId':deviceId, 'packageName': $scope.test.app.appPackageName}
  							    }).then(function(success, status, headers, config) {
  							    	$scope.cleanupStatus= success.data;
  							    	if($scope.cleanupStatus=='Success'){
  							    		var modalInstance = $modal.open({
  					  	   		   		    templateUrl: 'testCompleteModel.html',
  						   		   		    controller: 'ExecutionConfirmController',
  						   		   		    backdrop: 'static'
  						   		   		});
  							    	}

  							    }, function(error, status, headers, config) {
  						            // called asynchronously if an error occurs
  						            // or server returns response with an error status.

  							    });
  			    		}

  			    	} else {

  					}


  			    }, function(error, status, headers, config) {
  			            // called asynchronously if an error occurs
  			            // or server returns response with an error status.

  			    });
  	      }
  	  localStorageService.set('test', $scope.test);
  	  $ocLazyLoad.load(['app/modules/exe/controllers/execonfirm.js']);

   });
