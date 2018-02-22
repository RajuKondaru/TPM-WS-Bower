
angular.module('app')
.controller('TrashController', function ($scope, $http, localStorageService, $stateParams,$state) {
      		 var appid =  $stateParams.id;
      		 $http({
      	            method : 'POST',
      	            url : './MoveToArchiveApp',
      	            data :{
      	                   'appid' : appid,
      	                   'status' : "Archive"
      	               }
      	   	    }).then(function(success, status, headers, config) {
      	   	    	$state.go('home.dashboard');

      	   	    },function(error, status, headers, config) {
      	   	            // called asynchronously if an error occurs
      	   	            // or server returns response with an error status.
      	   	    	console.log('Error');

      	   	    });


      })

