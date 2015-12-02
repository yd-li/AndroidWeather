<?PHP   

$username = "neoskywa_project";  
$password = "project";  
$dbname = "neoskywa_projectx";  
$servername = "neoskywalker7.com";  

//connect
$conn = new mysqli($servername, $username, $password, $dbname);
if(!$conn) {
		die('Could not connect: ' . mysql_error());
}

//check if unused email
$check_email = "SELECT email FROM user WHERE email = $'email' ";
if (!mysql_query($check_email)) {
		echo "This email is in using.";
}

//check if unused uname
$check_uname = "SELECT uname FROM user WHERE uname = $'uname' ";
if (!mysql_query($check_email)) {
		echo "This username is in using.";
}

//sign up
$sign_up = "INSERT INTO user (uid, uname, email, signdate, passwd, bio)
		VALUES ($'uid', $'uname', $'email', $'signdate', $passwd', $'bio')";
if (mysql_query($sign_up)) {
		echo "Successfully signed up!";
} else {
		echo "";
}

$conn->close();
?>
