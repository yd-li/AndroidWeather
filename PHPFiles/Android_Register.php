<?php
    $con = mysqli_connect("neoskywalker7.com", "neoskywa_project", "project", "neoskywa_projectx");
    
    $uname = $_POST["uname"];
    $email = $_POST["email"];
    $passwd = $_POST["passwd"];
    $bio = $_POST["bio"];
    
    $statement = mysqli_prepare($con, "INSERT INTO user (uname, email, passwd, bio) VALUES (?, ?, ?, ?)");
    mysqli_stmt_bind_param($statement, "ssss", $uname, $email, $passwd, $bio);
    mysqli_stmt_execute($statement);
    mysqli_stmt_close($statement);
    mysqli_close($con);
?>