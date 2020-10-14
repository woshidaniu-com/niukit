(function(a){a.extend(a.validator.messages,{accept:a.validator.format("Please enter a value with a valid mimetype"),alphanumeric:"Letters, numbers, and underscores only please",bankaccountNL:"Please specify a valid bank account number",bankorgiroaccountNL:"Please specify a valid bank or giro account number",begin:a.validator.format("must begin with {0}"),bic:"Please specify a valid BIC (business identifier code) code",chrnum:"Letters, numbers only please",chinese:"Chinese only please",cifES:"Please specify a valid CIF number",creditcardtypes:"Please enter a valid credit card number",creditcard:"Please enter a valid credit card number",currency:"Please specify a valid currency",date:"Please enter a valid date",dateITA:"Please enter a valid date (format: dd/mm/yyyy)",dateNL:"Please enter a valid date (NL)",dateISO:"Please enter a valid date (ISO)\uff0cexample \uff1a2009-06-23\uff0c1998/01/22",dateTime:"Please enter a valid date (format: yyyy-mm-dd HH:mm)",dateTimeSecond:"Please enter a valid date (format: yyy-mm-dd HH:mm:ss)",decimal:"\u9a8c\u8bc1\u503c\u5c0f\u6570\u4f4d\u6570\u4e0d\u80fd\u8d85\u8fc7\u4e24\u4f4d",digits:"Please enter only digits",email:"Please enter a valid email address",email2:"\u8bf7\u8f93\u5165\u6709\u6548\u7684Email\u5730\u5740",equalTo:"Please enter the same value again",extension:a.validator.format("Please enter a value with a valid extension"),english:"Please enter only letters",fixed:function(c,b){return"\u8bf7\u8f93\u5165["+a.makeArray(c).join(",")+"]\u5176\u4e2d\u4e00\u4e2a\u503c\u6216\u8005\u4ed6\u4eec\u7684\u7ec4\u5408!"},fileRequest:function(c,b){if(!a.founded(a(b).val())){return"\u5fc5\u987b\u9009\u62e9\u6587\u4ef6"}else{return a(b).data("message")}},"float":"Please enter only float number",floatNumber:"\u5fc5\u987b\u662f4\u4f4d\u4ee5\u5185\u975e\u8d1f\u6b63\u6574\u6570",fourNumber:"\u5fc5\u987b\u662f4\u4f4d\u4ee5\u5185\u975e\u8d1f\u6b63\u6574\u6570",fourInteger:"\u5fc5\u987b\u662f4\u4f4d\u4ee5\u5185\u975e\u8d1f\u6b63\u6574\u6570",giroaccountNL:"Please specify a valid giro account number",gt:a.validator.format("Please enter a value greater than {0}"),iban:"Please specify a valid IBAN",idCard:"Please enter a valid 18 digit ID card number",IDCard:"Please enter a valid ID card number",integer:"A positive or negative non-decimal number please",ipv4:"Please enter a valid IP v4 address.",ipv6:"Please enter a valid IP v6 address.",lettersonly:"Letters only please",letterswithbasicpunc:"Letters or punctuation only please",LetterNumberUnderline:"Please enter 5-16 digit characters start with a letter that contains numbers and underscores",max:a.validator.format("Please enter a value less than or equal to {0}."),maxlength:a.validator.format("Please enter no more than {0} characters."),maxWords:a.validator.format("Please enter {0} words or less."),min:a.validator.format("Please enter a value greater than or equal to {0}."),minlength:a.validator.format("Please enter at least {0} characters."),minWords:a.validator.format("Please enter at least {0} words."),mobile:"Please specify a valid tellphone number(example\uff1a 010-00000000|00000000|13700000000)",mobileASGN:"Please specify a valid mobile phone number (Must be assigned number segment by operator ) or tellphone phone number (format: 010-00000000 or 00000000)",mobileCN:"Please specify a valid China mobile phone number ( Must be assigned number segment by operator [CMCC|CUT|CTC])",mobileCMC:"Please specify a valid CMCC phone number",mobileCUT:"Please specify a valid CUT phone number",mobileCTC:"Please specify a valid CTC phone number",mobileNL:"Please specify a valid mobile number",mobileUK:"Please specify a valid mobile number",NegativeInteger:"Please enter only negative integer",NegativeFloat:"Please enter only negative float",nieES:"Please specify a valid NIE number.",nifES:"Please specify a valid NIF number.",notEqualTo:"Please not enter the same value again",notEqual:a.validator.format("Not allowed {0}"),nowhitespace:"No white space please",number:"Please enter a valid number.",pattern:"Invalid format.",phone:"Please enter a valid 11 digit phone number",phoneNL:"Please specify a valid phone number.",phoneUK:"Please specify a valid phone number",phoneUS:"Please specify a valid phone number",phonesUK:"Please specify a valid uk phone number",postalcodeNL:"Please specify a valid postal code",postcodeUK:"Please specify a valid UK postcode",PositiveInteger:"Positive integer only please",positiveIntegerFloat:"Positive integer or positive float only please",PositiveFloat:"Positive float only please",positiveNumber:"Positive integer or 0 only please",pingyin:"Letters only please",range:a.validator.format("Please enter a value between {0} and {1}."),rangelength:a.validator.format("Please enter a value between {0} and {1} characters long."),rangeWords:a.validator.format("Please enter between {0} and {1} words."),required:"This field is required.",require_from_group:a.validator.format("Please fill at least {0} of these fields."),remote:"Please fix this field.",skip_or_fill_minimum:a.validator.format("Please either skip these fields or fill at least {0} of them."),strippedminlength:a.validator.format("Please enter at least {0} characters"),stringRangeLength:a.validator.format("\u8bf7\u8f93\u5165\u957f\u5ea6\u4e3a {0} \u81f3 {1} \u4e4b\u95f4\u7684\u5b57\u7b26(\u4e2d\u6587\u4e3a\u82f1\u6587\u957f\u5ea6\u4e24\u500d)"),stringMaxLength:a.validator.format("\u6700\u591a\u8f93\u5165 {0} \u4e2a\u5b57\u7b26(\u4e2d\u6587\u4e3a\u82f1\u6587\u957f\u5ea6\u4e24\u500d)"),stringMinLength:a.validator.format("\u6700\u5c11\u8f93\u5165 {0} \u4e2a\u5b57\u7b26(\u4e2d\u6587\u4e3a\u82f1\u6587\u957f\u5ea6\u4e24\u500d)"),tel:"\u8bf7\u8f93\u5165\u6709\u6548\u7684\u7535\u8bdd\u53f7\u7801(\u5982\uff1a 010-00000000\u621600000000)",ThreeNum:"\u53ea\u80fd\u662f\u6570\u5b57[0-2]\u7684\u4e09\u4f4d\u7ec4\u5408",time:"Please enter a valid time, between 00:00 and 23:59",time12h:"Please enter a valid time in 12-hour am/pm format",url:"Please enter a valid URL.",url2:"\u8bf7\u8f93\u5165\u6709\u6548\u7684\u7f51\u5740",UnPositiveInteger:"\u53ea\u80fd\u8f93\u5165\u975e\u6b63\u6574\u6570\uff08\u8d1f\u6574\u6570 + 0\uff09",UnNegativeInteger:"\u53ea\u80fd\u8f93\u5165\u975e\u8d1f\u6574\u6570\uff08\u6b63\u6574\u6570 + 0\uff09",UnNegativeFloat:"\u53ea\u80fd\u8f93\u5165\u975e\u8d1f\u6d6e\u70b9\u6570\uff08\u6b63\u6d6e\u70b9\u6570 + 0\uff09",vinUS:"The specified vehicle identification number (VIN) is invalid.",words:"\u53ea\u80fd\u8f93\u5165\u4e2d\u6587\u3001\u5b57\u6bcd\u3001\u4e0b\u5212\u7ebf\u6216\u7ec4\u5408",xfPositiveInteger:"\u8bf7\u8f93\u5165\u6709\u6548\u7684\u5b66\u5206(\u975e\u8d1f\u6574\u6570\u6216\u6d6e\u70b9\u6570\uff0c\u5982*.5,*.0)",zipcodeUS:"The specified US ZIP Code is invalid",ziprange:"Your ZIP-code must be in the range 902xx-xxxx to 905-xx-xxxx",zipCode:"\u8bf7\u8f93\u5165\u683c\u5f0f\u6b63\u786e\u7684\u90ae\u653f\u7f16\u7801\uff0c\u5982\uff1a100000",zhxsPositiveInteger:"\u8bf7\u8f93\u5165\u6709\u6548\u7684\u5b66\u65f6(\u6b63\u6574\u6570\u548c\u6b63\u6d6e\u70b9\u6570\uff0c\u5982*.5)",zpxs:"\u8bf7\u8f93\u5165\u6b63\u786e\u7684\u7167\u7247\u50cf\u7d20\uff08\u9ad8*\u5bbd\uff09\u8981\u6c42\uff0c\u5982:144*172"})}(jQuery));
