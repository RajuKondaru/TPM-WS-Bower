

/* right side menu */
var menuRight = document.getElementById( 'cbp-spmenu-s2' ),
				body = document.body;
       
	   showRight.onclick = function() {
				classie.toggle( this, 'active' );
				classie.toggle( menuRight, 'cbp-spmenu-open' );
				disableOther( 'showRight' );
			};
			

			function disableOther( button ) {
				
				if( button !== 'showRight' ) {
					classie.toggle( showRight, 'disabled' );
				}
				
			}
			
/* end right side menu */



/* rotate button  */

$(".rotateBtn").click(function(){
 $(this).toggleClass("down")  ; 
})

/* rotate button icon */