<tags:layout title="Hello world!">

<jsp:body>
    <h1 class="page-header">An example of an authorized request to the Koliseo API</h1>
    <p>This is a list of discounts configured in your Koliseo account

    <ul>
    <c:for items="${discounts}" var="discount">
        <li><a href="https://www.koliseo.com/${user.uuid}#discounts/${discount.uuid}">${discount.name}</a>
    </c:for>
    </ul>

</jsp:body>

</tags:layout>