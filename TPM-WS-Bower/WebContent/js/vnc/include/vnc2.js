/*
 * noVNC: HTML5 VNC client
 * Copyright (C) 2012 Joel Martin
 * Licensed under LGPL-3 (see LICENSE.txt)
 *
 * See README.md for usage and integration instructions.
 */

/*jslint evil: true */
/*global window, document, INCLUDE_URI */

/*
 * Load supporting scripts
 */
function get_INCLUDE_URI() {
    return (typeof INCLUDE_URI !== "undefined") ? INCLUDE_URI : "views/include/";
}

(function () {
    "use strict";
    var scripts=document.getElementsByName("script");
	var vncJs = [ 
	              { util: 'util.js'},
	              { webutil: 'webutil.js' },
	              { base64: 'base64.js'},
	              { websock: 'websock.js'},
	              { des: 'des.js' },
	              { input: 'input.js'},
	              { display: 'display.js'},
	              { rfb: 'rfb.js' },
	              { jsunzip: 'jsunzip.js'}
	             
               ];
	var i;
    for (i = 0; i < vncJs.length; i++) {
    	var ob=vncJs[i];
    	
    	
        for (var key in ob) {
            if (ob.hasOwnProperty(key)) {
            	var exist=false;
            	 for (var j = 0; j < scripts.length; j++) {
            		 if(scripts[j].src.includes(ob[key])){
            			 exist=true; 
            			 console.log(ob[key]+" is exist");
            		 } 
            		 
            	 }
            	 if(!exist){
            		 var d=document.createElement("script");
                     document.head.appendChild(d);
                     d.src=get_INCLUDE_URI()+ob[key]; 
            	 }
            	
            }
        }
    }
 
}());

