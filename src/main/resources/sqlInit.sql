create table Task(
    id INT AUTO_INCREMENT unique not null,
    numberOfItem INT not null,
    income DOUBLE default 0,
    paid INT default 0 not null,
    completed INT default 0 not null,
    totalTime DOUBLE default 0,
    createDate DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL,
    endDate DATETIME DEFAULT null,
    primary key (id)
);

create table TaskProgress(
    id INT AUTO_INCREMENT unique not null,
    taskId INT not null,
    startTime DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL,
    endTime DATETIME DEFAULT NULL,
    primary key(id),
    foreign key(taskId) references Task(id) on delete cascade on update cascade
);

create table CurrentTask(
    id INT auto_increment unique not null,
    taskId INT not null,
    primary key(id),
    foreign key(taskId) references Task(id) on delete cascade on update cascade
);