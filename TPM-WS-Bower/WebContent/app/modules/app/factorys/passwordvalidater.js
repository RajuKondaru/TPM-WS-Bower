angular.module('app')
.factory('passwordvalidater', [function() {
  	return {
  		score: function() {
  			console.log('arguments List : ', arguments);
  			var score = 0, value = arguments[0], passwordLength = arguments[1];
  			var containsLetter = /[a-zA-Z]/.test(value), containsDigit = /\d/.test(value), containsSpecial = /[^a-zA-Z\d]/.test(value);
  			var containsAll = containsLetter && containsDigit && containsSpecial;

  			console.log(" containsLetter - ", containsLetter,
  					" : containsDigit - ", containsDigit,
  					" : containsSpecial - ", containsSpecial);

  			if( value.length == 0 ) {
  				score = 0;
  			} else {
  				if( containsAll ) {
  					score += 3;
  				} else {
  					if( containsLetter ) score += 1;
  					if( containsDigit ) score += 1;
  					if( containsSpecial ) score += 1;
  				}
  				if(value.length >= passwordLength ) score += 1;
  			}
  			/*console.log('Factory Arguments : ', value, " « Score : ", score);*/
  			return score;
  		}
  	};
  }])

