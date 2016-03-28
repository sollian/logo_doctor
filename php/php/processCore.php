<?php
/**
 * Created by PhpStorm.
 * User: john
 * Date: 2015/10/15
 * Time: 14:57
 */
set_time_limit(0);
if (is_array($_GET) && count($_GET) > 0) {
    if (isset($_GET["path"])) {
        $logoPath = $_GET["path"];
    }
}
echo $logoPath;
if (isset($logoPath)) {
    //system("mysift.exe " . $logoPath);
    system("mysurf.exe " . $logoPath);
}
