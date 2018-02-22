/*A Custom Directive - "reusable component" directive
We could put logic into a model to confirm a password, but it is probably better
to separate the logic into a reusable directive.

The `ngController` directive attaches a controller class to the view.
MVC components in angular:
 *
 * * Model — Models are the properties of a scope; scopes are attached to the DOM where scope properties
 *   are accessed through bindings.
 * * View — The template (HTML with data bindings) that is rendered into the View.
 * * Controller — The `ngController` directive specifies a Controller class; the class contains business
 *   logic behind the application to decorate the scope with functions and values
 * https://www.youtube.com/watch?v=0r5QvzjjKDc
 * https://github.com/angular/angular.js/blob/10644432ca9d5da69ce790a8d9e691640f333711/src/ng/directive/input.js#L2522
 */


angular.module('app')
.directive('okPasswordDirective', ['myfactory', 'USERCONSTANTS', function(myfactory, USERCONSTANTS) {
	return {
		// restrict to only attribute and class [AC]
		restrict: 'AC',

		// use the NgModelController
		require: 'ngModel',

		// add the NgModelController as a dependency to your link function
		link: function($scope, $element, $attrs, ngModelCtrl) {
			console.log('Directive - USERCONSTANTS.PASSWORD_LENGTH : ', USERCONSTANTS.PASSWORD_LENGTH);

			$element.on('blur change keydown', function( evt ) {
				$scope.$evalAsync(function($scope) {
					// update the $scope.password with the element's value
					var pwd = $scope.password = $element.val();

					$scope.myModulePasswordMeter = pwd ? (pwd.length > USERCONSTANTS.PASSWORD_LENGTH
							&& myfactory.score(pwd, USERCONSTANTS.PASSWORD_LENGTH) || 0) : null;

					// define the validity criterion for okPassword constraint
					ngModelCtrl.$setValidity('okPasswordController', $scope.myModulePasswordMeter > 3);
				});
			});
		}
	};
}]);

