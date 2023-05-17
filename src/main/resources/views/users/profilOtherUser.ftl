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
        <a href="/profil"> Return on my profil </a>

    </nav>
</header>
<main>
    <#if pokemon?size == 0>
        <p>No pokemon in database, please click on freePokemon for add your first pokemon in your list.</p>
    </#if>

    <#list pokemon as pokemons>
        <li>${pokemons.idPokemon} , ${pokemons.name} <img src=${pokemons.sprite} size="100%"> ${pokemons.level}</li>
        <form action="/profil/${user.userName}" method="post">
            <input name="idPokemon" value="${pokemons.idPokemon}" hidden="true">
            <input value="Level up" type="submit">
        </form>
    </#list>
</main>
</body>
</html>