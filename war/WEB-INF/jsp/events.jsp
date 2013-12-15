<tags:layout title="Hello ${userUuid}!">

<jsp:body>
    <h1 class="page-header">These are your events</h1>
    <p>Logged in as <a href="https://www.koliseo.com/${userUuid}" target="_blank">${userUuid}</a>

    <form action="/events" method="post" class="form-inline">
        <p>
        <input name="eventName" placeholder="Introduce an event name and hit enter" class="span4">
        <input type="submit" class="btn btn-primary" value="Create event">
        <a href="/logout" class="btn">Log out</a>
    </form>

    <ul class="unstyled">
    <c:forEach items="${it.data}" var="event">
      <li class="event row">
        <div class="span2">
          <p><a href="https://www.koliseo.com/${event.uuid}" class="thumbnail" style="background-image:url(${event.photo.url}=s400)">${event.name}</a>
        </div>
        <div class="span10 description">
          <p>
          <a href="https://www.koliseo.com/${event.uuid}">${event.name}</a>
          <br><small>${event.description}</small>
        </div>
      </li>
    </c:forEach>
    </ul>

</jsp:body>

</tags:layout>