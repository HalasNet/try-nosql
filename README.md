try-nosql
=========

Example social graph and activities feed using NoSQL/RabbitMQ.

A simple model consisting of:
- user / profile (subset of OpenSocial)
- followers
- activities (subset of OpenSocial)

Research into using NoSQL (MongoDB and CouchDB) to model above.

Research into using queue for procesing activities and producing user activity feeds.

Program components as follows:
- two services: Social service and Activity service
- data holders: person, person name, plural field, relationship, activity
- dao interfaces: person, relationship, job, activity
- dao implementations: mongodb, couchdb
- job: activity processor (using RabbitMQ)

Tests exercise various components.

Also a load test which simulates a social application.

Concurrently, performs following operations:
- creates people (i.e. register)
- follows people
- posts activities
- generates activity feeds

Reports:
- operations per second, for each above operation
- for mongo and couch (mongo owns couch, btw)
