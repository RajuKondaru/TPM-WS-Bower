
  angular.module('app')
  .controller('loginController', function($scope, $http,  $state, localStorageService){
	  	
		if( $scope.forgotMessage == 'success' ) {
			var msg = "We've sent you an email with instructions on how to reset your password.";
			$('#resentPassword').html( msg );
			$('#resentPassword').addClass('form-group alert alert-success');
			/*$().role="alert"*/
			$('#resentPassword').show();
		}
		if( $scope.signupMessage == 'success' ) {
			var msg = "Your Account Successfully Created.";
			$('#resentPassword').html( msg );
			$('#resentPassword').addClass('form-group alert alert-success');
			/*$().role="alert"*/
			$('#resentPassword').show();
		}

		$scope.formSubmit = function () {
			var username = $scope.email;
			var password = $scope.password;
			console.log("username: ", username, " password: ", password);
		  
			$scope.dataLoading = true;
			if( $scope.forgotMessage != null && $scope.forgotMessage != 'undefined' ) {
				$('#resentPassword').html( '' );
				$('#resentPassword').addClass('');
				$('#resentPassword').hide();
			}

			for (var prop in $scope) {
				if (prop.substring(0,1) !== '$') {
					delete $scope[prop];
				}
			}

			

			var isAuthenticated = false;
			$http({
				method: 'POST',
				url: './account',
				headers: {'Content-Type': 'text/html'},
				data: {'username':username,'password':password}
			}).then( function(success) {
				console.log(success);
				var response;
 		    	if(typeof success =='object') {
 		    		// It is JSON
 		    		response=success.data;
 		    	} else if(typeof success == 'string'){
 		    		// It is string
 		    		response=JSON.parse(success);      ;
 		    	}
 		    	if(success.data=="failed") {
					$scope.password = '';
					console.log("Authentication Status : Fail « ", isAuthenticated);
					$scope.error = "Invalid email or password!";
					$('#errors').html($scope.error);
					$('#errors').addClass('form-group alert alert-danger');
					$scope.dataLoading = false;
					$('#errors').show();
				} else if(success.data=="already logged in") {
					$scope.password = '';
					console.log("Authentication Status : Fail « ", isAuthenticated);
					$scope.error = "Account is already signed somewhere";
					$('#errors').html($scope.error);
					$('#errors').addClass('form-group alert alert-danger');
					$scope.dataLoading = false;
					$('#errors').show();
				} else{
					localStorageService.set('user', response);
					isAuthenticated = true;
					console.log("Authentication Status : Pass « ", isAuthenticated);
					//$window.location.href = './Dashboard';
					//$location.path('/home/dashboard');
					 $state.go("home.dashboard"); 
				}
			},function(error){
				console.log('Error message. ', error);
        //$location.path('/home/dashboard');
				$scope.dataLoading = false;
			});
		};

  });
 

