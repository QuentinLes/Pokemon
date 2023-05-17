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
    <form action="/market/add" method="get">
        <input value="Add exchange" type="submit">
    </form>

    <#if market?size == 0>
        <p>No exchange in database, please click on add exchange if you want to add an exchange.</p>
    </#if>

    <#list market as exchange>
        <li>
            <ul>${exchange.id} , ${exchange.idOwner} </ul>
            <ul>Exchanged : ${exchange.exchangedPokemon.name} <img
                        src=${exchange.exchangedPokemon.sprite} size="100%"> level: ${exchange.exchangedPokemon.level}
            </ul>
            <ul>Wanted : ${exchange.wantedPokemon.name} <img src=${exchange.wantedPokemon.sprite} size="100%"></ul>
        </li>

        <form action="/market/exchange" method="get">
            <input name="idPokemon" value="${exchange.wantedPokemon.idAPI}" type="hidden">
            <input name="id" value="${exchange.id}" type="hidden">
            <input value="Exchange" type="submit">
        </form>
    </#list>
</main>
</body>
</html>