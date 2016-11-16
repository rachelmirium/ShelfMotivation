DROP DATABASE if exists ShelfMotivation;

CREATE DATABASE ShelfMotivation;

USE ShelfMotivation;

CREATE TABLE UserAccounts (
  userID int(11) primary key not null auto_increment,
  username varchar(50) not null,
  pw varchar(50) not null,
  fName varchar(50) not null,
  lName varchar(50) not null,
  doNotDisturb boolean not null,
  lastLogin varchar(50) not null
);

CREATE TABLE UserShelves (
  userID int(11) primary key not null,
  bookID varchar(50) not null
);

CREATE TABLE UserGoals (
  userID int(11) primary key not null,
  bookID varchar(50) not null,
  goalDate varchar(50) not null
);

CREATE TABLE BookClubAccounts (
  bcID int(11) primary key not null auto_increment,
  bcName varchar(50) not null,
  admin varchar(50) not null,
  dateCreated varchar(50) not null,
  currentBookID varchar(50) not null
);

CREATE TABLE BookClubMembers (
  bcID int(11) primary key not null,
  username varchar(50) not null,
  dateAdded varchar(50) not null
);

CREATE TABLE BookClubChat (
  bcID int(11) primary key not null,
  username varchar(50) not null,
  timeSent varchar(50) not null,
  message varchar(255)
);

