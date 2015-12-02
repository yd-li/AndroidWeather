<?php
    $con = mysqli_connect("localhost", "neoskywa_project", "project", "neoskywa_projectx");
    
    $uid= $_POST["uid"];
	$status = $_POST["status"];
	$moodindex = $_POST["mood"];
    
    $statement = mysqli_prepare($con, "INSERT INTO post (uid, status, moodindex) VALUES (?, ?, ?)");
    mysqli_stmt_bind_param($statement, "isi", $uid, $status, $moodindex);
    mysqli_stmt_execute($statement);
    mysqli_stmt_close($statement);
    mysqli_close($con);
?>
