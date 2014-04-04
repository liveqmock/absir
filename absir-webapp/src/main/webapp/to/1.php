<?php

/**
 * 检查token_id 有效
 */
function check_token_id($token_id) {
	return $token_id && preg_match('/^[A-Z0-9]{64}$/i', ($token_id = str_replace(' ', '', $token_id)));
}

/**
 * IOS PUSH 消息
 */
function push_notification($token_id, $title, $content) {
	if (!check_token_id($token_id)) {
		return "Not correct $token_id";
	}
	
	//echo($token_id);
	$message = array("aps" => array('alert' => cutString(strip_tags($content), 32), 'sound' => 'received5.caf'));
	
	$ctx = stream_context_create();
	$pem = dirname(__FILE__) . '/apns.pem';
	stream_context_set_option($ctx, 'ssl', 'local_cert', $pem);
	$pass = '123456';
	stream_context_set_option($ctx, 'ssl', 'passphrase', $pass);
	$fp = stream_socket_client('ssl://gateway.sandbox.push.apple.com:2195', $err, $errstr, 60, STREAM_CLIENT_CONNECT, $ctx);
	//$client = stream_socket_client('ssl://gateway.push.apple.com:2195', $err, $errstr, 60, STREAM_CLIENT_CONNECT, $ctx);

	if (!$fp) {
		return "Failed to connect $err $errstrn";
	}

	$message = json_encode($message);
	$notification = chr(0) . pack('n', 32) . pack('H*', $token_id) . pack('n', strlen($message)) . $message;

	fwrite($fp, $notification);
	fclose($fp);

	return "sending message :$message \n";
}

/*********************************************************/
// 函数名：cutString
// 功能：截取指定长度的字符串，一个中文字符在广义上也作为一个字符对待
// 注：在utf8编码方式下一个中文字符占3个字节 在gb编码方式下为两个字节
/*********************************************************/
function cutString($string, $length) {
   $strcut = '';
   $strLength = 0;
   if(strlen($string) > $length) {
       //将$length换算成实际UTF8格式编码下字符串的长度
       for($i = 0; $i < $length; $i++) {
           if ( $strLength >= strlen($string) )
               break;
           //当检测到一个中文字符时
           if( ord($string[$strLength]) > 127 )
               $strLength += 3;
           else
               $strLength += 1;
       }
       return substr($string, 0, $strLength);
   } else {
       return $string;
   }
}
?>