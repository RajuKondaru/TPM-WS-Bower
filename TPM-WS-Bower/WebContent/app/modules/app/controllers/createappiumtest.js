
angular.module('app')
.controller('createAppiumTestController', function ( $scope, $stateParams, $http, localStorageService, $modal) {
		
      	 var type=$stateParams.type;
      	 $scope.test = localStorageService.get('test');
      	 if(type!='undefined'){
      		 $scope.test.type=type;
      	 }
      	 $scope.save = function(testName, app){
      		 $scope.test.name=testName;
      		 $http({
      		        method : 'POST',
      		        url : './saveAppiumTest',
      		        data :{'testName':testName, 'app':app}
      	    }).then(function(success, status, headers, config) {
      	    	var response;
      		    	if(typeof success =='object') {
      		    		// It is JSON
      		    		response=success.data;
      		    	} else if(typeof success == 'string'){
      		    		// It is string
      		    		response=success;
      		    	}
      	    	if(response=="Success"){
      	    		 //$data.set($scope.test);
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
      		localStorageService.set('test',$scope.test);

      	 }





      })
