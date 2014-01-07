oauth2-client-example
==================

Example OAuth2 client application using Jersey and Guice on Google AppEngine. This application logs into Koliseo to perform restricted actions as a user. It can be changed easily to log into any other authentication provider.

To get started, download gradle >= 1.3 and execute <code>gradle eclipse</code> or <code>gradle idea</code> and import the resulting project into your IDE.

The project compiles your classes directly on <code>war/WEB-INF/classes</code>, so you can just launch and open your browser.

A live demo is available [here](http://oauth2-client-example.appspot.com/)

[![Analytics](https://ga-beacon.appspot.com/UA-3159223-5/icoloma/oauth2-client-example)](https://github.com/icoloma/oauth2-client-example)


Customize this example
======================

This demo app uses the following System properties that you may configure in app.yaml or as launcher properties (using -Dname=value):

* `auth_hostname`: The authorization server name, by default https://www.koliseo.com
* `auth_client_id`: The client ID to use. You can create one in your user profile page at https://www.koliseo.com
* `auth_client_secret`: The secret assigned to your client application.
* `auth_url` and `auth_accesstoken_url`: The authorization url and access token url. They have been configured to use Koliseo, but you can change them to any other provider.

The resources in this application are all defined in `Root.java`, including the OAUth dance and the request of restricted resources.
