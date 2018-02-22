angular.module('app')
.filter('passwordCount', [ function() {
	var passwordLengthDefault = 7;
	return function( expressionModelVal, argument1) {
		//console.log("expressionModelVal : ", expressionModelVal, "\t Provided Value : ", argument1);
		expressionModelVal = angular.isString(expressionModelVal) ? expressionModelVal : '';
		argument1 = isFinite(argument1) ? argument1 : passwordLengthDefault;
		var retrunVal = expressionModelVal && (expressionModelVal.length > argument1 ? argument1 + '+' : expressionModelVal.length);
		return retrunVal;
	};
} ]);

