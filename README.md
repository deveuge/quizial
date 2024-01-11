# Quizial
![Quizial Banner](preview.png)
_Spring CRUD Application - Create online quizzes, share them with your friends and discover your identity through engaging personality quizzes from other users_

## Features
* CRUD application that allows you to generate online tests, share them and take quizzes created by other people to get a result based on your answers.
* Initial login and registration page. 
	* Non-registered users can only access and take quizzes that have already been created, but cannot create new quizzes.
	* To verify the registration, a confirmation email will be sent to the email address provided during the signup process.
	* Password recovery functionality by sending a reset link to the email with which the account was created.
	* Functionality to remember credentials in order to keep the session open.
* Dashboard with the quizzes available in the application with the possibility to see the most popular, the newest or those created by friends. Customised searches can also be performed.
* Ability to take quizzes, with the option to reset progress, add to favourites or share on social networks.
* Personal user page that displays your created quizzes, those you have added to favourites and other users you have added as friends.
* Ability to create or edit existing quizzes, specifying up to 4 possible outcomes and up to 10 questions per quiz, with up to 4 answers each.
	* The test can have a header image, which will be uploaded to the server.
	* Each question and answer can have an illustrative image, which can be specified by URL or picked up from a GIPHY search thanks to an internal integration. 
![GIPHY Integration](preview-giphy.gif)

## Technical aspects

### Frontend
* Use of [UIKit](https://getuikit.com/) to implement a front-end library other than the classic Bootstrap.
* Use of [PrettyTime](https://www.ocpsoft.org/prettytime/) as an OpenSource time formatting library.
* Use of [Thymeleaf](https://www.thymeleaf.org/index.html) as a templating language as it has excellent integration with Spring.

### Backend
* Use of JPA Criteria Queries for database queries, extending CrudRepository and creating own implementations for custom methods with Spring Data JPA.
* JPA MetaModel Generation with Hibernate.
* DTO to Entity Mapping via MapStruct.
* Automatic column audition: CreatedAt, UpdatedAt and user modifications are saved automatically.
* Automatic entity audition: @PrePersist, @PreUpdate, @PreRemove to control the creation, modification and deletion of entities.
* Customised implementation of permission verification through a custom security expression with Spring Security.
* Integration with GIPHY to search for GIFs from within the application through its [search endpoint](https://developers.giphy.com/docs/api/endpoint/#search).
* Data mapping in the GIPHY integration via Jackson.
* Custom implementation to preserve transient fields for preupdate (through @Postmerge).
* Use of @SecondaryTable on view to calculate total sum of favourites.
* Exclusion of tables from hibernate autogeneration (used for the views).
* Application logging using Spring AOP (Aspect-Oriented Programming).
* Use of Lombok to prevent repetitive code generation.

## Future improvements
The project can be improved on several points:
* Add complex search filters.
* Allow editing of user data in the profile.
* Add an administration console and the ability to report quizzes and users.