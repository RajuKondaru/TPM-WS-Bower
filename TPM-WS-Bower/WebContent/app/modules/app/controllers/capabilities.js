
angular.module('app')
.controller('CapabilitiesController',  function ( $scope, $stateParams, $state,PagerService, localStorageService,$filter) {
      	 $scope.test = localStorageService.get('test');
      	 $scope.multi=false;
      	//console.log($scope.test);
      	var devices=[];
      	$scope.test.testDevices=[];
      	if($scope.test.devices.length!=0){
      		if($scope.test.devices.length>1){
      			 $scope.multi=true;
      		} else {
      			$scope.multi=false;
      			//$scope.test.tpAppiumPort= genRand();
			}
      		
      		 angular.forEach($scope.test.devices, function (device) {
      			device.tpAppiumPort= genRand();
      			devices.push(device);
      		 });
      			$scope.test.testDevices=devices;
      	}
      	
      	localStorageService.set('test',$scope.test);

      });

function genRand() {
    return Math.floor(Math.random()*8999+1000);
 };

     