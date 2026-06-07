/*
Navicat MySQL Data Transfer

Source Server         : MySQL5.7
Source Server Version : 50741
Source Host           : localhost:3306
Source Database       : gymmaster

Target Server Type    : MYSQL
Target Server Version : 50741
File Encoding         : 65001

Date: 2023-05-09 14:57:49
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for account
-- ----------------------------
DROP TABLE IF EXISTS `account`;
CREATE TABLE `account` (
  `aid` int(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'Account ID',
  `uid` int(4) unsigned NOT NULL COMMENT 'The customer that owns this account',
  `balance` decimal(9,2) NOT NULL DEFAULT '0.00' COMMENT 'How much money is left in the account',
  `method` varchar(255) NOT NULL COMMENT '"Cash","Credit Card"',
  `last_update` date DEFAULT NULL COMMENT 'Last time the account is updated (payment, create, refund)',
  `is_active` tinyint(1) unsigned DEFAULT '0' COMMENT 'Is this account in use/frozen/abandoned',
  PRIMARY KEY (`aid`),
  KEY `uid` (`uid`),
  CONSTRAINT `uid` FOREIGN KEY (`uid`) REFERENCES `customer` (`uid`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of account
-- ----------------------------
INSERT INTO `account` VALUES ('1', '2', '107.80', 'Credit Card', '2023-05-09', '1');
INSERT INTO `account` VALUES ('2', '2', '55.00', 'Cash', '2023-05-09', '1');

-- ----------------------------
-- Table structure for bill
-- ----------------------------
DROP TABLE IF EXISTS `bill`;
CREATE TABLE `bill` (
  `bid` int(4) unsigned NOT NULL AUTO_INCREMENT COMMENT 'Use UUID() function to create a unique series(for security concern)',
  `uid` int(4) unsigned NOT NULL COMMENT 'Customer who own this bill',
  `fname` varchar(20) NOT NULL COMMENT 'Facility name',
  `vname` varchar(20) NOT NULL COMMENT 'Venue name',
  `figure` decimal(9,2) unsigned NOT NULL COMMENT 'Use DECIMAL to store high precision value of money (for calculation accuracy',
  `bdate` datetime(6) NOT NULL COMMENT 'Transaction date',
  `operator` varchar(20) NOT NULL DEFAULT 'system' COMMENT '"GymMaster" or name of employee if payment is on-site',
  `brid` int(4) NOT NULL COMMENT 'foreign key to reservation table',
  PRIMARY KEY (`bid`),
  KEY `Buid` (`uid`),
  CONSTRAINT `Buid` FOREIGN KEY (`uid`) REFERENCES `customer` (`uid`)
) ENGINE=InnoDB AUTO_INCREMENT=28 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of bill
-- ----------------------------
INSERT INTO `bill` VALUES ('1', '1', 'pingpang', 'a', '30.00', '2023-04-04 21:44:43.000000', 'system', '1');
INSERT INTO `bill` VALUES ('2', '1', 'pingpang', 'pp1', '10.56', '2022-02-11 22:09:02.000000', 'system', '1');
INSERT INTO `bill` VALUES ('3', '1', 'pingpang', 'pp1', '20.00', '2022-02-11 22:09:43.000000', 'system', '1');
INSERT INTO `bill` VALUES ('4', '1', 'pingpang', 'pp1', '20.00', '2023-04-05 22:11:02.000000', 'system', '1');
INSERT INTO `bill` VALUES ('5', '1', 'BADMINTON', 'OO1', '15.00', '2023-04-05 22:11:45.000000', 'system', '1');
INSERT INTO `bill` VALUES ('6', '1', 'BADMINTON', 'OO1', '15.00', '2023-04-03 22:11:59.000000', 'system', '1');
INSERT INTO `bill` VALUES ('7', '1', 'TENNIS', 'TT1', '15.63', '2023-04-03 22:12:45.000000', 'system', '1');
INSERT INTO `bill` VALUES ('8', '1', 'TENNIS', 'TT1', '19.63', '2023-04-03 22:13:08.000000', 'system', '1');
INSERT INTO `bill` VALUES ('9', '1', 'TENNIS', 'TT1', '19.63', '2023-04-03 22:15:12.000000', 'system', '1');
INSERT INTO `bill` VALUES ('10', '2', 'Swimming pool', 'Main pool', '5.00', '2023-05-07 18:55:19.000000', 'system', '18');
INSERT INTO `bill` VALUES ('11', '2', 'Tennis Court', 'Squash 1', '15.00', '2023-05-07 18:55:20.000000', 'system', '19');
INSERT INTO `bill` VALUES ('12', '2', 'Swimming pool', 'Main pool', '5.00', '2023-05-07 18:59:01.000000', 'system', '20');
INSERT INTO `bill` VALUES ('13', '2', 'Tennis Court', 'Squash 1', '15.00', '2023-05-07 18:59:01.000000', 'system', '21');
INSERT INTO `bill` VALUES ('14', '2', 'Swimming pool', 'Main pool', '5.00', '2023-05-07 19:01:27.000000', 'system', '22');
INSERT INTO `bill` VALUES ('15', '2', 'Tennis Court', 'Squash 1', '15.00', '2023-05-07 19:01:42.000000', 'system', '23');
INSERT INTO `bill` VALUES ('16', '2', 'Swimming pool', 'Main pool', '5.00', '2023-05-07 19:03:20.000000', 'system', '24');
INSERT INTO `bill` VALUES ('17', '2', 'Tennis Court', 'Squash 1', '15.00', '2023-05-07 19:03:23.000000', 'system', '25');
INSERT INTO `bill` VALUES ('18', '2', 'Swimming pool', 'Main pool', '5.00', '2023-05-07 19:06:01.000000', 'system', '26');
INSERT INTO `bill` VALUES ('19', '2', 'Tennis Court', 'Squash 1', '15.00', '2023-05-07 19:06:03.000000', 'system', '27');
INSERT INTO `bill` VALUES ('20', '2', 'Squash court', 'Squash 1', '25.00', '2023-05-08 14:12:47.000000', 'system', '28');
INSERT INTO `bill` VALUES ('21', '2', 'Swimming pool', 'Main pool', '5.00', '2023-05-08 14:12:47.000000', 'system', '29');
INSERT INTO `bill` VALUES ('22', '2', 'Squash court', 'Squash 1', '25.00', '2023-05-08 14:13:31.000000', 'system', '30');
INSERT INTO `bill` VALUES ('23', '2', 'Swimming pool', 'Main pool', '5.00', '2023-05-08 14:14:54.000000', 'system', '32');
INSERT INTO `bill` VALUES ('24', '2', 'Swimming pool', 'Main pool', '5.00', '2023-05-09 11:32:52.000000', 'system', '33');
INSERT INTO `bill` VALUES ('25', '2', 'Tennis Court', 'Squash 1', '15.00', '2023-05-09 14:14:43.000000', 'system', '34');
INSERT INTO `bill` VALUES ('26', '2', 'Football field', 'Football 1', '8.00', '2023-05-09 14:37:50.000000', 'system', '35');
INSERT INTO `bill` VALUES ('27', '2', 'Sports hall', 'Squash 1', '20.00', '2023-05-09 14:37:50.000000', 'system', '36');

-- ----------------------------
-- Table structure for coach
-- ----------------------------
DROP TABLE IF EXISTS `coach`;
CREATE TABLE `coach` (
  `coaid` int(4) unsigned NOT NULL AUTO_INCREMENT COMMENT 'Coach ID',
  `username` varchar(20) NOT NULL COMMENT 'Coach username(within 20 characters)',
  `password` varchar(255) NOT NULL COMMENT 'Password encryptrd with MD5',
  `profile` varchar(255) DEFAULT NULL COMMENT 'File path of coach profile',
  `intro` varchar(255) NOT NULL DEFAULT 'Hello!' COMMENT 'File path of coach profile',
  `first_name` varchar(20) DEFAULT 'Coach' COMMENT 'First name within 20 characters',
  `last_name` varchar(20) DEFAULT 'Coach' COMMENT 'Last name within 20 characters',
  `certifications` varchar(255) DEFAULT NULL COMMENT 'Certification the coach has, ex.swimming, football, basketball, etc,',
  `salary` decimal(9,2) unsigned DEFAULT '0.00' COMMENT 'Salary is about money, use decimal for percisons',
  PRIMARY KEY (`coaid`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of coach
-- ----------------------------
INSERT INTO `coach` VALUES ('5', 'Tennis_John', 'aaaa', 'tennis_default.jpg', 'Whether you\'re a beginner or an advanced player, I am dedicated to helping you improve your technique, strategy, and overall performance on the court.', 'John', 'Smith', 'Tennis', '5000.00');
INSERT INTO `coach` VALUES ('6', 'Basketball_Tom', 'bbbb', 'Basketball_default.jpg', 'With a focus on strategy, teamwork, and individual improvement, I am committed to helping players of all levels reach their full potential. Get ready to take your game to the next level with our expert coach.', 'Tom', 'Brain', 'Basketball,fitness ', '3000.00');
INSERT INTO `coach` VALUES ('7', 'Football_Jack', 'cccc', 'Football_default.jpg', 'Experienced football coach with a passion for the game. Committed to developing players, building team morale and achieving success.', 'Matthew', 'Jack', 'Football', '6000.00');
INSERT INTO `coach` VALUES ('8', 'General_Jane', 'dddd', 'General_default.png', 'Hi, I\'m a general coach ready to help you achieve your goals. Let\'s work together to overcome obstacles and reach your full potential.', 'Joan', 'Jane', 'General', '10000.00');
INSERT INTO `coach` VALUES ('9', 'Strength_Stone', 'eeee', 'Strength_default.jpg', 'Hi, I\'m a strength coach committed to helping you build muscle, increase power and improve performance. Let\'s work together to reach your fitness goals.', 'Johnson', 'Stone', 'Strength', '8000.00');
INSERT INTO `coach` VALUES ('10', 'Yoga_Jenny', 'ffff', 'Flex_default.jpg', 'Namaste, I\'m a yoga coach dedicated to guiding you on your journey towards physical, mental, and spiritual well-being. Let\'s flow together and find inner peace.', 'Jenny', 'Jenny', 'Flexibility', '9000.00');
INSERT INTO `coach` VALUES ('11', 'Group_Rachel', 'gggg', 'Group_default.jpg', 'Hi, I\'m a children\'s coach passionate about helping kids learn new skills, build confidence, and have fun. Let\'s play, grow and achieve together.', 'Natasha', 'Rachel', 'Group', '7000.00');

-- ----------------------------
-- Table structure for comments
-- ----------------------------
DROP TABLE IF EXISTS `comments`;
CREATE TABLE `comments` (
  `commentid` int(4) unsigned NOT NULL AUTO_INCREMENT,
  `pid` int(4) NOT NULL,
  `sender` int(4) NOT NULL,
  `sender_type` varchar(255) NOT NULL COMMENT '''customer'',''coach'',''employee''',
  `content` varchar(255) DEFAULT 'This is a comment',
  `datesent` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`commentid`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of comments
-- ----------------------------
INSERT INTO `comments` VALUES ('1', '1', '10', 'Coach', 'Hello! Nice to meet you', '2023-05-02 10:38:56.000000');
INSERT INTO `comments` VALUES ('2', '1', '2', 'customer', 'test', '2023-05-04 23:24:53.228000');
INSERT INTO `comments` VALUES ('3', '3', '2', 'customer', 'comment', '2023-05-09 11:40:14.108000');
INSERT INTO `comments` VALUES ('4', '1', '2', 'customer', 'aaa', '2023-05-09 14:40:08.775000');

-- ----------------------------
-- Table structure for connect
-- ----------------------------
DROP TABLE IF EXISTS `connect`;
CREATE TABLE `connect` (
  `id` int(4) unsigned NOT NULL AUTO_INCREMENT,
  `student` int(4) NOT NULL,
  `course` int(4) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of connect
-- ----------------------------

-- ----------------------------
-- Table structure for course
-- ----------------------------
DROP TABLE IF EXISTS `course`;
CREATE TABLE `course` (
  `couid` int(4) unsigned NOT NULL AUTO_INCREMENT COMMENT 'Course id',
  `coaid` int(4) unsigned NOT NULL COMMENT 'Coach ID as the foreign key',
  `price` int(4) unsigned NOT NULL DEFAULT '0' COMMENT 'Price of the course per hour',
  `type` varchar(255) NOT NULL COMMENT '''General'',''Football'',''Basketball'',''Tennis'',''Strength'', ''Flexibility'',''Group''',
  `description` varchar(255) NOT NULL COMMENT 'Coach can describe the content of the course ',
  `course_facility` int(4) unsigned NOT NULL COMMENT 'Store the primary key of faclity',
  `course_venue` int(4) unsigned NOT NULL COMMENT 'Store the primary key of venue',
  `time` datetime(6) NOT NULL COMMENT 'Starting time(which can be shown on the page)',
  `capability` int(4) unsigned NOT NULL COMMENT 'A number indicating how many people can be hold in the course',
  `cover` varchar(255) NOT NULL COMMENT 'Cover page of the course',
  PRIMARY KEY (`couid`),
  KEY `coaid` (`coaid`),
  CONSTRAINT `coaid` FOREIGN KEY (`coaid`) REFERENCES `coach` (`coaid`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of course
-- ----------------------------
INSERT INTO `course` VALUES ('1', '5', '15', 'Tennis', 'Whether you\'re a beginner or an experienced player, we\'ll help you improve your skills, technique, and strategy. Let\'s get on the court and start playing!', '6', '1', '2023-05-24 15:00:00.000000', '10', '1.jpg');
INSERT INTO `course` VALUES ('2', '6', '5', 'Basketball', 'Our expert coaches will guide you through drills, exercises, and games to help you reach your full potential on the court. Let\'s hoop it up together!', '7', '1', '2023-05-10 10:00:00.000000', '20', '2.jpg');
INSERT INTO `course` VALUES ('3', '7', '10', 'Football', 'Are you ready to take your football skills to the next level? Our football course is designed to help players of all levels. Join us on the field and let\'s elevate your performance together!', '8', '1', '2023-05-29 16:00:00.000000', '20', '3.jpg');
INSERT INTO `course` VALUES ('4', '8', '10', 'General', 'Looking to get in shape and feel your best? Our fitness course is designed to help you achieve your health and wellness goals. Let\'s sweat, burn calories, and transform your body and mind!', '3', '1', '2023-05-23 19:00:00.000000', '30', '4.jpg');
INSERT INTO `course` VALUES ('5', '10', '20', 'Flexibility', 'Are you looking to improve your physical, mental, and spiritual well-being? Our yoga course is designed to help you achieve balance and harmony in your life. ', '5', '1', '2023-05-15 09:00:00.000000', '25', '5.jpg');
INSERT INTO `course` VALUES ('6', '9', '10', 'Strength', 'Are you ready to get strong? Our strength course is designed to help you build muscle, increase power, and improve your overall fitness.', '3', '1', '2023-05-11 14:00:00.000000', '20', '6.jpg');
INSERT INTO `course` VALUES ('7', '11', '15', 'Group', 'Looking for a fun and educational activity for your child? Our group children course offers a safe and supportive environment for kids to learn new skills, build confidence, and make friends. ', '5', '1', '2023-05-10 17:00:00.000000', '30', '7.jpg');

-- ----------------------------
-- Table structure for customer
-- ----------------------------
DROP TABLE IF EXISTS `customer`;
CREATE TABLE `customer` (
  `uid` int(4) unsigned NOT NULL AUTO_INCREMENT,
  `username` varchar(20) NOT NULL,
  `password` varchar(255) NOT NULL,
  `profile` varchar(255) NOT NULL,
  `first_name` varchar(20) NOT NULL,
  `last_name` varchar(20) NOT NULL,
  `email` varchar(100) NOT NULL,
  `gender` int(11) DEFAULT '0' COMMENT '0:unknown,1:male,2:female',
  `joindate` date DEFAULT NULL,
  `membership` varchar(255) NOT NULL DEFAULT 'free trail' COMMENT '''free trail'',''copper member'',''silver member'',''gold member''',
  `expiredate` date DEFAULT '9999-01-01',
  PRIMARY KEY (`uid`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of customer
-- ----------------------------
INSERT INTO `customer` VALUES ('1', 'John', '$2a$10$vYai9cSjsT6/Vjlbhzi7nuo6AqM2o5BhI2TCLpnPVgEmeGLZdv1FG', 'john.jpg', 'Matthew', 'John', 'john@example.com', '0', '2023-03-01', 'free trail', '9999-01-01');
INSERT INTO `customer` VALUES ('2', 'test', '$2a$10$FVUl9qeLvalMrd86VxM6AOO94AlabFC8BFsGWtmCk9KrIfTiPyxVC', 'default.png', 'Joh', 'Tom', 'test@example.com', '1', '2023-05-04', 'gold member', '2023-06-08');
INSERT INTO `customer` VALUES ('6', 'demo', '$2a$10$y78.YUgt3LJuOjK5fby/0OHHrdZR5INeQIrIX6RMnjt2VPPvS.aaS', 'default.png', 'demo', 'demo', 'demo@example.com', '0', '2023-05-08', 'free trail', '9999-01-01');

-- ----------------------------
-- Table structure for employee
-- ----------------------------
DROP TABLE IF EXISTS `employee`;
CREATE TABLE `employee` (
  `eid` int(4) unsigned NOT NULL AUTO_INCREMENT,
  `username` varchar(20) NOT NULL,
  `password` varchar(255) NOT NULL,
  `phone` varchar(11) NOT NULL,
  `e_mail` varchar(50) NOT NULL,
  `profile` varchar(255) NOT NULL,
  PRIMARY KEY (`eid`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of employee
-- ----------------------------
INSERT INTO `employee` VALUES ('2', 'test2', 'test1', 'test1', 'test1', 'test1');
INSERT INTO `employee` VALUES ('3', 'test3', 'test1', 'test1', 'test1', 'test1');

-- ----------------------------
-- Table structure for facility
-- ----------------------------
DROP TABLE IF EXISTS `facility`;
CREATE TABLE `facility` (
  `fid` int(4) unsigned NOT NULL AUTO_INCREMENT,
  `recommend` varchar(255) NOT NULL COMMENT '''Yes'',''No''',
  `vacancy` int(4) NOT NULL,
  `sales` int(4) NOT NULL,
  `location` varchar(50) NOT NULL DEFAULT 'GymMaster sports center',
  `fname` varchar(20) NOT NULL DEFAULT 'Faclity_X',
  `add_date` date NOT NULL,
  `description` varchar(255) NOT NULL DEFAULT 'new facility',
  `profile` varchar(255) NOT NULL DEFAULT 'default.jpg',
  `phone` varchar(11) DEFAULT '13800000000',
  PRIMARY KEY (`fid`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of facility
-- ----------------------------
INSERT INTO `facility` VALUES ('2', 'Yes', '1', '1', 'Indoor centre', 'Swimming pool', '2023-03-25', 'Experience aquatic bliss at GymMaster\'s stunning swimming pool, the perfect addition to your fitness journey.', 'swimming.jpg', '13800000000');
INSERT INTO `facility` VALUES ('3', 'No', '1', '0', 'Main Building', 'Fitness room', '2023-04-01', 'Unleash your inner athlete in our cutting-edge fitness room, equipped with top-of-the-line equipment and personalized training programs.', 'fitness_room.jpg', '13800000000');
INSERT INTO `facility` VALUES ('4', 'Yes', '4', '1', 'St.John\'s hall', 'Squash court', '2023-04-04', 'Take your game to the next level in our sleek and modern squash courts, the ultimate arena for competitive play.', 'Squash_room.jpg', '13800000000');
INSERT INTO `facility` VALUES ('5', 'No', '1', '0', 'Main centre', 'Sports hall', '2023-04-15', 'Score big in our versatile sports hall, offering endless opportunities for team sports, group fitness, and individual training.', 'hall.jpg', '13800000000');
INSERT INTO `facility` VALUES ('6', 'Yes', '6', '0', 'Central avenue', 'Tennis Court', '2023-04-04', 'Welcome to our tennis court! Our state-of-the-art facility offers a premier playing experience with top-of-the-line equipment and amenities. Come serve, volley, and smash with us!', 'tennis.jpg', '13800000000');
INSERT INTO `facility` VALUES ('7', 'No', '4', '1', 'Basketball hall', 'Basketball court', '2023-02-20', 'Our top-notch facility is perfect for players of all skill levels. Shoot some hoops, work on your dribbling, and play a game with friends. Let\'s basketball!', 'basketball.jpg', '13800000000');
INSERT INTO `facility` VALUES ('8', 'No', '2', '0', 'Outdoor', 'Football field', '2023-03-15', 'Welcome to our football court! Our field is designed to provide an exceptional playing experience for players of all ages and skill levels. Come kick, pass, and score with us!', 'football.jpg', '13800000000');

-- ----------------------------
-- Table structure for goal
-- ----------------------------
DROP TABLE IF EXISTS `goal`;
CREATE TABLE `goal` (
  `gid` int(4) unsigned NOT NULL AUTO_INCREMENT,
  `uid` int(4) NOT NULL,
  `height` int(4) DEFAULT '0',
  `weight` int(4) DEFAULT '0',
  `goal_weight` int(4) DEFAULT '0',
  `week_goal` int(4) DEFAULT '0',
  `target` varchar(200) NOT NULL DEFAULT 'describe your goal here',
  PRIMARY KEY (`gid`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of goal
-- ----------------------------
INSERT INTO `goal` VALUES ('1', '0', '0', '0', '0', '0', 'System initial');
INSERT INTO `goal` VALUES ('2', '3', '0', '0', '0', '0', 'System initial');
INSERT INTO `goal` VALUES ('3', '4', '0', '0', '0', '0', 'System initial');
INSERT INTO `goal` VALUES ('4', '5', '0', '0', '0', '0', 'System initial');
INSERT INTO `goal` VALUES ('5', '2', '181', '61', '71', '6', 'Hello world! Workout workout');
INSERT INTO `goal` VALUES ('6', '6', '0', '0', '0', '0', 'System initial');

-- ----------------------------
-- Table structure for logs
-- ----------------------------
DROP TABLE IF EXISTS `logs`;
CREATE TABLE `logs` (
  `lid` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `uid` int(4) NOT NULL,
  `uri` varchar(255) NOT NULL,
  `log_date` datetime NOT NULL,
  `operation` varchar(255) NOT NULL,
  PRIMARY KEY (`lid`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of logs
-- ----------------------------
INSERT INTO `logs` VALUES ('1', '2', 'static/pdfPackage/ce717d70-8ca4-49af-beb2-b6b720dd8210.pdf', '2023-05-08 10:01:10', 'static/billQR/3631b85d-793d-454b-9603-5894e6766cb8.jpg');
INSERT INTO `logs` VALUES ('2', '2', 'static/pdfPackage/555d99dd-b506-4ab0-aa1d-5a8fbe81c398.pdf', '2023-05-08 10:02:54', 'static/billQR/3bda0873-6847-4d22-a533-8541a21bf9f7.jpg');
INSERT INTO `logs` VALUES ('3', '2', 'static/pdfPackage/306326ed-42f2-4432-8b3d-d34bf2bdea77.pdf', '2023-05-09 11:33:49', 'static/billQR/f5633144-0f0c-45d1-861c-6667bf9d1ee1.jpg');
INSERT INTO `logs` VALUES ('4', '2', 'static/pdfPackage/41a38ee4-6f2a-4fbc-810b-1f7789d1bdf0.pdf', '2023-05-09 11:34:30', 'static/billQR/787ad889-5368-4a5c-956a-419e328a6f48.jpg');
INSERT INTO `logs` VALUES ('5', '2', 'static/pdfPackage/2f0ee14f-a1a2-48e0-b60c-f267966012d4.pdf', '2023-05-09 14:32:02', 'static/billQR/fecc0af4-b774-44ef-98ea-8da1f219ee06.jpg');

-- ----------------------------
-- Table structure for manager
-- ----------------------------
DROP TABLE IF EXISTS `manager`;
CREATE TABLE `manager` (
  `mid` int(4) unsigned NOT NULL AUTO_INCREMENT,
  `username` varchar(20) NOT NULL,
  `password` varchar(255) NOT NULL,
  `phone` varchar(11) NOT NULL,
  `email` varchar(50) NOT NULL,
  `profile` varchar(255) NOT NULL,
  PRIMARY KEY (`mid`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of manager
-- ----------------------------
INSERT INTO `manager` VALUES ('1', 'General', 'aaa', '13800000000', 'manager@example.com', 'general.jgp');

-- ----------------------------
-- Table structure for notice
-- ----------------------------
DROP TABLE IF EXISTS `notice`;
CREATE TABLE `notice` (
  `nid` int(4) unsigned NOT NULL AUTO_INCREMENT,
  `publisher` int(4) NOT NULL,
  `publisher_type` varchar(255) NOT NULL DEFAULT 'employee' COMMENT '''employee'',''manager''',
  `title` varchar(50) NOT NULL,
  `content` varchar(255) NOT NULL,
  `notice_media` varchar(255) DEFAULT NULL,
  `notice_date` datetime(6) NOT NULL,
  PRIMARY KEY (`nid`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of notice
-- ----------------------------
INSERT INTO `notice` VALUES ('1', '2', 'employee', 'Open Discount', 'Join us now ! As we are just openning, a 50% discount is being offred!', 'discount.jpg', '2023-03-24 13:47:53.000000');
INSERT INTO `notice` VALUES ('2', '1', 'manager', 'New Manager is here!', 'Hi everyone! This is a simple hello from your new manager!', 'manager.jpg', '2023-04-11 13:48:59.000000');

-- ----------------------------
-- Table structure for posts
-- ----------------------------
DROP TABLE IF EXISTS `posts`;
CREATE TABLE `posts` (
  `pid` int(4) unsigned NOT NULL AUTO_INCREMENT,
  `author` int(4) NOT NULL,
  `type` varchar(255) NOT NULL COMMENT '''customer'',''coach'',''employee'',''manager''',
  `content` varchar(255) NOT NULL,
  `media` varchar(255) NOT NULL,
  `datesent` datetime(6) NOT NULL,
  PRIMARY KEY (`pid`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of posts
-- ----------------------------
INSERT INTO `posts` VALUES ('1', '1', 'Customer', 'Hello everyone at the gym! I just wanted to take a moment to introduce myself and say hello. My name is John, and I\'m excited to be joining this community of fitness enthusiasts.', 'Customer_1_1.jpg', '2023-03-25 10:21:44.000000');
INSERT INTO `posts` VALUES ('2', '10', 'Coach', 'I\'ve been practicing yoga for 5 years and have seen firsthand the incredible benefits it can bring to our physical and mental well-being. ', 'Coach_10_2.jpg', '2023-03-30 10:24:13.000000');
INSERT INTO `posts` VALUES ('3', '2', 'customer', 'hello ', 'bda653ac-5b7e-4040-8bc3-57eb472c072f.JPG', '2023-05-07 22:44:13.772000');
INSERT INTO `posts` VALUES ('10', '2', 'customer', 'My second posts', '80a88289-1945-4b5e-a2a9-9ddb5862a6e3.jpg', '2023-05-07 23:05:08.210000');
INSERT INTO `posts` VALUES ('11', '2', 'customer', 'Refresh the page after sending a post', 'f19db93d-d3c2-477a-a90c-4fc6b6cba33f.jpg', '2023-05-07 23:08:15.972000');
INSERT INTO `posts` VALUES ('12', '2', 'customer', 'hhhhh', '1248d18d-a9a6-4a30-86d4-81637aacba6b.jpg', '2023-05-09 14:39:04.373000');
INSERT INTO `posts` VALUES ('13', '2', 'customer', 'hhhh', '11dc30da-68bc-4f26-a83e-d8932d8e2a76.jpg', '2023-05-09 14:39:29.904000');

-- ----------------------------
-- Table structure for reservation
-- ----------------------------
DROP TABLE IF EXISTS `reservation`;
CREATE TABLE `reservation` (
  `rid` int(4) unsigned NOT NULL AUTO_INCREMENT COMMENT 'Reservation ID, not the same as bill ID',
  `Ruid` int(4) unsigned NOT NULL COMMENT 'Customer (owner) of this reservation',
  `rdate` date NOT NULL COMMENT 'Which date is the reservation on (same activity on different dates)',
  `facility` int(4) unsigned NOT NULL COMMENT 'Which facility (use facility id)',
  `venue` int(4) unsigned NOT NULL COMMENT 'Which venue (use venue id)',
  `period` set('1','2','3','4','5','6','7','0','8') NOT NULL DEFAULT '' COMMENT 'Which time period (8 in total, one hour for each)',
  `payment` varchar(255) DEFAULT 'other' COMMENT '(cash, credit card, other)',
  `status` varchar(255) DEFAULT 'unable' COMMENT ' (vaild, expired, unpayed, unable)',
  `amount` int(4) NOT NULL DEFAULT '1' COMMENT 'the number of venue reserved',
  PRIMARY KEY (`rid`),
  KEY `Ruid` (`Ruid`)
) ENGINE=InnoDB AUTO_INCREMENT=37 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of reservation
-- ----------------------------
INSERT INTO `reservation` VALUES ('26', '2', '2023-05-07', '2', '3', '8', 'account', 'valid', '3');
INSERT INTO `reservation` VALUES ('27', '2', '2023-05-11', '6', '1', '0', 'account', 'valid', '1');
INSERT INTO `reservation` VALUES ('28', '2', '2023-05-08', '4', '1', '3', 'account', 'valid', '1');
INSERT INTO `reservation` VALUES ('29', '2', '2023-05-08', '2', '3', '3', 'account', 'valid', '3');
INSERT INTO `reservation` VALUES ('30', '2', '2023-05-08', '4', '1', '3', 'account', 'valid', '1');
INSERT INTO `reservation` VALUES ('31', '2', '2023-05-08', '2', '3', '3', 'account', 'unpaid', '3');
INSERT INTO `reservation` VALUES ('32', '2', '2023-05-08', '2', '3', '4', 'account', 'valid', '1');
INSERT INTO `reservation` VALUES ('33', '2', '2023-05-09', '2', '3', '6', 'account', 'valid', '1');
INSERT INTO `reservation` VALUES ('34', '2', '2023-05-24', '6', '1', '0', 'account', 'valid', '1');
INSERT INTO `reservation` VALUES ('35', '2', '2023-05-09', '8', '18', '4', 'account', 'valid', '3');
INSERT INTO `reservation` VALUES ('36', '2', '2023-05-15', '5', '1', '0', 'account', 'valid', '1');

-- ----------------------------
-- Table structure for venue
-- ----------------------------
DROP TABLE IF EXISTS `venue`;
CREATE TABLE `venue` (
  `vid` int(4) unsigned NOT NULL AUTO_INCREMENT,
  `fid` int(4) NOT NULL DEFAULT '-1',
  `vname` varchar(20) NOT NULL DEFAULT 'NULL',
  `price` int(4) NOT NULL DEFAULT '50',
  `description` varchar(255) NOT NULL DEFAULT 'new venue',
  `profile` varchar(255) NOT NULL DEFAULT 'default.jpg',
  `status` varchar(255) NOT NULL DEFAULT 'unknown' COMMENT '''available'',''maintenance'',''booked'',''unknown''',
  `capacity` int(4) NOT NULL DEFAULT '0',
  PRIMARY KEY (`vid`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of venue
-- ----------------------------
INSERT INTO `venue` VALUES ('1', '4', 'Squash 1', '30', 'The room features a soundproofing system, which helps to reduce noise and distractions from other parts of the gym, allowing players to focus on their game.', 'squash1.jpg', 'available', '4');
INSERT INTO `venue` VALUES ('2', '4', 'Squash 2', '30', 'The court is surrounded by a cushioned perimeter, which provides a safe and comfortable playing surface for players of all skill levels.', 'squash2.jpg', 'available', '4');
INSERT INTO `venue` VALUES ('3', '2', 'Main pool', '5', 'Looking for a fun and effective way to stay active and healthy? Come swim with us at GymMaster! Our state-of-the-art swimming pool is the perfect place to cool off, relax, and get a great workout. ', 'pool.jpg', 'available', '32');
INSERT INTO `venue` VALUES ('4', '3', 'Fitness room', '8', 'Fitness room: a space equipped with exercise machines and equipment for physical training and workouts', 'fitness.jpg', 'available', '25');
INSERT INTO `venue` VALUES ('5', '4', 'Squash Women', '20', 'Female only! a small enclosed court for playing the game of squash, usually with two players', 'squash3.jpg', 'available', '4');
INSERT INTO `venue` VALUES ('6', '4', 'Squash new', '25', 'a space designed for high-intensity racquet sport, offering a great cardiovascular workout.', 'squash4.jpg', 'available', '4');
INSERT INTO `venue` VALUES ('7', '5', 'Sports hall', '5', 'a large indoor area for playing various sports, such as basketball, volleyball, and badminton.', 'hall.jpg', 'available', '20');
INSERT INTO `venue` VALUES ('8', '6', 'Tennis 1', '15', 'an outdoor or indoor space with a net for playing tennis, a popular racket sport.', 'tennis1.jpg', 'available', '2');
INSERT INTO `venue` VALUES ('9', '6', 'Tennis 2', '20', 'a flat surface with defined lines for playing tennis, usually in pairs.', 'tennis2.jpg', 'available', '2');
INSERT INTO `venue` VALUES ('10', '6', 'Tennis 3', '12', 'a space for playing singles or doubles tennis, with different surfaces and lighting options.', 'tennis3.jpg', 'available', '2');
INSERT INTO `venue` VALUES ('11', '6', 'Tennis 4', '15', 'a venue for tournaments, clinics, and lessons, with specialized equipment and accessories.', 'tennis4.jpg', 'available', '2');
INSERT INTO `venue` VALUES ('12', '6', 'Tennis 5', '15', 'a place for improving physical fitness, coordination, and mental agility through tennis.', 'tennis5.jpg', 'available', '2');
INSERT INTO `venue` VALUES ('13', '6', 'Tennis 6', '20', 'a social and competitive environment for players of all ages and skill levels.', 'tennis6.jpg', 'available', '2');
INSERT INTO `venue` VALUES ('14', '7', 'Basketball James', '10', 'a flat surface with hoops and lines for playing basketball, a team sport.', 'basketball1.jpg', 'available', '10');
INSERT INTO `venue` VALUES ('15', '7', 'Basketball John', '10', 'an indoor or outdoor space for practicing shooting, dribbling, and passing skills.', 'basketball2.jpg', 'available', '10');
INSERT INTO `venue` VALUES ('16', '7', 'Basketball Luis', '10', 'a venue for games, tournaments, and events, with seating and scoreboard options.', 'basketball3.jpg', 'available', '10');
INSERT INTO `venue` VALUES ('17', '7', 'Basketball 4', '10', 'a place for improving physical fitness, endurance, and teamwork through basketball.', 'basketball4.jpg', 'available', '10');
INSERT INTO `venue` VALUES ('18', '8', 'Football 1', '8', 'a grass or turf field with goalposts for playing soccer, a popular team sport.', 'football1.jpg', 'available', '11');
INSERT INTO `venue` VALUES ('19', '8', 'Football 2', '8', 'aa space for training, scrimmages, and games, with various field sizes and surfaces.', 'football2.jpg', 'available', '11');
