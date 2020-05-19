<!DOCTYPE html>

<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"></meta>
    <title>Web Checkers | ${title}</title>
    <link rel="stylesheet" type="text/css" href="/css/style.css">
</head>

<body>
<div class="page">

    <h1>Web Checkers | ${title}</h1>

    <div class="body">

        <!-- Provide a message to the user, if supplied. -->
        <#include "message.ftl" />
        <form action="/signin" method="POST">
            Please enter an alphanumeric name (spaces allowed).
            <br/>
            <input name="userName" />
            <br/><br/>
            <button type="submit">Log In</button>
        </form>


    </div>

</div>
</body>

</html>
