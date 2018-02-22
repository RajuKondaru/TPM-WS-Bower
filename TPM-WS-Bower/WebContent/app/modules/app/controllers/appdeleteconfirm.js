
angular.module('app')
.controller('AppDeleteConfirmController', function($scope, $modalInstance, $stateParams,$http,   localStorageService,$state) {

      	$scope.error=false;
      	var appid =  $stateParams.id;
      	 $scope.apps=  localStorageService.get('app');

      	 angular.forEach($scope.apps,function(avalue,index){
      		   if(avalue.id==appid){
      			   $scope.app=avalue;
      		   }
      	 });


      	  $scope.deleteApp = function () {
      	    $modalInstance.close($scope.test);
      	    $http({
                  method : 'POST',
                  url : './DeleteApp',
                  data :{
                     'appid' : appid
                  }
         	    }).then(function(success, status, headers, config) {
         	    	if(success==='Success'){
         	    		$state.go('home.dashboard');
         	    	} else {
         	    		$scope.error=true;
         	    	}

         	    }, function(error, status, headers, config) {
         	            // called asynchronously if an error occurs
         	            // or server returns response with an error status.

         	    });
      	  };
      	  $scope.cancel = function () {
      	    $modalInstance.dismiss('cancel');
      	    $state.go('home.dashboard');
      	  };



      })
