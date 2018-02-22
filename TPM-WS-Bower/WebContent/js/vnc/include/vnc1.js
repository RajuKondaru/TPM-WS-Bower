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
    return (typeof INCLUDE_URI !== "undefined") ? INCLUDE_URI : "js/vnc/include/";
}

(function () {
    "use strict";
    
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
            	var d=document.createElement("script");
                document.head.appendChild(d);
                d.src=get_INCLUDE_URI()+ob[key];
            }
        }
    }
        
            
        

    
    
   /* var extra = "", start, end;
    	console.log(get_INCLUDE_URI());
    start =  get_INCLUDE_URI();
    end = "'><\/script>";

    // Uncomment to activate firebug lite
    //extra += "<script src='http://getfirebug.com/releases/lite/1.2/" + 
    //         "firebug-lite-compressed.js'><\/script>";

    extra += start + "util.js" + end;
    extra += start + "webutil.js" + end;
    extra += start + "base64.js" + end;
    extra += start + "websock.js" + end;
    extra += start + "des.js" + end;
    extra += start + "input.js" + end;
    extra += start + "display.js" + end;
    extra += start + "rfb.js" + end;
    extra += start + "jsunzip.js" + end;
    document.getElementsByTagName("head")[0].appendChild(extra);*/
   // document.write(extra);
}());

