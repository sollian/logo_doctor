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
}

if (isset($sql)) {
    $mysql = new MySql();
    $mysql->query($sql);
    echo $mysql->affectedrows();
}
