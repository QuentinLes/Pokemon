<#ftl encoding="utf-8">

<body xmlns="http://www.w3.org/1999/html">

<ul>
    <#list users as user>
        <li>${user.id} <a href="profil/${user.userName}"> ${user.userName} </a> ${user.numberPokemon}</li>
    </#list>
</ul>

</body>

</html>
