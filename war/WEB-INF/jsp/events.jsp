<tags:layout title="Hello ${userUuid}!">

<jsp:body>
    <h1 class="page-header">These are your events</h1>
    <p>Logged in as <a href="https://www.koliseo.com/${userUuid}" target="_blank">${userUuid}</a>

    <form action="/events" method="post" class="form-inline">
        <p>
        <input name="eventName" placeholder="Introduce an event name and hit enter" class="span4">
        <select name="background">
            <option class="galaxy" value="/less/bg/galaxy.jpg">Galaxy</option>
            <option class="fire" value="/less/bg/fire.jpg" selected>Fire</option>
            <option class="guard" value="/less/bg/guard.jpg">Guard</option>
            <option class="city" value="/less/bg/city.jpg">City</option>
        </select>

        <input type="submit" class="btn btn-primary" value="Create event">
        <a href="/logout" class="btn">Log out</a>
    </form>
    <p>
    <c:forEach items="${it.data}" var="event">
        <a href="https://www.koliseo.com/${event.uuid}" class="thumbnail" style="background-image:url(https://www.koliseo.com${event.bg.thumbnail})"> ${event.name}</a>
    </c:forEach>
    </p>

</jsp:body>

</tags:layout>