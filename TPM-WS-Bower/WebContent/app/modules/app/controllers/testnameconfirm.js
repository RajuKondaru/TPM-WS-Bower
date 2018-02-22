
angular.module('app')
.controller('TestNameComformController', function($scope, $modalInstance,  localStorageService,$state) {

      	$scope.test = localStorageService.get('test');
      	  $scope.ok = function () {
      	    $modalInstance.dismiss('cancel');
      	    $state.go('home.app.devices');
      	  };



      })