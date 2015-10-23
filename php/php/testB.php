<?php
/**
 * Created by PhpStorm.
 * User: john
 * Date: 2015/10/15
 * Time: 14:41
 */
if (is_array($_GET) && count($_GET) > 0) {
    if (isset($_GET["user"])) {
        $user = $_GET["user"];
    }
}

if(isset($user)) {
    $content = $user;
} else {
    $content = "no user";
}
system("testout.exe");
//$fp = fopen("test.txt", "w");
//fwrite($fp, $content);
//fclose($fp);