--updated 2016/12/29
-- Create table
create table ACT_RE_ASSIGNMENT
(
  id_          NVARCHAR2(64) not null,
  group_id_    NVARCHAR2(255),
  user_id_     NVARCHAR2(255),
  type_        NVARCHAR2(255) not null,
  task_def_id_ NVARCHAR2(64) not null,
  proc_def_id_ NVARCHAR2(64) not null
);
-- Add comments to the table 
comment on table ACT_RE_ASSIGNMENT
  is '流程任务办理人管理表（扩展表，activiti不提供该表）';
-- Add comments to the columns 
comment on column ACT_RE_ASSIGNMENT.id_
  is '主键';
comment on column ACT_RE_ASSIGNMENT.group_id_
  is '组ID';
comment on column ACT_RE_ASSIGNMENT.user_id_
  is '用户ID';
comment on column ACT_RE_ASSIGNMENT.type_
  is '类型';
comment on column ACT_RE_ASSIGNMENT.task_def_id_
  is '流程任务定义ID';
comment on column ACT_RE_ASSIGNMENT.proc_def_id_
  is '流程定义ID';


-- Create/Recreate indexes 
create index ACT_IDX_RE_AST_TASKDEFID on ACT_RE_ASSIGNMENT (task_def_id_);
-- Create/Recreate primary, unique and foreign key constraints 
alter table ACT_RE_ASSIGNMENT add primary key (ID_);