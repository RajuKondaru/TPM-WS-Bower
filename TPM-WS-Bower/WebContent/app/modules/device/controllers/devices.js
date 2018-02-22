
angular.module('app')
 .controller('DevicesController', function ($scope, $http, localStorageService, $state) {

  	 $scope.test = localStorageService.get('test');
  	 $http({
  	        method : 'POST',
  	        url : './GetAllConnectedDevices',
  	        data :''
  		    }).then(function(success, status, headers, config) {
  		    	
  		    	if(success!=('null')){
  		    		//console.log(success.data);
  		    		localStorageService.set('devices',success.data);
  			    	$scope.devicesInfo = success.data;
  		    	} else {
  		    		$scope.error= "Devices Are Undermaintainence..!";
  		    		// console.log($scope.error);
  		    	}

  		    }, function(error, status, headers, config) {
  		            // called asynchronously if an error occurs
  		            // or server returns response with an error status.

  		    });


  	$scope.$watch(function(scope) { return scope.devicesInfo },
  			function(newValue, oldValue) {
  				$scope.devices = newValue;
  				if($scope.devices){
  					if($scope.devices!='null'){
  						//console.log($scope.devices);
  					}

  				}

  				//$scope.loading = false;

           	}
      );
  	
  	$scope.selectedDevices = {};
  	$scope.test.devices=[];
    $scope.submitDevices = function(testType) {
    	//console.log($scope.selectedDevices);
    	angular.forEach($scope.selectedDevices, function (selected, deviceId) {
    		
            if (selected) {
            	//console.log($scope.devices);
            	angular.forEach($scope.devices, function (device) {
                    if (device.DeviceUDID == deviceId) {
                    	device.vncStatus= false;
                    	device.status="Not started";
                      $scope.test.devices.push(device);
                    }
                });
              
            }
        });
    	//console.log($scope.test.devices);
    	localStorageService.set('test',$scope.test);
    	if(testType=='appium'){
    		$state.go("home.app.capabilities"); 
    	}else if(testType=='robotium'){
    		$state.go("home.app.execution"); 
    	} else {
    		$state.go("home.manual"); 
    	}
    		
    }
  	

   })