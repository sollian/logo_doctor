<?php
/**
 * Created by PhpStorm.
 * User: john
 * Date: 2015/10/15
 * Time: 14:40
 */
//$fp = fsockopen("localhost", 80);
//fputs($fp, "GET /logodoctor/php/testB.php?user=sollian\r\n\r\n");
//fclose($fp);
$fp = fsockopen("localhost", 80);
fputs($fp, "GET /logodoctor/php/processCore.php?path=../img/history/sollian/9a6bad001b11457190355400d1c7e5fb.jpg\r\n\r\n");
fclose($fp);