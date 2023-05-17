<#ftl encoding="utf-8">
<html lang="en">
<header>

</header>

<main>
    <head>
        <nav>
            <a href="/register"> Register </a>
            <a href="/login"> Login</a>
        </nav>
    </head>
    <body xmlns="http://www.w3.org/1999/html">
    <form action="/login" method="post">
        <label for="userName">Username :</label>
        <input name="userName" id="userName" type="text">
        <label for="password">Password :</label>
        <input name="password" id="password" type="text">
        <input type="submit" value="OK" id="submit">
        <input type="reset" value="Reset">
    </form>

    <#if msg?has_content>
        <p> ${msg}</p>
    </#if>
    </body>
</main>
</html>