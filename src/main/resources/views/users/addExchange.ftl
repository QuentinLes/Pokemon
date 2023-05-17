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
    <form action="/market/add" method="post">

        <label for="pokemonExchange">Witch pokemon would you want to exchanged?</label>
        <select name="pokemonExchange">
            <option value="">--Please choose an option--</option>
            <#list pokemonUser as pokemonName>
                <option value=${pokemonName.idPokemon}>${pokemonName.name}</option>
            </#list>
        </select>

        <label for="pokemonWanted">Witch pokemon would you want to exchanged?</label>
        <select name="idPokemon">
            <option value="">--Please choose an option--</option>
            <#list allPokemon as pokemonNames>
                <option value=${pokemonNames.idAPI}>${pokemonNames.name}</option>
            </#list>
        </select>

        <input type="checkbox" name="shiny">
        <label for="shiny">Shiny mandatory ?</label>

        <input type="submit" value="Add Exchange">
    </form>

</main>
</body>
</html>