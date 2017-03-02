# AMA Tool

[![Build Status](https://travis-ci.com/4806/ama.svg?token=MPuUxtfuLzmhXtay93BR&branch=master)](https://travis-ci.com/4806/ama)

##Project Description

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

To vote and ask questions, a user must create a profile. They will be able to view their profile to see a 
history of their interactions, such as upvoting or asking questions. Users can view other user profiles to see past activity,
and can 'follow' them to be notifified when that user creates an AMA. Users will have various ways to sort
AMAs, such as most recent,most questions asked, or AMAs created by 'Most Followed' users.

   
## Project BackLog

A list of current issues in the project backlog can be found [here](https://github.com/4806/ama/milestone/1)

## Deployment

The app is deployed using heroku to https://sysc4806-ama.herokuapp.com/

