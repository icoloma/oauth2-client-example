<tags:layout title="Hello world!">

<jsp:body>
    <h1 class="page-header">An example of an authorized request to the Koliseo API</h1>
    <p>Logged in as <a href="https://www.koliseo.com/${userUuid}" target="_blank">${userUuid}</a>

    <form action="/create" method="post" class="form-inline">
        <p>
        <input name="eventName" placeholder="Introduce an event name and hit enter" class="span4">
        <input type="submit" class="btn" value="Create event">
    </form>
    <ul>
    <c:forEach items="${it.data}" var="event">
        <li><a href="https://www.koliseo.com/${event.uuid}">${event.name}</a>
    </c:forEach>
    </ul>

</jsp:body>

</tags:layout>