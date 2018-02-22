
angular.module('app')
.controller('forgotPasswordController', function($scope, $http, $state, USERCONSTANTS){


   /*$scope.loadScript('http://localhost:8080/DemoAngularJs/js/fileuploadstyle.js', 'text/javascript', 'utf-8');*/

   $scope.passwordLength = USERCONSTANTS.PASSWORD_LENGTH;
   //console.log('PASSWORD_LENGTH : ', USERCONSTANTS.PASSWORD_LENGTH, "\t USERS_DOMAIN", USERCONSTANTS.USERS_DOMAIN);

   //console.log('registerControllerFun');
   $scope.formSubmit = function () {

     var userName = $scope.userName,
       email =  $scope.email,
       password = $scope.password,
       conformPassword = $scope.conformPassword,
       firstName = $scope.firstName,
       lastName = $scope.lastName;
     //console.log("User: ", userName, " Pass: ", password, "conformPassword :", conformPassword, " Email: ", email,	" F: ", firstName, " L: ", lastName);

     var targetRequestPath = './register';
     var targetRequestParamsREQ = { 'userName': userName, 'password': password,'conformPassword':conformPassword, 'email' : email, 'firstName': firstName, 'lastName': lastName };
     var isAuthenticated = false;
     $http({
       method: 'POST',
       url: targetRequestPath,
       headers: {'Content-Type': 'application/json'},
       data: targetRequestParamsREQ
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
       $scope.confirmPwd = '';

       if(response == 'success') {
         isAuthenticated = true;
         $scope.error = '';
         $scope.email = '';
         //console.log("Authentication Status : Pass « ", isAuthenticated);

         $scope.signupMessage == 'success';
         //console.log('registerControllerFun : scope signupMessage Vlaue = ', $scope.signupMessage);
         $state.go("main.login"); 
        // $location.path('/login');
       } else {
         //console.log("Authentication Status : Fail « ", isAuthenticated);
         /* This Email ID already Registered, Either use another Email ID or click on Forget password.*/
         $scope.error = "This Email ID already Registered.";
         /*  http://api.jquery.com/html/ */
         $('#errors').html($scope.error);
         $('#errors').addClass('form-group alert alert-danger');
         $('#errors').show();
       }
     },function(error){
       console.log('Error message. ', error);
     });
   };

  });
  

