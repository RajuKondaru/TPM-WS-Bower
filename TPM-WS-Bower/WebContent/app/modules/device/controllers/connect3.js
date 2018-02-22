
angular.module('app')

   .controller('ConnectController', function ($scope, localStorageService, $http, $interval, $modal, $ocLazyLoad)  {
	   
		   $ocLazyLoad.load(['./js/vnc/include/util.js']);
		   $ocLazyLoad.load(['./js/vnc/include/webutil.js']);
		   $ocLazyLoad.load(['./js/vnc/include/base64.js']);
		   $ocLazyLoad.load(['./js/vnc/include/websock.js']);
		   $ocLazyLoad.load(['./js/vnc/include/des.js']);
		   $ocLazyLoad.load(['./js/vnc/include/input.js']);
		   $ocLazyLoad.load(['./js/vnc/include/display.js']);
		   $ocLazyLoad.load(['./js/vnc/include/jsunzip.js']);
		   $ocLazyLoad.load(['./js/vnc/include/rfb.js']);
	 	 $scope.state = true;
	 	 $scope.test = localStorageService.get('test');
		 //console.log($scope.test);
		 $scope.loaderMessage="Initializing device..!"
		 $scope.vncStatus=false;
		 $scope.appInstallStatus= false;
		 $scope.installAUTAppStatus= false;
		 var packageName=null;
		 var promise;
		 $('#fn-btn').attr('disabled',true);
		 if($scope.test.devices.length==1){
		  		$scope.test.device=$scope.test.devices[0];
		  		/*$scope.test.devices=null;
		  		$scope.test.results=null;*/
		  	 }
		 var deviceId=$scope.test.device.DeviceUDID;
		 $scope.connect= false;
		 $scope.appiumlog='';
		 $scope.devicelog='';
		 $scope.$watch('test.testCase', function(oldVal,newVal) {
	    	if(oldVal!=newVal){
	    		var updatedData=JSON.stringify($scope.test.testCase);
	    		console.log(updatedData);
	    		if(!updatedData.includes('pending')){
	    			$scope.enable=true;
	    			$('#fn-btn').attr('disabled',false);
	    		} else {
	    			$('#fn-btn').attr('disabled',true);
				}
	    		//$scope.test.testCase = updatedData;
	    	}
	       
	     }, true);
		 
		 
		 $http({
		        method : 'POST',
		        url : './GetVNCConnection',
		        data :{'deviceId':deviceId,'orientation':'PORTRAIT'}
	     }).then(function(success, status, headers, config) {
	    	 console.log(success);
		    if(success.data=='Success'){
		    		
		    	$scope.vncStatus= true;
		    		
		    		if($scope.vncStatus && $scope.test.senario=='manual' && $scope.test.app.appType=='webapp' ){
		    		 $scope.loaderMessage="Initializing Browser..!";
		    		 $http({
					        method : 'POST',
					        url : './LaunchBrowser',
					        data :{'deviceId':deviceId,'test':$scope.test}
						    }).then(function(success, status, headers, config) {
						    	if(success.data=='Success'){
						    		$scope.message=true;
						    		
						    	} else {
						    		$scope.stop();
								}
					        	
						    }, function(error, status, headers, config) {
						            // called asynchronously if an error occurs
						            // or server returns response with an error status.
						    	$scope.stop();
						    });
		    		 
		    		
		    	} else if($scope.vncStatus && $scope.test.senario=='manual' && $scope.test.app.appType=='native' ){
		    		 $scope.loaderMessage="Installing App..!";
		    			$http({
					        method : 'POST',
					        url : './installApp',
					        data :{'deviceId':deviceId,'appId': $scope.test.app.id.toString() }
						    }).then(function(success, status, headers, config) {
						    	if(success.data=='Success'){
						    		$scope.appInstallStatus= true;
						    		
						    	}
						    	if($scope.appInstallStatus){
						    		$scope.loaderMessage="Launching Your App..!"
						    		 $http({
									        method : 'POST',
									        url : './LaunchApp',
									        data :{'deviceId':deviceId,'test':$scope.test, 'app': $scope.test.app}
										    }).then(function(success, status, headers, config) {
										    	if(success.data=='Success'){
										    		$scope.message = true;
										    		
										    	} else {
										    		$scope.stop();
												}
									        	
										    }, function(error, status, headers, config) {
										            // called asynchronously if an error occurs
										            // or server returns response with an error status.
										    	$scope.stop();
										    });
						    	}
						    }, function(error, status, headers, config) {
					            // called asynchronously if an error occurs
					            // or server returns response with an error status.
						    	$scope.stop();
						    });
		    	} 
		    	 
		    }	
	     }, function(error, status, headers, config) {
	            // called asynchronously if an error occurs
	            // or server returns response with an error status.
	    	 	$scope.stop();
	     });
		
		  
	 
			 $scope.$watch(function(scope) { return scope.message },
					function(newValue, oldValue) {
				 		
						$scope.connectStatus = newValue;
						//console.log($scope.connectStatus);
						if($scope.connectStatus){
							
					       //I change here
				        	/*jslint white: false */
					        /*global window, $, Util, RFB, */
				        	"use strict";
				        	var rfb;
				        	
				        	function passwordRequired(rfb) {
					            var msg;
					            msg = '<form onsubmit="return setPassword();"';
					            msg += '  style="margin-bottom: 0px">';
					            msg += 'Password Required: ';
					            msg += '<input type=password size=10 id="password_input" class="noVNC_status">';
					            msg += '<\/form>';
					            $D('noVNC_status_bar').setAttribute("class", "noVNC_status_warn");
					            $D('noVNC_status').innerHTML = msg;
					        }
					        function setPassword() {
					            rfb.sendPassword($D('password_input').value);
					            return false;
					        }
					        function sendCtrlAltDel() {
					            rfb.sendCtrlAltDel();
					          //  console.log("sendCtrlAltDel");
					            return false;
					        }
					        function updateState(rfb, state, oldstate, msg) {
					            var s, sb, cad, level;
					            console.log( state);
					            if ((oldstate === 'failed') && (state === 'disconnected')) {
					            	$scope.state = false;
					            }
					           
					            s = $D('noVNC_status');
					            sb = $D('noVNC_status_bar');
					            cad = $D('sendCtrlAltDelButton');
					            switch (state) {
					                case 'failed':       level = "error";  break;
					                case 'fatal':        level = "error";  break;
					                case 'normal':       level = "normal"; break;
					                case 'disconnected': level = "normal"; break;
					                case 'loaded':       level = "normal"; break;
					                default:             level = "warn";   break;
					            }
					
					            if (state === "normal") { cad.disabled = false; }
					            else                    { cad.disabled = true; }
					
					            if (typeof(msg) !== 'undefined') {
					                sb.setAttribute("class", "noVNC_status_" + level);
					                s.innerHTML = msg;
					            }
					        }
					        function getParameterByName(name, url) {
				                if (!url) {
				                  url = window.location.href;
				                  //console.log(url);
				                }
				                name = name.replace(/[\[\]]/g, "\\$&");
				              //  console.log(name);
				                var regex = new RegExp("[?&]" + name + "(=([^&#]*)|&|#|$)"),
				                    results = regex.exec(url);
				                if (!results) return null;
				                if (!results[2]) return '';
				                return decodeURIComponent(results[2].replace(/\+/g, " "));
				            }
					
					        (function () {
					            var host, port, password, path, token;
					
					            $D('sendCtrlAltDelButton').style.display = "inline";
					            $D('sendCtrlAltDelButton').onclick = sendCtrlAltDel;
					            
					           
					           // document.title = unescape(WebUtil.getQueryVar('title', 'noVNC'));
					            // By default, use the host and port of server that served this file
					            //host="172.16.100.26";
								//port="5901";
								host= $scope.test.device.Host;
								port= $scope.test.device.ForwardPort;
					            // host = WebUtil.getQueryVar('host', '172.16.100.59');
								//  port = WebUtil.getQueryVar('port', '5901');
					            // host = WebUtil.getQueryVar('host', window.location.hostname);
								// port = WebUtil.getQueryVar('port', window.location.port); 
					
					            // If a token variable is passed in, set the parameter in a cookie.
					            // This is used by nova-novncproxy.
					            token = WebUtil.getQueryVar('token', null);
					            if (token) {
					                WebUtil.createCookie('token', token, 1)
					            }
					
					            password = WebUtil.getQueryVar('password', '');
					            path = WebUtil.getQueryVar('path', 'websockify');
					
					            if ((!host) || (!port)) {
					                updateState('failed',
					                    "Must specify host and port in URL");
					                return;
					            }
					            var view_only = true;
					            if($scope.test.senario=='manual'){
					            	view_only = false;
					            	
					            	
					            }
					          //  console.log($scope.test.senario);
				            //	console.log('view_only ::'+view_only);
					            
					            $scope.rfb = new RFB({'target':       $D('noVNC_canvas'),
					                           'encrypt':      WebUtil.getQueryVar('encrypt',
					                                    (window.location.protocol === "https:")),
					                           'repeaterID':   WebUtil.getQueryVar('repeaterID', ''),
					                           'true_color':   WebUtil.getQueryVar('true_color', true),
					                           'local_cursor': WebUtil.getQueryVar('cursor', true),
					                           'shared':       WebUtil.getQueryVar('shared', true),
					                           'view_only':    WebUtil.getQueryVar('view_only', view_only),
					                           'updateState':  updateState,
					                           'onPasswordRequired':  passwordRequired});
					            $scope.rfb.connect(host, port, password, path);
					           
					            $scope.reConnect = function () {
					            	$scope.state=true;
					            	$scope.rfb.connect(host, port, password, path);
					            	
					   		 	}
					            
					        }());
					        if($scope.test.senario=='manual'){
					        	var countDownDate = new Date();
						        var countDate=countDownDate.getTime()+(60*1*1000);
						        var tick = function() {
						        	  // Get todays date and time
						              var now = new Date().getTime();
						              
						              // Find the distance between now an the count down date
						              var distance = countDate - now;
						              
						              // Time calculations for days, hours, minutes and seconds
						              var days = Math.floor(distance / (1000 * 60 * 60 * 24));
						              var hours = Math.floor((distance % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60));
						              var minutes = Math.floor((distance % (1000 * 60 * 60)) / (1000 * 60));
						              var seconds = Math.floor((distance % (1000 * 60)) / 1000);
						              if (distance < 0) {
						            	  $scope.stop();
						            	  $scope.clock =  'Your Session Expired' ;
						              } else {
						            	  $scope.clock =  'Session Expires in '+minutes + "m " + seconds + "s" ;
						              }
						            
						          }
						          tick();
						          promise = $interval(tick, 1000);
					        	
					        }
						} else {
						
						}
							
		         	}
		    );
			
		 	$scope.stop = function () {
		 		$scope.message = true;
		 		$scope.disconnectStatus = 'failed';
		 		try{
		 			$scope.rfb.disconnect();
		 		}catch(err){
		 			
		 		}
		 		
		
		 		$http({
			        method : 'POST',
			        url : './CloseVNCConnection',
			        data :{'deviceId':deviceId}
				    }).then(function(success, status, headers, config) {
				    	$scope.disconnectStatus= success.data;
				    //	console.log("Connection Closed ::"+$scope.disconnectStatus);
				    	if($scope.disconnectStatus=='Success'){
				    		if($scope.test.senario=='manual' && $scope.test.app.appType=='webapp' ){
				    	 		$interval.cancel(promise);
				    	 		$scope.clock ='Your Session Stopped';
				    	 		$http({
							        method : 'POST',
							        url : './BrowserCleanupActivity',
							        data :{'deviceId':deviceId}
								    }).then(function(success, status, headers, config) {
								    	$scope.cleanupStatus= success.data;
								    	if($scope.cleanupStatus=='Success'){
								    		localStorageService.set('test',$scope.test);
								    		var modalInstance = $modal.open({
						  	   		   		    templateUrl: 'testCompleteModel.html',
							   		   		    controller: 'ExecutionConfirmController',
							   		   		    backdrop: 'static'
							   		   		});
								    	}
								    	
								    }, function(error, status, headers, config) {
							            // called asynchronously if an error occurs
							            // or server returns response with an error status.
							    	
								    });
				    		} else {
				    			if($scope.test.senario=='manual' && $scope.test.app.appType=='native' ){
				    				$interval.cancel(promise);
					    	 		$scope.clock ='Your Session Stopped';
					    	 	}
				    			
				    			$http({
							        method : 'POST',
							        url : './AppCleanupActivity',
							        data :{'deviceId':deviceId, 'packageName': $scope.test.app.appPackageName}
								    }).then(function(success, status, headers, config) {
								    	$scope.cleanupStatus= success.data;
								    	if($scope.cleanupStatus=='Success'){
								    		localStorageService.set('test',$scope.test);
								    		var modalInstance = $modal.open({
						  	   		   		    templateUrl: 'testCompleteModel.html',
							   		   		    controller: 'ExecutionConfirmController',
							   		   		    backdrop: 'static'
							   		   		});
								    	}
								    	
								    }, function(error, status, headers, config) {
							            // called asynchronously if an error occurs
							            // or server returns response with an error status.
							    	
								    });
				    		}
				    		
				    	} else {
							
						}
				    	
				    	
				    }, function(error, status, headers, config) {
				            // called asynchronously if an error occurs
				            // or server returns response with an error status.
				    	
				    });
		      }
		 	
							    	 
	 
   });
