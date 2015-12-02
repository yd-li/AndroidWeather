<?php
    $con = mysqli_connect("localhost", "neoskywa_project", "project", "neoskywa_projectx");
    
    $uid= $_POST["uid"];
    $lat = $_POST["lat"];
    $lng = $_POST["lng"];
    $status = $_POST["status"];
    $moodindex = $_POST["mood"];
    
    $statement = mysqli_prepare($con, "INSERT INTO post (uid, status, moodindex, lat, lng) VALUES (?, ?, ?, ?, ?)");
    mysqli_stmt_bind_param($statement, "isidd", $uid, $status, $moodindex, $lat, $lng);
    mysqli_stmt_execute($statement);
    mysqli_stmt_close($statement);
    mysqli_close($con);
?>