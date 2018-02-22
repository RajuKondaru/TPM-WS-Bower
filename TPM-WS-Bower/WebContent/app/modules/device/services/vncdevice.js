angular.module('app')

.service('connection', function($ocLazyLoad) {
	
	var rfb;
	this.state = true;
	this.init = function(){
	   $ocLazyLoad.load(['./js/vnc/include/util.js']);
	   $ocLazyLoad.load(['./js/vnc/include/webutil.js']);
	   $ocLazyLoad.load(['./js/vnc/include/base64.js']);
	   $ocLazyLoad.load(['./js/vnc/include/websock.js']);
	   $ocLazyLoad.load(['./js/vnc/include/des.js']);
	   $ocLazyLoad.load(['./js/vnc/include/input.js']);
	   $ocLazyLoad.load(['./js/vnc/include/display.js']);
	   $ocLazyLoad.load(['./js/vnc/include/jsunzip.js']);
	   $ocLazyLoad.load(['./js/vnc/include/rfb.js']);
	}
   
 
    this.start = function (test) {
    	 //I change here
    	/*jslint white: false */
        /*global window, $, Util, RFB, */
    	"use strict";
    	
    	function passwordRequired() {
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
            //console.log( state );
            if ((state === 'failed') && (msg === 'Connect timeout')) {
            	 state = false;
            	
            	
            } else if ((state === 'failed') && (msg === 'Disconnect timeout')) {
            	 rfb.disconnect();
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
			host=  test.device.Host;
			port=  test.device.ForwardPort;
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
            if( test.senario=='manual'){
            	view_only = false;


            }
            rfb = new RFB({'target':       $D('noVNC_canvas'),
                           'encrypt':      WebUtil.getQueryVar('encrypt',
                                    (window.location.protocol === "https:")),
                           'repeaterID':   WebUtil.getQueryVar('repeaterID', ''),
                           'true_color':   WebUtil.getQueryVar('true_color', true),
                           'local_cursor': WebUtil.getQueryVar('cursor', true),
                           'shared':       WebUtil.getQueryVar('shared', true),
                           'view_only':    WebUtil.getQueryVar('view_only', view_only),
                           'updateState':  updateState,
                           'onPasswordRequired':  passwordRequired});
            rfb.connect(host, port, password, path);

           

        }());
    }
    this.stop = function(){
 		try{
 			 rfb.disconnect();
 		}catch(err){
 		}
	}
    this.restart = function(){
    	rfb.connect(host, port, password, path);
    }
     
});