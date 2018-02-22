define(function () {
  var app = angular.module('app',['oc.lazyLoad','ui.router','ngCookies','angularModalService','ui.bootstrap','ngSanitize','angular-spinkit','LocalStorageModule']);
  app.constant('USERCONSTANTS', (function() {
  	return {

  		PASSWORD_LENGTH: 7
  	}
  })());
  app.config(function($ocLazyLoadProvider,$stateProvider, $urlRouterProvider,localStorageServiceProvider) {
	  localStorageServiceProvider
	    .setPrefix('TPM')
	    .setStorageType('sessionStorage')
	    .setDefaultToCookie(false)
	     .setNotify(true, true);
	  
    $ocLazyLoadProvider.config({
                'debug': true, // For debugging 'true/false'
                'events': true // For Event 'true/false'
            });
    

    $stateProvider
    .state('main', {
        url: "/main",
        abstract: true,
        views : {
        	"main" : {
                templateUrl:"templates/home/main.html"
            }
        }
    
    })
    
    .state('main.login',
      //AngularAMD is requesting the controllers on demand
    	{
	        url:'/login',
	        views : {
	        	    "sub" : {
		            	controller:'loginController',
		                templateUrl:"templates/home/login.html"
		            }
		        }
	        ,
	        resolve:{
	        	loadloginCntrl: ['$ocLazyLoad', function($ocLazyLoad) {
	                return $ocLazyLoad.load({
	                    name: 'login',
	                    serie: true,
	                    files: [
	                        'app/modules/core/controllers/login.js'
	                    ]
	                });
	                
	            }]
	        }
        }
    )
    .state('main.logout',
      //AngularAMD is requesting the controllers on demand
    	{
	        url:'/logout',
	        views : {
	        	    "sub" : {
		            	controller:'logoutController',
		                
		            }
		        }
	        ,
	        resolve:{
	        	loadloginCntrl: ['$ocLazyLoad', function($ocLazyLoad) {
	                return $ocLazyLoad.load({
	                    name: 'logout',
	                    serie: true,
	                    files: [
	                        'app/modules/core/controllers/logout.js'
	                    ]
	                });
	                
	            }]
	        }
        }
    )
    .state('home.userdetails',
      //AngularAMD is requesting the controllers on demand
    	{
	        url:'/userdetails',
	        views : {
	        	    "sub" : {
		            	controller:'UserController',
		                templateUrl:"templates/dashboard/account-details.html"
		            }
		        }
	        ,
	        resolve:{
	        	loadloginCntrl: ['$ocLazyLoad', function($ocLazyLoad) {
	                return $ocLazyLoad.load({
	                    name: 'user',
	                    serie: true,
	                    files: [
	                        'app/modules/app/controllers/user.js'
	                    ]
	                });
	                
	            }]
	        }
        }
    )
    .state('main.forget',
      //AngularAMD is requesting the controllers on demand
    	{
	        url:'/forget',
	        views : {
		            "sub" : {
	            	controller: 'forgotPasswordController',
	                templateUrl:"templates/home/forgetPassword.html"
	            }
	        },
	        resolve:{
	        	loadloginCntrl: ['$ocLazyLoad', function($ocLazyLoad) {
	                return $ocLazyLoad.load({
	                    name: 'forget',
	                    serie: true,
	                    files: [
	                        'app/modules/core/controllers/forget.js'
	                    ]
	                });
	                
	            }]
	        }
    	}
    )
    .state('main.register',
      //AngularAMD is requesting the controllers on demand
    	{
        url:'/register',
        views : {
	            "sub" : {
	            	controller: 'registerController',
	                templateUrl:"templates/home/register.html"
	            }
	        },
	        resolve:{
	        	loadloginCntrl: ['$ocLazyLoad', function($ocLazyLoad) {
	                return $ocLazyLoad.load({
	                    name: 'register',
	                    serie: true,
	                    files: [
	                        'app/modules/core/controllers/register.js',
	                        'app/modules/core/filters/passwordCount.js',
	                        'app/modules/core/directives/compareTo.js'
	                    ]
	                });
	                
	            }]
	        }
    	}
    )
    
    .state('home', {
        url: "/home",
        abstract: true,
        views : {
        	"main" : {
        		controller: 'UserController',
                templateUrl:"templates/dashboard/home.html"
            }
        },
        resolve:{
        	loadloginCntrl: ['$ocLazyLoad', function($ocLazyLoad) {
                return $ocLazyLoad.load({
                    name: 'login',
                    serie: true,
                    files: [
                    	'app/modules/app/controllers/user.js'
                        
                    ]
                });
                
            }]
        }
    })
    .state('home.dashboard', {
        url: "/dashboard",
        views : {
        	"sub" : {
        		controller: 'DashboardController',
                templateUrl:"templates/dashboard/dashboard.html"
            }
        },
        resolve:{
        	loadloginCntrl: ['$ocLazyLoad', function($ocLazyLoad) {
                return $ocLazyLoad.load({
                    name: 'login',
                    serie: true,
                    files: [
                    	'app/modules/app/controllers/dashboard.js'
                        
                    ]
                });
                
            }]
        }
    })
    .state('home.updateinfo', {
        url: "/updateinfo",
        views : {
        	"sub" : {
        		controller: 'UpdateDashboardController',
                templateUrl:"templates/dashboard/dashboard.html"
            }
        }, 
        resolve: {
            loadMyCtrl: ['$ocLazyLoad', function($ocLazyLoad) {
                return $ocLazyLoad.load([
                	'app/modules/app/factorys/pager.js',
                	'app/modules/app/util/timestamp.js',
                	'app/modules/app/controllers/updatedashboard.js',
                	'./js/custom/custom.js',
                	'./js/custom/sidebar.js']); // Resolve promise and load before view 
            }]
        }
    })
    
    
    .state('home.newapp', {
        url: "/newapp",
        views : {
        	"sub" : {
        		controller: 'AppUploadController',
                templateUrl:"templates/dashboard/newapp.html"
            }
        }, 
        resolve: {
            loadMyCtrl: ['$ocLazyLoad', function($ocLazyLoad) {
                return $ocLazyLoad.load([
                	'app/modules/app/controllers/appupload.js',
                	'app/modules/app/controllers/appconfirm.js'
                	]); // Resolve promise and load before view 
            }]
        }
    })
    
    
    .state('home.devicelab', {
        url: "/devicelab",
        views : {
        	"sub" : {
        		templateUrl:"templates/dashboard/devicelab.html"
            }
        }
    })
    /*.state('home.activeapps', {
        url: "/active",
        views : {
        	"tab@home.activeapps" : {
        		templateUrl:"templates/dashboard/active.html"
            }
        }
    })
    
    .state('home.archiveapps', {
        url: "/archive",
        views : {
        	"tab@home.activeapps" : {
        		templateUrl:"templates/dashboard/archive.html"
            }
        }
    })
    */
    
    
    .state('home.reports', {
        url: "/reports",
        views : {
        	"sub" : {
        		controller: 'ReportController',
                templateUrl:"templates/dashboard/reports.html"
            }
        }, 
        resolve: {
            loadMyCtrl: ['$ocLazyLoad', function($ocLazyLoad) {
                return $ocLazyLoad.load([
                	'app/modules/app/util/timestamp.js',
                	'app/modules/app/controllers/report.js'
                	]); // Resolve promise and load before view 
            }]
        }
    })
    .state('home.bugs', {
        url: "/bugs",
        views : {
        	"sub" : {
        		templateUrl:"templates/dashboard/bugs.html"
            }
        }
    })
    .state('home.support', {
        url: "/support",
        views : {
        	"sub" : {
        		templateUrl:"templates/dashboard/support.html"
            }
        }
    })
    
    
    .state('home.accountdetails', {
        url: "/accountdetails",
        views : {
        	"sub" : {
                templateUrl:"templates/dashboard/account-details.html"
            }
        }
    })
    .state('home.app', {
        url: "/app/:id",
        views : {
        	"sub" : {
        		controller: 'AppController',
                templateUrl:"templates/dashboard/selectedapp.html"
            },
            "bread@home.app" : {
            	controller: 'BreadController',
                templateUrl:"templates/dashboard/appbread.html"
            },
            "sub1@home.app" : {
        		
                templateUrl:"templates/dashboard/appinfo.html"
            },
            "sub2@home.app" : {
            	
        		templateUrl:"templates/dashboard/testdetails.html"
            }
        },
        resolve: {
            loadMyCtrl: ['$ocLazyLoad', function($ocLazyLoad) {
                return $ocLazyLoad.load([
                	'app/modules/app/factorys/pager.js',
                	'app/modules/app/filters/pagien.js',
                	'app/modules/app/controllers/bread.js',
                	'app/modules/app/controllers/app.js'
                	
                	]); // Resolve promise and load before view 
            }]
        }
    })
    .state('home.trash', {
        url: "/trash/:id",
        views : {
        	"sub" : {
        		controller: 'TrashController',
                templateUrl:"templates/dashboard/dashboard.html"
            }
        },
        resolve: {
            loadMyCtrl: ['$ocLazyLoad', function($ocLazyLoad) {
                return $ocLazyLoad.load([
                	'app/modules/app/controllers/trash.js'
                	
                	]); 
            }]
        }
    })
      .state('home.active', {
        url: "/active/:id",
        views : {
        	"sub" : {
        		controller: 'ActiveController',
                templateUrl:"templates/dashboard/dashboard.html"
            }
        },
        resolve: {
            loadMyCtrl: ['$ocLazyLoad', function($ocLazyLoad) {
                return $ocLazyLoad.load([
                	'app/modules/app/controllers/active.js'
                	
                	]); 
            }]
        }
    })
    .state('home.delete', {
        url: "/delete/:id",
        views : {
        	"sub" : {
        		controller: 'DeleteController',
                templateUrl:"templates/dashboard/dashboard.html"
            }
        },
        resolve: {
            loadMyCtrl: ['$ocLazyLoad', function($ocLazyLoad) {
                return $ocLazyLoad.load([
                	'app/modules/app/controllers/delete.js',
                	'app/modules/app/controllers/appdeleteconfirm.js',
                	
                	
                	]); 
            }]
        }
    })
    .state('home.app.newtest', {
        url: "/newtest",
        views : {
        	"bread@home.app" : {
        		controller: 'BreadController',
        	    templateUrl:"templates/dashboard/newtestbread.html"
            },
            
            "sub2@home.app" : {
            	templateUrl:"templates/dashboard/manualAndAutomation.html"
            }
        }
    })
    
    .state('home.app.activities', {
        url: "/:senario",
        views : {
        	"bread@home.app" : {
        		controller: 'BreadController',
                templateUrl:"templates/dashboard/recentactivitybread.html"
            },
            "sub2@home.app" : {
            	controller: 'TestController',
            	templateUrl:"templates/dashboard/recentActivies.html"
            }
        },
        resolve: {
            loadMyCtrl: ['$ocLazyLoad', function($ocLazyLoad) {
                return $ocLazyLoad.load([
                	'app/modules/app/controllers/test.js'
                	]); // Resolve promise and load before view 
            }]
        }
    })
    
    .state('home.app.senario', {
        url: "/:senario",
        views : {
        	"bread@home.app" : {
        		controller: 'BreadController',
        		templateUrl:"templates/dashboard/senariobread.html"
            },
            "sub2@home.app" : {
            	controller: 'typeController',
            	templateUrl:"templates/dashboard/testsenario.html"
            }
        },
        resolve: {
            loadMyCtrl: ['$ocLazyLoad', function($ocLazyLoad) {
                return $ocLazyLoad.load([
                	'app/modules/app/controllers/type.js'
                	]); // Resolve promise and load before view 
            }]
        }
    })
    .state('home.app.robotium', {
        url: "/:type",
        views : {
        	"bread@home.app" : {
        		controller: 'BreadController',
        		templateUrl:"templates/dashboard/robobread.html"
            },
            "sub2@home.app" : {
            	controller: 'RoboController',
            	 templateUrl:"templates/dashboard/robotiumAUTfileupload.html"
            }
        },
        resolve: {
            loadMyCtrl: ['$ocLazyLoad', function($ocLazyLoad) {
                return $ocLazyLoad.load([
                	'app/modules/app/controllers/robo.js'
                	]); // Resolve promise and load before view 
            }]
        }
    })
    .state('home.app.testcases', {
        url: "/testcases",
        views : {
        	"bread@home.app" : {
        		controller: 'BreadController',
        		templateUrl:"templates/dashboard/robotestcasebread.html"
            },
            "sub2@home.app" : {
            	controller: 'RoboTCController',
            	 templateUrl:"templates/dashboard/robotiumTestCases.html"
            }
        },
        resolve: {
            loadMyCtrl: ['$ocLazyLoad', function($ocLazyLoad) {
                return $ocLazyLoad.load([
                	'app/modules/app/controllers/robotc.js',
                	'app/modules/app/controllers/testnameconfirm.js'
                	]); // Resolve promise and load before view 
            }]
        }
    })
    .state('home.app.appium', {
        url: "/:type",
        views : {
        	"bread@home.app" : {
        		controller: 'BreadController',
        		templateUrl:"templates/dashboard/appiumbread.html"
            },
            "sub2@home.app" : {
            	controller: 'createAppiumTestController',
            	templateUrl:"templates/dashboard/createAppiumTest.html"
            }
        },
       
        resolve: {
            loadMyCtrl: ['$ocLazyLoad', function($ocLazyLoad) {
                return $ocLazyLoad.load([
                	'app/modules/app/controllers/createappiumtest.js',
                	'app/modules/app/controllers/testnameconfirm.js'
                	]); // Resolve promise and load before view 
            }]
        }
    })
    .state('home.app.devices', {
        url: "/devices",
        views : {
        	
        	"bread@home.app" : {
        		controller: 'BreadController',
        		templateUrl:"templates/dashboard/devicesbread.html"
            },
            "sub2@home.app" : {
            	controller: 'DevicesController',
            	templateUrl:"templates/dashboard/devices.html"
            }
        },
       
        resolve: {
            loadMyCtrl: ['$ocLazyLoad', function($ocLazyLoad) {
                return $ocLazyLoad.load([
                	'app/modules/device/controllers/devices.js',
                	'app/modules/app/controllers/testnameconfirm.js'
                	]); // Resolve promise and load before view 
            }]
        }
    })
    
    .state('home.save-mtc', {
        url: "/save-mtc/:id",
        views : {
        	"sub" : {
        		controller: 'mtcController',
                templateUrl:"templates/dashboard/uploadTestCase.html"
            }
        }
    })
    .state('home.app.execution', {
        url: "/execution",
      
	    views : {
	    	"bread@home.app" : {
	    		controller: 'BreadController',
	    		templateUrl:"templates/dashboard/exebread.html"
	        },
	        "sub2@home.app" : {
	        	controller: 'DeviceController',
	        	templateUrl:"templates/dashboard/exepanel.html"
	        }
	    },
	   
	    resolve: {
	        loadMyCtrl: ['$ocLazyLoad', function($ocLazyLoad) {
	            return $ocLazyLoad.load([
	            	
	            	'app/modules/device/controllers/device.js',
	            	'app/modules/exe/controllers/execonfirm.js',
	            	'app/modules/app/util/timestamp.js'
	            	]); // Resolve promise and load before view 
	        }]
	    }
    })
    .state('home.manual', {
        url: "/manual",
        views : {
        	"bread@home.manual" : {
            	templateUrl:"templates/dashboard/exebread.html"
            },
        	
        	"sub" : {
        		controller: 'ConnectController',
        		templateUrl:"templates/dashboard/launch2.html"
            }
        },
        resolve: {
	        loadMyCtrl: ['$ocLazyLoad', function($ocLazyLoad) {
	            return $ocLazyLoad.load([
	            	'app/modules/app/util/timestamp.js',
	            	'app/modules/device/controllers/connect3.js',
	            	'app/modules/exe/controllers/execonfirm.js'
	            	]); // Resolve promise and load before view 
	        }]
	    }
    })
    .state('home.app.capabilities', {
        url: "/capabilities",
        views : {
        	"bread@home.app" : {
	    		controller: 'BreadController',
	    		templateUrl:"templates/dashboard/capBread.html"
	        },
        	"sub2@home.app" : {
        		controller: 'CapabilitiesController',
                templateUrl:"templates/dashboard/capabilities.html"
            }
        },
	    resolve: {
	        loadMyCtrl: ['$ocLazyLoad', function($ocLazyLoad) {
	            return $ocLazyLoad.load([
	            	
	            	'app/modules/app/controllers/capabilities.js'
	            	]); // Resolve promise and load before view 
	        }]
	    }
    })
    .state('home.app.writeTest', {
        url: "/writeTest",
        views : {
        	"bread@home.app" : {
	    		controller: 'BreadController',
	    		templateUrl:"templates/dashboard/writetestbread.html"
	        },
        	"sub2@home.app" : {
        		controller: 'writeTestController',
                templateUrl:"templates/dashboard/writeTest.html"
            }
        },
	    resolve: {
	        loadMyCtrl: ['$ocLazyLoad', function($ocLazyLoad) {
	            return $ocLazyLoad.load([
	            	
	            	'app/modules/app/controllers/writetest.js',
	            	'app/modules/app/controllers/testcaseconfirm.js'
	            	]); // Resolve promise and load before view 
	        }]
	    }
    })
    .state('home.devices', {
        url: "/devices/:id",
        views : {
        	"sub" : {
        		controller: 'AppController',
                templateUrl:"templates/dashboard/writeTest.html"
            }
        }
    })
    .state('home.app.uploadTest', {
        url: "/uploadTest/:id",
        views : {
        	"sub" : {
        		controller: 'AppController',
                templateUrl:"templates/dashboard/uploadTest.html"
            }
        }
    })
    .state('home.detailedreports', {
        url: "/detailedreports",
        views : {
        	"sub" : {
        		controller: 'TestCaseResultController',
                templateUrl:"templates/dashboard/detailedreports.html"
            }
        },
        resolve: {
	        loadMyCtrl: ['$ocLazyLoad', function($ocLazyLoad) {
	            return $ocLazyLoad.load([
	            	'./js/vendor/ng-infi.js',
	            	'app/modules/app/factorys/loghandle.js',
	            	'app/modules/exe/controllers/testcaseresult.js'
	            	]); // Resolve promise and load before view 
	        }]
	    }
    })
    .state('home.testtype', {
        url: "/testtype",
        views : {
        	"sub" : {
        		templateUrl:"templates/dashboard/manualAndAutomation.html"
            }
        }
    })
    
  
    .state('home.newAutomationTest', {
        url: "/newAutomationTest/:id",
        views : {
        	"sub" : {
        		controller: 'AppController',
                templateUrl:"templates/dashboard/automationActivity.html"
            }
        }
    })
    
    
    .state('home.automationLaunchDevice', {
        url: "/automationLaunchDevice",
        views : {
        	"sub" : {
        		controller: 'DevicesController',
                templateUrl:"templates/dashboard/selectDevice.html"
            }
        }
    })
    
    
    
    $urlRouterProvider.otherwise('/main/login')
   


  })
  return app;
});
