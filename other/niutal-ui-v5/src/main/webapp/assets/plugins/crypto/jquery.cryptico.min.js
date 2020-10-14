/**
 * @discretion	: 扩展基于cryptico.js的jquery加密解密插件，实现RSA,
 * @author    	: wandalong 
 * @version		: v1.0.1
 * @email     	: hnxyhcwdl1003@163.com
 * @example   	: 1.引用jquery的库文件js/jquery.js
  				  2.引用样式文件css/syncscroll-1.0.0.css
 				  3.引用效果的具体js代码文件 jquery.syncscroll-1.0.0.js
 				  4.<script language="javascript" type="text/javascript">
					jQuery(function($) {
					
						$("#scrollDiv").syncscroll({
							
						});
						
					});
					</script>
 */
;(function($){
	
	if(typeof cryptico == 'undefined'){
		/*
		 * Cryptico.js是一个易于使用的JavaScript工具包用于在客户端对文本进行加密。
		 * 它支持RSA + AES方法，任意字节长度(228, 1024等)的文本都可以进行加密。 
		 */
		throw new Error("need cryptico.js ");
	}
	
	var _VERSION = "1.0";

	$.rsa = ( function( $ ) {
		
		/*
		 * passphrase	: The passphrase used to repeatably generate this RSA key.,
		 * bitlength	: The length of the RSA key, in bits.
		 */
		$.getRSAKey = function(passphrase, bitlength){
			
			var MattsRSAkey = cryptico.generateRSAKey(passphrase, bitlength);
			var MattsPublicKeyString = cryptico.publicKeyString(MattsRSAkey); 
		};
		
		function _decode( s ) {
			
		}
		
		function _encode( s ) {
		    if ( arguments.length !== 1 ) {
		      throw "SyntaxError: exactly one argument required";
		    }
	
		    s = String( s );
		    
		}
			
		return {
			decode: _decode,
	    	encode: _encode,
	    	VERSION: _VERSION
		};
	}( $ ) );
	
	$.md5 = function(){
		
	};

}(jQuery));

