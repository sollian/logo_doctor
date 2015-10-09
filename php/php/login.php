<?php
/**
 * Created by PhpStorm.
 * User: john
 * Date: 2015/9/29
 * Time: 10:29
 */
include_once "utils/Tables.php";
include_once "utils/MySql.php";
include_once "model/User.php";

define("QUERY_USER", "SELECT * FROM " . TABLE_USER . " WHERE name=");

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
$sql = QUERY_USER . "'" . $name . "'";
$result = $mysql->query($sql);
$value = $mysql->fetcharray($result);
$user = User::getUser($value);
if ($mysql->affectedrows() <= 0) {
    echo "该用户名不存在";
    return;
}
if ($user->password != $password) {
    echo "密码错误";
    return;
}
echo "";