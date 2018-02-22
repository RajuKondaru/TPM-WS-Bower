
angular.module('app')
.controller('newTestController', function ( $scope,localStorageService ) {
	 $scope.test = localStorageService.get('test');
      })

