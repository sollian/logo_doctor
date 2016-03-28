<?php
/**
 * Created by PhpStorm.
 * User: john
 * Date: 2015/10/26
 * Time: 17:18
 */
include_once "utils/Tables.php";
include_once "utils/MySql.php";
include_once "model/History.php";

define("DELETE_HISTORY_BY_ID", "DELETE FROM " . TABLE_HISTORY . " WHERE `id` IN ");
define("DELETE_HISTORY_BY_USER", "DELETE FROM " . TABLE_HISTORY . " WHERE `userName`=");


if (is_array($_GET) && count($_GET) > 0) {
    if (isset($_GET["ids"])) {
        $ids = $_GET["ids"];
    }
    if (isset($_GET["user"])) {
        $userName = $_GET["user"];
    }
}

if (isset($ids)) {
    $sql = DELETE_HISTORY_BY_ID . "(" . $ids . ")";
} else if (isset($userName)) {
    $sql = DELETE_HISTORY_BY_USER . '"' . $userName . '"';
    //删除用户历史文件夹
    $historyDir = "../img/history/" . $userName;
    deldir($historyDir);
}

if (isset($sql)) {
    //删除用户历史
    $mysql = new MySql();
    $mysql->query($sql);
    echo $mysql->affectedrows();
}


function deldir($dir)
{
    //先删除目录下的文件：
    $dh = opendir($dir);
    while ($file = readdir($dh)) {
        if ($file != "." && $file != "..") {
            $fullpath = $dir . "/" . $file;
            if (!is_dir($fullpath)) {
                unlink($fullpath);
            } else {
                deldir($fullpath);
            }
        }
    }
    closedir($dh);
    //删除当前文件夹：
    if (rmdir($dir)) {
        return true;
    } else {
        return false;
    }
}