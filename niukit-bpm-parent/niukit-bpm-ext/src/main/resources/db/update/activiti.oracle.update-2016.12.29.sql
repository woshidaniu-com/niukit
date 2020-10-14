--updated 2016/12/29
CREATE OR REPLACE VIEW V_PROCESS_TRACE AS
select "P_ACT_ID","P_ACT_NAME","P_ACT_TYPE","P_ASSIGNEE","P_USER_ID","P_DURATION","P_END_TIME","P_START_TIME","P_PROC_DEF_ID","P_PROC_INST_ID","P_TASK_ID","P_TASK_COMMENT","P_TASK_FULL_COMMENT" from (SELECT a.act_id_ as p_act_id,
       a.act_name_ as p_act_name,
       a.act_type_ as p_act_type,
       a.assignee_ as p_assignee,
       a.duration_ as p_duration,
       TO_CHAR(a.end_time_, 'YYYY-MM-DD HH24:MI:SS')as p_end_time,
       TO_CHAR(a.start_time_, 'YYYY-MM-DD HH24:MI:SS')as p_start_time,
       a.proc_def_id_ as p_proc_def_id,
       a.proc_inst_id_ as p_proc_inst_id,
       a.task_id_ as p_task_id,
			 b.user_id_ as p_user_id,
       b.message_ as p_task_comment,
       b.full_msg_ as P_TASK_FULL_COMMENT
  FROM act_hi_actinst a left join act_hi_comment b on a.task_id_ = b.task_id_
 where a.end_time_ is not null
   and a.act_type_ in ('startEvent', 'userTask', 'endEvent'))order by p_end_time asc;
   
   
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