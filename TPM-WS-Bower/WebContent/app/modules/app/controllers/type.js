
angular.module('app')
.controller('typeController', function ( $scope, $stateParams, localStorageService) {
      	 $scope.test=localStorageService.get('test');
      	 var senario = $stateParams.senario;
      	 $scope.test.senario=senario;
      	localStorageService.set('test',$scope.test);
  })
