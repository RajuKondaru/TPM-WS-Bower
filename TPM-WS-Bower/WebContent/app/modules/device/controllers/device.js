
angular.module('app')

   .controller('DeviceController', function ($scope, localStorageService, $http, $stateParams, $state, $ocLazyLoad, $modal, $timeout) {
	   $ocLazyLoad.load(['./js/vnc/include/util.js']);
	   $('.closebtn').css("display", "block");
	    $('#myNav').css("display", "none");
   	 	$scope.test = localStorageService.get('test');
   	 	$scope.user=localStorageService.get('user');
   	 	//console.log($scope.test);
   	 	$scope.testcaseCompleted="0";
   	 	$scope.isResult = false;
	   	$scope.switchView= function(device) {
	   		console.log(device);
	   		if(device.vncStatus== true){
	   			$('#myNav').css("display", "block");
		   		$('#deviceView').css("display", "none");
		   		$('#message').css("display", "block");
	 			$("#message").html("<div><font style='font-family: \"Lato\", sans-serif;  color: #818181; font-size: 24px;'> Please wait. Connecting Device  </font> <img ng-if='progress' src='data:image/gif;base64,R0lGODlhEAAQAPIAAP///wAAAMLCwkJCQgAAAGJiYoKCgpKSkiH/C05FVFNDQVBFMi4wAwEAAAAh/hpDcmVhdGVkIHdpdGggYWpheGxvYWQuaW5mbwAh+QQJCgAAACwAAAAAEAAQAAADMwi63P4wyklrE2MIOggZnAdOmGYJRbExwroUmcG2LmDEwnHQLVsYOd2mBzkYDAdKa+dIAAAh+QQJCgAAACwAAAAAEAAQAAADNAi63P5OjCEgG4QMu7DmikRxQlFUYDEZIGBMRVsaqHwctXXf7WEYB4Ag1xjihkMZsiUkKhIAIfkECQoAAAAsAAAAABAAEAAAAzYIujIjK8pByJDMlFYvBoVjHA70GU7xSUJhmKtwHPAKzLO9HMaoKwJZ7Rf8AYPDDzKpZBqfvwQAIfkECQoAAAAsAAAAABAAEAAAAzMIumIlK8oyhpHsnFZfhYumCYUhDAQxRIdhHBGqRoKw0R8DYlJd8z0fMDgsGo/IpHI5TAAAIfkECQoAAAAsAAAAABAAEAAAAzIIunInK0rnZBTwGPNMgQwmdsNgXGJUlIWEuR5oWUIpz8pAEAMe6TwfwyYsGo/IpFKSAAAh+QQJCgAAACwAAAAAEAAQAAADMwi6IMKQORfjdOe82p4wGccc4CEuQradylesojEMBgsUc2G7sDX3lQGBMLAJibufbSlKAAAh+QQJCgAAACwAAAAAEAAQAAADMgi63P7wCRHZnFVdmgHu2nFwlWCI3WGc3TSWhUFGxTAUkGCbtgENBMJAEJsxgMLWzpEAACH5BAkKAAAALAAAAAAQABAAAAMyCLrc/jDKSatlQtScKdceCAjDII7HcQ4EMTCpyrCuUBjCYRgHVtqlAiB1YhiCnlsRkAAAOwAAAAAAAAAAAA==' /></div>");
		   		if(device){
		   			
		   			try{
		   			//I change here
			        	/*jslint white: false */
				        /*global window, $, Util, RFB, */
			        	"use strict";
			        	var rfb;
			        	 // Load supporting scripts
			            Util.load_scripts(["webutil.js", "base64.js", "websock.js", "des.js",
			                               "input.js", "display.js",
			                               "jsunzip.js", "rfb.js"]);


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
				            //console.log( state);
				            if ((oldstate === 'failed') && (state === 'disconnected')) {
				            	$('#message').css("display", "block");
				     			$("#message").html("<div class='pading-20'><font style='font-family: \"Lato\", sans-serif;  color: #818181; font-size: 24px;'> Connection Lost.. Please try again  </font> <img ng-if='progress' src='data:image/gif;base64,R0lGODlhEAAQAPIAAP///wAAAMLCwkJCQgAAAGJiYoKCgpKSkiH/C05FVFNDQVBFMi4wAwEAAAAh/hpDcmVhdGVkIHdpdGggYWpheGxvYWQuaW5mbwAh+QQJCgAAACwAAAAAEAAQAAADMwi63P4wyklrE2MIOggZnAdOmGYJRbExwroUmcG2LmDEwnHQLVsYOd2mBzkYDAdKa+dIAAAh+QQJCgAAACwAAAAAEAAQAAADNAi63P5OjCEgG4QMu7DmikRxQlFUYDEZIGBMRVsaqHwctXXf7WEYB4Ag1xjihkMZsiUkKhIAIfkECQoAAAAsAAAAABAAEAAAAzYIujIjK8pByJDMlFYvBoVjHA70GU7xSUJhmKtwHPAKzLO9HMaoKwJZ7Rf8AYPDDzKpZBqfvwQAIfkECQoAAAAsAAAAABAAEAAAAzMIumIlK8oyhpHsnFZfhYumCYUhDAQxRIdhHBGqRoKw0R8DYlJd8z0fMDgsGo/IpHI5TAAAIfkECQoAAAAsAAAAABAAEAAAAzIIunInK0rnZBTwGPNMgQwmdsNgXGJUlIWEuR5oWUIpz8pAEAMe6TwfwyYsGo/IpFKSAAAh+QQJCgAAACwAAAAAEAAQAAADMwi6IMKQORfjdOe82p4wGccc4CEuQradylesojEMBgsUc2G7sDX3lQGBMLAJibufbSlKAAAh+QQJCgAAACwAAAAAEAAQAAADMgi63P7wCRHZnFVdmgHu2nFwlWCI3WGc3TSWhUFGxTAUkGCbtgENBMJAEJsxgMLWzpEAACH5BAkKAAAALAAAAAAQABAAAAMyCLrc/jDKSatlQtScKdceCAjDII7HcQ4EMTCpyrCuUBjCYRgHVtqlAiB1YhiCnlsRkAAAOwAAAAAAAAAAAA==' /></div>");
				     			
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

				            if (state === "normal") { 
				            	cad.disabled = false;
				            	deviceView
				            	$('#deviceView').css("display", "block");
				            	$('#message').css("display", "none");
				            } else { 
				            	cad.disabled = true; 
				            }

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
							host= device.Host;
							port= device.ForwardPort;
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
				                           'view_only':    WebUtil.getQueryVar('view_only', false),
				                           'updateState':  updateState,
				                           'onPasswordRequired':  passwordRequired});
				            $scope.rfb.connect(host, port, password, path);

				           

				        }());
		   			} catch(e){
		   				//console.log(e);
		   				//$('#message').css("display", "block");
		     			//$("#message").html("<div class='pading-20'><font style='font-family: \"Lato\", sans-serif;  color: #818181; font-size: 24px;'> Something went wrong.. <br> Please Wait, We are trying to resolve..  </font> <img ng-if='progress' src='data:image/gif;base64,R0lGODlhEAAQAPIAAP///wAAAMLCwkJCQgAAAGJiYoKCgpKSkiH/C05FVFNDQVBFMi4wAwEAAAAh/hpDcmVhdGVkIHdpdGggYWpheGxvYWQuaW5mbwAh+QQJCgAAACwAAAAAEAAQAAADMwi63P4wyklrE2MIOggZnAdOmGYJRbExwroUmcG2LmDEwnHQLVsYOd2mBzkYDAdKa+dIAAAh+QQJCgAAACwAAAAAEAAQAAADNAi63P5OjCEgG4QMu7DmikRxQlFUYDEZIGBMRVsaqHwctXXf7WEYB4Ag1xjihkMZsiUkKhIAIfkECQoAAAAsAAAAABAAEAAAAzYIujIjK8pByJDMlFYvBoVjHA70GU7xSUJhmKtwHPAKzLO9HMaoKwJZ7Rf8AYPDDzKpZBqfvwQAIfkECQoAAAAsAAAAABAAEAAAAzMIumIlK8oyhpHsnFZfhYumCYUhDAQxRIdhHBGqRoKw0R8DYlJd8z0fMDgsGo/IpHI5TAAAIfkECQoAAAAsAAAAABAAEAAAAzIIunInK0rnZBTwGPNMgQwmdsNgXGJUlIWEuR5oWUIpz8pAEAMe6TwfwyYsGo/IpFKSAAAh+QQJCgAAACwAAAAAEAAQAAADMwi6IMKQORfjdOe82p4wGccc4CEuQradylesojEMBgsUc2G7sDX3lQGBMLAJibufbSlKAAAh+QQJCgAAACwAAAAAEAAQAAADMgi63P7wCRHZnFVdmgHu2nFwlWCI3WGc3TSWhUFGxTAUkGCbtgENBMJAEJsxgMLWzpEAACH5BAkKAAAALAAAAAAQABAAAAMyCLrc/jDKSatlQtScKdceCAjDII7HcQ4EMTCpyrCuUBjCYRgHVtqlAiB1YhiCnlsRkAAAOwAAAAAAAAAAAA==' /></div>");
		     			$timeout(function() {
		     				$scope.switchView(device);
		     			    }, 2000);
		     			
		     		    
		     			
		   			}
				       
				       
				} else {

				}

	   		}
	   		
	   		
			
     	}
	   	$scope.closeView = function () {
	   		try{
	   			$('#deviceView').css("display", "none");
	   			$('#myNav').css("display", "none");
	   			
  	 			$scope.rfb.disconnect();
  	 			
  	 		}catch(err){

  	 		}
	   	};
	   	
	    
	    $scope.isConnected= false;
	    $scope.isApp = false;
	    $scope.isAUTApp = false;
	    $scope.isStarted= false;
	    $scope.isTCFinish=false;
	    $scope.isFinish= false;
	    $scope.isRoboExe= false;
	    $scope.isAppiumExeCom = false;
	    $scope.isAppiumServerClose = false;
	    $scope.isAppiumExeStart = false;
	    $scope.isAppiumConnect = false;
	    var val = [];
	    var deviceId;
	    angular.forEach($scope.test.testDevices, function (device) {
	    	try{
				//var ws = $websocket('ws://' + document.location.host + window.location.pathname.substring(0, window.location.pathname.indexOf("/",2)) +'/deviceendpoint');
				//var ws = $websocket('ws://localhost:8080/TestPaceMobile/deviceendpoint');
				 var ws = new WebSocket('ws://localhost:8080/TestPaceMobile/deviceendpoint');
				 ws.onopen = function(event) {
					//console.log('connection opened', event);
						 console.log(device);
						 ws.send(JSON.stringify({'connection':{'deviceId':device.DeviceUDID,'status': 'false'}}));
					
	             };
					
	             ws.onmessage = function (event) { 
						if (typeof event.data === "string") {
							 // If the server has sent text data, then display it.
							//console.log('message: '+ event.data);

						 }
						var data = JSON.parse(event.data);
						//console.log(data);
						if(data.hasOwnProperty('deviceId') ){
							if(data.hasOwnProperty('connect')){
								 $scope.$apply(function () {
									$scope.status="Started";
									
								 });
								 if(data.connect=="true"){
									 //console.log("connected");
										 $scope.$apply(function () {
											 var status="Connecting Device..!";
											 updateStatus(data.deviceId,status);
											 $scope.connectionStatus= status;
											 ws.send(JSON.stringify({'VNCConnection':{'deviceId':data.deviceId,'orientation':'PORTRAIT'}}));
									        });
														 }
							 }  else if(data.hasOwnProperty('appiumLog')){

								$scope.appiumlog = $scope.appiumlog + '<p class="log-pad">'+data.appiumLog+'</p>';
								//console.log(data.appiumLog)
							} else if(data.hasOwnProperty('deviceLog')){
								//$scope.devicelog = $scope.devicelog + '<p class="log-pad">'+data.deviceLog+'</p>';
								//console.log(data.deviceLog)
							} else if(data.hasOwnProperty('openStatus')){
			                    		if(data.openStatus=='Success'){
			                    			 $scope.$apply(function () {
			                    				 $scope.connectionStatus="Device Connection is Established";
				                    			 $scope.isConnected= true;
				                    			 updateVNCStatus(data.deviceId,true);
			                    				
			                    		        });
			                    			
				                    		
				                    	} else {
				                    		 $scope.$apply(function () {
				                    			 var status=data.openStatus;
				                    			 updateStatus(data.deviceId,status);
				                    			 $scope.error=status;
				                    			});
				                    			
				 				    	}
			                    		 var status="Installing Apk..!";
			                    		 updateStatus(data.deviceId,status);
	                    				 $scope.appStatus = status;
	                    				 ws.send(JSON.stringify({'installApp':{'deviceId':data.deviceId, 'appId': $scope.test.app.id.toString(), 'testType': $scope.test.type, 'user':$scope.user.userId}}));
			                    	} else if(data.hasOwnProperty('installStatus')){
			                    		if(data.installStatus=='Success'){
			                    			 $scope.$apply(function () {
			                    				 $scope.isApp = true;
					                    			$scope.appStatus="Apk Installed Successfully";
				                    			 
										        });
			                    			
			                    			if($scope.test.type=='robotium'){
			                    				 $scope.$apply(function () {
			                    					 $scope.isRoboExe= true;
			                    					 var status="Installing AUT Apk..!";
			                    					 updateStatus(data.deviceId,status);
			                    					 $scope.autAppStatus = status;
			                    					 ws.send(JSON.stringify({'installAUTApp':{'deviceId':data.deviceId, 'autId':$scope.test.autinfo.autId.toString(), 'appId':$scope.test.app.id.toString(), 'user':$scope.user.userId }}));
											        });
			                    					 
			                    			}else if($scope.test.type=='appium'){
			                    				 $scope.$apply(function () {
			                    					 $scope.isRoboExe= false;
			                    					 var status="Connecting Appium..!";
			                    					 updateStatus(data.deviceId,status);
			                    					 $scope.appiumStatus=status;
			                    					 var port = getPort(data.deviceId);
			                    					 console.log(port);
			                    					 var str = JSON.stringify({'appiumTestExecuter':{'deviceId':data.deviceId,'app': $scope.test.app, 'testName': $scope.test.name,'tpAppiumPort': port}});
			                    					 //console.log(str);
			                    					 ws.send(str);
			                    					 
											        });
			                    					 
			                    			}
				                    	} else {
				                    		 $scope.$apply(function () {
				                    			 var status=data.installStatus;
				                    			 updateStatus(data.deviceId,status);
				                    			 $scope.error=status;
				                    		  	 ws.send(JSON.stringify({'closeVNCConnection':{'deviceId':data.deviceId}}));
				                    		
										        });
				                    			
				                    	}
			                    	} else if(data.hasOwnProperty('installAUTStatus')){
			                    		if(data.installAUTStatus=='Success'){
			                    			 $scope.$apply(function () {
			                    				 $scope.isAUTApp = true;
					                    			$scope.autAppStatus="AUT Apk Installed Successfully";
				                    	        });
			                    			
				                    		if($scope.test.type=='robotium'){
				                    			var testIns=$scope.test.testsuite;
									    		//console.log("testIns ::"+testIns);
									    		
									    		for(i=0;i<testIns.length;i++){
									    			if(testIns[i].select){
									    				 val[i] =testIns[i].id;
									    				// console.log("val["+i+"] ::"+ val[i]);
									    			}

									    		}
									    		//console.log("val ::"+val);
									    	    var lastPoint = val[0].lastIndexOf(".");
									            packageName = val[0].substring(0, lastPoint);
									        	//console.log("packageName ::"+packageName);

									        	var instruments = val.toString();
									        	//console.log("instruments ::"+ instruments);
									        	 $scope.$apply(function () {
									        		 $scope.isStarted= true;
									        		 var status="Test Begined";	
									        		 updateStatus(data.deviceId,status);
									        		 $scope.exeStatus = status;
									        		 var tempTest =$scope.test;
										        	 tempTest.results='';
										             $scope.testcaseStatus= "TestCase "+val.length+" are initiating..";
										        	 ws.send(JSON.stringify({'InstrumentExecuter':{'deviceId':data.deviceId,'instruments': instruments,'packageName': packageName,'test':tempTest, 'user':$scope.user.userId}}));
					                    			
											        });
									        	}
				                    		 
				                    	} else {
				                    		 $scope.$apply(function () {
				                    			 var status=data.installAUTStatus;
				                    			 updateStatus(data.deviceId,status);
				                    			 $scope.error = status;
				                    			 ws.send(JSON.stringify({'closeVNCConnection':{'deviceId':data.deviceId}})); 
										        });
				                    			
				                    	}
			                    	} else if(data.hasOwnProperty('InstrumentExecuterStatus')){
			                    		if(data.InstrumentExecuterStatus=='Success'){
			                    			 $scope.$apply(function () {
			                    				 var status="Test Completed Successfully!";
			                    				 updateStatus(data.deviceId,status);
			                    				 $scope.isFinish= true;
			                    				 $scope.finalStatus= status;
			                    				 $scope.closeView();
			                    				 ws.send(JSON.stringify({'closeVNCConnection':{'deviceId':data.deviceId}}));
										        });
			                    				
			                    			
			                    		} else {
			                    			 $scope.$apply(function () {
			                    				 $scope.error="Test Failed!";
				                    			 $scope.isFinish= true;
			                    			 });
			                    		
			                    		}
			                    	} else if(data.hasOwnProperty('closeStatus')){
			                    		if(data.closeStatus=='Success'){
			                    			ws.send(JSON.stringify({'appCleanupActivity':{'deviceId':data.deviceId, 'packageName': $scope.test.app.appPackageName}}));
			                    		}
			                    	} else if(data.hasOwnProperty('testCaseRunStatus')){
			                    		 $scope.$apply(function () {
			                    			 $scope.testcaseStatus="TestCase "+val.length+"/"+data.testcaseno+" is Executing..";
			                    		 });
			                    	} else if(data.hasOwnProperty('testCaseStatus')){
			                    		 $scope.$apply(function () {
			                    			 
				                    	
				                    		$scope.testcaseCompleted=data.testcaseno;
				                    		$scope.testcaseStatus="TestCase "+val.length+"/"+data.testcaseno+" is Finished";
				                    		if(val.length==data.testcaseno){
		                    					$scope.isTCFinish=true;
		                    					$scope.finalStatus="Generating Test Results..";
				                    		}
				                    		/*if(data.testCaseStatus=='Passed'){
				                    			 $scope.testcaseStatus="TestCase "+data.testcaseno+" is "+data.testCaseStatus;
				                    		} else if (data.testCaseStatus=='Failed') {
				                    			 $scope.testcaseStatus="TestCase "+data.testcaseno+" is "+data.testCaseStatus;
											} else if (data.testCaseStatus=='Error') {
												 $scope.testcaseStatus="TestCase "+data.testcaseno+" is "+data.testCaseStatus;
											}*/
			                    		 });
			                    	} else if(data.hasOwnProperty('cleanupStatus')){
			                    		if(data.cleanupStatus=='Success'){
			                    			 $scope.$apply(function () {
			                    				 $scope.isResult = true;
			                    			 });
			                    			/*var modalInstance = $modal.open({
	  					  	   		   		    templateUrl: 'testCompleteModel.html',
	  						   		   		    controller: 'ExecutionConfirmController',
	  						   		   		    backdrop: 'static'
	  						   		   		});*/
			                    		}
			                    	} else if(data.hasOwnProperty('appiumServerStartStatus')){
			                    		 $scope.$apply(function () {
			                    			 //console.log(data.appiumServerStartStatus);
			                    			 $scope.isAppiumConnect = true;
			                    			 var status = data.appiumServerStartStatus;
			                    			 updateStatus(data.deviceId,status);
			                    			 $scope.appiumstart = status;
			                    			 
		                    			 });
			                    	}  else if(data.hasOwnProperty('appiumExeStatus')){
			                    		 $scope.$apply(function () {
			                    			 //console.log(data.appiumExeStatus);
			                    			 $scope.isAppiumExeStart = true;
			                    			 var status = data.appiumExeStatus;
			                    			 updateStatus(data.deviceId,status);
			                    			 $scope.appiumexestart = status;
			                    			
		                    			 });
			                    		
			                    	} else if(data.hasOwnProperty('appiumServerCloseStatus')){
			                    		 $scope.$apply(function () {
			                    			 //console.log(data.appiumServerCloseStatus);
			                    			 $scope.isAppiumServerClose = true;
			                    			 device.status = data.appiumServerCloseStatus;
			                    			 $scope.appiumServerClose = data.appiumServerCloseStatus;
			                    			
		                    			 });
			                    		
			                    	} else if(data.hasOwnProperty('appiumExeComStatus')){
			                    		
			                    			 $scope.$apply(function () {
			                    				 var status="Test Completed Successfully!";
			                    				 updateStatus(data.deviceId,status);
			                    				 $scope.isAppiumExeCom = true;
			                    				 $scope.appiumExeComStatus = status;
				                    			 //console.log($scope.appiumExeComStatus);
			                    				
			                    				 ws.send(JSON.stringify({'closeVNCConnection':{'deviceId':data.deviceId}}));
			                    				 $scope.closeView();
										        });
			                    		
			                    	}
							
						        	
			                    	
			                   
			               
			              
					         
							
						}  
						
				    
	               };
					
	               ws.onclose = function(event)
	               { 
	            	   console.log('connection closed', event);


	            	   
	               };
	               ws.onerror= function (event) {
	 		    	  console.log('connection Error', event);
	 		    	
	 		    	  
	               };
					
			
		   
			    

			}catch (e) {
				console.log(e);
				$scope.loaderMessage = "Something went wrong.. Please Try Again!"
				$scope.closeView();
			}

	    });
		
	   	
		$scope.getResult = function(test, device) {

			$http({
	 	        method : 'POST',
	 	        url : './getTestResult',
	 	        data :{'testName':test.name, 'app': test.app, 'deviceId':device.DeviceUDID}
	 		    }).then(function(success, status, headers, config) {
	 		    	var response;
	 		    	if(typeof success =='object') {
	 		    		// It is JSON
	 		    		response=success.data;
	 		    	} else if(typeof success == 'string'){
	 		    		// It is string
	 		    		response=success;
	 		    	}
	 		    	var exTime = getTimeStamp(response.excTime);
	 		    	$scope.test.excTime=exTime;

	 		    	$scope.result=response;
	 		    	//console.log($scope.result);
	 		    	if($scope.test.type=='robotium' || $scope.test.type=='Robotium' ) {

	 		    	} else if($scope.test.type=='appium' || $scope.test.type=='Appium' )  {
	 		    		console.log($scope.result.sessionId);
	 		    		$http({
	 			 	        method : 'POST',
	 			 	        url : './getScreenShotsInfo',
	 			 	        data :{'sessionId':$scope.result.sessionId}
	 			 		    }).then(function(success, status, headers, config) {
	 			 		    	var response;

	 			 		    	if(typeof success =='object') {
	 			 		    	  // It is JSON
	 			 		    	  response=success.data;

	 			 		    	} else if(typeof success == 'string'){
	 			 		    		 // It is string
	 			 		    		response=success;
	 			 		    	}
	 			 		    	if(response!='failed'){
	 			 		    		for (var key in response) {
			 			 		    	  if (response.hasOwnProperty(key)) {
			 			 		    	    var val = response[key];
			 			 		    	   // console.log(val);
			 			 		    	    //data[key]=document.location.origin+"/ScreenShotStream?fileName="+val;
			 			 		    	  response[key]=document.location.origin+"/TestPaceMobile/ScreenShotStream?fileName="+val;
			 			 		    	  }
			 			 		    	}
	 			 		    	}


	 			 		    	$scope.test.screens=response;
	 			 		    	localStorageService.set('test', $scope.test);
	 					    }, function(error, status, headers, config) {
	 			 		            // called asynchronously if an error occurs
	 			 		            // or server returns response with an error status.

	 			 		    });


	 				}


	 		    	//document.location.origin+"/ScreenShotStream?fileName=screen_9_58378329-f626-462b-bad7-444a6b53481e.png
	 		    	//screen_9_58378329-f626-462b-bad7-444a6b53481e.png
	 		    	//$scope.result.filename=document.location.origin+"/VideoStream?fileName="+$scope.result.filename;
	 		    	$scope.result.filename=document.location.origin+"/TestPaceMobile/VideoStream?fileName="+$scope.result.filename;

	 				$scope.test.result= $scope.result;
	 				$scope.test.device= device;
	 				localStorageService.set('test', $scope.test);
	 				$state.go('home.detailedreports');
			    }, function(error, status, headers, config) {
	 		            // called asynchronously if an error occurs
	 		            // or server returns response with an error status.

	 		    });


	
		}
		function updateStatus(deviceId, status){
			angular.forEach($scope.test.testDevices, function (device) {
				if(device.DeviceUDID==deviceId){
					//console.log(deviceId);
					//console.log(device.status);
					
					 device.status=status;
					
				}
				
			});
		}
		function updateVNCStatus(deviceId, status){
			angular.forEach($scope.test.testDevices, function (device) {
				if(device.DeviceUDID==deviceId){
					//console.log(deviceId);
					//console.log(device.vncStatus);
					
						 device.vncStatus= true;
				}
				
			});
		}
		function getPort(deviceId){
			var port= null;
			angular.forEach($scope.test.testDevices, function (device) {
				if(device.DeviceUDID==deviceId){
					//console.log(deviceId);
					//console.log(device.tpAppiumPort);
					port = device.tpAppiumPort;
				}
				
			});
			return port;
		}
		
		
		
		/*angular.forEach($scope.test.testDevices, function (device) {
			$scope.socket(device);
    	});*/
       localStorageService.set('test', $scope.test);


   })