<?php
/**
 * Created by PhpStorm.
 * User: john
 * Date: 2015/9/21
 * Time: 20:06
 */
include_once "utils/Tables.php";
include_once "utils/MySql.php";
include_once "model/History.php";

define("QUERY_ALL", "SELECT * FROM " . TABLE_HISTORY . " WHERE `userName`=");
define("QUERY_BY_MINID", "SELECT * FROM " . TABLE_HISTORY . " WHERE id<");
define("ORDER", " ORDER BY createTime DESC");
define("LIMIT", " LIMIT 10");

if (is_array($_GET) && count($_GET) > 0) {
    if (isset($_GET["maxId"])) {
        $maxId = $_GET["maxId"];
    }
    if (isset($_GET["user"])) {
        $user = $_GET["user"];
    }
}

if (!isset($user)) {
    return;
}

$mysql = new MySql();

if (isset($maxId) && $maxId > 0) {
    $result = $mysql->query(QUERY_BY_MINID . $maxId . " AND `userName`=" . "'$user'" . ORDER . LIMIT);
} else {
    $result = $mysql->query(QUERY_ALL . "'$user'" . ORDER . LIMIT);
}
$arr = $mysql->fetchall($result);
echo History::getJsons($arr);