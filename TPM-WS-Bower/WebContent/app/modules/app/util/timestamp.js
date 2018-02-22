

      var getTimeStamp=  function(input, p_allowFuture) {
          var substitute = function (stringOrFunction, number, strings) {
                  var string = $.isFunction(stringOrFunction) ? stringOrFunction(number, dateDifference) : stringOrFunction;
                  var value = (strings.numbers && strings.numbers[number]) || number;
                  return string.replace(/%d/i, value);
              },
              nowTime = (new Date()).getTime(),
              date = (new Date(input)).getTime(),

              allowFuture = p_allowFuture || false,
              strings= {
                  prefixAgo: null,
                  prefixFromNow: null,
                  suffixAgo: "ago",
                  suffixFromNow: "from now",
                  second: "Just Now",
                  seconds: "%d seconds",
                  minute: "about a minute",
                  minutes: "%d minutes",
                  hour: "about an hour",
                  hours: "%d hours",
                  day: "a day",
                  days: "%d days",
                  month: "a month",
                  months: "%d months",
                  year: "about a year",
                  years: "%d years"
              },
              dateDifference = nowTime - date,
              words,
              seconds = Math.abs(dateDifference) / 1000,

              minutes = seconds / 60,
              hours = minutes / 60,
              days = hours / 24,
              years = days / 365,
              separator = strings.wordSeparator === undefined ?  " " : strings.wordSeparator,

              prefix = strings.prefixAgo;
              suffix = strings.suffixAgo;

              if (seconds < 10) {
              	 suffix = '';
              }
              seconds = Math.round(seconds);
          if (allowFuture) {
              if (dateDifference < 0) {
                  prefix = strings.prefixFromNow;
                  suffix = strings.suffixFromNow;
              }
          }

          words = seconds < 10 && substitute(strings.second, seconds, strings) ||
          seconds > 10 && seconds < 59 && substitute(strings.seconds, seconds, strings) ||
          minutes < 59 && substitute(strings.minutes, Math.round(minutes), strings) ||
          minutes < 59 && substitute(strings.hour, 1, strings) ||
          hours < 24 && substitute(strings.hours, Math.round(hours), strings) ||
          hours < 42 && substitute(strings.day, 1, strings) ||
          days < 30 && substitute(strings.days, Math.round(days), strings) ||
          days < 45 && substitute(strings.month, 1, strings) ||
          days < 365 && substitute(strings.months, Math.round(days / 30), strings) ||
          years < 1.5 && substitute(strings.year, 1, strings) ||
          substitute(strings.years, Math.round(years), strings);

          return $.trim([prefix, words, suffix].join(separator));

      }