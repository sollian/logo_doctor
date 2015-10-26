<?php
/**
 * Created by PhpStorm.
 * User: john
 * Date: 2015/10/15
 * Time: 14:40
 */
//$url = "http://localhost/logodoctor/php/testB.php";
$fp = fsockopen("localhost", 80);
fputs($fp, "GET /logodoctor/php/testB.php?user=sollian\r\n\r\n");
fclose($fp);


//$params = $_SESSION['SIMULATION'];
//foreach ($params as $key => &$val) {
//    if (is_array($val)) $val = implode(',', $val);
//    $post_params[] = $key.'='.urlencode($val);
//}
//$post_string = implode('&', $post_params);
//$parts=parse_url($url);
//$fp = fsockopen($parts['host'],isset($parts['port'])?$parts['port']:80,$errno, $errstr, 30);
//$out = "GET ".$parts['path']." HTTP/1.1\r\n";
//$out.= "Host: ".$parts['host']."\r\n";
//$out.= "Content-Type: application/x-www-form-urlencoded\r\n";
//$out.= "Content-Length: ".strlen($post_string)."\r\n";
//$out.= "Connection: Close\r\n\r\n";
//if (isset($post_string)) $out.= $post_string;
//fwrite($fp, $out);
//fclose($fp);