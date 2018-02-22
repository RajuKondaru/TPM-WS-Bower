
angular.module('app')
.controller('AppUploadController', function($scope,  $modal, $ocLazyLoad) {
    	 
      	$ocLazyLoad.load(['./js/vendor/fileuploadstyle.js']);
      	$( ".buttonText" ).html( "New App" );
      	$( ".icon-span-filestyle" ).removeClass('icon-span-filestyle glyphicon glyphicon-cloud-upload').addClass("glyphicon glyphicon-cloud-upload");
      	$( ".buttonText" ).css('color', 'white');
      	$( ".glyphicon-phone" ).css('color', 'white');
      	$( ".btn-primary " ).css('width', '100px');
      	$(".buttonText").ready(function() {
      		//
      			//$("#message").css("display", "none");
      			var allowedFiles = [".apk", ".ipa", ".app"];
      			var regex = new RegExp("(" + allowedFiles.join('|') + ")$");
      			var regexpWhiteSpace = /^\S*$/;
      			var jsonData=[];
      		     $('#appfile').change(function(e){

      		    	 var modalInstance = $modal.open({
        		   		      templateUrl: 'appModalContent.html',
        		   		      controller: 'AppConfirmController',
        		   		      backdrop: 'static'
        		   		    });
      		    	 if (!regexpWhiteSpace.test($('#appfile').val().toLowerCase())) {
      		    		 $('#title').html ("<font color='red'>Wanring</font>");
      		    		 $('#message').html("<font color='#025984'>White Scapes not allowed for file name. Please remove white spaces and try again...!</font>");
      		    		 $('#message').css("display", "block");
      		    		 $('#cancelBtn').css("display", "block");
      		    	 } else if (!regex.test($('#appfile').val().toLowerCase())) {
      		    		 $('#title').html ("<font color='red'>Wanring</font>");
      		    		 $('#message').html("<font color='#025984'>Please upload files having extensions: <b>" + allowedFiles.join(', ') + "</b> only.</font>");
      		    		 $('#message').css("display", "block");
      		    		 $('#cancelBtn').css("display", "block");
      		    		/* $("#message").show().delay(5000).fadeOut();*/
      		             return false;
      		         } else{
      		        	 var file = this.files[0];
      		             var form = new FormData();
      		             form.append('app', file);

      		             $(".progress").css("display", "block");
      		        	 $("#progressbar").css('width',0+'%');
      		        		var ajax = new XMLHttpRequest();
      		        		ajax.upload.addEventListener("progress", progressHandler, false);
      		        		ajax.addEventListener("load", completeHandler, false);
      		        		ajax.addEventListener("error", errorHandler, false);
      		        		ajax.addEventListener("abort", abortHandler, false);
      		        		ajax.open("POST", "./AppUpload?appName=" + $scope.appname + "&appVersion=" + $scope.appversion+" &apptype=native", true);
      		        		ajax.responseType = "application/json; charset=UTF-8";
      		        		ajax.send(form);

      		         }

      		     });
      		    function progressHandler(event){
      	    		var percent = (event.loaded / event.total) * 100;
      	    		$("#progressbar").css('width',percent+'%');
      	    		$("#progressbar").html(percent+'%');
      	    		if(percent==100){
      	    			 $(".progress").css("display", "none");
      	    			$scope.progress=true;
      	    			$("#message").html("<font color='green'> Please wait. App Validation Progressing..  </font> <img ng-if='progress' src='data:image/gif;base64,R0lGODlhEAAQAPIAAP///wAAAMLCwkJCQgAAAGJiYoKCgpKSkiH/C05FVFNDQVBFMi4wAwEAAAAh/hpDcmVhdGVkIHdpdGggYWpheGxvYWQuaW5mbwAh+QQJCgAAACwAAAAAEAAQAAADMwi63P4wyklrE2MIOggZnAdOmGYJRbExwroUmcG2LmDEwnHQLVsYOd2mBzkYDAdKa+dIAAAh+QQJCgAAACwAAAAAEAAQAAADNAi63P5OjCEgG4QMu7DmikRxQlFUYDEZIGBMRVsaqHwctXXf7WEYB4Ag1xjihkMZsiUkKhIAIfkECQoAAAAsAAAAABAAEAAAAzYIujIjK8pByJDMlFYvBoVjHA70GU7xSUJhmKtwHPAKzLO9HMaoKwJZ7Rf8AYPDDzKpZBqfvwQAIfkECQoAAAAsAAAAABAAEAAAAzMIumIlK8oyhpHsnFZfhYumCYUhDAQxRIdhHBGqRoKw0R8DYlJd8z0fMDgsGo/IpHI5TAAAIfkECQoAAAAsAAAAABAAEAAAAzIIunInK0rnZBTwGPNMgQwmdsNgXGJUlIWEuR5oWUIpz8pAEAMe6TwfwyYsGo/IpFKSAAAh+QQJCgAAACwAAAAAEAAQAAADMwi6IMKQORfjdOe82p4wGccc4CEuQradylesojEMBgsUc2G7sDX3lQGBMLAJibufbSlKAAAh+QQJCgAAACwAAAAAEAAQAAADMgi63P7wCRHZnFVdmgHu2nFwlWCI3WGc3TSWhUFGxTAUkGCbtgENBMJAEJsxgMLWzpEAACH5BAkKAAAALAAAAAAQABAAAAMyCLrc/jDKSatlQtScKdceCAjDII7HcQ4EMTCpyrCuUBjCYRgHVtqlAiB1YhiCnlsRkAAAOwAAAAAAAAAAAA==' />");
      	    			 $('#message').css("display", "block");
      	    		}
      		    }
      		    function completeHandler(event){
      		    	$scope.progress=false;
      		    	$('#message').css("display", "none");
      	    		var res = event.target.responseText;
      	    		//console.log(res);
      	    		if(res!='failed'){

      	    			$('#title').html("<font color='green'>Upload Status</font>");
      	    			jsonData=JSON.parse(res);
      		    		var image = jsonData["imgBase64String"];
      		    		var appName = jsonData["appName"];
      		    		var versionName = jsonData["version"];
      		    		var packageName = jsonData["packageName"];
      		    		var activityName = jsonData["activityName"];
      				      var statusVal = JSON.stringify(res.status);
      						 if(statusVal == "false") {
      						 	$("#message").html(JSON.stringify(response.message));
      						 	$('#message').css("display", "block");
      						 	$('#cancelBtn').css("display", "block");
      					     } else{
      					    		 var out = '';
      					    	         out = out +
      					    	         '<div class="col-md-12 col-sm-6 col-xs-12">'+
      	                                     '<div class="col-md-3 col-sm-6 col-xs-12">'+
      	                                      	'<img src="data:image/png;base64,'+image+'">'+
      	                                     '</div>'+
      	                                     '<div class="col-md-9 col-sm-6 col-xs-12">'+
      		                                     '<table data-toggle="table" data-striped="true">'+
      			                                     '<tbody>'+
      				                                     '<tr>'+
      					                                     '<td><label>App Name  </label></td>'+
      					                                     '<td><span class="rep-justify">:</span></td>'+
      					                                     '<td>'+appName+'</td>'+
      				                                     '</tr>'+
      				                                     '<tr>'+
      					                                     '<td><label>Version  </label></td>'+
      					                                     '<td><span class="rep-justify">:</span></td>'+
      					                                     '<td>'+versionName+'</td>'+
      				                                     '</tr>'+
      				                                     '<tr>'+
      					                                     '<td><label>Package  </label></td>'+
      					                                     '<td><span class="rep-justify">:</span></td>'+
      					                                     '<td>'+packageName+'</td>'+
      				                                     '</tr>'+
      				                                     '<tr>'+
      					                                     '<td><label>Activity  </label></td>'+
      					                                     '<td><span class="rep-justify">:</span></td>'+
      					                                     '<td>'+activityName+'</td>'+
      				                                     '</tr>'+
      			                                     '</tbody>'+
      		                                     '</table>'+
      	                                     '</div>'+
                                           '</div>';




      					    	    $("#message").html("<font color='green'> # " +  appName + " app Uploaded successfully. </font>");
      					    	    $('#okBtn').css("display", "block");
      					    	    $('#cancelBtn').css("display", "none");
      					    	    $('#message').css("display", "block");
      					    	    $('#kv-success-2').html(out);
      					    	    $('#kv-success-2').fadeIn('slow');
      					    	    $('#kv-success-2').show();

      					     }
      	    		} else{
      	    			$('#title').html("<font color='red'> #Error </font>");
      	    			$(".progress").css("display", "none");

      	    			$("#message").html("<font color='#025984'>Something went wrong. App may be currepted. Plaese try Again</font>");
      	    			$('#message').css("display", "block");
      	    			$('#cancelBtn').css("display", "block");

      	    		}



      	    	}
      	    	function errorHandler(event){
      	    		$('#message').html = "Upload Failed";
      	    		$('#message').css("display", "block");
      	    		 $('#cancelBtn').css("display", "block");
      	    	}
      	    	function abortHandler(event){
      	    		$('#message').html = "Upload Aborted";
      	    		$('#message').css("display", "block");
      	    		 $('#cancelBtn').css("display", "block");

      	    	}


      		 });




      });