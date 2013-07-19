alter table lesson_builder_groups add siteId varchar(250);
alter table lesson_builder_items add showPeerEval bit;
alter table lesson_builder_items add groupOwned bit;
alter table lesson_builder_items add ownerGroups longvarchar;
alter table lesson_builder_items add attributeString longvarchar;
    create table lesson_builder_p_eval_results (
        PEER_EVAL_RESULT_ID bigint generated by default as identity (start with 1),
        PAGE_ID bigint not null,
        TIME_POSTED timestamp,
        GRADER varchar(255) not null,
        GRADEE varchar(255) not null,
        ROW_TEXT varchar(255) not null,
        COLUMN_VALUE integer not null,
        SELECTED bit,
        primary key (PEER_EVAL_RESULT_ID)
    );

alter table lesson_builder_pages add groupid varchar(36);
    create table lesson_builder_properties (
        id bigint generated by default as identity (start with 1),
        attribute varchar(255) not null,
        value longvarchar,
        primary key (id),
        unique (attribute)
    );

    create table lesson_builder_q_responses (
        id bigint generated by default as identity (start with 1),
        timeAnswered timestamp not null,
        questionId bigint not null,
        userId varchar(255) not null,
        correct bit not null,
        shortanswer longvarchar,
        multipleChoiceId bigint,
        originalText longvarchar,
        overridden bit not null,
        points double,
        primary key (id)
    );

    create table lesson_builder_qr_totals (
        id bigint generated by default as identity (start with 1),
        questionId bigint,
        responseId bigint,
        respcount bigint,
        primary key (id)
    );

alter table lesson_builder_student_pages add groupid varchar(36);
create index lesson_builder_group_site on lesson_builder_groups(siteId);
create index lesson_builder_item_gb on lesson_builder_items(gradebookid);
create index lesson_builder_item_altgb on lesson_builder_items(altGradebook);
create index lesson_builder_prop_idx on lesson_builder_properties(attribute);
create index lesson_builder_qr_questionId_userId on lesson_builder_q_responses(questionId, userId);
create index lesson_builder_qr_total_qi on lesson_builder_qr_totals(questionId);
create index lesson_builder_qr_questionId on lesson_builder_q_responses(questionId);
