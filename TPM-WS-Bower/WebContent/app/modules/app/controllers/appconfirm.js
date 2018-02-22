
angular.module('app')
.controller('AppConfirmController', function($scope, $modalInstance, localStorageService,$state) {

      	 $scope.test = localStorageService.get('test');

      	  $scope.ok = function () {
      	    $modalInstance.close($scope.test);
      	    $state.go('home.dashboard');
      	  };
      	  $scope.cancel = function () {

      	    $modalInstance.dismiss('cancel');
      	    $('#appFrom')[0].reset();

      	  };



      })