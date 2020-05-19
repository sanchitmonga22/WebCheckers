<!DOCTYPE html>

<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"></meta>
  <meta http-equiv="refresh" content="10">
  <title>Web Checkers | ${title}</title>
  <link rel="stylesheet" type="text/css" href="/css/style.css">
</head>

<body>
<div class="page">

  <h1>Web Checkers | ${title}</h1>

  <!-- Provide a navigation bar -->
  <#include "nav-bar.ftl" />

  <div class="body">

    <!-- Provide a message to the user, if supplied. -->
    <#include "message.ftl" />

    <h1> Players Online </h1>
      <ul>
        <#if currentUser??>
          <#if (players.size() > 1)>
            <#list players.fetchPlayers() as player>
              <#if (player.equals(currentUser) == false)>
                <li>
                  <form action="/game" method="GET">
                      <input type=submit name=competitor value="${player.getUsername()}">
                  </form>
                </li>
              </#if>
            </#list>
          <#else>
            <li> There are no players online.
          </#if>
        <#else>
            <#if (players.size() > 0)>
              <li> There are ${players.size()} players online.
            <#else>
              <li> There are no players online.
            </#if>
        </#if>
      </ul>

    <h1> Active Matches </h1>
    <ul>
        <#if currentUser??>
            <#if currentMatch??>
                <h2> My Current Match </h2>
                <form action = "/game" method="GET">
                    <input type=submit name=competitor value="${currentMatch.getUsername()}">
                </form>
            </#if>
            <#if matches??>
                <h2> Other Matches </h2>
                <#list matches as challenged, challenger>
                    <#if !currentUser.equals(challenged) && !currentUser.equals(challenger)>
                        <li>
                            <form action = "/spectate/game" method="GET">
                                <input type=submit name=spectate-match value="${challenged.getUsername()} challenged by ${challenger.getUsername()}">
                            </form>
                        </li>
                    </#if>
                </#list>
            <#else>
                <li> There are no matches to spectate. </li>
            </#if>
        <#else>
            <li> Sign in to view active matches! </li>
        </#if>

    </ul>

    <h1> Previous Matches </h1>
    <ul>
        <#if replays??>
            <#list replays as opponents, replay>
                <li>
                    <form action = "/replay/game" method="POST">
                        <input type=submit name=replay-match value="${opponents}">
                    </form>
                </li>
            </#list>
        <#else>
            <li> There are no replays to watch. </li>
        </#if>
    </ul>
  </div>

</div>
</body>

</html>