angular.module('app')
.filter('reverseArrayOnly', function() {
	  return function(items) {
	    if(!angular.isArray(items)) { return items; }
	    return items.slice().reverse();
	  };
	})