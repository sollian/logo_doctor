<?php
/**
 * Created by PhpStorm.哈哈
 * User: john
 * Date: 2015/9/18
 * Time: 19:34
 */
include_once "Tables.php";
include_once "MySql.php";

define("QUERY_ALL", "select * from ");

echo "哈哈哈<br/>";

$mysql = new MySql();
$result = $mysql->query(QUERY_ALL . TABLE_LOGO);
echo "success";
$arr = $mysql->fetchall($result);
foreach ($arr as $value) {
    echo '<pre>';
    print_r($value);
    echo '</pre>';
}