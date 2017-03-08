# AMA Tool

[![Build Status](https://travis-ci.com/4806/ama.svg?token=MPuUxtfuLzmhXtay93BR&branch=master)](https://travis-ci.com/4806/ama)

## Project Description

The purpose of this project is to create an Ask-Me-Anything (AMA) tool. This tool will allow a user to 
create a new AMA. This AMA can be configured with options such as the deadline for submitting questions,
open to the public, or open to a restricted list of users. Once an AMA is created, it will become visible
to other users. These users can view the AMA, ask questions, and vote on questions they like or dislike. 
A user can only upvote or downvote a given question once.

No questions can be asked once the AMA deadline is reached, and voting on questions is disabled. An AMA can 
also be terminated before the specified deadline manually by the AMA creator. A user who has voted on a
question or asked their own question will be notified when those questions are answered. A user can also be
notified when answers to an AMA are posted by 'following' that AMA. Once an AMA has been closed, it is 
archived and remains searchable.

To vote and ask questions, a user must create a profile. They will be able to view their profile to see which
AMAs they have liked or created, and which other users they follow. Users can view other user profiles to see
past activity, and can 'follow' them to be notified when that user creates an AMA. Users will have various 
ways to sort AMAs, such as most recent, most questions asked, or AMAs created by 'Most Followed' users.

## Project Backlog

A list of current issues in the project backlog can be found [here](https://github.com/4806/ama/milestone/1)

## Deployment

The app is deployed using Heroku to https://sysc4806-ama.herokuapp.com/

## Next Milestone

This iteration introduced AMAs and questions to the system. However, there is currently no notion of a user
or any security within the system. The next iteration will focus on adding this feature including a user
login, user profile and user sessions. The specific use cases for the next milestone are listed below. Once
the user has been created then the use cases for creating questions and AMAs will be updated to that only
logged in users can create them and are then the author of the question or AMA.

#### User Creation

###### Precondition

An AMA site visitor currently does not have an account with this AMA site

###### Postcondition

The AMA site visitor now has an account and is an AMA user

###### Description

1. The visitor selects to create a new account with the system
2. The visitor enters some basic account information and chooses a unique name
3. The visitor accepts the terms of the ama site
4. The visitor receives a confirmation that their account has been created


#### User Login

###### Precondition

The given visitor has an account and is not currently logged in

###### Postcondition

The given visitor is now logged into the system and has a user session

###### Description

1. The visitor clicks to "Login" to the system
2. The visitor enters their account name and password
3. The visitor receives confirmation that they have logged in


#### User Logout

###### Precondition

The AMA user is currently logged in

###### Postcondition

The AMA user is no longer logged in

###### Description

1. The user clicks the "Logout" button
2. The user receives confirmation that they have been logged out




