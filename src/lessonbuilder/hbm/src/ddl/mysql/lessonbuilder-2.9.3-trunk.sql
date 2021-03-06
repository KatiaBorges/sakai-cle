alter table lesson_builder_items add showPeerEval bit;
alter table lesson_builder_items add groupOwned bit;
alter table lesson_builder_items add ownerGroups longtext;
alter table lesson_builder_items add attributeString longtext;
    create table lesson_builder_p_eval_results (
        PEER_EVAL_RESULT_ID bigint not null auto_increment,
        PAGE_ID bigint not null,
        TIME_POSTED datetime,
        GRADER varchar(255) not null,
        GRADEE varchar(255) not null,
        ROW_TEXT varchar(255) not null,
        COLUMN_VALUE integer not null,
        SELECTED bit,
        primary key (PEER_EVAL_RESULT_ID)
    );

alter table lesson_builder_pages add groupid varchar(36);

    create table lesson_builder_q_responses (
        id bigint not null auto_increment,
        timeAnswered datetime not null,
        questionId bigint not null,
        userId varchar(255) not null,
        correct bit not null,
        shortanswer longtext,
        multipleChoiceId bigint,
        originalText longtext,
        overridden bit not null,
        points double precision,
        primary key (id)
    );

    create table lesson_builder_qr_totals (
        id bigint not null auto_increment,
        questionId bigint,
        responseId bigint,
        respcount bigint,
        primary key (id)
    );

alter table lesson_builder_student_pages add groupid varchar(36);
alter table lesson_builder_items modify html mediumtext; 
alter table lesson_builder_items modify ownerGroups text;
alter table lesson_builder_items modify gradebookId varchar(100);
alter table lesson_builder_items modify altGradebook varchar(100);

create index lesson_builder_qr_questionId_userId on lesson_builder_q_responses(questionId, userId);
create index lesson_builder_qr_total_qi on lesson_builder_qr_totals(questionId);
create index lesson_builder_qr_questionId on lesson_builder_q_responses(questionId);
