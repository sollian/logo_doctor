<?php
/**
 * Created by PhpStorm.
 * User: john
 * Date: 2015/9/29
 * Time: 10:01
 */
include_once "utils/Tables.php";
include_once "utils/MySql.php";

define("REGISTER", "INSERT INTO " . TABLE_USER . " (`name`, `password`) VALUE ");
define("QUERY_ALL", "SELECT `id` FROM " . TABLE_USER . " WHERE name=");

if (is_array($_GET) && count($_GET) > 0) {
    if (isset($_GET["name"])) {
        $name = $_GET["name"];
    }
    if (isset($_GET["password"])) {
        $password = $_GET["password"];
    }
}

if (!isset($name)) {
    echo "用户名不能为空";
    return;
}
if (!isset($password)) {
    echo "密码不能为空";
    return;
}

$mysql = new MySql();
$sql = QUERY_ALL . "'" . $name . "'";
$mysql->query($sql);
$result = $mysql->affectedrows();
if ($result >= 1) {
    echo "该用户名已被注册";
    return;
}

$sql = REGISTER . "('" . $name . "', '" . $password . "')";
$mysql->query($sql);
$result = $mysql->affectedrows();
echo $result;