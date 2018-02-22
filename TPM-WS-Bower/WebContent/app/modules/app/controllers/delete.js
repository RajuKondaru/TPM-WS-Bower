
angular.module('app')
.controller('DeleteController', function ($scope,  $modal ) {

      	 var modalInstance = $modal.open({
      	   		templateUrl: 'AppDeleteConfirmModel.html',
         		    controller: 'AppDeleteConfirmController',
         		    backdrop: 'static'
         		});

      })
