/* mysql数据库的SQL */

--实体类表
CREATE TABLE `author` (
	`id` INT(11) NOT NULL AUTO_INCREMENT,
	`name` VARCHAR(100) NULL DEFAULT NULL,
	`email` VARCHAR(50) NULL DEFAULT NULL,
	`birth` INT(11) NULL DEFAULT NULL,
	PRIMARY KEY (`id`)
)
COMMENT='论文作者'
COLLATE='latin1_swedish_ci'
ENGINE=InnoDB
AUTO_INCREMENT=4
;

CREATE TABLE `paper` (
	`id` INT(11) NOT NULL AUTO_INCREMENT,
	`doi` VARCHAR(50) NULL DEFAULT NULL,
	`document_id` VARCHAR(50) NULL DEFAULT NULL,
	`publisher` VARCHAR(50) NULL DEFAULT NULL,
	`publication_date` VARCHAR(50) NULL DEFAULT NULL,
	`abstract` VARCHAR(255) NULL DEFAULT NULL,
	`keywords` VARCHAR(100) NULL DEFAULT NULL,
	PRIMARY KEY (`id`)
)
COMMENT='论文详细信息'
COLLATE='latin1_swedish_ci'
ENGINE=InnoDB
AUTO_INCREMENT=2
;

CREATE TABLE `genre` (
	`id` INT(11) NOT NULL AUTO_INCREMENT,
	`type` VARCHAR(255) NULL DEFAULT NULL,
	PRIMARY KEY (`id`)
)
COMMENT='论文类别'
COLLATE='latin1_swedish_ci'
ENGINE=InnoDB
AUTO_INCREMENT=2
;

CREATE TABLE `meeting` (
	`id` INT(11) NOT NULL AUTO_INCREMENT,
	`location` VARCHAR(100) NULL DEFAULT NULL,
	`date` VARCHAR(100) NULL DEFAULT NULL,
	`name` VARCHAR(100) NULL DEFAULT NULL,
	PRIMARY KEY (`id`)
)
COMMENT='论文参与的会议'
COLLATE='latin1_swedish_ci'
ENGINE=InnoDB
AUTO_INCREMENT=2
;

CREATE TABLE `origination` (
	`id` INT(11) NOT NULL AUTO_INCREMENT,
	`name` VARCHAR(255) NULL DEFAULT NULL,
	`location` VARCHAR(255) NULL DEFAULT NULL,
	PRIMARY KEY (`id`)
)
COMMENT='作者属于的组织'
COLLATE='latin1_swedish_ci'
ENGINE=InnoDB
AUTO_INCREMENT=2
;


--关联表，在转入neo4j会转化成相应的relationship
CREATE TABLE `author_paper` (
	`author_id` INT(11) NOT NULL,
	`paper_id` INT(11) NULL DEFAULT NULL,
	UNIQUE INDEX `author_id` (`author_id`),
	INDEX `paper_id` (`paper_id`),
	CONSTRAINT `FK__author_paper_author` FOREIGN KEY (`author_id`) REFERENCES `author` (`id`),
	CONSTRAINT `FK__author_paper_paper` FOREIGN KEY (`paper_id`) REFERENCES `paper` (`id`)
)
COLLATE='latin1_swedish_ci'
ENGINE=InnoDB
;

CREATE TABLE `paper_genre` (
	`paper_id` INT(11) NOT NULL,
	`genre_id` INT(11) NULL DEFAULT NULL,
	UNIQUE INDEX `paper_id` (`paper_id`),
	INDEX `genre_id` (`genre_id`),
	CONSTRAINT `FK__paper_genre_genre` FOREIGN KEY (`genre_id`) REFERENCES `genre` (`id`),
	CONSTRAINT `FK__paper_genre_paper` FOREIGN KEY (`paper_id`) REFERENCES `paper` (`id`)
)
COLLATE='latin1_swedish_ci'
ENGINE=InnoDB
;

CREATE TABLE `paper_meeting` (
	`paper_id` INT(11) NOT NULL,
	`meeting_id` INT(11) NOT NULL,
	UNIQUE INDEX `paper_id` (`paper_id`),
	INDEX `meeting_id` (`meeting_id`),
	CONSTRAINT `FK__paper_meeting_meeting` FOREIGN KEY (`meeting_id`) REFERENCES `meeting` (`id`),
	CONSTRAINT `FK__paper_meeting_paper` FOREIGN KEY (`paper_id`) REFERENCES `paper` (`id`)
)
COLLATE='latin1_swedish_ci'
ENGINE=InnoDB
;

CREATE TABLE `author_origination` (
	`author_id` INT(11) NOT NULL,
	`origination_id` INT(11) NOT NULL,
	UNIQUE INDEX `author_id` (`author_id`),
	INDEX `origination_id` (`origination_id`),
	CONSTRAINT `FK__author_origination_author` FOREIGN KEY (`author_id`) REFERENCES `author` (`id`),
	CONSTRAINT `FK__author_origination_origination` FOREIGN KEY (`origination_id`) REFERENCES `origination` (`id`)
)
COLLATE='latin1_swedish_ci'
ENGINE=InnoDB
;

