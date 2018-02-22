
angular.module('app')
 
.controller('TestCaseResultController', function($scope, $http,  logHandelService,localStorageService ) {
/*	var kendo;
	$ocLazyLoad.load(['http://kendo.cdn.telerik.com/2016.3.914/js/kendo.all.min.js']).then(function() {
        kendo = $injector.get('kendo');
    });
*/
	$scope.serverLog = false;
	$scope.deviceLog = true;
	$scope.testLog = false;
	$scope.test = localStorageService.get('test');
	$scope.test.testResult = $scope.test.result.testResult;
	console.log($scope.test.testResult );
	$scope.generatePDF = function() {
		kendo.drawing.drawDOM($("#gen-rep")).then(function(group) {
			// (Optional) set PDF arguments, see the "PDF options" section below
			  group.options.set("pdf", {
				  title:"TestPace",
				  author:"Raju.Kondaru", subject:"Consolidate Report",
			      margin: {
			          left   : "20mm",
			          top    : "40mm",
			          right  : "20mm",
			          bottom : "40mm"
			      }
			  });
		    kendo.drawing.pdf.saveAs(group, $scope.test.name+".pdf");
		  });
		}
	$scope.isShow= function(log) {
		
		if(log=='deviceLog'){
			$scope.serverLog = false;
			$scope.deviceLog = true;
			$scope.testLog = false;
			
		} else if(log=='serverLog'){
			$scope.serverLog = true;
			$scope.deviceLog = false;
			$scope.testLog = false;
		} else if(log=='testLog'){
			
			$scope.serverLog = false;
			$scope.deviceLog = false;
			$scope.testLog = true;
			
		}
	}
	/*$scope.isShow= function() {
		return ($scope.test.senario=='automation' || $scope.test.senario=='Automation') ? true : false;
	}*/

	if($scope.test.senario=='automation' || $scope.test.senario=='Automation'){
		/*if($scope.test.type.toLowerCase()=='robotium' ) {*/
			//var URI = document.location.origin+'/LogStream?testName='+$scope.test.name+'&logType=devicelog';
			var URI = document.location.origin+'/TestPaceMobile/LogStream?testName='+$scope.test.name+'&logType=devicelog&deviceId='+$scope.test.device.DeviceUDID;

			 logHandelService.async(URI).then(function(result) {
				// console.log(result);
				 $scope.devicelogs = result.slice(0, 20);
				 $scope.getMoreDeviceLogs  = function () {
					 $scope.devicelogs = result.slice(0, $scope.devicelogs.length + 20);
				  }
			  });



			/*$scope.devicelogs = $scope.test.result.logs.devicelog.slice(0, 5);
		      $scope.getMoreRobotiumLogs = function () {
		          $scope.devicelogs = $scope.test.result.logs.devicelog.slice(0, $scope.devicelogs.length + 5);
		      }*/
		/*}*/
		if($scope.test.type=='appium' || $scope.test.type=='Appium'  ) {

			//var URI = document.location.origin+'/LogStream?testName='+$scope.test.name+'&logType=appiumLog';
			var URI = document.location.origin+'/TestPaceMobile/LogStream?testName='+$scope.test.name+'&logType=appiumLog&deviceId='+$scope.test.device.DeviceUDID;
			 logHandelService.async(URI).then(function(result) {
				 $scope.appiumLogs = result.slice(0, 20);
				 $scope.getMoreAppiumLogs  = function () {
					 $scope.appiumLogs = result.slice(0,  $scope.appiumLogs.length + 20);
				  }
			  });


			/*$scope.appiumLogs = $scope.test.result.logs.appiumLog.slice(0, 5);
		      $scope.getMoreAppiumLogs = function () {
		          $scope.appiumLogs = $scope.test.result.logs.appiumLog.slice(0, $scope.appiumLogs.length + 5);
		      }*/
		}
		if($scope.test.type=='robotium' || $scope.test.type=='Robotium' ) {
			//var URI = document.location.origin+'/LogStream?testName='+$scope.test.name+'&logType=testLogs';
			var URI = document.location.origin+'/TestPaceMobile/LogStream?testName='+$scope.test.name+'&logType=testLogs&deviceId='+$scope.test.device.DeviceUDID;

			 logHandelService.async(URI).then(function(result) {
				
			/*	 $scope.testLogs = result.slice(0, 20);
				 $scope.getMoreRoboTestLogs  = function () {
					 var testLogStr = result.slice(0,  $scope.testLogs.length + 20);
					 var testLog=eval('(' + 'testLogStr' + ')');
					 var tlog=[];
					 for (var key in testLog) {
							if (testLog.hasOwnProperty(key)) {
								var value = testLog[key];
								var log={};
								log.name=key;
								log.value=testLog[key];

								tlog.push(log);

							}
						}

					 $scope.testLogs=tlog;
				  }*/

				var tlog=[];
				for (var i = 0; i < result.length; i++) {
					var testLog=result[i];
					for (var key in testLog) {
						if (testLog.hasOwnProperty(key)) {
							var value = testLog[key];
							var log={};
							var logs=[];
							log.name=key.substr(key.lastIndexOf(".")+1,key.length);
							var eachTestResult;
							value=value.replace(/[\][]/g, '');
							if(value.includes('Failure')){
								eachTestResult='Failed';
							} else if(value.includes('OK')){
								eachTestResult='Passed';
							} else if(value.includes('Exception')){
								eachTestResult='Error';
							}

							logs=value.split(",");
							log.value=logs;
							log.result=eachTestResult;
							tlog.push(log);

						}
					}
				}


				 $scope.testLogs=tlog;
			  });
		}

	}
	localStorageService.set('test', $scope.test);

})