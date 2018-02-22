angular.module('app')
.controller('TestCaseConfirmController', function($scope, $modalInstance, $location, localStorageService,$state) {
	
	 $scope.test = localStorageService.get('test');
	 // console.log($scope.testCase);
	
	  $scope.ok = function () {
	    $modalInstance.close($scope.test);
	    $state.go('home.app.writeTest');
	   
	  };
	  $scope.cancel = function () {
	    $modalInstance.dismiss('cancel');
	    $scope.test.type= "execution";
	    localStorageService.set('test',$scope.test);
	    $state.go('home.app.devices');
	  };
	  
	 

})