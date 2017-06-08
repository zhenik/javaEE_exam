[![Build Status](https://travis-ci.com/NikitaZhevnitskiy/javaEE_exam.svg?token=6FYqXrfAk2ZHo34Tq8Gp&branch=master)](https://travis-ci.com/NikitaZhevnitskiy/javaEE_exam)

# 48 hours home exam. Java EE
* Student: Nikita Zhevnitskiy
* Subject: PG5100-1 17V Enterpriseprogrammering 1

This repository contains exam delivery of javaEE application (enterprise system).

https://github.com/NikitaZhevnitskiy/javaEE_exam.git

#### Exercises
I have done with all E1-E9 exercise
 
#### Project structure
Exam module contains 3 modules: backend, frontend, report  
Packages are named intuitive.


#### Tests
All tests from task (E2) locates in backend module, at the beginning of RestaurantEJBTest.java  
They are named the same way.  

All tests from task (E7) locates in frontend module in MyCantinaIT.  

Rest tests needs to check additional functionality and constraints.  
(E8) Coverage report total: `96%` 


## Extra functionality (E9)
* User roles (nonRegistered, Customer, Chef)  
`nonRegistered user:`  
can: login, register;  
cannot: see/interact with dishes,menus,menu list  
`Customer user:`  
can: logout, only see menu and travers among them  
cannot: see/interact with dishes, menu list, create menu  
`Chef user:`  
can: create entities(dish, menu), delete entity menu, travers all pages on webapp  

* menuList page (for chef only)  
`list menus`  
`remove menu` 

* additional tests for user entity and selenium for extra functionality
Arquillian tests UserEJBTest in backend module  
Arquillian tests at the end of class RestaurantEJBTest in backend module  
Selenium tests WebPageIT in frontend module  

* travis for Arquillian test



