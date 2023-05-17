<#ftl encoding="utf-8">
<html lang="fr">

<head>
    <title> Pokemon</title>
    <meta charset="utf-8">
    <meta name="viewport" content="width = device-width, initial-scale = 1.0">
    <meta name="author" content="Lescure Quentin">
</head>

<body>
<header>
    <nav>
        <a href="/market"> Market </a>
        <a href="/userList"> User </a>
        <form action="/logout" method="post">
            <input type="submit" value="Logout">
        </form>
    </nav>
</header>
<main>
    <form action="/user/freePokemon" method="post">
        <input value="Free pokemon" type="submit">
    </form>

    <#if pokemon?size == 0>
        <p>No pokemon in database, please click on freePokemon for add your first pokemon in your list.</p>
    </#if>

    <#list pokemon as pokemons>
        <li>${pokemons.idPokemon} , ${pokemons.name} <img src=${pokemons.sprite} size="100%"> ${pokemons.level}</li>
        <form action="/profil" method="post">
            <input name="idPokemon" value="${pokemons.idPokemon}" hidden="true">
            <input value="Level up" type="submit">
        </form>
    </#list>
</main>
</body>
</html>