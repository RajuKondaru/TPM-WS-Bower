
angular.module('app')

.controller('TestCaseConfirmController', function($scope, $modalInstance, $data, $state) {

	 $scope.test = $data.get();
	 // console.log($scope.testCase);

	  $scope.ok = function () {
	    $modalInstance.close($scope.test);
	    $state.go('home.writeTest');
	    //$location.path('/writeTest');
	  };
	  $scope.cancel = function () {
	    $modalInstance.dismiss('cancel');
	    $scope.test.type= "execution";
	    $data.set($scope.test);
	    $state.go('home.selectdevice');
	    //$location.path('/selectdevice');
	  };



})