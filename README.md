# coffee-shop
Coffee shop app, based on a MongoDB presentation, converted to Spring Boot

I took (and passed) an online MongoDB University course, "M101J: MongoDB for Java Developers". One recommendation was a presentation given by one of their then-employees, which can be found here

https://www.infoq.com/presentations/demo-java-javascript-mongodb

Unfortunately the presentation is a couple of years old, and the versions of some of the technologies have moved on (significant changes to Dropwizard, for example). There are also multiple versions of this presentation around the web, all a little different - no doubt being updated for newer technologies.

I don't know Dropwizard, so I followed the presentation and converted it to Spring Boot (which seems to have all the benefits given for Dropwizard). 

The first commit is practically a like-for-like version of the presentation. It's a demo, done in a hour, to show what can be done with the technologies. Some simple (well, relatively) fixes that can be made to improve the project are:

- use an embedded or fake DB for the unit tests - can't rely on an external running instance
- split out the DB access from the controller, i.e. introduce a service layer
- use the most recent MongoDB Java driver (Document vs BasicDBObject)

I hope to make these changes, but for now this project shows how to create the application using Spring Boot - and remember, it's a demo! :)
