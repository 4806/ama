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

The app is deployed using Heroku to http://sysc4806-ama.herokuapp.com/

## Next Milestone

The next iteration of this application will be the final iteration. This will complete the AMA
application. For this final iteration, users will be able to follow and unfollow other users.
They will be provided with more advanced controls for searching AMAs and questions. Users will
also have notifications for events such as receiving a question on one of their AMAs or for
answers to their questions.

#### Follow User

###### Precondition

The AMA user is logged in and is not currently following the desired user

###### Postcondition

The AMA user is now following another user

###### Description

1. An AMA user selects to "follow" another AMA user

#### Unfollow User

###### Precondition

The AMA user is logged in and is currently following the desired user

###### Postcondition

The AMA user is no longer following the desired user

###### Description

1. The AMA user selects to "unfollow" a user they are following

#### Set AMA Permissions

###### Precondition

An AMA Subject is logged in and has created an AMA

###### Postcondition

Permissions have been added to an AMA

###### Description

1. The AMA subject selects to make an AMA private
2. The AMA subject allows specific users to view their AMA

#### View User Profile

###### Precondition

The AMA user is logged in

###### Postcondition

The AMA user is viewing their profile

###### Description

1. The AMA user select to view their profile
2. The AMA user is presented with their profile


#### Notification for Question on AMA

###### Precondition 

An AMA subject has created an AMA

###### Postcondition

The AMA subject has a notification about a question

###### Description

1. An AMA user creates a question on an AMA subject's page

#### Notification for Answered Question

###### Precondition

An AMA user has created a question

###### Postcondition

The AMA user has a new notification on their profile

###### Description

1. An AMA user creates a question
2. An AMA subject answers their question


