<?php

/**
 * Created by PhpStorm.
 * User: john
 * Date: 2015/9/29
 * Time: 9:56
 */
class User
{
    public $id;
    public $name;
    public $password;

    function __construct($id, $name, $password)
    {
        $this->id = $id;
        $this->name = $name;
        $this->password = $password;
    }

    public static function getJson($value)
    {
        if (!$value) {
            return null;
        }
        $user = User::getUser($value);
        if ($user) {
            return json_encode($user, JSON_UNESCAPED_UNICODE);
        } else {
            return null;
        }
    }

    public static function getUser($value)
    {
        if ($value) {
            return @new User($value["id"], $value["name"], $value["password"]);
        } else {
            return null;
        }
    }

}