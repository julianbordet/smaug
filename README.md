# smaug

Smaug is a (fullstack) in-development bug tracker MVC web app that I'm currently developing as my first portfolio project. Written in java, it leverages Springboot, Spring Security, Hibernate and Thymeleaf (as well as mySQL for CRUD operations).

Deployed on Heroku:
www.smaug.cc
(test credentials= user: DemoDev, password: "password".

Current features:
- Login authentication using Spring Security.
- Dashboard page with user-specific statistics about numbers of bugs pending, due, etc.
- Creation, update and deletion of "Projects".
- Adding/Removing developers to Projects.
- Creation/Editing/Removing of Bugs.
- "Update History" tracker for each bug, that keeps track of what changes have been made to the bug and who made the changes.
- Frontend and backend validation of user input.
