
  angular.module('app')
  .controller('logoutController', function($scope, $http,  $state, localStorageService){
	  $http({
			method: 'POST',
			url: './logout',
			headers: {'Content-Type': 'text/html'},
			data: {}
		}).then( function(success) {
			console.log(success);
			if(success.data=="falied") {
				 //$state.go("main.login"); 
			} else if(success.data=="success"){
				 //console.log(success.data);
				 localStorage.clear();
				 $state.go("main.login"); 
				
			}
			
		},function(error){
			console.log('Error message. ', error);
 
		});

  });
 

