<?php

/**
 * Created by PhpStorm.
 * User: john
 * Date: 2015/9/23
 * Time: 14:52
 */
include_once "model/History.php";
include_once "utils/Tables.php";
include_once "utils/MySql.php";

if (is_array($_GET) && count($_GET) > 0) {
    if (isset($_GET["user"])) {
        $user = $_GET["user"];
    }
}

if (!isset($user)) {
    echo "未知用户";
} else {
    $img_path = "/img/history/" . $user . "/";
    $target_path = ".." . $img_path;

    if (!is_dir($target_path)) {
        mkdir($target_path);//如果不存在目录，则建立
    }

    $img_path = $img_path . ($_FILES['file']['name']);
    $target_path = ".." . $img_path;
    $target_path = iconv("UTF-8", "gb2312", $target_path);
    if (!move_uploaded_file($_FILES['file']['tmp_name'], $target_path)) {
        echo $target_path;
        echo "服务器保存文件失败";
    } else {
        chmod($target_path, 511);
        $history = new History(null, null, $img_path, null, null, null);
        /**
         * 插入
         */
        $sql = $history->getInsertSql(TABLE_HISTORY);
        $mysql = new MySql();
        $mysql->query($sql);
        /**
         * 查询
         */
        $sql = "SELECT `id` FROM " . TABLE_HISTORY . " WHERE img='" . $img_path . "'";
        $result = $mysql->query($sql);
        $value = $mysql->fetcharray($result);
        $history = History::getHistory($value);
        echo $history->id;
        /**
         * 开启匹配程序
         */
        $fp = fsockopen("localhost", 80);
        fputs($fp, "GET /logodoctor/php/processCore.php?path=$target_path\r\n\r\n");
        fclose($fp);
    }
}