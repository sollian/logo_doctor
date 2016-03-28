<?php

/**
 * Created by PhpStorm.
 * User: john
 * Date: 2015/9/23
 * Time: 14:52
 */
include_once "utils/Tables.php";
include_once "utils/MySql.php";

if (is_array($_GET) && count($_GET) > 0) {
    if (isset($_GET["id"])) {
        $id = $_GET["id"];
    }
    if (isset($_GET["name"])) {
        $name = $_GET["name"];
    }
    if (isset($_GET["description"])) {
        $description = $_GET["description"];
    }
    if (isset($_GET["extra"])) {
        $extra = $_GET["extra"];
    }
    if (isset($_GET["category"])) {
        $category = $_GET["category"];
    }
}

$img_path = "/img/logo/upload/";
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

    $mysql = new MySql();
    if (isset($name)) {
        $sql = "SELECT `id` FROM " . TABLE_LOGO . " WHERE `name`='$name'";
        $result = $mysql->query($sql);
        if ($mysql->affectedrows() > 0) {
            $value = $mysql->fetcharray($result);
            $id = $value["id"];
        }
    }
    if (isset($id)) {
        /**
         * 获取
         */
        $sql = "SELECT `moreImg` FROM " . TABLE_LOGO . " WHERE `id`=" . $id;

        $result = $mysql->query($sql);
        $value = $mysql->fetcharray($result);
        $moreImg = $value["moreImg"];
        if ($moreImg) {
            $moreImg = $moreImg . "," . $img_path;
        } else {
            $moreImg = $img_path;
        }
        /**
         * 更新
         */
        $sql = "UPDATE " . TABLE_LOGO . " SET `moreImg`='$moreImg' WHERE `id`=" . $id;
    } else {
        if (!isset($extra)) {
            $extra = NULL;
        }
        //插入
        $sql = "INSERT INTO " . TABLE_LOGO
            . " (`name`, `img`, `extra`, `description`, `category`) VALUES ('$name','$img_path','$extra','$description','$category')";
    }
    $result = $mysql->query($sql);
    $affectedRows = $mysql->affectedrows();
    if ($affectedRows > 0) {
        $file = fopen("../logos.txt", "a");
        fputs($file, "\r\n" . $target_path);
        fclose($file);
    }
    echo $affectedRows;
}