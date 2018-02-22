
angular.module('app')
.controller('registerController', function( $scope, $http, $state){


		//console.log('forgotPasswordControllerFun');
		$scope.formSubmit = function () {
			var email = $scope.email;
			console.log("forgotPasswordControllerFun « User: ", email);
			var targetRequestPath = './forgot';
			var targetRequestParams = { 'email': email };
			var isAuthenticated = false;
			$http({
				method: 'POST',
				url: targetRequestPath,
				headers: {'Content-Type': 'application/json'},
				data: targetRequestParams
			}).then( function(success) {
				//console.log('Response Data : ', data);
				var response;
 		    	if(typeof success =='object') {
 		    		// It is JSON
 		    		response=success.data;
 		    	} else if(typeof success == 'string'){
 		    		// It is string
 		    		response=success;
 		    	}
				$scope.password = '';
				if(response == 'valid') {
					/* Successfully sent the password to your registered email.
					 * Your password reset email was sent - check your mail!*/
					isAuthenticated = true;
					$scope.error = '';
					$scope.email = '';
					console.log("Authentication Status : Pass « ", isAuthenticated);

					$scope.forgotMessage = "success";
					// template login with success message.
					//$location.path('/login');
					$state.go("main.login"); 
					/*$window.location.href = '../account/loginSucess';*/
				} else {
					/* That email address doesn't have an associated user account. Are you sure you've registered?
					 * Account does not exist | Can't find that email, sorry.
					 * Email or Mobile no. entered is not registered with us. */
					console.log("Authentication Status : Fail « ", isAuthenticated);
					$scope.error = "Entered Email is not registered with us.";

					/*  http://api.jquery.com/html/ */
					$('#errors').html($scope.error);
					$('#errors').addClass('form-group alert alert-danger');
					$('#errors').show();
					$scope.forgotMessage = "";
				}
			}, function(error){
				console.log('Error message. ', error);
			});
		};

  });

