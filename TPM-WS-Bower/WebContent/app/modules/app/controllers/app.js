
angular.module('app')
.controller('AppController',  function ( $scope, $stateParams, $state,PagerService, localStorageService,$filter) {
      	 $scope.test = localStorageService.get('test');
      	 var appid =  $stateParams.id;
      	// console.log(appid);
      	 $scope.apps=  localStorageService.get('app');
      	 $scope.currentApp = $scope.apps.filter(function( obj ) {
      		  return obj.id == appid;
      		});
        $scope.test.app= $scope.currentApp[0];
        //console.log( $scope.test.app);
        $scope.totalResults=[];
        if(typeof  $scope.test.results != 'undefined'){
        	$scope.filterResult = $filter('filter')( $scope.test.results, {appId: appid});
        	if($scope.filterResult.length!=0){
          		var vm = this;
                vm.dummyItems =$scope.filterResult; // dummy array of items to be paged
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
                    $scope.totalResults = vm.dummyItems.slice(vm.pager.startIndex, vm.pager.endIndex + 1);

                }
          
          		
          		 $scope.reportError = false;
          	} else {
          		 $scope.reportError = true;
    		}
          	 
          

            $scope.vm=vm;
          	//console.log($scope.vm);
        } else {
        	
        	 $scope.reportError = true;
		}
      	
      	
      	localStorageService.set('test',$scope.test);

      })
