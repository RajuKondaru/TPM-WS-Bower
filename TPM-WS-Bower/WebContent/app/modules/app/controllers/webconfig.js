
angular.module('app')
.controller('addWebConfigController', function ($scope, $http, $app, $state) {
          $scope.submitForm = function() {
              $http({
                  method : 'POST',
                  url : './SaveWebApp',
                  data :{
                      'url' : $scope.weburl ,
                      'name' : $scope.webname ,
                      'version' : $scope.webversion,
                      'iconurl' : $scope.weburl+'/favicon.ico',
                      'apptype' :'webapp'
                  }
              }).then(function(success, status, headers, config) {
              	  var response;
      		    	if(typeof success =='object') {
      		    		// It is JSON
      		    		response=success.data;
      		    	} else if(typeof success == 'string'){
      		    		// It is string
      		    		response=success;
      		    	}
              	 if(status==200){
              		 $app.set(response);
              		 $state.go('home.dashboard');
              	 }


              },function(error, status, headers, config) {
                      // called asynchronously if an error occurs
                      // or server returns response with an error status.

              });
      	};

       })