angular.module('app')
.controller('BreadController', function($scope, localStorageService) {
	 $scope.test = localStorageService.get('test');
});