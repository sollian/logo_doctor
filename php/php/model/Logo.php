<?php

/**
 * Created by PhpStorm.
 * User: john
 * Date: 2015/9/19
 * Time: 9:04
 */
class Logo
{
    public $id;
    public $name;
    public $img;
    public $extra;
    public $desc;
    public $category;

    function __construct($id, $name, $img, $extra, $desc, $category)
    {
        $this->id = $id;
        $this->name = $name;
        $this->img = $img;
        $this->extra = $extra;
        $this->desc = $desc;
        $this->category = $category;
    }

    public static function getJsons($arr)
    {
        if (!$arr) {
            return null;
        }
        $logos = Logo::getLogos($arr);
        if ($logos) {
            return json_encode($logos, JSON_UNESCAPED_UNICODE);
        } else {
            return "[]";
        }
    }

    public static function getJson($value)
    {
        if (!$value) {
            return null;
        }
        $logo = Logo::getLogo($value);
        if ($logo) {
            return json_encode($logo, JSON_UNESCAPED_UNICODE);
        } else {
            return null;
        }
    }

    public static function getLogos($arr)
    {
        if (!$arr) {
            return null;
        }
        foreach ($arr as $value) {
            $logos[] = Logo::getLogo($value);
        }
        if (isset($logos)) {
            return $logos;
        } else {
            return null;
        }
    }

    public static function getLogo($value)
    {
        if ($value) {
            return @new Logo($value["id"], $value["name"], $value["img"], $value["extra"],
                $value["description"], $value["category"]);
        } else {
            return null;
        }
    }
}