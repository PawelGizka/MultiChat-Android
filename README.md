# GSenger-Android

It is a multi chat application. This app was created only for learning purpose. 

Application feauteres:
- send message to individual person,
- informing user whether and when message was delivered and viewed,
- possibility to login with facebook,
- synchronize contacts with local Contacts book and facebook friends (if user was logged in using facebook),
- send text, photos, videos and files,
- creating group chat and ability to add other users to existing group chat,
- each user can set their own user name and image,

GSenger uses REST and Json based protocol to comunicate with remote Server. MVP is used ad main pattern, therefor app has
clean and testable architecture. OAuth authentication is used in order to allow users to login with facebook. 

App uses following libraries: 
- Realm - persistance
- Dagger 2 - dependency injection
- Retrofit - safe http requests
- Android Priority Job Queue - scheduling bacground jobs
- Glide - downloading, displaying and caching images
- ButterKnife - binding views
- Event Bus - delivering events
- Mockito - mocking dependencies during unit tests

Application server code is available here: https://github.com/PawelGizka/GSenger-Server.
