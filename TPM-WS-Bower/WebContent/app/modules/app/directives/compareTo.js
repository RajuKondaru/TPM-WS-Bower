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
.directive("compareTo", function () {
	return {
		require: "ngModel",
		/* Anything coming through the current scope and passing through the attribute and can be accessed using SCOPE.
		 * data-compare-to="registerForm.password" - [ scope: { data: "=DirectiveName"} ]
		 * [OR] data-compare-to  data="value" - [ scope: { data: "="} ]
		 *
		 * paramScope: "=paramScopeView" - single-directional binding the data.
		 * if you change the data in controller as scope.paramScope = SomeValue. this value will not affect in view.
		 *
		 * paramScope: "=" - by-directional binding the data.
		 * if you change the data in controller as scope.paramScope = SomeValue. this value will be affect back in the view.
		 * */
		scope: {
			passwordAttribute: "=compareTo"
		},
		/*scope - not as - $scope as it is not dependency injection*/
		link: function(scope, element, attributes, ngModel) {

			ngModel.$validators.compareTo = function( confirmPassword ) {
				if( scope.passwordAttribute.$modelValue != 'undefined'
					&& scope.passwordAttribute.$modelValue != null && scope.passwordAttribute.$valid ) {
					//console.log("Password : ", scope.passwordAttribute.$modelValue,"\t confirmPassword : ", confirmPassword);
					var result = confirmPassword == scope.passwordAttribute.$modelValue;;
					//console.log("Confirm password : ", result);
					return result;
				} else {
					//console.log('Please enter valid password, before conforming the password.', scope.passwordAttribute);
					return false;
				}
			};
			scope.$watch("passwordAttribute", function() {
				//console.log('Confirm password - Password Watcher.')
				ngModel.$validate();
			});

		},
		controller: function ($scope) {
			//console.log("compareTo - Custom directive controller function called.");
		}
	};

});

