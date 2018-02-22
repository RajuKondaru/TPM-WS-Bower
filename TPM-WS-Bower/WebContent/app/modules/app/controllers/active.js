
angular.module('app')
 .controller('ActiveController', function ($scope, $http, $stateParams, $state) {

      	 var appid =  $stateParams.id;
      	 $http({
                  method : 'POST',
                  url : './MoveToActiveApp',
                  data :{
                     'appid' : appid,
                     'status' : "Active"
                 }
         	    }).then(function(success, status, headers, config) {
         	    	$state.go('home.dashboard');
         	    },function(error, status, headers, config) {
         	            // called asynchronously if an error occurs
         	            // or server returns response with an error status.

         	    });


      })