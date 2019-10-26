<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Wordiada-Login</title>

    <!-- Link the Bootstrap (from twitter) CSS framework in order to use its classes-->
    <link rel="stylesheet" href="common/bootstrap.min.css">

    <!-- Link jQuery JavaScript library in order to use the $ (jQuery) method-->
    <!-- <script src="common/jquery-2.0.3.min.js"></script>-->
    <!-- and\or any other scripts you might need to operate the JSP file behind the scene once it arrives to the client-->
    <style>
        body {
            background: #ffffff url("/wordiada/images/welcomePage.gif");
            background-size:cover;
        }
    </style>
</head>
<body style= "margin:200px -50px">
<div style="height:500px; width:501px;margin: 200px -50px;position:relative;top:20%;left:45%; ">
    <div class="container"; style="height:500px; width:501px";>
        <br/>
        <style>
            h4 {
                display: block;
                font-size: 2em;
                margin-top: 1.33em;
                margin-bottom: 0.33em;
                margin-left: 0;
                margin-right: 0;
                font-weight: bold;
                color: black  ;
                background-color: lightgray;
            }
        </style>
        <br>
        <h4> errorMessage </h4>
        <h4>Please enter a unique user name:</h4>
        <form method="GET" action="pages/menu/MenuPage" style="margin: 0px 30px">
            &emsp;<input type="text" name="username" class=""/>
            <br>
            &emsp; A computer player?&emsp;&emsp;&ensp;
            <input type="checkbox" name="isComputer" value="true" >
            <br>
            &emsp; &emsp;&emsp;&emsp;<input type="submit" value="Login"/>
        </form>
    </div>
</div>
</body>
</html>