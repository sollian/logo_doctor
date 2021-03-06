<?php
/**
 * Created by PhpStorm.哈哈
 * User: john
 * Date: 2015/9/18
 * Time: 19:34
 */
include_once "utils/Tables.php";
include_once "utils/MySql.php";
include_once "model/Logo.php";

define("QUERY_ALL", "SELECT `id`, `name`, `img`, `extra`, `category` FROM " . TABLE_LOGO);
define("QUERY_ID", "SELECT * FROM " . TABLE_LOGO . " WHERE id=");

if (is_array($_GET) && count($_GET) > 0) {
    if (isset($_GET["id"])) {
        $id = $_GET["id"];
    }
}

$mysql = new MySql();
if (isset($id)) {
    $result = $mysql->query(QUERY_ID . $id);
    $value = $mysql->fetcharray($result);
    echo Logo::getJson($value);
} else {
    $result = $mysql->query(QUERY_ALL);
    $arr = $mysql->fetchall($result);
    echo Logo::getJsons($arr);
}
