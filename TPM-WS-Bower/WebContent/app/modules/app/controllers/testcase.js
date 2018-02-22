angular.module('app')

   .controller('TestCaseController', function ($scope, localStorageService, $http, $interval, $modal) {
	   $scope.test = localStorageService.get('test');
	   $('#fn-btn').attr('disabled',true);
	   $scope.$watch('test.testCase', function(oldVal,newVal) {
	      	if(oldVal!=newVal){
	      		var updatedData=JSON.stringify($scope.test.testCase);
	      		if(!updatedData.includes('pending')){
	      			$scope.enable=true;
	      			$('#fn-btn').attr('disabled',false);
	      		} else {
	      			$('#fn-btn').attr('disabled',true);
	  			}
	      	}

	       }, true);
   });