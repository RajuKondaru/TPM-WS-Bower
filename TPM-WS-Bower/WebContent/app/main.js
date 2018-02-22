
require.config({
  waitSeconds: 10,
  paths:  {
    'jquery':'../bower_components/jquery/dist/jquery.min',
    'bootstrap':'../bower_components/bootstrap/dist/js/bootstrap.min',
    'angular':'../bower_components/angular/angular',
    'angular-ui-router':'../bower_components/angular-ui-router/release/angular-ui-router',
    'angular-cookies':'../bower_components/angular-cookies/angular-cookies',
    'angular-local-storage':'../bower_components/angular-local-storage/dist/angular-local-storage.min',
    
    'angular-spinkit':'../bower_components/angular-spinkit/build/angular-spinkit',
    'angularAMD': '../bower_components/angularAMD/angularAMD.min',
    'ocLazyLoad': '../bower_components/oclazyload/dist/ocLazyLoad',
    'angularModalService': '../bower_components/angular-modal-service/dst/angular-modal-service',
    
    'scrollglue': '../bower_components/angular-scroll-glue/src/scrollglue',
    'bootstrap-filestyle': '../bower_components/bootstrap-filestyle/src/bootstrap-filestyle',
    'ui-bootstrap': '../bower_components/angular-ui-bootstrap-bower/ui-bootstrap-tpls',
    'ngSanitize': '../bower_components/angular-sanitize/angular-sanitize',
    'underscore-min': '../bower_components/underscore-min/underscore-min',
    'html2canvas':'../bower_components/html2canvas/build/html2canvas.min',
    'jsPDF':'../bower_components/jsPDF/dist/jspdf.debug',
    'htmlToPdfSave':'../bower_components/angular-save-html-to-pdf/dist/saveHtmlToPdf.min',
   
    
    'app':'app',
    
  },
  shim:{
    'angular':{
      deps:['jquery','underscore-min']
    },
   
    'jsPDF':{
        deps:[ 'html2canvas']
      },
    'htmlToPdfSave':{
      deps:['angular','jsPDF']
    },
    'bootstrap':{
        deps:['jquery']
      },
    'bootstrap-filestyle':{
        deps:['bootstrap']
      },
    'angular-spinkit':{
      deps:['angular']
    },
    'angular-ui-router':{
      deps:['angular']
    },
    'angular-cookies':{
      deps:['angular']
    },
       'ocLazyLoad':{
      deps:['angular']
    },
    'angularAMD':{
      deps:['angular']
    },
    'angularModalService':{
        deps:['angular']
    },
   
    'scrollglue':{
        deps:['angular']
    },
    'ui-bootstrap':{
        deps:['angular']
    },
    'ngSanitize':{
        deps:['angular']
    },
    'angular-local-storage':{
        deps:['angular']
    },
    
    'app':{
      deps:['angular-ui-router','angular-spinkit','ocLazyLoad','angular-cookies','angularModalService','ui-bootstrap','ngSanitize','angular-local-storage']
    },
    
    
  },
  deps:['app']
});
require(['app'],function () {
  return angular.bootstrap(document,['app']);
});
