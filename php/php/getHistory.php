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

define("QUERY_ALL", "SELECT * FROM " . TABLE_HISTORY);
define("QUERY_BY_MINID", "SELECT * FROM " . TABLE_HISTORY . " WHERE id>=");
define("LIMIT", " LIMIT 10");
define("ORDER", " ORDER BY createTime DESC");

if (is_array($_GET) && count($_GET) > 0) {
    if (isset($_GET["minId"])) {
        $minId = $_GET["minId"];
    }
}

$mysql = new MySql();

if (isset($minId) && $minId > 0) {
    $result = $mysql->query(QUERY_BY_MINID . $minId . LIMIT . ORDER);
} else {
    $result = $mysql->query(QUERY_ALL . ORDER);
}
$arr = $mysql->fetchall($result);
echo History::getJsons($arr);