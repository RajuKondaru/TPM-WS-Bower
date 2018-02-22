
angular.module('app')
.controller('TestController', function ( $scope, $stateParams, localStorageService, $filter,PagerService) {
      	 $scope.test=localStorageService.get('test');
      	 var senario = $stateParams.senario;
      	 $scope.test.senario=senario;
      	console.log($scope.test.senario);
      	$scope.totalAutoResults=[];
        if(typeof  $scope.test.results != 'undefined'){
        	$scope.filterResultwrtappid = $filter('filter')( $scope.test.results, {appId: $scope.test.app.id});
          	if($scope.filterResultwrtappid.length!=0){
          		//console.log($scope.filterResultwrtappid);
          		$scope.filterResultwrttesttype = $filter('filter')( $scope.filterResultwrtappid, {testSenario: senario});
          		//console.log($scope.filterResultwrttesttype);
          		if($scope.filterResultwrttesttype.length!=0){
          			var vm = this;
                    vm.dummyItems =$scope.filterResultwrttesttype; // dummy array of items to be paged
                    vm.pager = {};
                    vm.setPage = setPage;
                    initController();
                    function initController() {
                        // initialize to page 1
                        vm.setPage(1);
                    }
                    function setPage(page) {
                        if (page < 1 || page > vm.pager.totalPages) {
                            return;
                        }
                        // get pager object from service
                        vm.pager = PagerService.GetPager(vm.dummyItems.length, page);
                        // get current page of items
                        $scope.totalAutoResults = vm.dummyItems.slice(vm.pager.startIndex, vm.pager.endIndex + 1);

                    }
                    $scope.reportError = false;
                    $scope.vm=vm;
          		} else {
             		 $scope.reportError = true;
          		}
          		
          		
          
          		
          		 
          	} else {
          		 $scope.reportError = true;
    		}
        } else {
        	 $scope.reportError = true;
		}
      	
      	 
      

        
      	 
      	 
      	localStorageService.set('test',$scope.test);


      })
