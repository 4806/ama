# SYSC 4806 - Project Requirements

### Description

A user can create an AMA by entering a bit of preamble text and set a few parameters (such as the
topic tags/keywords, the deadline, is it “open” to the public or “restricted” to a given supplied
list). Users can ask a question or vote “up” or “down” existing questions. The questions can be
listed in increasing or decreasing order of votes, and they dynamically move up or down as the
votes are in. A user can vote on many questions in an AMA, but cannot vote more than once on any
given question (that’s ballot stuffing!). When the deadline is reached, no more votes are allowed,
and the user who created the AMA can now answer them by another deadline that is known to the
participants. All AMAs (active and archived) that are public are searchable by keywords/tags,
posted date, deadline, creator’s name/handle. User can “like” a given AMA and “follow” users, and
so they can also search for most “liked” AMAs, or AMAs posted by the most “followed” users, or
restrict their search to only those users they follow. One can also lookup a user profile and see
which AMAs they’ve created or liked, and which other users they follow.

## Process

1. Identify actors 
    - Identify the different types of users of the future system
    - Which user groups are supported by the system to perform their work? 
    - Which user groups execute the system’s main functions?
    - Which user groups perform secondary functions, such as maintenance and administration? 
    - Will the system interact with any external hardware or software system?
2. Identify scenarios 
    - Identify scenarios for typical functionalities 
3. Identify use cases 
    - Abstract scenarios into use cases 
4. Identify non-functional requirements
    - Identify aspects visible to the user but not directly related to functionalities 
5. Refine use cases
    - Is the system specification complete (e.g., exceptional conditions) 
6. Identify relationships among use cases
    - Consolidate the use case model by eliminating redundancies

## Identify Actors

- Site Visitor
    - The user has no account 
    - The user is not able to vote
    - The user is not able to comment
    - The user is able to create a new profile
    - The user is able to view public ama posts
- AMA User
    - User will have a unique user handle
    - User will have an account page with site settings
    - User will have a profile for their user account which they can update
    - User is able to view all public views
    - Is able to upvote or downvote proposed questions on user’s walls
    - User can vote on multiple questions but can only vote on a single comment once
    - Can create new questions for an AMA Subject
    - Can follow/unfollow another user
    - Can like/unlike an AMA forum or question
- AMA Subject
    - User will be the subject that will answer a question. 
    - Questions will be posted to this person’s page and they will post the most popular
    - Must be able to view all of the questions that are posted to his/her wall

## Identify Scenarios

Bob is an interesting fellow, his friend suggests that people might be interested in his life. He
goes to the AMA site, and creates a new AMA which is accessible to the public. Jim sees Bob’s AMA
on the list of open AMAs, and decides to participate. He asks Bob a question. Alice and John see
Jim’s question, and decide it is an interesting question. Alice and John decide to vote for Jim’s
question. Jim’s question moves up the list of open questions for this AMA. Bob sees Jim’s
question at the top of the list, and writes an answer. Jim, Alice, and John all see Bob’s answer
and blow air out their noses.

Bob creates another AMA, because people didn’t get enough Bob the first time. Jim asks another
question. This time, Peter sees the question. Peter is a nasty guy, and thinks Jim’s question is
bad. Peter downvotes the question. Bob never sees the question because it is low down on the list,
so he answers more interesting questions.

Michael wants to clear the air about his reputation. He creates a posting to ask him anything.
Mary Sue wants to know if Michael is single or in a relationship. Mary asks "Is Billie Jean still
your girl?". This angers Michael as he has clearly stated that Billie Jean is not his girl.
Michael then deletes his AMA forum.

Arnold’s close friends all want to ask him questions. Arnold is a private person who does not want
to share his answers with everyone. Arnold creates an AMA forum and only invites his close friends
to ask him questions.


## Identify Use Cases
- Create AMA Forum
    1. The AMA Subject selects the “Create New Forum” option
    2. The AMA Subject specifies keywords that should be associated with this forum.
    3. The AMA Subject sets the deadline for the AMA forum
    4. Set a deadline for when you answer the questions, but not as important as deadline for questions
    5. The AMA Subject can choose to make the question public or restricted to a set of users
    6. The AMA Subject reviews and posts the new forum
- Create Question
    1. An AMA User views AMA forum that they have permission to
    2. The AMA User chooses to create a question
    3. The AMA User finishes the question
    4. Answer Question
- Upvote/Downvote Question
    1. An AMA User can upvote or downvote any question
    2. A single user can only vote on a single question once
    3. The user can vote on many questions
- Remove Forum
    1. The AMA Subject can remove their forum
    2. The AMA Admin can remove an AMA Subject’s forum
    3. When a forum is deleted all questions and answers that forum are also deleted
- Remove Question
    1. An AMA User can remove their question
    2. View User Profile
    3. An AMA  User searches for a user with a particular handle.
    4. The AMA User selects a user from a list of matches.
    5. The selected user’s profile is displayed, showing a list of AMAs they’ve created, a list of
        AMAs they’ve liked, and a list of other AMA Users they are following
- Follow User
    1. The AMA User selects the “Follow” option
    2. The selected user is available in AMA User’s list of followed users
- Ban User
    1. The AMA Admin bans a user from a post
    2. The Banned user is no longer able to post comments on the AMA post
- Search by Likes
- Search by User Follows
- Search by Followed Users

## Identify Non-Functional Requirements

## Refine Use Cases

## Identify Use Case Relationships
