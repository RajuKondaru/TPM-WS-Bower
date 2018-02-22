
angular.module('app')
.controller('UserController', function ( $scope, $stateParams, localStorageService) {
      	 $scope.user=localStorageService.get('user');
      	
  })
