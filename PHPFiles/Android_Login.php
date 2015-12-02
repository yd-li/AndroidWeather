<?php
    $con = mysqli_connect("localhost", "neoskywa_project", "project", "neoskywa_projectx");

    $uname = $_POST["uname"];
    $passwd = $_POST["passwd"];

    $statement = mysqli_prepare($con, "SELECT * FROM user WHERE uname = ? AND passwd = ?");
    mysqli_stmt_bind_param($statement, "ss", $uname, $passwd);
    mysqli_stmt_execute($statement);

    mysqli_stmt_store_result($statement);
    mysqli_stmt_bind_result($statement, $uid, $uname, $email, $passwd);

    $user = array();

    while(mysqli_stmt_fetch($statement)) {
    	$user["uid"] = $uid;
        $user["uname"] = $uname;
	$user["email"] = $email;
        $user["passwd"] = $passwd;
    }

    echo json_encode($user);
    mysqli_close($con);
?>
