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
        <a href="/market"> Return on market </a>
    </nav>
</header>
<main>

    <#if pokemonUser?size == 0>
        <p> Vous ne pouvez pas faire d'echange car vous n'avez pas de pokemons a echanger</p>
    </#if>
    <form action="/market/exchange" method="post">

        <label for="pokemonExchange">Witch pokemon would you want to exchanged?</label>
        <select name="pokemonExchange">
            <option value="">--Please choose an option--</option>
            <#list pokemonUser as pokemonName>
                <option value=${pokemonName.idPokemon}>${pokemonName.name} ${pokemonName.level}</option>
            </#list>
        </select>
        <input type="hidden" value="${idExchange}" name="idExchange">
        <input type="submit" value="Exchange">
    </form>

</main>
</body>
</html>