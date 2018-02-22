
$(document).ready(function() {
	$( ".buttonText" ).html( "New App" );
	$( ".icon-span-filestyle" ).removeClass('icon-span-filestyle glyphicon glyphicon-folder-open').addClass("glyphicon glyphicon-phone");
	var allowedFiles = [".apk"];
	var regex = new RegExp("(" + allowedFiles.join('|') + ")$");
	
     $('#appfile').change(function(e){
    	 if (!regex.test($('#appfile').val().toLowerCase())) {
    		 $('#message').html("<font color='red'>"+"Please upload files having extensions: <b>" + allowedFiles.join(', ') + "</b> only."+"</font>");
    		 $("#message").show().delay(5000).fadeOut();
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
        		ajax.open("POST", "./uploadaut", true);
        		ajax.responseType = "text";
        		ajax.send(form);
         }
        
				
				 	
     });
    function progressHandler(event){
		var percent = (event.loaded / event.total) * 100;
		$("#progressbar").css('width',percent+'%');
		$("#progressbar").html(percent+'%');
    }
	function completeHandler(event){
		var res = event.target.responseText;
		//var json = JSON.parse(res);
		var jsonData=JSON.parse(res);
		console.log(jsonData);
		 
	      var statusVal = JSON.stringify(res.status);
			 if(statusVal == "false") {
		     	$("#message").html("<font color='red'>"+JSON.stringify(response.message)+"</font>");
		     	
		     } else{
		    	 
		    	 var inner="";
		    	 for (var i = 0; i < jsonData.length; i++) {
		    		 var data=jsonData[i].split(".");
		    		 var l=data.length;
					
					 //inner=inner+"<input type='radio' name='group2' style='margin: 20px; '>"+ response[i]+"<br>";
					 inner="<div class='info-block block-info clearfix' style='float:left; width:33%;'>"+
			                       " <div class='square-box pull-left' >"+
			                           "<span class='glyphicon glyphicon-tags glyphicon-lg'></span>"+
			                       " </div>"+
			                       " <div data-toggle='buttons' class='btn-group bizmoduleselect'>"+
			                          " <label class='btn btn-default'>"+
			                               "<div class='bizcontent' style='overflow: hidden;'>"+
			                                   "<input type='checkbox' name='var_id[]' autocomplete='off' value='"+jsonData[i]+"'>"+
			                                   "<span class='glyphicon glyphicon-ok glyphicon-lg'></span>"+
			                                   "<h5>"+data[l-1]+"</h5>"+
			                               "</div>"+
			                           "</label>"+
			                       " </div>"+
			                    "</div>"+inner;
				}
		    	$('#resetButton').addClass('disabled');
	    		$('#selectButton').removeClass('disabled');
		    	$(".container").css("display", "block");
		    	$("#suites").html(inner); 
		    	$(".progress").css("display", "none");
		    	$('input:checkbox').change(function() {
    		    	if($(this).is(':checked')){
    		    		$('#runButton').removeClass('disabled');
    		    	} else {
    		    		if($('input').is(':checked')){
    		    		
    		    		} else {
    		    			$('#runButton').addClass('disabled');
						}
    		    	}
    		    	
    		    });
		    	$('input[type="text"]').val('')
		    	//checks for the button click event
		    
		    	$("#selectButton").click(function(e){
		    		 $('input:checkbox').each(function(i){
		    			 if($(this).prop("checked") == true){
		    				
		    			 }else{
		    				 $(this).click();
		    			 }
			              $('#selectButton').addClass('disabled');
				    	  $('#resetButton').removeClass('disabled');
			          });
		    	});
		    	$("#resetButton").click(function(e){
		    		 $('input:checkbox:checked').each(function(i){
		    			 $(this).click();
			    		 $('#resetButton').addClass('disabled');
			    		 $('#selectButton').removeClass('disabled');
		    		 });
		    	});
		    	 
		    	
		        $("#runButton").click(function(e){
		        	var val = [];
		            $(':checkbox:checked').each(function(i){
		              val[i] = $(this).val();
		            });
		           
	                var lastPoint = val[0].lastIndexOf(".");
	                var packageName = val[0].substring(0, lastPoint);
		                
		          
	                dataString = "instruments=" + val+"&packageName="+packageName;
	                console.log(dataString);
	                //make the AJAX request, dataType is set to json
	                //meaning we are expecting JSON data in response from the server
	                $.ajax({
	                    type: "POST",
	                    url: "./InstrumentExecuter",
	                    data: dataString,
	                    dataType: "json",
	                    
	                    //if received a response from the server
	                    success: function( jsonData, textStatus, jqXHR) {
	                    	
	                        //our country code was correct so we have some information to display
	                        var testLogs="";
	                        var deviceLogs="";
	                        var exeVideo="";
	                        var deviceId="";
	                        for (var result in jsonData) {
                        		// console.log("result ::"+result);
	       						 if (jsonData.hasOwnProperty(result)) {
	       						    var results = jsonData[result];
	       						 	var consoleresult = results.toString().replace(/\,/g,"<br>")
	       						 	
									  if(result.toString().includes("sessionid") == true){
										 
										  exeVideo=exeVideo+"<iframe id='video' width='1100' height='540' src='${pageContext.servletContext.contextPath}/videos/"+results+".mp4'></iframe> ";
							    			  
										//  $('#video').attr('src', '${pageContext.servletContext.contextPath}/videos/'+results+'.mp4');
										 
									  } else  if(result.toString().includes("deviceid") == true) {
										  deviceId = results;
									  } else  if(result.toString().includes("devicelog") == true) {
										  deviceLogs = deviceLogs+" <div class='jumbotron' style='background-color: #007ba7 !important;    margin-right: 30px; padding: 10px 0px 10px 60px !important;'>"+
				   			                 "<h3 style='color: #040000; '># Test Device Name</h3>"+
							                 "<p id='testname' style='font-size: 18px;font-size: 18px; font-family: monospace;background-color: #fff; width: 96%;color: black;padding: 10px 0px 10px 50px;'>"+deviceId+"</p>"+
												 "<h3 style='color: #040000; '># Test Device Log</h3>"+
												 "<div style=' width:96%; height:250px; overflow: hidden;'>"+
												 "<div id='testresult' style='overflow:scroll; width:105%; font-size: 16px; line-height: 1.5;   font-family: Courier New, monospace; background-color: #fff;color: black; height: 267px;padding: 10px 0px 10px 50px;'>"+
								             	 consoleresult+
								             	
								             	 "</div>"+
								             	 "</div>"+
							            "</div> ";
									  } else  {
										  var testsuitename=result.substring(result.lastIndexOf(".")+1, result.length);
										  testLogs=testLogs+" <div class='jumbotron' style='background-color: #007ba7 !important;    margin-right: 30px; padding: 10px 0px 35px 60px !important;'>"+
				   			                 "<h3 style='color: #040000; '># Test Suite Name</h3>"+
							                 "<p id='testname' style='font-size: 18px;font-size: 18px; font-family: monospace;background-color: #fff; width: 96%;color: black;padding: 10px 0px 10px 50px;'>"+testsuitename+"</p>"+
												 "<h3 style='color: #040000; '># Test Suite Log</h3>"+
												 
											
												 "<div style=' width:96%; height:250px; overflow: hidden;'>"+
												 "<div id='testresult' style='overflow:scroll; width:105%; font-size: 16px; line-height: 1.5;  font-family: Courier New, monospace; background-color: #fff;color: black; height: 267px; padding: 10px 0px 10px 50px;'>"+
								             	 consoleresult+
								             	
								             	 "</div>"+
								             	 "</div>"+
							       			
							            "</div> ";
									}         
	       						  }
	       						 $("#exevideo").click(function(e){
	       							 if(!$( "#exevideo" ).hasClass( "active" )){
	       								$('#result').html(exeVideo);
						    			 $('#exevideo').addClass('active');
						    			 $('#devicelog').removeClass('active');
						    			 $('#testlog').removeClass('active');
	       							 }
					    			 
					    		 });
	       						 $("#testlog").click(function(e){
	       							 if(!$( "#testlog" ).hasClass( "active" )){
	       								 $('#result').html(testLogs);
						    			 $('#exevideo').removeClass('active');
						    			 $('#devicelog').removeClass('active');
						    			 $('#testlog').addClass('active');
	       							 }
					    			
					    		 });
	       						 $("#devicelog").click(function(e){
	       							 if(!$( "#devicelog" ).hasClass( "active" )){
	       								 $('#result').html(deviceLogs);
						    			 $('#exevideo').removeClass('active');
						    			 $('#devicelog').addClass('active');
						    			 $('#testlog').removeClass('active');
	       							 }
					    			
					    		 });
	       						 $('#result').html(testLogs);
	       						 $("#results").css("display", "block");
	       			    		 $('#result').show();
	       			    		 $(':checkbox:checked').each(function(i){
	      			                $(this).click();
	      			             });
	       			    		 $('#resetButton').addClass('disabled');
					    		 $('#selectButton').removeClass('disabled');
					    		 
					    		
       						}
	                        
	                    },
	                    
	                    //If there was no resonse from the server
	                    error: function(jqXHR, textStatus, errorThrown){
	                         console.log("Something really bad happened " + textStatus);
	                          $("#message").html("Something really bad happened " +jqXHR.responseText);
	                          $("#message").show().delay(5000).fadeOut();
	                    },
	                    
	                    //capture the request before it was sent to server
	                    beforeSend: function(jqXHR, settings){
	                        
	                        //disable the button until we get the response
	                        $('#runButton').addClass('disabled');
	                    },
	                    
	                    //this is called after the response or error functions are finsihed
	                    //so that we can take some action
	                    complete: function(jqXHR, textStatus){
	                        //enable the button 
	                        $('#runButton').attr("disabled", false);
	                    }
	          
	                });        
		        });
		    		   
		    		
		     }
		  
	}
	function errorHandler(event){
		$('#message').html = "Upload Failed";
		$('#message').show();
	}
	function abortHandler(event){
		$('#message').html = "Upload Aborted";
		$('#message').show();

	}
});

	