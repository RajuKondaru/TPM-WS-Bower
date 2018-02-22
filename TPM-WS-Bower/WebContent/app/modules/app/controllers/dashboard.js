
angular.module('app')
.controller('DashboardController',function ($scope, $http, $state,localStorageService ) {
        	var user = {};
        	if (localStorageService.get("user")) {
        		$scope.user = localStorageService.get("user");
          		//console.log($scope.user);
        	}

        	$http({
                method : 'POST',
                url : './UpdateDashboard',
                data :''
      	    }).then(function(success, status, headers, config) {
        	    	var jsonData;
        	    	if(typeof success =='object') {
        	    	  // It is JSON
        	    		jsonData=success.data;
        	    		//console.log(jsonData);
        	    	}
        	    	var appsInfo = jsonData.apps;
        	    	var devicesInfo = jsonData.devices;
            		var aps = JSON.parse(appsInfo);
            		var dvs = JSON.parse(devicesInfo);
            		var resultsInfo = jsonData["results"];
            		var rsi = eval(resultsInfo);
            		//console.log(rsi);
            		/*$results.set(rsi);
            		$app.set(aps);
            		$device.set(dvs);*/
            		localStorageService.set('results',rsi);
            		localStorageService.set('app',aps);
            		localStorageService.set('device',dvs);
            		localStorageService.set('test',{});
            		$state.go('home.updateinfo');
        	    	//$location.path('home.updateinfo');
      	    },function(error, status, headers, config) {
      	            // called asynchronously if an error occurs
      	            // or server returns response with an error status.

      	    });

      })
  

