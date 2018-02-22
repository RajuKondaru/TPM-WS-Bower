
angular.module('app')
  .controller('RoboController', function ($http, $scope, $stateParams, localStorageService, $ocLazyLoad, $state, $timeout) {
    	 $ocLazyLoad.load(['./js/vendor/fileuploadstyle.js']);
      	var type =  $stateParams.type;
      	//console.log(type);
      	$scope.test = localStorageService.get('test');
      	$scope.test.type=type;
  		$( ".icon-span-filestyle" ).removeClass('icon-span-filestyle glyphicon glyphicon-folder-open').addClass("glyphicon glyphicon-phone");
  		var allowedFiles = [".apk"];
  		var regex = new RegExp("(" + allowedFiles.join('|') + ")$");
  		$('#myNav').css("display", "none");
  		$scope.setFile = function(element) {
  			 //document.getElementById("myNav").style.height = "100%";
  			$('#myNav').css("display", "block");
  			$('#message').css("display", "none");
  			$(".progress").css("display", "block");
  			$("#progressbar").css('width',0+'%');
  	        $scope.$apply(function($scope) {
  	            $scope.theFile = element.files[0];
  	            var formData = new FormData();
   	            formData.append("appid",$scope.test.app.id);
   	            formData.append("app", $scope.theFile);
  	            var ajax = new XMLHttpRequest();
	      		ajax.upload.addEventListener("progress", progressHandler, false);
	      		ajax.addEventListener("load", completeHandler, false);
	      		ajax.addEventListener("error", errorHandler, false);
	      		ajax.addEventListener("abort", abortHandler, false);
	      		ajax.open("POST", "./uploadaut", true);
	      		ajax.responseType = "application/json;";
	      		ajax.send(formData);
  	        });
  	    };
  	  function progressHandler(event){
	  		var percent = (event.loaded / event.total) * 100;
	  		$("#progressbar").css('width',percent+'%');
	  		$("#progressbar").html(percent+'%');
	  		//10 seconds delay
	       
	        	if(percent==100){
		  			 //$(".progress").css("display", "none");
		  			 $scope.progress=true;
		  			 $('#message').css("display", "block");
		  			 $("#message").html("<div class='pading-20'><font style='font-family: \"Lato\", sans-serif;  color: #818181; font-size: 24px;'> Please wait. App Validation Progressing..  </font> <img ng-if='progress' src='data:image/gif;base64,R0lGODlhEAAQAPIAAP///wAAAMLCwkJCQgAAAGJiYoKCgpKSkiH/C05FVFNDQVBFMi4wAwEAAAAh/hpDcmVhdGVkIHdpdGggYWpheGxvYWQuaW5mbwAh+QQJCgAAACwAAAAAEAAQAAADMwi63P4wyklrE2MIOggZnAdOmGYJRbExwroUmcG2LmDEwnHQLVsYOd2mBzkYDAdKa+dIAAAh+QQJCgAAACwAAAAAEAAQAAADNAi63P5OjCEgG4QMu7DmikRxQlFUYDEZIGBMRVsaqHwctXXf7WEYB4Ag1xjihkMZsiUkKhIAIfkECQoAAAAsAAAAABAAEAAAAzYIujIjK8pByJDMlFYvBoVjHA70GU7xSUJhmKtwHPAKzLO9HMaoKwJZ7Rf8AYPDDzKpZBqfvwQAIfkECQoAAAAsAAAAABAAEAAAAzMIumIlK8oyhpHsnFZfhYumCYUhDAQxRIdhHBGqRoKw0R8DYlJd8z0fMDgsGo/IpHI5TAAAIfkECQoAAAAsAAAAABAAEAAAAzIIunInK0rnZBTwGPNMgQwmdsNgXGJUlIWEuR5oWUIpz8pAEAMe6TwfwyYsGo/IpFKSAAAh+QQJCgAAACwAAAAAEAAQAAADMwi6IMKQORfjdOe82p4wGccc4CEuQradylesojEMBgsUc2G7sDX3lQGBMLAJibufbSlKAAAh+QQJCgAAACwAAAAAEAAQAAADMgi63P7wCRHZnFVdmgHu2nFwlWCI3WGc3TSWhUFGxTAUkGCbtgENBMJAEJsxgMLWzpEAACH5BAkKAAAALAAAAAAQABAAAAMyCLrc/jDKSatlQtScKdceCAjDII7HcQ4EMTCpyrCuUBjCYRgHVtqlAiB1YhiCnlsRkAAAOwAAAAAAAAAAAA==' /></div>");
		  			
		  		}
	       
	  		
	    }
	    function completeHandler(event){
	    	
	    	if(event.target.status==200){
	    		//10 seconds delay
		      
		        	$scope.progress=false;
			    	$('#message').css("display", "none");
			    	console.log(event);
			    	var response = event.target.responseText;
			    	//console.log(response);
					if(response=='unsupported' || response=='empty'){
						$(".progress").css("display", "none");
						 $('#message').css("display", "block");
			  			 $("#message").html("<div class='pading-20'><font  style='font-family: \"Lato\", sans-serif;  color: #91000D; font-size: 70px; padding: 20px;'>Oops..!</font></div><div><font style='font-family: \"Lato\", sans-serif;  color: #818181; font-size: 24px;'>App is not valid or testcases are not available.<br>Please verify app and try again.</font></div>");
			  			$('.closebtn').css("display", "block");
			  			
					} else {
						var jsonData=JSON.parse(response);
				    	//console.log(jsonData);
				    	$scope.test.autinfo=jsonData;
			 	    	
			 	    	localStorageService.set('test',$scope.test);
			 	    	
			 	    	$('#myNav').css("display", "none");
			 	    	$state.go('home.app.testcases');
			 	    	
			 	    	
					}
			    	
		       
	    	} else{
	    		$(".progress").css("display", "none");
	    		$('#message').css("display", "block");
	  			 $("#message").html("<div class='pading-20'><font  style='font-family: \"Lato\", sans-serif;  color: #91000D; font-size: 70px; padding: 20px;'>Error</font></div><div><font  style='font-family: \"Lato\", sans-serif;  color: #818181; font-size: 24px;'>Something went wrong. Please try again.</font></div>");
	  			 $('.closebtn').css("display", "block");
	    	}
	    	 
	    	
		 
	  	}
	  	function errorHandler(event){
	  		$(".progress").css("display", "none");
	  		$('#message').html = "Upload Failed";
	  		$('#message').css("display", "block");
	  		
	  	}
	  	function abortHandler(event){
	  		$(".progress").css("display", "none");
	  		$('#message').html = "Upload Aborted";
	  		$('#message').css("display", "block");
	  		
	
	  	}
  	 

      })
       


