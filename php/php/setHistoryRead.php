<?php
/**
 * Created by PhpStorm.
 * User: john
 * Date: 2015/9/22
 * Time: 15:05
 */
include_once "utils/Tables.php";
include_once "utils/MySql.php";

define("SET_READ", "UPDATE " . TABLE_HISTORY . " SET `isRead` = '1' WHERE `id`=");

if (is_array($_GET) && count($_GET) > 0) {
    if (isset($_GET["id"])) {
        $id = $_GET["id"];
    }
}

$mysql = new MySql();

if (isset($id) && $id > 0) {
    $mysql->query(SET_READ . $id);
    echo $mysql->affectedrows();
} else {
    echo "参数错误";
}